package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BankManagerCustomersPage {

	WebDriver driver;

	public BankManagerCustomersPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@ng-model='searchCustomer']")
	private WebElement searchCustomerInput;

	@FindBy(xpath = "//a[contains(text(),'First Name')]")
	private WebElement firstNameSortLink;

	@FindBy(xpath = "//tr[contains(@ng-repeat,'cust in Customers')]")
	private List<WebElement> customerTableRows;

	public WebElement searchCustomerInputFun() {
		return searchCustomerInput;
	}

	public WebElement firstNameSortLinkFun() {
		return firstNameSortLink;
	}

	public List<WebElement> customerTableRowsFun() {
		return customerTableRows;
	}

	
}
