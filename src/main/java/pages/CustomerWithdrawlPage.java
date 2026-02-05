package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CustomerWithdrawlPage {
	
	WebDriver driver;
	
	public CustomerWithdrawlPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//button[@ng-class='btnClass3']")
	private WebElement withdrawlButtonClick;
	
	@FindBy(xpath="//div[@class='form-group']/label[text()='Amount to be Withdrawn :']")
	private WebElement amountWithdrawlText;
	
	@FindBy(css="input[ng-model='amount']")
	private WebElement withdrawlAmountEnter;
	
	@FindBy(xpath="//button[@type='submit']")
	private WebElement withdrawlSubmitButtonClick;
	
	@FindBy(xpath="//span[@class='error ng-binding' and contains(text(), 'Transaction successful')]")
	private WebElement withdrawlSuccessMessage;
	
	public WebElement withdrawlButtonClickFun()
	{
		return withdrawlButtonClick;
	}
	
	public WebElement amountWithdrawlTextFun()
	{
		return amountWithdrawlText;
	}
	
	public WebElement withdrawlAmountEnterFun()
	{
		return withdrawlAmountEnter;
	}
	
	public WebElement withdrawlSubmitButtonClickFun()
	{
		return withdrawlSubmitButtonClick;
	}
	
	public WebElement withdrawlSuccessMessageFun()
	{
		return withdrawlSuccessMessage;
	}



}
