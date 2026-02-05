package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePage{
	WebDriver driver;
	public HomePage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//button[@ng-click='customer()' and contains(@class, 'btn-primary')]")
	private WebElement customerLoginButton;
	
	@FindBy(xpath="//button[contains(@class, 'btn btn-primary') and @ng-click='manager()']")
	private WebElement bankManagerLogin;
	
	public WebElement customerLoginButtonFun()
	{
		return customerLoginButton;
	}
	
	public WebElement bankManagerLoginFun()
	{
		return bankManagerLogin;
	}

}
