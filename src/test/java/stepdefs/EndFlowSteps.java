package stepdefs;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import base.DriverSetup;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.BankManagerAddCustomerPage;
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

public class EndFlowSteps extends DriverSetup {

	ConfigReader config = new ConfigReader();
	ExplicitWait ew = new ExplicitWait();
	HomePage homePage = new HomePage(driver);
	BankManagerLogin managerPage = new BankManagerLogin(driver);
	CustomerTransactionPage tp = new CustomerTransactionPage(driver);

	@Given("The user logins the page")
	public void setup_executed() 
	{
		setup();
		ew.waitForVisibility(driver, By.xpath("//button[@ng-click='customer()']"));
	}

	@Given("The bank manager navigates to home dashboard")
	public void bank_manager_navigates_to_home_dashboard() 
	{
		ew.waitForVisibility(driver,By.xpath("//button[contains(@class, 'btn btn-primary') and @ng-click='manager()']"));
		homePage.bankManagerLoginFun().click();
		ew.waitForVisibility(driver, By.xpath("//button[@ng-click='addCust()']"));
	}

	@When("The manager adds customer {string} {string} with post code {string}")
    public void manager_adds_customer(String firstName, String lastName, String postCode) 
	{
        managerPage.addCustomerButtonFun().click();
        BankManagerAddCustomerPage addCustomerPage = new BankManagerAddCustomerPage(driver);
        addCustomerPage.firstNameInputFun().sendKeys(firstName);
        addCustomerPage.lastNameInputFun().sendKeys(lastName);
        addCustomerPage.postCodeInputFun().sendKeys(postCode);
        addCustomerPage.addCustomerSubmitButtonFun().click();
	}
        
        
     @And("The add customer alert appears and is accepted")
     public void add_customer_alert() 
     {
       	Alert a1 = driver.switchTo().alert();
    	Assert.assertTrue(a1.getText().contains("Customer added successfully"), "Customer not added");
    	a1.accept();
        }
       

	@And("The manager opens account for {string} {string} with currency index {int}")
	public void manager_opens_account(String firstName, String lastName, Integer currencyIndex) 
	{
		managerPage.openAccountButtonFun().click();
		BankManagerOpenAccountPage openAccountPage = new BankManagerOpenAccountPage(driver);
		ew.waitForVisibility(driver, By.id("userSelect"));
		Select s1 = new Select(openAccountPage.customerSelectFun());
		s1.selectByVisibleText(firstName + " " + lastName);
		Select s2 = new Select(openAccountPage.currencySelectFun());
		s2.selectByIndex(currencyIndex);
		openAccountPage.processButtonFun().click();
	}

	@Then("The account creation alert appears and is accepted")
	public void account_creation_alert() 
	{
		Alert a2 = driver.switchTo().alert();
		Assert.assertTrue(a2.getText().contains("Account created successfully with account Number"),"Account is not created with Account number");
		a2.accept();
	}

	@Given("The customer logging as {string}")
	public void customer_logging(String fullName) 
	{
		driver.get(config.getURL());
		ew.waitForVisibility(driver, By.xpath("//button[@ng-click='customer()']"));
		homePage.customerLoginButtonFun().click();

		CustomerLoginPage loginPage = new CustomerLoginPage(driver);
		Select selectName = new Select(loginPage.selectNameFun());
		selectName.selectByVisibleText(fullName);
		loginPage.loginButtonClickFun().click();
		ew.waitForVisibility(driver, By.xpath("//span[text()='" + fullName + "']"));
	}

	@And("The customer selects account index {int}")
	public void customer_selects_account(Integer accountIndex) 
	{
		CustomerWelcomePage welcome = new CustomerWelcomePage(driver);
		String name = config.getFullName();
		ew.waitForVisibility(driver, By.xpath("//span[text()='" + name + "']"));

		Select selectId = new Select(welcome.selectIdFun());
		selectId.selectByIndex(accountIndex);
		ew.waitForVisibility(driver, By.xpath("//div[@ng-hide='noAccount']"));
	}

	@When("The customer deposits {int}")
	public void customer_deposits(Integer amount) 
	{
		CustomerDepositPage dp = new CustomerDepositPage(driver);
		dp.depositButtonClickFun().click();
		Assert.assertTrue(dp.amountDepositTextFun().isDisplayed(), "Deposit input not visible");
		dp.depositAmountEnterFun().sendKeys(String.valueOf(amount));
		dp.depositSubmitButtonClickFun().click();
		Assert.assertTrue(dp.depositSuccessMessageFun().isDisplayed(), "Deposit success message missing");
	}

	@And("The customer withdraws {int}")
	public void customer_withdraws(Integer amount) 
	{
		CustomerWithdrawlPage wp = new CustomerWithdrawlPage(driver);
		wp.withdrawlButtonClickFun().click();
		Assert.assertTrue(wp.amountWithdrawlTextFun().isDisplayed(), "Withdraw input not visible");
		wp.withdrawlAmountEnterFun().sendKeys(String.valueOf(amount));
		wp.withdrawlSubmitButtonClickFun().click();
		Assert.assertTrue(wp.withdrawlSuccessMessageFun().isDisplayed(), "Withdraw success message missing");
	}

	@Then("The transactions show latest type {string} amount {int}")
	public void transactions_show(String latestType, Integer latestAmount) throws InterruptedException 
	{
		ew.waitForVisibility(driver, By.xpath("//button[@ng-class='btnClass1']"));
		Thread.sleep(3000); //sometimes page takes time to load the values
		tp.transactionButtonClickFun().click();
		ew.waitForVisibility(driver, By.xpath("//table[@class='table table-bordered table-striped']"));

		List<WebElement> rows = tp.transactionTableTotalRowsFun();
		WebElement lastRow = rows.get(rows.size() - 1);
		String transactionType = tp.getTransactionTypeFromRow(lastRow);
		String transactionAmount = tp.getTransactionAmountFromRow(lastRow);

		Assert.assertEquals(transactionType, latestType, "Latest transaction type mismatch");
		Assert.assertEquals(transactionAmount, String.valueOf(latestAmount), "Latest transaction amount mismatch");
	}

	@And("The balance resets to {int}")
	public void balance_resets(Integer expected) 
	{
		tp.resetButtonFun().click();
		tp.backButtonFun().click();
		ew.waitForVisibility(driver, By.xpath("//div[@ng-hide='noAccount']"));
		CustomerWelcomePage welcome = new CustomerWelcomePage(driver);
		String inline = welcome.detailsOfAccountInLineFun().getText();
		int balance = Integer.parseInt(inline.split("Balance\\s*:\\s*")[1].split(",")[0].trim());
		Assert.assertEquals(balance, expected.intValue(), "Balance after reset mismatch");
	}

	@Then("The user closes the browsers")
	public void teardown_is_executed() 
	{
		tearDown();
	}
}
