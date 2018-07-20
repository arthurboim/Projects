package CashBack;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TopCashBack {

	public synchronized ChromeDriver MainTopCashBack(String Asin) throws InterruptedException
	{
		ChromeOptions options = new ChromeOptions();
		
		options.addArguments("user-data-dir=C:\\Users\\Noname\\AppData\\Local\\Google\\Chrome\\User");
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(Driver, 20);
		try{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Driver.get("https://www.topcashback.com/amazon/");
		
		try{
			Thread.sleep(2000);
		if (Driver.findElementsByXPath("//*[@id='ctl00_GeckoTwoColSecondary_SignUpSideBar_txtEmail']").size()>0)
		{
			Driver.findElementByXPath("//*[@id='ctl00_ctl25_hypLogin']").click();
			Thread.sleep(2000);
			Driver.findElementByXPath("//*[@id='txtEmail']").click();				
			Driver.findElementByXPath("//*[@id='txtEmail']").clear();
			Driver.findElementByXPath("//*[@id='txtEmail']").sendKeys("arthur.boim@gmail.com");
			Driver.findElementByXPath("//*[@id='ctl00_GeckoOneColPrimary_Login_txtPassword']").click();
			Driver.findElementByXPath("//*[@id='ctl00_GeckoOneColPrimary_Login_txtPassword']").clear();
			Driver.findElementByXPath("//*[@id='ctl00_GeckoOneColPrimary_Login_txtPassword']").sendKeys("b0104196");
			Driver.findElementByXPath("//*[@id='Loginbtn']").click();

		}
		}catch(Exception e)
		{
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return null;
		}
		wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='ctl00_GeckoTwoColSecondary_merchantOffersPanelMini_ctl00_hypCashbackLink']")));

		Driver.findElementByXPath("//*[@id='ctl00_GeckoTwoColSecondary_merchantOffersPanelMini_ctl00_hypCashbackLink']").click();
		Thread.sleep(7000);

		
		ArrayList<String> tabs2 = new ArrayList<String> (Driver.getWindowHandles());
		Driver.switchTo().window(tabs2.get(1));
		
		
		Thread.sleep(2000);
		//wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='twotabsearchtextbox']")));
		
		Driver.findElementByXPath("//*[@id='twotabsearchtextbox']").click();
		Driver.findElementByXPath("//*[@id='twotabsearchtextbox']").sendKeys(Asin);
		Driver.findElementByXPath("//input[@type='submit']").click();
		/*
		try{
			Thread.sleep(1000);
			Webelements = Driver.findElementsByTagName("input");
			for (WebElement ele:Webelements)
			{
				if (ele.getAttribute("value").equals("Go")) ele.click();
				break;
			}

		}catch(Exception e){  
			Webelements = Driver.findElementsByTagName("div");
			for (WebElement ele:Webelements)
			{
				if (ele.getAttribute("class").equals("nav-right")) ele.click();
				break;
			}
		}
		*/
		Thread.sleep(4000);
		try{
			
		if (Driver.findElementByXPath("//*[@id='noResultsTitle']").getText().contains("did not match any products")&&Driver.findElementByXPath("//*[@id='noResultsTitle']").getText().contains("Your search")&&Driver.findElementByXPath("//*[@id='noResultsTitle']").getText().contains(Asin))
		{
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return null;				  
		}
		if (Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains("did not match any products")&&Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains("Your search")&&Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains(Asin))
		{
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return null;				  
		}
		}catch(Exception e1)
		{
		System.out.println("Product exist");
		}
		
		Webelements = Driver.findElementsByTagName("a");
		int flag2 = 1;
		for (WebElement ele:Webelements)
		{
			try{
			System.out.println(ele.getAttribute("href"));
			if (ele.getAttribute("href").contains("/dp/"+Asin+"/")&&ele.getAttribute("href").contains("https://www.amazon.com/"))
			{
				ele.click();
				flag2 = 0;
				break;
				
			}
			}catch(Exception e1){			
			if(ele.getAttribute("href")!=null)
			{
			System.out.println(e1);
			System.out.println("Error in cash back");
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return null;
			}
			}
		}
		if (flag2==0)
		return Driver;
		else {
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			}
			return null;
		}catch(Exception e)
		{
			System.out.println(e);
			System.out.println("Error in cash back");
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return null;
		}

	}
	

}
