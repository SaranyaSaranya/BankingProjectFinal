package finalProjBankingProj;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.DriverSetup;
import pages.BankManagerAddCustomerPage;
import pages.BankManagerCustomersPage;
import pages.BankManagerLogin;
import pages.BankManagerOpenAccountPage;
import pages.CustomerDepositPage;
import pages.CustomerLoginPage;
import pages.CustomerTransactionPage;
import pages.CustomerWelcomePage;
import pages.CustomerWithdrawlPage;
import pages.HomePage;
import utils.ConfigReader;
import utils.ExplicitWait;

@Listeners(listerners.Listerner.class)
public class EndFlowTest extends DriverSetup {

	Logger log = LogManager.getLogger(BankManager.class);
	ConfigReader config = new ConfigReader();
	ExplicitWait ew = new ExplicitWait();

	@Test(priority = 1, description = "Manager adds customer and opens account", groups = {"regression", "Smoke"})
	public void managerCreatesCustomerAndAccount() {
		HomePage homePage = new HomePage(driver);
		homePage.bankManagerLoginFun().click();

		BankManagerLogin managerPage = new BankManagerLogin(driver);

		managerPage.addCustomerButtonFun().click();
		BankManagerAddCustomerPage addCustomerPage = new BankManagerAddCustomerPage(driver);
		addCustomerPage.firstNameInputFun().sendKeys(config.getManagerFirstName());
		addCustomerPage.lastNameInputFun().sendKeys(config.getManagerLastName());
		addCustomerPage.postCodeInputFun().sendKeys(config.getManagerPostCode());
		addCustomerPage.addCustomerSubmitButtonFun().click();

		Alert a1 = driver.switchTo().alert();
		Assert.assertTrue(a1.getText().contains("Customer added successfully"), "Customer not added");
		a1.accept();

		managerPage.openAccountButtonFun().click();
		BankManagerOpenAccountPage openAccountPage = new BankManagerOpenAccountPage(driver);
		Select s1 = new Select(openAccountPage.customerSelectFun());
		s1.selectByVisibleText(config.getManagerFirstName() + " " + config.getManagerLastName());
		Select s2 = new Select(openAccountPage.currencySelectFun());
		s2.selectByIndex(config.getManagerCurrencyIndex());
		openAccountPage.processButtonFun().click();

		Alert a2 = driver.switchTo().alert();
		Assert.assertTrue(a2.getText().contains("Account created successfully with account Number"),
				"Account is not created with Account number");
		a2.accept();
	}

	@Test(priority = 2, description = "Customer login and deposit", dependsOnMethods = "managerCreatesCustomerAndAccount", groups = {"regression", "Smoke"})
	public void customerDepositsAmount() {
		driver.get(config.getURL());
		HomePage homePagebutton = new HomePage(driver);
		
		ew.waitForVisibility(driver, By.xpath("//button[contains(@class, 'btn btn-primary') and @ng-click='manager()']"));
		homePagebutton.customerLoginButtonFun().click();

		CustomerLoginPage customerLoginPagebutton = new CustomerLoginPage(driver);
		Select selectName = new Select(customerLoginPagebutton.selectNameFun());
		selectName.selectByVisibleText(config.getFullName());
		customerLoginPagebutton.loginButtonClickFun().click();

		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);

		String name = config.getFullName();
		ew.waitForVisibility(driver, By.xpath("//span[text()='" + name + "']"));

		Select selectId = new Select(customerWelcomePageButton.selectIdFun());
		selectId.selectByIndex(config.getAccountSelectIndex());

		ew.waitForVisibility(driver, By.xpath("//div[@ng-hide='noAccount']"));

		String beforeDeposit = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int beforeDepositBalance = Integer.parseInt(beforeDeposit.split("Balance\\s*:\\s*")[1].split(",")[0].trim());

		CustomerDepositPage dp = new CustomerDepositPage(driver);
		dp.depositButtonClickFun().click();
		Assert.assertTrue(dp.amountDepositTextFun().isDisplayed());
		dp.depositAmountEnterFun().sendKeys(config.getDepositAmount());
		dp.depositSubmitButtonClickFun().click();

		String afterDeposit = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int afterDepositBalance = Integer.parseInt(afterDeposit.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		Assert.assertTrue(beforeDepositBalance < afterDepositBalance, "Balance did not increase after deposit");

		Assert.assertTrue(new CustomerDepositPage(driver).depositSuccessMessageFun().isDisplayed(), "Deposit message missing");
	}

	@Test(priority = 3, description = "Customer withdraws amount", dependsOnMethods = "customerDepositsAmount", groups = {"regression", "Smoke"})
	public void customerWithdrawsAmount() {
		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);
		String beforeTransaction = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int beforeTransactionBalance = Integer.parseInt(beforeTransaction.split("Balance\\s*:\\s*")[1].split(",")[0].trim());

