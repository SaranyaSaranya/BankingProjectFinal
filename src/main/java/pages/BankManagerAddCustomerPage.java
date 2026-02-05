package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BankManagerAddCustomerPage {

	WebDriver driver;

	public BankManagerAddCustomerPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@ng-model='fName' and @placeholder='First Name']")
	private WebElement firstNameInput;

	@FindBy(xpath = "//input[@ng-model='lName' and @placeholder='Last Name']")
	private WebElement lastNameInput;

	@FindBy(xpath = "//input[@ng-model='postCd' and @placeholder='Post Code']")
	private WebElement postCodeInput;

	@FindBy(xpath = "//button[@type='submit' and text()='Add Customer']")
	private WebElement addCustomerSubmitButton;

	public WebElement firstNameInputFun() {
		return firstNameInput;
	}

	public WebElement lastNameInputFun() {
		return lastNameInput;
	}

	public WebElement postCodeInputFun() {
		return postCodeInput;
	}

	public WebElement addCustomerSubmitButtonFun() {
		return addCustomerSubmitButton;
	}
}
