package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class CustomerWelcomePage{
	
	WebDriver driver;
	
	public CustomerWelcomePage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="accountSelect")
	private WebElement selectId;
	
	@FindBy(xpath="//div[@ng-hide='noAccount']")
	private WebElement detailsOfAccountInLine;
	

	public WebElement selectIdFun()
	{
		return selectId;
	}
	
	public WebElement detailsOfAccountInLineFun()
	{
		return detailsOfAccountInLine;
	}
	
	

}
