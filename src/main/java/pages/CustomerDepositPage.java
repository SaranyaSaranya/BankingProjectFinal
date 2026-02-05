package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class CustomerDepositPage {
	WebDriver driver;
	public CustomerDepositPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//button[@ng-class='btnClass2']")
	private WebElement depositButtonClick;
	
	@FindBy(xpath="//div[@class='form-group']/label[text()='Amount to be Deposited :']")
	private WebElement amountDepositText;
	
	@FindBy(css="input[ng-model='amount']")
	private WebElement depositAmountEnter;
	
	@FindBy(xpath="//button[@type='submit']")
	private WebElement depositSubmitButtonClick;
	
	@FindBy(xpath="//span[@class='error ng-binding' and contains(text(), 'Deposit Successful')]")
	private WebElement depositSuccessMessage;
	
	public WebElement depositButtonClickFun()
	{
		return depositButtonClick;
	}
	
	public WebElement amountDepositTextFun()
	{
		return amountDepositText;
	}
	
	public WebElement depositAmountEnterFun()
	{
		return depositAmountEnter;
	}
	
	public WebElement depositSubmitButtonClickFun()
	{
		return depositSubmitButtonClick;
	}
	
	public WebElement depositSuccessMessageFun()
	{
		return depositSuccessMessage;
	}


}
