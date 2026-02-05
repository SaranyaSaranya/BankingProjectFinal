package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BankManagerOpenAccountPage {

	WebDriver driver;

	public BankManagerOpenAccountPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "userSelect")
	private WebElement customerSelect;

	@FindBy(id = "currency")
	private WebElement currencySelect;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement processButton;

	public WebElement customerSelectFun() {
		return customerSelect;
	}

	public WebElement currencySelectFun() {
		return currencySelect;
	}

	public WebElement processButtonFun() {
		return processButton;
	}
}
