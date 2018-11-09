package Amazon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Ebay.eBayCalls;
import Item.Item;



public class AmazonCalls implements Runnable {

	/* Variables */
	private  String Connection = null;
	private  String scham = null;
	private  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	private  String user = null;
	private  String pass = null;
	private  String webdriverPath = null;
	private  ArrayList<Item> list;
	private  ChromeOptions options;
	private  ChromeDriver Driver;
	private  IDataBase SQLDB;
	private  ResultSet res;
	private  eBayCalls eBayCaller;
	
	/* Constractor */
	public AmazonCalls() {
		InitFromFile();
    	list = new ArrayList<Item>();
    	options = new ChromeOptions();
    	options.addArguments("--start-maximized");
    	System.setProperty("webdriver.chrome.driver", webdriverPath);
		Driver = new ChromeDriver(options);
		SQLDB = new SQLDataBase();
		eBayCaller = new eBayCalls();

	}

	/* Init */
	private void InitFromFile()
	{
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{

				if (sCurrentLine.contains("APass: "))
				{
					pass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+"APass: ".length());
				}
				
				if (sCurrentLine.contains("Acid: "))
				{
					user = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ")+"Acid: ".length());
				}
				
				if (sCurrentLine.contains("webdriver.chrome.driver:"))
				{
					webdriverPath = sCurrentLine.substring(sCurrentLine.indexOf("webdriver.chrome.driver:")+"webdriver.chrome.driver: ".length());
				}

				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					Connection =Connection+scham;
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+"Schame: ".length());
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			}

	
	}
	
	
	
	
	
	/* Thread public runnable */
    @Override
	public void run() {

		Boolean success = Login(Driver);
		if (false == success)
		{
			Login2(Driver);
		}
		
			
		while (true)
		{
			try {
				readItems();
				System.out.println("list size = "+list.size());
				if (list.size() > 0)
				{
					for (Item ele:list)
					{
						CheckWithScraper(ele);
						if (ele.isPrime())
						{
							eBayCaller.UpdateItemPosition(ele);
							if ((((ele.getMarketPlaceLowestPrice())/(ele.getCurrentSupplierPrice()+0.3)		 > 1.14)   && (ele.getCurrentTax() == 0)) ||
								(((ele.getMarketPlaceLowestPrice())/(ele.getCurrentSupplierPrice()+ele.getCurrentTax() + 0.3) > 1.185) && (ele.getCurrentTax() > 0)))
							{
								System.out.println("Item added "+ele.getSupplierCode());
								SQLDB.Write("UPDATE  amazon.productfromsellers SET asin = '"+ele.getSupplierCode()+"' , Amazon_price = "+ele.getCurrentSupplierPrice()+", AmazonAvailavle = 1 , placeinlowestprice = "+ele.getPlaceInLowestPrice()+" , BreakEvenForlowest = "+(ele.getMarketPlaceLowestPrice())/(ele.getCurrentSupplierPrice()+ele.getCurrentTax()+0.3)+" , arbitrajeLowestprice = "+ele.getArbitraje()+" , EbayLowestprice = "+(ele.getMarketPlaceLowestPrice())+" , Tax = "+ele.getCurrentTax()+" where Code = '"+ele.getUniversalCode()+"';");
							}else 
							{
								SQLDB.Write("DELETE FROM amazon.productfromsellers WHERE Code = '"+ele.getUniversalCode()+"';");
							}
						}else 
						{
							SQLDB.Write("DELETE FROM amazon.productfromsellers WHERE Code = '"+ele.getUniversalCode()+"';");
						}
					}
					
					list.removeAll(list);
				}
				
				Thread.sleep((1000*60*1)/2);
				
			} catch (InterruptedException e) 
			{
				System.out.println(e);
			} 
		}
		
		
	}
    
    
    
    
    
    /* Private functions*/
    
	private void CheckWithScraper(Item ele) 
	{
		double temp;
		String CurrenteTax;
		String Asin = null;
		
		try{
			Driver.get("https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="+ele.getUniversalCode().replaceAll(" ", "")+"");
			waitForLoad(Driver);
			Asin = Driver.findElementByXPath("//*[@id='result_0']/div/div/div/div[2]/div[1]/div/a").getAttribute("href");
			Asin = Asin.substring(Asin.lastIndexOf("dp/")+3, Asin.lastIndexOf("/ref="));
			Driver.get("https://www.amazon.com/gp/offer-listing/"+Asin+"/ref=olp_f_new?ie=UTF8&f_all=true&f_new=true&f_primeEligible=true");
			waitForLoad(Driver);
			temp = Double.parseDouble(Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]").getText().replace("$",""));
			ele.setCurrentSupplierPrice(temp);
			
			try{
				CurrenteTax = Driver.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();
				CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+")+3, CurrenteTax.indexOf("estimated tax")-1);
				ele.setCurrentTax(Double.parseDouble(CurrenteTax));
			}catch(Exception e)
			{
				
			}
			
			if (Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[2]/i").getAttribute("aria-label").contains("Eligible for free shipping with Amazon Prime."))
			{
				ele.setSupplierCode(Asin);
				ele.setPrime(true);
			}else 
			{
				ele.setPrime(false);
			}
		}catch(Exception e)
		{
			ele.setCurrentSupplierPrice(-1);
			ele.setPrime(false);
		}
	}
	
    private void readItems()
    {
		try {
			if (false == SQLDB.isExistInDataBase("SELECT * FROM amazon.productfromsellers where AmazonAvailavle = 0;"))
			{
				return;
			}
			
			res = SQLDB.Read("SELECT * FROM amazon.productfromsellers where AmazonAvailavle = 0;");
			while (res.next())
			{
				Item newItem = new Item();
				newItem.setCodeType(res.getString("CodeType"));
				newItem.setUniversalCode(res.getString("Code"));
				list.add(newItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	private boolean Login(ChromeDriver Driver)
	{
		try {
			Driver.get("https://www.amazon.com/");
			waitForLoad(Driver);
			Driver.findElement(By.id("nav-link-accountList")).click();
			waitForLoad(Driver);
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
			Driver.findElementByXPath("//*[@id='continue']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
}
	
	private void  Login2(ChromeDriver Driver)
	{			
			waitForLoad(Driver);
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
			//Driver.findElementByXPath("//*[@id='continue']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
	}

    private void waitForLoad(ChromeDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}
