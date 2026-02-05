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
import pages.HomePage;
import utils.ExplicitWait;

@Listeners(listerners.Listerner.class)
public class BankManager extends DriverSetup {

	Logger log = LogManager.getLogger(BankManager.class);
	ExplicitWait ew = new ExplicitWait();

	@Test(priority = 1, description = "Manager adds customer and opens account", groups = {"regression"})
	public void managerCreatesCustomerAndAccount() 
	{
		HomePage homePage = new HomePage(driver);
		homePage.bankManagerLoginFun().click();

		BankManagerLogin managerPage = new BankManagerLogin(driver);

		managerPage.addCustomerButtonFun().click();
		BankManagerAddCustomerPage addCustomerPage = new BankManagerAddCustomerPage(driver);
		addCustomerPage.firstNameInputFun().sendKeys("Customer1");
		addCustomerPage.lastNameInputFun().sendKeys("Dummy");
		addCustomerPage.postCodeInputFun().sendKeys("12345");
		addCustomerPage.addCustomerSubmitButtonFun().click();

		Alert a1 = driver.switchTo().alert();
		if (a1.getText().contains("Customer added successfully with customer id")) 
		{
			a1.accept();
			log.debug("New customer added successfully");
		} 
		else 
		{
			log.error("Customer not added");
		}

		managerPage.openAccountButtonFun().click();
		BankManagerOpenAccountPage openAccountPage = new BankManagerOpenAccountPage(driver);
		Select s1 = new Select(openAccountPage.customerSelectFun());
		s1.selectByVisibleText("Customer1 Dummy");

		Select s2 = new Select(openAccountPage.currencySelectFun());
		s2.selectByIndex(1);

		openAccountPage.processButtonFun().click();
		Alert a2 = driver.switchTo().alert();
		Assert.assertTrue(a2.getText().contains("Account created successfully with account Number"),"Account is not created with Account number");
		a2.accept();
	}

	@Test(priority = 2, description = "Customers sorting check", dependsOnMethods = "managerCreatesCustomerAndAccount", groups = {"regression"})
	public void customersSortingChecks() 
	{
		
		BankManagerLogin managerPage = new BankManagerLogin(driver);
		managerPage.customersButtonFun().click();
		ew.waitForVisibility(driver, By.xpath("//tr[contains(@ng-repeat,'cust in Customers')]"));

		BankManagerCustomersPage customersPage = new BankManagerCustomersPage(driver);

		customersPage.firstNameSortLinkFun().click();
		List<WebElement> rowsd = customersPage.customerTableRowsFun();
		for (int i = 0; i < rowsd.size() - 1; i++) 
		{
			String current = rowsd.get(i).findElement(By.xpath("./td[1]")).getText();
			String next = rowsd.get(i + 1).findElement(By.xpath("./td[1]")).getText();
			Assert.assertTrue(current.compareToIgnoreCase(next) >= 0, "descending order failed:");
		}
		log.debug("Descending order verified");

		customersPage.firstNameSortLinkFun().click();
		List<WebElement> rowsa = customersPage.customerTableRowsFun();
		for (int i = 0; i < rowsa.size() - 1; i++) 
		{
			String current = rowsa.get(i).findElement(By.xpath("./td[1]")).getText();
			String next = rowsa.get(i + 1).findElement(By.xpath("./td[1]")).getText();
			Assert.assertTrue(current.compareToIgnoreCase(next) <= 0, "Ascending order failed:");
		}
		log.debug("Ascending order verified");
	}

	@Test(priority = 3, description = "Customers search", dependsOnMethods = "customersSortingChecks", groups = {"regression"})
	public void customersSearch() 
	{
		ew.waitForVisibility(driver, By.xpath("//input[@ng-model='searchCustomer']"));
		BankManagerCustomersPage customersPage = new BankManagerCustomersPage(driver);
		customersPage.searchCustomerInputFun().sendKeys("Customer1");
		List<WebElement> tableRows = customersPage.customerTableRowsFun();
		Assert.assertTrue(tableRows.size() > 0, "No customer found for valid search");
	}

	@Test(priority = 4, description = "Delete customer and verify", dependsOnMethods = "customersSearch", groups = {"regression"})
	public void deleteCustomerAndVerify() 
	{
		BankManagerCustomersPage customersPage = new BankManagerCustomersPage(driver);
		List<WebElement> tableRows = customersPage.customerTableRowsFun();
		for (WebElement r : tableRows) 
		{
			String firstName = r.findElement(By.xpath("./td[1]")).getText();
			String lastName = r.findElement(By.xpath("./td[2]")).getText();
			if (firstName.equals("Customer1") && lastName.equals("Dummy")) 
			{
				r.findElement(By.xpath("./td[5]/button")).click();
				log.debug("Customer deleted");
			}
		}

		ew.waitForVisibility(driver, By.xpath("//input[@ng-model='searchCustomer']"));
		customersPage.searchCustomerInputFun().clear();
		customersPage.searchCustomerInputFun().sendKeys("Customer1");
		List<WebElement> rowsAfterDelete = customersPage.customerTableRowsFun();
		Assert.assertEquals(rowsAfterDelete.size(), 0, "Customer still present after delete!");
	}
}
