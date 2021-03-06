package Amazon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Ebay.eBayCalls;
import Item.Item;

public class AmazonCalls implements Runnable {

	/* Variables */
	private String Connection = null;
	private String scham = null;
	private String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	private String user = null;
	private String pass = null;
	private String webdriverPath = null;
	private ArrayList<Item> list;
	private ChromeOptions options;
	private ChromeDriver Driver;
	private IDataBase SQLDB;
	private ResultSet res;
	private eBayCalls eBayCaller;
	private int counter;
    private Robot rob;
    
	/* Constractor */
	public AmazonCalls() {
		InitFromFile();
		list = new ArrayList<Item>();
		options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", webdriverPath);
		Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		SQLDB = new SQLDataBase();
		eBayCaller = new eBayCalls();
		counter = 0;
		
		try {
			rob = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Init */
	private void InitFromFile() {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {

				if (sCurrentLine.contains("APass: ")) {
					pass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ") + "APass: ".length());
				}

				if (sCurrentLine.contains("Acid: ")) {
					user = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ") + "Acid: ".length());
				}

				if (sCurrentLine.contains("webdriver.chrome.driver:")) {
					webdriverPath = sCurrentLine.substring(
							sCurrentLine.indexOf("webdriver.chrome.driver:") + "webdriver.chrome.driver: ".length());
				}

				if (sCurrentLine.contains("Connection:")) {
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:") + 12);
				}

				if (sCurrentLine.contains("Schame:")) {
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:") + 8);
					Connection = Connection + scham;
				}

				if (sCurrentLine.contains("Schame:")) {
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:") + "Schame: ".length());
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
		int counter = 0;
		Boolean success = Login(Driver);
		if (false == success) {
			Login2(Driver);
		}

		while (true) 
		{

			readItems();
			for (Item ele : list) 
			{
				try {
					if (counter > 100)
					{
						RestDriver();
						counter = 0;
						Thread.sleep(1000*60);
					}
					else
					{
						counter++;
					}
					
					CheckWithScraper(ele);
					if (ele.isPrime()) {
						eBayCaller.UpdateItemPosition(ele);
						if ((((ele.getMarketPlaceLowestPrice()) / (ele.getCurrentSupplierPrice() + 0.3) > 1.14)
								&& (ele.getCurrentTax() == 0))
								|| (((ele.getMarketPlaceLowestPrice())
										/ (ele.getCurrentSupplierPrice() + ele.getCurrentTax() + 0.3) > 1.185)
										&& (ele.getCurrentTax() > 0))) {
							System.out.println("Item added " + ele.getSupplierCode());
							SQLDB.Write("UPDATE  amazon.productfromsellers SET asin = '" + ele.getSupplierCode()
									+ "' , Amazon_price = " + ele.getCurrentSupplierPrice()
									+ ", AmazonAvailavle = 1 , placeinlowestprice = " + ele.getPlaceInLowestPrice()
									+ " , BreakEvenForlowest = "
									+ (ele.getMarketPlaceLowestPrice())
											/ (ele.getCurrentSupplierPrice() + ele.getCurrentTax() + 0.3)
									+ " , arbitrajeLowestprice = " + ele.getArbitraje() + " , EbayLowestprice = "
									+ (ele.getMarketPlaceLowestPrice()) + " , Tax = " + ele.getCurrentTax()
									+ " where Code = '" + ele.getUniversalCode() + "';");
						} else {
							SQLDB.Write("DELETE FROM amazon.productfromsellers WHERE Code = '" + ele.getUniversalCode()
									+ "';");
						}
					} else {
						SQLDB.Write(
								"DELETE FROM amazon.productfromsellers WHERE Code = '" + ele.getUniversalCode() + "';");
					}

				} catch (Exception e) {
					SQLDB.Write("DELETE FROM amazon.productfromsellers WHERE Code = '" + ele.getUniversalCode() + "';");
					e.printStackTrace();
				}
			}

			list.clear();
			try {
				Thread.sleep(1000 * 20);

			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}

	}

	/* Private functions */

	private void CheckWithScraper(Item ele) throws InterruptedException {
		double temp;
		String CurrenteTax = null;
		String Asin = null;
		
		try {
			Thread.sleep(1000);
			counter++;
			System.out.println(counter);
			//waitForLoad(Driver);
			//Driver.get("https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%Daps&field-keywords="
				//	+ ele.getUniversalCode().replaceAll(" ", ""));
			Driver.get("https://www.amazon.com/ref=nb_sb_noss_null");
			Driver.findElement(By.xpath("//*[@id='twotabsearchtextbox']")).click();
			Driver.findElement(By.xpath("//*[@id='twotabsearchtextbox']")).clear();
			Driver.findElement(By.xpath("//*[@id='twotabsearchtextbox']"))
					.sendKeys(ele.getUniversalCode().replaceAll(" ", ""));
			Thread.sleep(1000);

			Driver.findElement(By.xpath("//*[@id='nav-search']/form/div[2]/div/input")).click();
			//waitForLoad(Driver);
			Thread.sleep(1000);


			if (Driver.findElements(By.id("noResultsTitle")).size() > 0) {
				ele.setCurrentSupplierPrice(-1);
				ele.setPrime(false);
				return;
			}

			try {
				Asin = Driver.findElementByXPath("//*[@id='result_0']/div/div/div/div[2]/div[1]/div/a")
						.getAttribute("href");
			} catch (Exception e) {
				Asin = Driver.findElementByXPath("//*[@id='result_0']/div/div[3]/div[1]/a").getAttribute("href");
			}

			Asin = Asin.substring(Asin.lastIndexOf("dp/") + 3, Asin.lastIndexOf("/ref="));
			Driver.get("https://www.amazon.com/gp/offer-listing/" + Asin
					+ "/ref=olp_f_new?ie=UTF8&f_all=true&f_new=true&f_primeEligible=true");
			Thread.sleep(1000);

			temp = Double.parseDouble(Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]")
					.getText().replace("$", ""));
			ele.setCurrentSupplierPrice(temp);

			try {

				if (Driver.findElements(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).size() > 0 && Driver.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText()
						.contains("FREE Shipping")) {
					CurrenteTax = Driver
							.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span"))
							.getText();
					CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+") + 3,
							CurrenteTax.indexOf("estimated tax") - 1);
				} else if (Driver.findElements(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).size()>0)
				{
					CurrenteTax = Driver.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();
					CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+") + 3,CurrenteTax.indexOf("estimated tax") - 1);
				}else
				{
					ele.setCurrentSupplierPrice(-1);
					ele.setPrime(false);
					return;
				}

				ele.setCurrentTax(Double.parseDouble(CurrenteTax));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (Driver.findElements(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[2]/i")).size() > 0)
			{
				if (Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[2]/i")
						.getAttribute("aria-label").contains("Eligible for free shipping with Amazon Prime.")) {
					ele.setSupplierCode(Asin);
					ele.setPrime(true);
				} else {
					ele.setPrime(false);
				}
			}else
			{
				ele.setPrime(false);
			}
		} catch (Exception e) {
			ele.setCurrentSupplierPrice(-1);
			ele.setPrime(false);
			e.printStackTrace();
			Thread.sleep(1000);
		}
	}
	
	
	
	private void RestDriver()
	{
		String currentUrl = Driver.getCurrentUrl();
		Driver.close();
		Driver.quit();
		Driver = null;
		Driver = new ChromeDriver(options);
		Driver.get(currentUrl);
	}
	
	
	
	
	
	private void readItems() {
		try {
			if (false == SQLDB
					.IsExcist("SELECT * FROM amazon.productfromsellers where AmazonAvailavle = 0;")) {
				return;
			}

			res = SQLDB.Read("SELECT * FROM amazon.productfromsellers where AmazonAvailavle = 0;");
			while (res.next()) {
				Item newItem = new Item();
				newItem.setCodeType(res.getString("CodeType"));
				newItem.setUniversalCode(res.getString("Code"));
				list.add(newItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean Login(ChromeDriver Driver) {
		try {
			Driver.get("https://www.amazon.com/");
			Thread.sleep(2000);
			Driver.findElementByXPath("//*[@id='nav-link-accountList']").click();
			Thread.sleep(2000);
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
			// Driver.findElementByXPath("//*[@id='continue']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private void Login2(ChromeDriver Driver) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Driver.findElementByXPath("//*[@id='ap_email']").click();
		Driver.findElementByXPath("//*[@id='ap_email']").clear();
		Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
		// Driver.findElementByXPath("//*[@id='continue']").click();
		Driver.findElementByXPath("//*[@id='ap_password']").click();
		Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
		Driver.findElementByXPath("//*[@id='signInSubmit']").click();
	}

	private void waitForLoad(ChromeDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			wait.until(pageLoadCondition);
		} catch (Exception e) {
			System.out.println("Exception on wait...");
			rob.keyPress(KeyEvent.VK_F5);
			rob.keyRelease(KeyEvent.VK_F5);
			

		}
	}
}
