package base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import utils.ConfigReader;

public class DriverSetup {
	
	protected static WebDriver driver;
	static Logger log = LogManager.getLogger(DriverSetup.class.getName());
	
	@BeforeClass
	public void setup() 
	{
		log.info("Test Execution Started");
		ConfigReader reader = new ConfigReader();
		String driverName = reader.getBrowser();
		if (driverName.equalsIgnoreCase("edge"))
		{
			driver = new EdgeDriver();
			log.info("Edge browser launched");
		}
			
		else if (driverName.equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
			log.info("Firefox browser launched");
		}
			
		else if (driverName.equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			log.info("Chrome browser launched");
		}
			
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		driver.get(reader.getURL());
	}
	
	@AfterClass
	public void tearDown()
	{
		driver.quit();
		log.info("Browser closed");
	}

}
