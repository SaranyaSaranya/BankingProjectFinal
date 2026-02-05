package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	
	Properties prop;
	public ConfigReader()
	{
		try
		{
			InputStream fil =  getClass().getClassLoader().getResourceAsStream("data.properties");
			prop = new Properties();
			prop.load(fil);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getBrowser()
	{
		return prop.getProperty("browser");
	}
	
	public String getURL()
	{
		return prop.getProperty("url");
	}

	public String getCustomerName()
	{
		return prop.getProperty("customerName");
	}

	public String getDepositAmount()
	{
		return prop.getProperty("depositAmount");
	}

	public String getWithdrawAmount()
	{
		return prop.getProperty("withdrawAmount");
	}

	public int getAccountSelectIndex()
	{
		String var = prop.getProperty("accountSelectIndex");
		return Integer.parseInt(var);
 			
	}

	public String getManagerFirstName()
	{
		return prop.getProperty("managerFirstName");
	}

	public String getManagerLastName()
	{
		return prop.getProperty("managerLastName");
	}

	public String getManagerPostCode()
	{
		return prop.getProperty("managerPostCode");
	}

	public int getManagerCurrencyIndex()
	{
		String var = prop.getProperty("managerCurrencyIndex", "1");
		return Integer.parseInt(var);
	}

	public String getFullName()
	{
		String fn = prop.getProperty("managerFirstName");
		String ln = prop.getProperty("managerLastName");
		String fullname = fn + " " + ln;
		return fullname;
		
	}

}
