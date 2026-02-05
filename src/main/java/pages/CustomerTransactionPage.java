package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CustomerTransactionPage {
	
	WebDriver driver;
	
	public CustomerTransactionPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//button[@ng-class='btnClass1']")
	private WebElement transactionButtonClick;
	
	@FindBy(xpath="//table[@class='table table-bordered table-striped']")
	private WebElement transactionTableVerify;
	
	@FindBy(xpath="//table[@class='table table-bordered table-striped']/tbody/tr")
	private List<WebElement> transactionTableTotalRows;
	
	
	@FindBy(xpath="//button[@ng-click='reset()']")
	private WebElement resetButton;
	
	@FindBy(xpath="//button[@ng-click='back()']")
	private WebElement backButton;
	
	
	public WebElement transactionButtonClickFun()
	{
		return transactionButtonClick;
	}
	
	public WebElement transactionTableVerifyFun()
	{
		return transactionTableVerify;
	}
	
	public List<WebElement> transactionTableTotalRowsFun()
	{
		return transactionTableTotalRows;
	}
	
	public WebElement resetButtonFun()
	{
		return resetButton;
	}

	public WebElement backButtonFun()
	{
		return backButton;
	}
	
	public String getTransactionDateFromRow(WebElement row) {
	    return row.findElement(By.xpath("./td[1]")).getText();
	}

	public String getTransactionAmountFromRow(WebElement row) {
	    return row.findElement(By.xpath("./td[2]")).getText();
	}

	public String getTransactionTypeFromRow(WebElement row) {
	    return row.findElement(By.xpath("./td[3]")).getText();
	}


}
