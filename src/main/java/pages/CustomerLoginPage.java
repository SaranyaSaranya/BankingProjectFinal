package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CustomerLoginPage {
	WebDriver driver;
	public CustomerLoginPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="userSelect")
	private WebElement selectName;
	
	@FindBy(xpath="//button[contains(@class, 'btn btn-default') and @type='submit']")
	private WebElement loginButtonClick;
	
	public WebElement selectNameFun()
	{
		return selectName;
	}
	
	public WebElement loginButtonClickFun()
	{
		return loginButtonClick;
	}

}
