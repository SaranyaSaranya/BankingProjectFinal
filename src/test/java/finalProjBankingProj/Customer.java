package finalProjBankingProj;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.apache.logging.log4j.Logger;

import base.DriverSetup;
import pages.*;
import utils.ConfigReader;
import utils.ExplicitWait;

@Listeners(listerners.Listerner.class)
public class Customer extends DriverSetup {

	Logger log = LogManager.getLogger(Customer.class.getName());
	ConfigReader config = new ConfigReader();
	ExplicitWait ew = new ExplicitWait();
	

	@Test(priority = 1, description = "Customer login and select account", groups = {"regression"})
	public void customerLoginAndSelectAccount() 
	{
		HomePage homePagebutton = new HomePage(driver);
		homePagebutton.customerLoginButtonFun().click();

		// Selecting name of the customer
		CustomerLoginPage customerLoginPagebutton = new CustomerLoginPage(driver);
		Select selectName = new Select(customerLoginPagebutton.selectNameFun());
		selectName.selectByVisibleText(config.getCustomerName());
		customerLoginPagebutton.loginButtonClickFun().click();

		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);

		// explicit wait for page to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'Harry Potter']")));
		
		// Selecting ID on welcoming page
		Select selectId = new Select(customerWelcomePageButton.selectIdFun());
		selectId.selectByIndex(config.getAccountSelectIndex());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@ng-hide='noAccount']")));
		System.out.println(customerWelcomePageButton.detailsOfAccountInLineFun().getText());
	}

	@Test(priority = 2, description = "Customer deposit", dependsOnMethods = "customerLoginAndSelectAccount", groups = {"regression"})
	public void customerDeposit() 
	{
		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);
		String beforeDeposit = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int beforeDepositBalance = Integer.parseInt(beforeDeposit.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		System.out.println(beforeDepositBalance);

		CustomerDepositPage dp = new CustomerDepositPage(driver);
		dp.depositButtonClickFun().click();

		Assert.assertTrue(dp.amountDepositTextFun().isDisplayed());

		dp.depositAmountEnterFun().sendKeys(config.getDepositAmount());
		dp.depositSubmitButtonClickFun().click();

		System.out.println(customerWelcomePageButton.detailsOfAccountInLineFun().getText());
		String afterDeposit = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int afterDepositBalance = Integer.parseInt(afterDeposit.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		System.out.println(afterDepositBalance);
		Assert.assertTrue(beforeDepositBalance < afterDepositBalance, "Balance did not increase after deposit");

		if (dp.depositSuccessMessageFun().isDisplayed())
			log.debug("deposit successful");
		else
			log.error("Not successfully deposited");
	}

	@Test(priority = 3, description = "Customer withdraw", dependsOnMethods = "customerDeposit", groups = {"regression"})
	public void customerWithdraw() 
	{
		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);

		CustomerWithdrawlPage wp = new CustomerWithdrawlPage(driver);

		System.out.println(customerWelcomePageButton.detailsOfAccountInLineFun().getText());
		String beforeTransaction = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int beforeTransactionBalance = Integer
				.parseInt(beforeTransaction.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		System.out.println(beforeTransactionBalance);

		wp.withdrawlButtonClickFun().click();
		Assert.assertTrue(wp.amountWithdrawlTextFun().isDisplayed());
		wp.withdrawlAmountEnterFun().sendKeys(config.getWithdrawAmount());
		wp.withdrawlSubmitButtonClickFun().click();

		System.out.println(customerWelcomePageButton.detailsOfAccountInLineFun().getText());
		String afterTransaction = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int afterTransactionBalance = Integer
				.parseInt(afterTransaction.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		System.out.println(afterTransactionBalance);
		Assert.assertTrue(beforeTransactionBalance > afterTransactionBalance,
				"Balance not decreased after transaction");

		if (wp.withdrawlSuccessMessageFun().isDisplayed())
			log.debug("Withdrawl Transaction successful");
		else
			log.error("Not successfully transacted on withdrawl");
	}

	@Test(priority = 4, description = "Transactions and reset", dependsOnMethods = "customerWithdraw", groups = {"regression"})
	public void transactionsAndReset() throws InterruptedException
	{
		CustomerTransactionPage tp = new CustomerTransactionPage(driver);
		ew.waitForVisibility(driver, By.xpath("//button[@ng-class='btnClass1']"));
		Thread.sleep(2000); //page takes time to get the content
		tp.transactionButtonClickFun().click();
		ew.waitForVisibility(driver, By.xpath("//table[@class='table table-bordered table-striped']"));

		// Get all rows of the transaction table
		List<WebElement> rows = tp.transactionTableTotalRowsFun();

		// Get last row 
		int rowsize = rows.size();
		int rowindex = rowsize - 1;
		WebElement lastRow = rows.get(rowindex);

		// Extract details
		String transactionAmount = tp.getTransactionAmountFromRow(lastRow);
		String transactionType = tp.getTransactionTypeFromRow(lastRow);
		Assert.assertEquals(transactionType, "Debit", "Deposit not recorded correctly"); 
		Assert.assertEquals(transactionAmount, "100", "Deposit amount mismatch");

		tp.resetButtonFun().click();
		ew.waitForVisibility(driver, By.xpath("//button[@ng-click='back()']"));
		tp.backButtonFun().click();
		ew.waitForVisibility(driver, By.xpath("//div[@ng-hide='noAccount']"));
		
		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);
		String amountInLine = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int amountInLineTrim = Integer.parseInt(amountInLine.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		System.out.println(amountInLineTrim);
		Assert.assertEquals(amountInLineTrim, 0, "Value not reset correctly");

	}

}