		CustomerWithdrawlPage wp = new CustomerWithdrawlPage(driver);
		wp.withdrawlButtonClickFun().click();
		Assert.assertTrue(wp.amountWithdrawlTextFun().isDisplayed());
		wp.withdrawlAmountEnterFun().sendKeys(config.getWithdrawAmount());
		wp.withdrawlSubmitButtonClickFun().click();

		String afterTransaction = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int afterTransactionBalance = Integer.parseInt(afterTransaction.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		Assert.assertTrue(beforeTransactionBalance > afterTransactionBalance, "Balance not decreased after transaction");

		Assert.assertTrue(new CustomerWithdrawlPage(driver).withdrawlSuccessMessageFun().isDisplayed(), "Withdrawl success message missing");
	}

	@Test(priority = 4, description = "Verify transactions and reset", dependsOnMethods = "customerWithdrawsAmount", groups = {"regression", "Smoke"})
	public void verifyTransactionsAndReset() throws InterruptedException {
		CustomerTransactionPage tp = new CustomerTransactionPage(driver);
		ew.waitForVisibility(driver, By.xpath("//button[@ng-class='btnClass1']"));
		Thread.sleep(2000);
		tp.transactionButtonClickFun().click();
		ew.waitForVisibility(driver, By.xpath("//table[@class='table table-bordered table-striped']"));

		List<WebElement> rows = tp.transactionTableTotalRowsFun();
		WebElement lastRow = rows.get(rows.size() - 1);

		//String transactionDate = tp.getTransactionDateFromRow(lastRow);
		String transactionAmount = tp.getTransactionAmountFromRow(lastRow);
		String transactionType = tp.getTransactionTypeFromRow(lastRow);

		Assert.assertEquals(transactionType, "Debit", "Withdraw not recorded correctly");
		Assert.assertEquals(transactionAmount, "100", "Withdraw amount mismatch");

		tp.resetButtonFun().click();
		tp.backButtonFun().click();

		ew.waitForVisibility(driver, By.xpath("//div[@ng-hide='noAccount']"));
		CustomerWelcomePage customerWelcomePageButton = new CustomerWelcomePage(driver);
		String amountInLine = customerWelcomePageButton.detailsOfAccountInLineFun().getText();
		int amountInLineTrim = Integer.parseInt(amountInLine.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		Assert.assertEquals(amountInLineTrim, 0, "Value not reset correctly");
	}

	@Test(priority = 5, description = "Manager customers: sort, search, delete", dependsOnMethods = "verifyTransactionsAndReset", groups = {"regression", "Smoke"})
	public void managerCustomersOpsAndDelete() throws InterruptedException {
		driver.get(config.getURL());
		HomePage homePage = new HomePage(driver);
		homePage.bankManagerLoginFun().click();

		BankManagerLogin managerPage = new BankManagerLogin(driver);
		managerPage.customersButtonFun().click();
		ew.waitForVisibility(driver, By.xpath("//tr[contains(@ng-repeat,'cust in Customers')]"));

		BankManagerCustomersPage customersPage = new BankManagerCustomersPage(driver);

		customersPage.firstNameSortLinkFun().click();
		List<WebElement> rowsd = customersPage.customerTableRowsFun();
		for (int i = 0; i < rowsd.size() - 1; i++) {
			String current = rowsd.get(i).findElement(By.xpath("./td[1]")).getText();
			String next = rowsd.get(i + 1).findElement(By.xpath("./td[1]")).getText();
			Assert.assertTrue(current.compareToIgnoreCase(next) >= 0, "descending order failed:");
		}

		customersPage.firstNameSortLinkFun().click();
		List<WebElement> rowsa = customersPage.customerTableRowsFun();
		for (int i = 0; i < rowsa.size() - 1; i++) {
			String current = rowsa.get(i).findElement(By.xpath("./td[1]")).getText();
			String next = rowsa.get(i + 1).findElement(By.xpath("./td[1]")).getText();
			Assert.assertTrue(current.compareToIgnoreCase(next) <= 0, "Ascending order failed:");
		}

		customersPage.searchCustomerInputFun().sendKeys(config.getManagerFirstName());
		List<WebElement> tableRows = customersPage.customerTableRowsFun();
		Assert.assertTrue(tableRows.size() > 0, "No customer found for valid search");

		for (WebElement r : tableRows) {
			String firstName = r.findElement(By.xpath("./td[1]")).getText();
			String lastName = r.findElement(By.xpath("./td[2]")).getText();
			if (firstName.equals(config.getManagerFirstName()) && lastName.equals(config.getManagerLastName())) {
				r.findElement(By.xpath("./td[5]/button")).click();
				break;
			}
		}

		ew.waitForVisibility(driver, By.xpath("//input[@ng-model='searchCustomer']"));
		customersPage.searchCustomerInputFun().clear();
		customersPage.searchCustomerInputFun().sendKeys(config.getManagerFirstName());
		List<WebElement> rowsAfterDelete = customersPage.customerTableRowsFun();
		Assert.assertEquals(rowsAfterDelete.size(), 0, "Customer still present after delete!");
	}
}

	

