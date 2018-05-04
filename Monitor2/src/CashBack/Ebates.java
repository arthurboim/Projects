package CashBack;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Ebates {

	public ChromeDriver EbatesMain(String Asin) throws InterruptedException
	{
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		
		//options.addArguments("--user-data-dir=C:\\Users\\Noname\\AppData\\Local\\Google\\Chrome\\User Data");
		options.addArguments("--user-data-dir=C:\\User Data");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		//Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Driver.get("https://www.ebates.com/amazon.htm");
		Driver.findElementByXPath("//*[@id='amz-right']/div[1]/div[2]/ul/li[4]/a").click();
		Thread.sleep(5000);
		ArrayList<String> tabs2 = new ArrayList<String> (Driver.getWindowHandles());
		Driver.switchTo().window(tabs2.get(1));
		Driver.findElementByXPath("//*[@id='twotabsearchtextbox']").click();
		Driver.findElementByXPath("//*[@id='twotabsearchtextbox']").sendKeys(Asin);
		Driver.findElementByXPath("//*[@id='nav-search']/form/div[2]/div").click();
		Thread.sleep(2000);
		try{
		if (Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains("did not match any products")&&Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains("Your search")&&Driver.findElementByXPath("//*[@id='noResultsTitle']/span").getText().contains(Asin))
		return null;
		}catch(Exception e){System.out.println("Product exist");}
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
		//Driver.close();
		//return null;
	}

}
