package Control;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import Database.Item;

public class UniversalCodeCheckerFromAsinLab {
	
	// Variables 
	private ChromeDriver Driver;
	private Robot robot ;
	private String PathToDownloadedFile;
	
	// Contractor
	public UniversalCodeCheckerFromAsinLab() {
		
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver = new ChromeDriver();
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		PathToDownloadedFile = "C:\\Users\\Noname\\Downloads\\asinlookup.html";
	}

	
	// Public functions 
	public void GetItemInfo(Item item)
	{
		GetAsinInfo(item.SupplierCode);	
		item.UPC = GetUPC();
		item.EAN = GetEAN();
		DeleteFile(PathToDownloadedFile);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GetResultsByCode(Item item)
	{
		Driver.get("https://www.ebay.com/");
		FinditemByWebDriver(item);
	}
	
	public void FinditemByWebDriver(Item productToCheck)
	{
		String Keyword = null;

		if (true  == IsCharacterExist(productToCheck.SupplierCode))
		{	
			if (true  == IsContainsXinTheEnd(productToCheck.SupplierCode) &&
				1	  == AmountOfCharsInString(productToCheck.SupplierCode))
			{
				Keyword = productToCheck.SupplierCode;
			}else
			{
				if (null != productToCheck.UPC)
				{
					Keyword = productToCheck.UPC;	
				}else
				{
					productToCheck.eBayResults = 0;
					return;
				}
			}
		}else
		{
			Keyword = productToCheck.SupplierCode;
		}
		
		Driver.findElement(By.id("gh-ac")).click();
		Driver.findElement(By.id("gh-ac")).clear();
		Driver.findElement(By.id("gh-ac")).sendKeys(Keyword);
		Driver.findElement(By.id("gh-btn")).click();

		try{
			Driver.findElement(By.id("e1-20")).click();
	 	}catch(Exception e)
    	{
	 		System.out.println("Exception 1");
    	}
    	
    	try{
    		Driver.findElement(By.cssSelector("a[class='small cbx btn btn-s btn-ter tab tgl_button last_b']")).click();
    	}catch(Exception e)
    	{
	 		System.out.println("Exception 2");
    	}
    	
    	try{
    		productToCheck.eBayResults = Integer.parseInt(Driver.findElement(By.className("rcnt")).getText());
    	}catch(Exception e)
    	{
    		try{
    			String s = Driver.findElement(By.className("srp-controls__count-heading")).getText();
    			s = s.substring(0, s.indexOf("result"));
    			s = s.trim();
        		productToCheck.eBayResults = Integer.parseInt(s);
    		}
    		catch(Exception e1)
    		{
    			System.out.println("Parsing error 1");
    			productToCheck.eBayResults = -1; 
    			restDriver();
    		}
    	}
    	
    	
    	if(0 == productToCheck.eBayResults)
    	{
    		return;
    	}
    
    	
	}
	
	
	
	// Private functions
	
	public void restDriver()
	{
		Driver.close();
		Driver.quit();
	    Driver = new ChromeDriver();
	    Driver.get("https://www.ebay.com/");
	}
	
	// This is not the place for this function!
	public  boolean isBook(Item productToCheck)
	{
		if (true  == IsCharacterExist(productToCheck.SupplierCode))
		{	
			if (true  == IsContainsXinTheEnd(productToCheck.SupplierCode) &&
				1	  == AmountOfCharsInString(productToCheck.SupplierCode))
			{
				return true;
			}else
			{
				return false;
			}
		}else
		{
			return true;
		}
	}
	
	private  int AmountOfCharsInString(String str)
	{
		int charCount = 0;
		char temp;

		for( int i = 0; i < str.length( ); i++ )
		{
		    temp = str.charAt( i );

		    if( Character.isLetter(temp))
		        charCount++;
		}
		
		return charCount;
	}
	
	private  boolean IsCharacterExist(String str)
	{
		Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches())
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	private  boolean IsContainsXinTheEnd(String str)
	{
		int count = StringUtils.countMatches(str, "X");
		if (1 == count && str.indexOf("X") == str.length()-1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
 	private void GetAsinInfo(String ASIN)
	{
		Driver.get("http://www.asinlab.com/asin-to-upc/");
		WebElement ele = Driver.findElement(By.id("itemid"));
		ele.click();
		ele.clear();
		ele.sendKeys(ASIN);
		Driver.findElement(By.xpath("//*[@id='post-354']/div/center/div/p[2]/button")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Driver.switchTo().window(Driver.getTitle());

		robot.keyPress(KeyEvent.VK_SPACE); 
		robot.keyRelease(KeyEvent.VK_SPACE);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
		robot.keyPress(KeyEvent.VK_TAB); 
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyPress(KeyEvent.VK_SPACE); 
		robot.keyRelease(KeyEvent.VK_SPACE);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Driver.get(PathToDownloadedFile);
	}
	
	private String GetUPC()
	{ 
		try{
			if (Driver.findElement(By.xpath("//*[@id='bulk']/tbody/tr[2]/td[3]/div")).getText().equals(""))
			{
				return null;
			}else
			{
				return Driver.findElement(By.xpath("//*[@id='bulk']/tbody/tr[2]/td[3]/div")).getText();
			}
		}catch(Exception e)
		{
			return null;
		}
	}
	
	private String GetEAN()
	{
		return null;
	}
	
	private void DeleteFile(String path)
	{
		try{
    		File file = new File(path);
    		if(file.delete()){
    			//System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	
	
	
	
	// Destractor

	@Override
	protected void finalize() throws Throwable {

		try
		{
			Driver.close();
			Driver.quit();
			//DeleteFile(PathToDownloadedFile);
		}catch(Exception e)
		{
			
		}
	
	}

	
	
}
