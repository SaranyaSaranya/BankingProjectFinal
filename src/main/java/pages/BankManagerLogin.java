package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class BankManagerLogin {

	WebDriver driver;

	public BankManagerLogin(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[@ng-click='addCust()']")
	private WebElement addCustomerButton;

	@FindBy(xpath = "//button[@ng-click='openAccount()']")
	private WebElement openAccountButton;

	@FindBy(xpath = "//button[@ng-click='showCust()']")
	private WebElement customersButton;

	public WebElement addCustomerButtonFun() {
		return addCustomerButton;
	}

	public WebElement openAccountButtonFun() {
		return openAccountButton;
	}

	public WebElement customersButtonFun() {
		return customersButton;
	}
}
