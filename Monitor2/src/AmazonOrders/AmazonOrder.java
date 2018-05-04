package AmazonOrders;


import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ebay.soap.eBLBaseComponents.OrderType;

import CashBack.TopCashBack;
import Ebay.SalesRecord;
import Main.DatabaseMain;
import Main.Email;
import Main.Yaballe;
import PriceChanger.ItemsPosition;

public class AmazonOrder {

	public static java.sql.Connection con ;
	public static java.sql.Statement statement ;
	public static String Connection = null;
	public static String scham = null;
	public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public static String AWS_SECRET_KEY_FROM_FILE = null;
	public static String ENDPOINT_FROM_FILE = null;
	public static String Cid = null;
	public static String Acid = null;
	public AmazonOrder() {
		
		//System.out.println("Constractor of Database");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("AWS_ACCESS_KEY_ID:"))
				{
					AWS_ACCESS_KEY_ID_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_ACCESS_KEY_ID:")+19);
					//System.out.println("AWS_ACCESS_KEY_ID_FROM_FILE = "+AWS_ACCESS_KEY_ID_FROM_FILE);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
					//System.out.println("AWS_SECRET_KEY_FROM_FILE = "+AWS_SECRET_KEY_FROM_FILE);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
					//System.out.println("ENDPOINT = "+ENDPOINT_FROM_FILE);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Cid:"))
				{
					Cid = sCurrentLine.substring(sCurrentLine.indexOf("Cid:")+"Cid:".length());

				}
				
				if (sCurrentLine.contains("Acid:"))
				{
					Acid = sCurrentLine.substring(sCurrentLine.indexOf("Acid:")+"Acid:".length());

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

	public synchronized Order_report Order_on_amazon (OrderType order_details , String Asin,Order_report Report,String MonitorUser,String MonitorPassword) throws InterruptedException, AWTException
	{
		
		String Temp_price=null;
		String Temp_Fees=null;
		List<WebElement> Webelements = new ArrayList<WebElement>();
		String OrderNumber = null;
		DatabaseMain Db = new DatabaseMain();
		Double Price = null;
		Double Fees = null;
		int login = 0;
		int FBA = 1;//

		
		//Ebates Ebats = new Ebates();
		//ChromeDriver Driver = Ebats.EbatesMain(Asin);
		
		TopCashBack TopCashBack = new TopCashBack();
		ChromeDriver Driver = TopCashBack.MainTopCashBack(Asin);

		Thread.sleep(2000);
		CheckPopUpKindle(Driver);
		CheckAvailableFromTheseSellers(Driver);
		if (Driver==null)
		{
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		//options.addArguments("--user-data-dir=C:\\User Data");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver = new ChromeDriver(options);

		//Driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		Report.Code_place =1;// Here is the stage where we enter to the list//
		//Driver.get("https://www.amazon.com/");
		//Thread.sleep(2000);
		Driver.get("https://www.amazon.com/dp/"+Asin);
		Report.Asin = Asin;
		//Thread.sleep(2000);	
		}
		WebDriverWait wait = new WebDriverWait(Driver, 20);
		JavascriptExecutor jse = (JavascriptExecutor)Driver;

		login = 1;
		try{
		try{
		String PrimeCheck = Driver.findElement(By.xpath("//*[@id='pe-bb-signup-button-announce']")).getText();
		if (PrimeCheck.contains("Try Prime free for 30 days")) 
		{
			Driver.findElement(By.xpath("//*[@id='nav-link-accountList']")).click();
			//Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='ap_email']"))));

			Driver.findElement(By.xpath("//*[@id='ap_email']")).sendKeys(Acid);
			//Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='ap_password']"))));
			Driver.findElement(By.xpath("//*[@id='ap_password']")).sendKeys("a69fa2");
			//Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='signInSubmit']"))));
			Driver.findElement(By.xpath("//*[@id='signInSubmit']")).click();
			//Thread.sleep(2000);
			login = 1;
		}else 
		{
		wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='pe-bb-signup-button-announce']"))));
		Webelements = Driver.findElements(By.xpath("//*[@id='pe-bb-signup-button-announce']"));
		}
		}
		catch(Exception e)
		{
			System.out.println("Webelements on xpath error");
		}
		
		
		
		
		try{
		if (Webelements.size()>0 && Driver.findElement(By.xpath("//*[@id='pe-bb-signup-button-announce']")).getText().contains("Try Prime free for 30 days"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='pe-link-sign-in']"))));

				Driver.findElement(By.xpath("//*[@id='pe-link-sign-in']")).click();
				//Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='ap_email']"))));
				Driver.findElement(By.xpath("//*[@id='ap_email']")).sendKeys(Acid);
			//	Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='ap_password']"))));

				Driver.findElement(By.xpath("//*[@id='ap_password']")).sendKeys("a69fa2");
				//Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='signInSubmit']"))));

				Driver.findElement(By.xpath("//*[@id='signInSubmit']")).click();
				//Thread.sleep(2000);
				login = 1;
		}
		}catch(Exception e)
		{
			System.out.println("NO Try Prime free for 30 days!");
		}
		
		
		Report.Code_place =2;// Here is the stage where add to card//
		
		try{
		CleanCard(Driver,wait);
		}catch(Exception e){System.out.println("Clean crad exception");}

		
		

		
		
		Report.Code_place =2;// Here is the stage where add to card//
		
		try{
		Checkcoupon(Driver);
		}catch(Exception e){}
		Thread.sleep(2000);
		if (Driver.findElementsByXPath("//*[@id='ap_email']").size()>0)
		{
		Login2(Driver);
		login = 1;
		}
		
		
		wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='add-to-cart-button']"))));
		Driver.findElement(By.xpath("//*[@id='add-to-cart-button']")).click();
		
		//Thread.sleep(2000);
		
		
		System.out.println("checking if No thanks pop up is displayed");
		try{
			Thread.sleep(2000);
			Webelements = Driver.findElementsByTagName("button");
			for (WebElement ele:Webelements)
			{
				if (ele.getText().contains("No Thanks")) ele.click();
			}
			//Driver.findElementByXPath("//*[@id='siNoCoverage-announce']").click();
		}catch(Exception e)
		{
			System.out.println("No Thanks exception");
			//Driver.close();
		}
		 

		
		try{
			//Thread.sleep(2000);//*[@id="hlb-view-cart"]/span
		Webelements = Driver.findElements(By.className("a-button-inner"));
		for (WebElement ele:Webelements)
		{
			if (ele.getText().contains("No thanks"))
			{
				System.out.println("************************");
				ele.click();
				break;
			}
		}// end of for //
		}catch(Exception e1){}
		
		
		/* no thanks here*/
		/*login here*/
		try{
		SetLogin(Driver);
		SetPass(Driver);
		}catch(Exception e){}
		
		Report.Code_place =3; // Here is the stage where press card//

		try{
		wait.until(ExpectedConditions.elementToBeClickable(	Driver.findElement(By.xpath("//*[@id='hlb-view-cart-announce']"))));
		Driver.findElement(By.xpath("//*[@id='hlb-view-cart-announce']")).click();
		//Thread.sleep(100);
		}catch(Exception e)
		{
			try{
			wait.until(ExpectedConditions.elementToBeClickable(	Driver.findElement(By.xpath("//*[@id='smartShelfAddToCartNative']"))));
			Driver.findElement(By.xpath("//*[@id='smartShelfAddToCartNative']")).click();
			}catch(Exception er){
				
				Report.ExceptionE = e.getMessage();
				Report.Message = "Cart button press error";
				for(String handle : Driver.getWindowHandles()) 
				{
				 Driver.switchTo().window(handle);
				 Driver.close();
				}
			return Report;
			}
		}
		
		
		
		
		Report.Code_place =4; // Here is the stage where we mark the gift //
		try{
	
			FBA =SetAsGift(Driver); 
			}catch(Exception e){
			Report.ExceptionE = e.getMessage();
			Report.Message = "Error in Fba loop";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		
		// here is the testing stage where we set quantity of the order //
		
		if (order_details.getTransactionArray().getTransaction(0).getQuantityPurchased()>1)
		{
		Email email = new Email();
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='a-autoid-3-announce']")));
			Driver.findElementByXPath("//*[@id='a-autoid-3-announce']").click();
		//	Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='a-popover-3']/div/div/ul/li["+order_details.getTransactionArray().getTransaction(0).getQuantityPurchased()+"]")));
			Driver.findElementByXPath("//*[@id='a-popover-3']/div/div/ul/li["+order_details.getTransactionArray().getTransaction(0).getQuantityPurchased()+"]").click();
			//Thread.sleep(1000);
			email.Send_report_mail(Asin, null, null, "More than 1 item order check it");
			email = null;
			System.gc();
		}catch(Exception e){}
		}
		
		
		

		Report.Code_place =5;// Here is the stage where press Proceed to checkout//
		try{
		//Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='sc-buy-box-ptc-button']/span/input"))));
		Driver.findElement(By.xpath("//*[@id='sc-buy-box-ptc-button']/span/input")).click();
		}catch(Exception e){
			Report.ExceptionE = e.getMessage();
			Report.Message = "Error in buy-box-ptc-button";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return Report;
		}
		
		
		
		
		
		Thread.sleep(500);
		Report.Code_place =6; // Here is the stage where we login to the account //
		int flag =0;
		try{
		if (Driver.findElement(By.xpath("//*[@id='ap_email']")).isDisplayed()) flag = 1;
		}catch(Exception e){}
		try{
		if (login==0||flag==1)			
		{
		Thread.sleep(500);
		SetLogin(Driver);
		SetPass(Driver);
		login = 1;
		}
		}catch(Exception e){
			Report.ExceptionE = e.getMessage();
			Report.Message = "Login Error";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		
		
		
		if (login ==0)	
		Login2(Driver);
		
		Thread.sleep(3000);
		System.out.println("Before Order summery");
		int giftcardpayment = 0;
		try{
		if (Driver.findElementByXPath("//*[@id='payment-information']/div[2]/span[2]").isDisplayed())
		giftcardpayment = CheckIfGiftCard(Driver);
		}catch(Exception e){System.out.println("CheckIfGiftCard");}
		
		try
		{	
		if (FBA==1)
		{
		if (Driver.findElementByXPath("//*[@id='spc-order-summary']/h3").getText().equals("Order Summary"))
		{
			Driver.findElementByXPath("//*[@id='addressChangeLinkId']").click();
		}
		}
		}catch(Exception e)
		{
			System.out.println("Exception Order summery");

			Report.ExceptionE = e.getMessage();
			Report.Message = "Error in setting order details";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		
		}
		
		Report.Code_place =7; // Here is the stage where we set order details //
		try{
		try{
		Thread.sleep(6000);
	    jse.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
		Thread.sleep(2000);
		}
	    catch(Exception ee){}
		//if (order_details.isIsMultiLegShipping()==false)
		//{
	   // Thread.sleep(2000);     
		wait.until(ExpectedConditions.elementToBeClickable(	Driver.findElement(By.xpath("//*[@id='add-new-address-popover-link']"))));
		Driver.findElement(By.xpath("//*[@id='add-new-address-popover-link']")).click();
		//Thread.sleep(2000);														
		Driver.findElement(By.xpath("//*[@id='enterAddressFullName']")).sendKeys(order_details.getShippingAddress().getName());
		Report.BuyerName = order_details.getShippingAddress().getName();
		//Thread.sleep(2000);
		Driver.findElement(By.xpath("//*[@id='enterAddressAddressLine1']")).sendKeys(order_details.getShippingAddress().getStreet1());
		Report.Buyer_Street1 = order_details.getShippingAddress().getStreet1();
		//Thread.sleep(2000);
		Driver.findElement(By.xpath("//*[@id='enterAddressAddressLine2']")).sendKeys(order_details.getShippingAddress().getStreet2());
		Report.Buyer_Street2 = order_details.getShippingAddress().getStreet2();
		//Thread.sleep(2000);
		Driver.findElement(By.xpath("//*[@id='enterAddressCity']")).sendKeys(order_details.getShippingAddress().getCityName());
		Report.Buyer_CityName = order_details.getShippingAddress().getCityName();
		//Thread.sleep(2000);
		Driver.findElement(By.xpath("//*[@id='enterAddressStateOrRegion']")).sendKeys(order_details.getShippingAddress().getStateOrProvince());
		Report.Buyer_StateOrProvince = order_details.getShippingAddress().getStateOrProvince();
		//Thread.sleep(2000);
		Driver.findElement(By.xpath("//*[@id='enterAddressPostalCode']")).sendKeys(order_details.getShippingAddress().getPostalCode());
		Report.Buyer_PostalCode = order_details.getShippingAddress().getPostalCode();
		//Thread.sleep(2000);
		if (order_details.getShippingAddress().getPhone().length()==0)
		{
			Driver.findElement(By.xpath("//*[@id='enterAddressPhoneNumber']")).sendKeys("054 780 5987");
			Report.Buyer_Phone = "054 780 5987";

		}else
		{
		Driver.findElement(By.xpath("//*[@id='enterAddressPhoneNumber']")).sendKeys(order_details.getShippingAddress().getPhone());
		Report.Buyer_Phone = order_details.getShippingAddress().getPhone();
		}
		//}else 
		//{
			/*
		    //Thread.sleep(2000); 
			wait.until(ExpectedConditions.elementToBeClickable(	Driver.findElement(By.xpath("//*[@id='add-new-address-popover-link']"))));
			Driver.findElement(By.xpath("//*[@id='add-new-address-popover-link']")).click();
			//Thread.sleep(2000);															

			Driver.findElement(By.xpath("//*[@id='enterAddressFullName']")).sendKeys(order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getName());
			Report.BuyerName = order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getName();
			//Thread.sleep(2000);
			Driver.findElement(By.xpath("//*[@id='enterAddressAddressLine1']")).sendKeys(order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getStreet1());
			
			Report.Buyer_Street1 = order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getStreet1();
			//Thread.sleep(2000);
			Driver.findElement(By.xpath("//*[@id='enterAddressAddressLine2']")).sendKeys(order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getReferenceID());
			Report.Buyer_Street2 = order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getReferenceID();
			//Thread.sleep(2000);
			Driver.findElement(By.xpath("//*[@id='enterAddressCity']")).sendKeys(order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getCityName());
			
			Report.Buyer_CityName = order_details.getShippingAddress().getCityName();
			//Thread.sleep(2000);
			Driver.findElement(By.xpath("//*[@id='enterAddressStateOrRegion']")).sendKeys(order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getStateOrProvince());
			Report.Buyer_StateOrProvince = order_details.getShippingAddress().getStateOrProvince();
			//Thread.sleep(2000);
			Driver.findElement(By.xpath("//*[@id='enterAddressPostalCode']")).sendKeys("41025");
			Report.Buyer_PostalCode = order_details.getMultiLegShippingDetails().getSellerShipmentToLogisticsProvider().getShipToAddress().getPostalCode();
			//Thread.sleep(2000);
			if (order_details.getShippingAddress().getPhone().length()==0)
			{
				Driver.findElement(By.xpath("//*[@id='enterAddressPhoneNumber']")).sendKeys("054 780 5987");
				Report.Buyer_Phone = "054 780 5987";

			}else
			{
			Driver.findElement(By.xpath("//*[@id='enterAddressPhoneNumber']")).sendKeys(order_details.getShippingAddress().getPhone());
			Report.Buyer_Phone = order_details.getShippingAddress().getPhone();
			}
			*/	
		//}
		}catch(Exception e1){
			Report.ExceptionE = e1.getMessage();
			Report.Message = "Error in setting order details";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		
		//order_details.getOrderStatus().
		
		
		Report.Code_place =8; // Here we press on use this address button//

		try{
			//Thread.sleep(100);	
			int clicked = 0;
			//Driver.findElement(By.xpath("//*[@id='newShippingAddressFormFromIdentity']/div[1]/div/form/div[6]/span/span/input")).click();
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='a-popover-1']/div/div[3]/div/span[1]/span/span/span"))));
			if (Driver.findElement(By.xpath("//*[@id='a-popover-1']/div/div[3]/div/span[1]/span/span/span")).getText().contains("Use this address")) 
			{
			
			Driver.findElement(By.xpath("//*[@id='a-popover-1']/div/div[3]/div/span[1]/span/span/input")).click(); 
			clicked=1;
			Thread.sleep(4000);	
				
				// here to put the second click
			try{
			Driver.findElement(By.xpath("//*[@id='a-popover-1']/div/div[3]/div/span[1]/span/span/input")).click(); 
			clicked=2;
			}catch(Exception e1){

				Webelements = Driver.findElements(By.className("input"));
				for (WebElement ele:Webelements)
				{
					if (ele.getText().contains("Use this address"))
					{
						ele.click();
						clicked=2;
						break;
					}
				}	

			}
			
			try{
			if (clicked==1)
			{
			Driver.findElement(By.xpath("//*[@id='a-popover-2']/div/div[3]/div/span[1]/span/span/input")).click(); 
			}
			}catch(Exception e1){
			try{
			Webelements = Driver.findElements(By.className("input"));
			for (WebElement ele:Webelements)
			{
				if (ele.getText().contains("Use this address"))
				{
					ele.click();
					break;
				}
			}// end of for //	
			}catch(Exception e){}
			}
			
			Thread.sleep(4000);	
			if (clicked==0&&Driver.findElement(By.xpath("//*[@id='a-popover-2']/div/div[3]/div/span[1]/span/span/span")).getText().contains("Use this address")) 
			{
			Driver.findElement(By.xpath("//*[@id='a-popover-2']/div/div[3]/div/span[1]/span/span/input")).click(); 
			}
			}
		}catch(Exception e1){
			
			Report.ExceptionE = e1.getMessage();
			Report.Message = "Message in Use this address button";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		

		if(giftcardpayment==0)
		{
		
		Report.Code_place =9; // here the new pop up window //
		try{
			Thread.sleep(4000);
			Webelements = Driver.findElements(By.tagName("input"));
			for (WebElement ele:Webelements)
			{
				if (ele.getAttribute("name").equals("useSelectedAddress")&&ele.getAttribute("class").equals("a-button-input")&&ele.getAttribute("type").equals("submit")) 
				{
					ele.click();
					Report.PopUp_Correction_Address = "Yes";
					break;
				}
			}
			System.out.println("No pop up to correct the address");
			Report.PopUp_Correction_Address = "No";
			}
			catch(Exception e){} //meanwhile i don't make the catch//
		
		Report.Code_place =9; // here the new pop up window //
		
		//try{
		//Thread.sleep(5000);
	//	}catch(Exception e){System.out.println("Thread sleep exception");}
		

		
		
		
		if (FBA==0)
		{
			SetThankYou(Driver, Report,order_details);
		}
		//System.out.println(" after  FBA = 0");
		Report.Code_place =10; // Credit card check selected //
		try{ //here need a change //
		Thread.sleep(5000);
		if (Driver.findElements(By.xpath("//*[@id='pm_gc_radio']")).size()>0)
		{
		if (Driver.findElement(By.xpath("//*[@id='pm_gc_radio']")).isSelected()==false)
		{
		if (Driver.findElements(By.xpath("//*[@id='pm_0']")).size()>0)
		{
		if (Driver.findElement(By.xpath("//*[@id='pm_0']")).isSelected()==false) 
		{
	//	wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='pm_0']"))));
		Driver.findElement(By.xpath("//*[@id='pm_0']")).click();
		Thread.sleep(100);
	//	wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='addCreditCardNumber']"))));
		try{
		Driver.findElement(By.xpath("//*[@id='addCreditCardNumber']")).sendKeys(Cid);
		Thread.sleep(1000);
		}catch(Exception e){Thread.sleep(1000);}
		//wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='confirm-card']"))));
		Driver.findElement(By.xpath("//*[@id='confirm-card']")).click();
		Thread.sleep(3000);
		}
		}
		}
		}else
		{
		if (Driver.findElements(By.xpath("//*[@id='pm_0']")).size()>0)
		{
		if (Driver.findElement(By.xpath("//*[@id='pm_0']")).isSelected()==false) 
		{
		//wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='pm_0']"))));
		Driver.findElement(By.xpath("//*[@id='pm_0']")).click();
		Thread.sleep(100);
	//	wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='addCreditCardNumber']"))));
		try{
		Driver.findElement(By.xpath("//*[@id='addCreditCardNumber']")).sendKeys(Cid);
		Thread.sleep(1000);
		}catch(Exception e){Thread.sleep(1000);}
	//	wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='confirm-card']"))));
		Driver.findElement(By.xpath("//*[@id='confirm-card']")).click();
		Thread.sleep(3000);
		}
		}
			
		}
		Thread.sleep(3000);
	//	wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='continue-top']")));

		if (Driver.findElementsByXPath("//*[@id='continue-top']").size()>0)
		Driver.findElement(By.xpath("//*[@id='continue-top']")).click();
		//Thread.sleep(3000);
		}catch(Exception e){
			System.out.println("###");
			Report.ExceptionE = e.getMessage();
			Report.Message = "Credit card step error";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		
		} // gift card fix test //
		
		
		Report.Code_place =11; // Fba = 1 send keys //
		//Thread.sleep(2000);
		//System.out.println("FBA = "+FBA);
		if (FBA==1) 
		{
			try{
				System.out.println("Clicking on add a gift card");
				Thread.sleep(3000);
			Driver.findElement(By.xpath("//*[@id='spc-orders']/div/div/div[2]/div/div/div[3]/div[1]/div/div/div/div/div/div[2]/div[6]/div[1]/span/span[2]")).click(); // try catch !!
			}catch(Exception e)
			{
				try{
					Webelements = Driver.findElements(By.tagName("span"));
					for (WebElement ele:Webelements)
					{										
						if (ele.getText().contains("Add a gift receipt")) 
						{
							ele.click();
							break;
						}
					}
					}catch(Exception e1){
						System.out.println("Exit from thread here is the problem");

						System.out.println(e1);
						Report.ExceptionE = e.getMessage();
						Report.Message = "Error in submit Order button";
						for(String handle : Driver.getWindowHandles()) 
						{
						 Driver.switchTo().window(handle);
						 Driver.close();
						}

					
						return Report;}
			}
			System.out.println("Clicking on add a gift card FINISH!");
			//Thread.sleep(2000);
			if (order_details.getTransactionArray().getTransaction(0).getQuantityPurchased()>1)
			{
			Thread.sleep(2000);
			SetThankYou(Driver, Report,order_details);
			}
			else
			{
			Thread.sleep(2000);
			test(Driver,wait);
			}
	
			
		}
		
		Report.Code_place =12; // Get info of the price to database//
		try{
		Thread.sleep(6000);
		//wait.until(ExpectedConditions.elementToBeClickable(Driver.findElement(By.xpath("//*[@id='subtotals-marketplace-table']/table/tbody/tr[4]/td[2]"))));
		// Here need to calculate the right profit after cupons or discounts //									
		Temp_price = Driver.findElement(By.xpath("//*[@id='subtotals-marketplace-table']/table/tbody/tr[4]/td[2]")).getText();
		Temp_price = Temp_price.substring(Temp_price.indexOf("$")+1);
		Temp_Fees  = Driver.findElement(By.xpath("//*[@id='subtotals-marketplace-table']/table/tbody/tr[5]/td[2]")).getText();
		Temp_Fees = Temp_Fees.substring(Temp_Fees.indexOf("$")+1);
		Price = Double.parseDouble(Temp_price);
		//if (Price>400) return (Report);
		Fees  = Double.parseDouble(Temp_Fees);
		price_and_fees_write_to_database(order_details.getOrderID(),Price,Fees);
		}catch (Exception e) 
		{
			System.out.println("Can't get Price and fees");
		}
		
		
		Report.Code_place =13; //submit Order//
		try{
		Thread.sleep(1000);	
		try{
		jse.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
		}catch(Exception e){}
		Driver.findElement(By.xpath("//*[@id='submitOrderButtonId']/span/input")).click();
		}catch(Exception e)
		{
			System.out.println(e);
			Report.ExceptionE = e.getMessage();
			Report.Message = "Error in submit Order button";
			for(String handle : Driver.getWindowHandles()) 
			{
			 Driver.switchTo().window(handle);
			 Driver.close();
			}
			return (Report);
		}
		
		
		Report.Code_place =13; //Order number get error//
		try{
		Thread.sleep(4000);
	
		Webelements = Driver.findElementsByTagName("span");
		for (WebElement ele:Webelements)
		{
			if (ele.getAttribute("id").contains("order-number"))
			{
				OrderNumber = ele.getAttribute("id");
				OrderNumber = OrderNumber.substring(OrderNumber.indexOf("number-")+7);
				break;
			}
		}
		Order_number_write_to_database(order_details.getOrderID(),OrderNumber);
		Report.Database_update = "Order number database update success";
		}catch(Exception e) {
			Report.Database_update = "Error";
			System.out.println("Can't write the amazon order number to database");
		}
		
		Report.Code_place =14; //Update profit error//
		try
		{
		Db.Update_profit(order_details.getOrderID(),order_details.getAmountPaid().getValue(), Price, Fees);
		Report.Database_update = "Order number + Profit database update success";
		}catch(Exception e)
		{
			Report.Database_update = Report.Database_update+ "Profit database update Error";
			System.out.println("Can't write the profit to database");
		}

		
		}catch(Exception e3) {				
			Driver.close();
			System.out.println("Error in amazon ordering process");
			return (Report);
		}			

		Report.Code_place =15; //Success//
		Report.Message = "Success";
		Report.ExceptionE = "No Exception";
		
		for(String handle : Driver.getWindowHandles()) 
		{
		 Driver.switchTo().window(handle);
		 Driver.close();
		}
		//Driver.close();
		try{
		//ChangeSetings(Asin);
			//order_details.getAmountPaid().getValue(), Price, Fees
		//if (Fees>0||((order_details.getAmountPaid().getValue()*0.871-0.3-Fees)<Price)) //here we need to change //
		//ChangeSetingsAutoDsTool(Asin);
		/*RiseItemPriceYaballe(Asin,Fees,MonitorUser,MonitorPassword);*/
		SalesRecord SalesRecord = new SalesRecord(Asin);
		try{
		SalesRecord.SalesRecordMain(Asin);
		}catch(Exception e){System.out.println("Exception in sales record");}
		
		}catch(Exception e)
		{
			System.out.println("Error in ChangeSetings(Asin);");
		}
		
		
		
		 Temp_price=null;
		 Temp_Fees=null;
		 Webelements = null;
		 OrderNumber = null;
		 Db = null;
		 Price = null;
		 Fees = null;
		 TopCashBack = null;
		 Driver = null;
		 System.gc();
		 return Report;
	}

	public synchronized double GetBreakEven(String Asin)
	{

		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where amazonasin = '"+Asin+"';"); 
			res.next();
			return res.getDouble("BreakEven");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	
	}
	
	public synchronized void price_and_fees_write_to_database(String OrderId,Double price ,Double fees) throws SQLException
	{
		 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();//

				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET Amazon_price_before_tax="+price+", Fees = "+fees+" WHERE OrderId='"+OrderId+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}			
	}
	
	public synchronized void Order_number_write_to_database(String OrderId,String OrderNumber) throws SQLException
	{
		 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();//

				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET Amazon_OrderNumber='"+OrderNumber+"' WHERE OrderId='"+OrderId+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e){e.printStackTrace();}			
	}
	
	public synchronized void SetThankYou(ChromeDriver Driver,Order_report Report,OrderType OrderInfo ) throws InterruptedException
	{

		System.out.println("Quantity Purchased = "+OrderInfo.getTransactionArray().getTransaction(0).getQuantityPurchased());
		for(int i=0;i<OrderInfo.getTransactionArray().getTransaction(0).getQuantityPurchased();i++)
		{						
			
			try{ 
				Thread.sleep(3000);
				Driver.findElement(By.xpath("//*[@id='message-area-"+i+"']")).clear();
				Driver.findElement(By.xpath("//*[@id='message-area-"+i+"']")).sendKeys("Thank you for buying with us!");
				}catch(Exception e) 
				{
					try{
						System.out.println("No box for comment");
						Driver.findElement(By.xpath("//*[@id='giftForm']/div/div[2]/div/span[1]/span/input")).click();
						Thread.sleep(4000);
					}catch(Exception e1){
						Driver.close();
					}

				}	
		}
		try{
		if (Driver.findElement(By.xpath("//*[@id='giftForm']/div/div[2]/div/span[1]/span/input")).isDisplayed())
		Driver.findElement(By.xpath("//*[@id='giftForm']/div/div[2]/div/span[1]/span/input")).click();
		}catch(Exception e){}
		Thread.sleep(2000);
	}

	public synchronized void test(ChromeDriver Driver,WebDriverWait wait)
	{

		try{
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='a-popover-content-3']/div/div/ol/li[2]/span/div/div[2]/div/div[1]/textarea")));
			Driver.findElementByXPath("//*[@id='a-popover-content-3']/div/div/ol/li[2]/span/div/div[2]/div/div[1]/textarea").click();
			Thread.sleep(500);
			Driver.findElementByXPath("//*[@id='a-popover-content-3']/div/div/ol/li[2]/span/div/div[2]/div/div[1]/textarea").clear();
			Thread.sleep(500);
			Driver.findElementByXPath("//*[@id='a-popover-content-3']/div/div/ol/li[2]/span/div/div[2]/div/div[1]/textarea").sendKeys("Thank you for buying with us!");
			}catch(Exception e){
				try{
				List<WebElement> Webelements = new ArrayList<WebElement>();
				Webelements = Driver.findElementsByTagName("textarea");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("placeholder").equals("Add your message.")) 
						{
						ele.clear();
						ele.sendKeys("Thank you for buying with us!");
						break;
						}
				}
				Webelements = null;
				}catch(Exception e1){return;}
			}
		
		
		try{
			Thread.sleep(2000);			
			try{
			Driver.findElementByXPath("//*[@id='a-popover-content-2']/div/div/ol/li[3]/span/span[2]/span/input").click();
			return;
			}catch(Exception e2){}
			
			try{ 					   
			Driver.findElementByXPath("//*[@id='a-popover-content-3']/div/div/ol/li[3]/span/span[2]/span/input").click();
			return;
			}catch(Exception e2){}
		}catch(Exception e){
			try{
				int clicked = 0;
			
				List<WebElement> Webelements = new ArrayList<WebElement>();
				try{
				Thread.sleep(1000);
				Webelements = Driver.findElementsByTagName("span");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("class").equals("a-button-inner"))
					{
						
						ele.click();
						clicked = 1;
						break;
					}
				}
				}catch(Exception e1){}
				
				
				try{
				Thread.sleep(1000);
				if (clicked==0)
				{
				Webelements = Driver.findElementsByTagName("input");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("class").equals("a-button-input"))
					{
						ele.click();
						clicked = 1;
						break;
					}
				}
				}
				}catch(Exception e1){}
				
				try{
				Thread.sleep(1000);
				if (clicked==0)
				{
				Webelements = Driver.findElementsByTagName("span");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("class").equals("a-button-text"))
					{
						ele.click();
						clicked = 1;
						break;
					}
				}
				}
				}catch(Exception e1){}
				
				
				try{
				Thread.sleep(1000);
				if (clicked==0)
				{
				Webelements = Driver.findElementsByTagName("span");
				for (WebElement ele:Webelements)
				{
					if (ele.getText().contains("Save gift options and continue"))
					{
						ele.click();
						clicked = 1;
						break;
					}
				}
				}
				}catch(Exception e1){}
				
				try{
				Thread.sleep(1000);
				if (clicked==0)
				{
				Webelements = Driver.findElementsByTagName("span");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("class").equals("a-button a-spacing-mini a-button-primary set-gift-options-button"))
					{
						ele.click();
						clicked = 1;
						break;
					}
				}
				}
				}catch(Exception e1){}
				
				
				System.out.println("clicked ="+clicked);
				}catch(Exception e1){return;}
			}
		/*
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='submitOrderButtonId']/span/input")));
			Driver.findElementByXPath("//*[@id='submitOrderButtonId']/span/input").click();
			}catch(Exception e){
				try{
				List<WebElement> Webelements = new ArrayList<WebElement>();
				Webelements = Driver.findElementsByTagName("input");
				for (WebElement ele:Webelements)
				{
					if (ele.getAttribute("type").equals("submit")) 
						{
						ele.click();
						break;
						}
				}
				}catch(Exception e1){return;}
			}
		*/
	}
	
	public synchronized void ChangeSetings(String Asin) throws InterruptedException // Changing profit of item //
	{
		DatabaseMain Db = new DatabaseMain();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Driver.get("https://www.priceyak.com/stores/5H6FriU3XKH6R2GtA6X31c/listings?count=50000");
		Thread.sleep(3000);
		List<WebElement> list = new ArrayList<WebElement>();
		list = Driver.findElementsByTagName("input");
		for (WebElement ele:list)
		{
			if (ele.getAttribute("name").equals("email")) ele.sendKeys("arthur.boim@gmail.com");
			if (ele.getAttribute("name").equals("password")) ele.sendKeys("b0104196");
		}
		Driver.findElement(By.xpath("//*[@id='loginForm']/button")).click();
		Thread.sleep(3000);
		Driver.get("https://www.priceyak.com/stores/5H6FriU3XKH6R2GtA6X31c/listings?count=50000");
		int i=2;
		do
		{

			if (Driver.findElementByXPath("/html/body/div[3]/table/tbody/tr["+i+"]/td[2]/div/a").getText().equals(Asin))
			{
				System.out.println(Driver.findElementByXPath("/html/body/div[3]/table/tbody/tr["+i+"]/td[6]/a").getText());
				System.out.println(i);
				
				Driver.findElementByXPath("/html/body/div[3]/table/tbody/tr["+i+"]/td[6]/a").sendKeys("");
				Thread.sleep(2000);
				Driver.findElementByXPath("/html/body/div[3]/table/tbody/tr["+i+"]/td[6]/a").click();
				i = -1; 				   
			}else i++;
		}while(i!=-1);
		
		WebElement element = Driver.findElement(By.tagName("input"));
		JavascriptExecutor js = (JavascriptExecutor)Driver;
		js.executeScript("arguments[0].scrollIntoView();", element); 
	
		int profitnow = -1;
		Thread.sleep(5000);
		list = Driver.findElementsByTagName("input");
		for (WebElement ele:list)
		{
			try{
			//System.out.println(ele.getAttribute("class"));
			if (ele.getAttribute("name").equals("override_pricer.margin_percent")) 
				{
				ele.click();
				profitnow = Db.GetOnlineItem(Asin);
				System.out.println("profitnow = "+profitnow);
				if (profitnow<12&&profitnow>5) 
					{
					profitnow +=2 ;
					ele.clear();
					ele.sendKeys(""+profitnow);
					System.out.println("Change profit to "+profitnow);
					Db.UpdateOnlineItem(Asin, profitnow);
					Thread.sleep(3000);
					element = Driver.findElement(By.xpath("//*[@id='updateListingSettingsForm']/button[1]"));
					js = (JavascriptExecutor)Driver;
					js.executeScript("arguments[0].scrollIntoView();", element); 
					Driver.findElementByXPath("//*[@id='updateListingSettingsForm']/button[1]").click();
					Thread.sleep(3000);
					System.out.println("Change sucsses");
					}
				break;
				}
			}
			catch(Exception e)
			{
				System.out.println("Can't get override_quantity");
			}
			
		}
		
		System.out.println("Sucsses");
		Driver.close();
	}

	public synchronized void ChangeSetingsAutoDsTool(String Asin) throws InterruptedException, SQLException
	{
		
		DatabaseMain Db = new DatabaseMain();
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		Driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Driver.get("https://autodstools.com/");
		Driver.findElementByXPath("//*[@id='email']").sendKeys("arthur.boim@gmail.com");
		Driver.findElementByXPath("//*[@id='password']").sendKeys("b0104196");
		Driver.findElementByXPath("//*[@id='form_login']/div[4]/button").click();
		Thread.sleep(4000);
		Driver.get("https://autodstools.com/active_listings/?ln=500");
		Thread.sleep(7000);       
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").sendKeys(Asin);
		Thread.sleep(17000);

		try{
					
				Driver.findElementByXPath("//*[@id='products_table']/tbody/tr/td[2]/span/img").click();
				Thread.sleep(2000);
				int profitnow = Db.GetOnlineItem(Asin);
				System.out.println("Old profit percent ="+profitnow);
				if (profitnow<6) profitnow+=2;
				System.out.println("New profit percent ="+profitnow);
				
				Driver.findElementByXPath("//*[@id='new_additional_percentage']").clear();
				Driver.findElementByXPath("//*[@id='new_additional_percentage']").sendKeys(""+profitnow);
				Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[6]").click();						   
				Thread.sleep(2000);
				Driver.findElementByXPath("/html/body/div[9]/div[7]/div/button").click();
				Db.UpdateOnlineItem(Asin, profitnow);
				Thread.sleep(10000);
				System.out.println("HERE");
				//Driver.findElementByXPath("/html/body/div[8]/div[7]/div/button").click();
				Driver.close();
		}catch(Exception e)
		{
			System.out.println("Error update profit");
			Driver.close();
		}

	}
	
	public void RiseItemPriceYaballe(String Asin,double tax,String MonitorUser,String MonitorPassword) throws InterruptedException, SQLException
	{
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		//Yaballe Login = new Yaballe();
		ItemsPosition YaballePage = new ItemsPosition();
		Yaballe.YaballeLogin(Driver,MonitorUser,MonitorPassword);
	
		YaballePage.ItemSearch(Driver, Asin);
		
		YaballePage.SetRisePriceFileds(Driver,tax);
		Thread.sleep(2000);
		Driver.close();
	}
	
	public synchronized void CleanCard(ChromeDriver Driver,WebDriverWait wait) throws InterruptedException
	{
	
		wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='nav-cart-count']")));

		if (!Driver.findElementByXPath("//*[@id='nav-cart-count']").getText().equals("0"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(Driver.findElementByXPath("//*[@id='nav-cart']/span[3]")));

			Driver.findElementByXPath("//*[@id='nav-cart']/span[3]").click();
			//Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(	Driver.findElementByXPath("//*[@id='activeCartViewForm']/div[2]/div/div[4]/div/div[1]/div/div/div[2]/div/span[1]")));

			Driver.findElementByXPath("//*[@id='activeCartViewForm']/div[2]/div/div[4]/div/div[1]/div/div/div[2]/div/span[1]").click();
			Thread.sleep(2000);
			Driver.navigate().back();
			Thread.sleep(2000);
			Driver.navigate().refresh();
		}
	}
	
	public int  CheckIfGiftCard(ChromeDriver Driver)
	{
		try{
		if (Driver.findElementByXPath("//*[@id='payment-information']/div[2]/span[2]").getText().contains("gift card balance"))
		{
			System.out.println("Paying with gift card");
			return 1;
		}
		else return 0;
		}catch(Exception e){System.out.println("CheckIfGiftCard");}
		return 0;
	}
	
	public int  Login2(ChromeDriver Driver)
	{	
		SetLogin(Driver);
		SetPass(Driver);
		return 0;
	}
	
	public void  SetLogin(ChromeDriver Driver)
	{
		try{							//*[@id="ap_email"]
		if (Driver.findElementsByXPath("//*[@id='ap_email']").size()>0)
		{
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(Acid);
			try {
			if (Driver.findElementsByXPath("//*[@id='continue']").size()>0) //*[@id="continue"]
			Driver.findElementByXPath("//*[@id='continue']").click();
			Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	    }catch(Exception e){System.out.println(e);}
	}
	
	public void  SetPass(ChromeDriver Driver)
	{
		try{
		if (Driver.findElementsByXPath("//*[@id='ap_password']").size()>0)
		{
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys("a69fa2");
			if (Driver.findElementsByXPath("//*[@id='signInSubmit']").size()>0) 
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		}
		}catch(Exception e){System.out.println(e);}
	}
	
	public void  Checkcoupon(ChromeDriver Driver) throws InterruptedException
	{
		Thread.sleep(2000);
		try{
		List<WebElement> Webelements = Driver.findElementsByTagName("label");
		for (WebElement ele:Webelements)
		{
			if (ele.getText().contains("Save an extra")&&ele.getText().contains("coupon"))
			{							
				Driver.findElementByXPath("//*[@id='vpcButton']/div/label/i").click();
				Thread.sleep(2000);
				break;
			}
		}
		}catch(Exception e){System.out.println(e);}
	
	}
	
	public void  ClickConfirm(ChromeDriver Driver)
	{
		
	}
	
	public void  CheckPopUpKindle(ChromeDriver Driver) throws InterruptedException
	{
		Thread.sleep(1000);
		try{
		if (Driver.findElementByXPath("//*[@id='a-popover-3']/div/div[3]/div/span[1]").isDisplayed())
		{
			Driver.findElementByXPath("//*[@id='a-popover-3']/div/div[3]/div/span[1]").click();
		}
		}catch(Exception e)
		{
			System.out.println("No kindle pop up");
		}
		Thread.sleep(1000);
	}
	
	public void  CheckAvailableFromTheseSellers(ChromeDriver Driver)
	{
		try{
		if (Driver.findElementByXPath("//*[@id='availability']/span").getText().contains("Available from"))
		{
			System.out.println("Loging needed");
			Driver.findElementByXPath("//*[@id='nav-link-accountList']").click();
		}
		}catch(Exception e)
		{
			System.out.println("Amazon Available");
		}
	}
	
	public int SetAsGift(ChromeDriver Driver)
	{
		try{
			Thread.sleep(1500);
			Driver.findElementByXPath("//*[@id='sc-buy-box-gift-checkbox']").click();
			Thread.sleep(1500);
		}catch(Exception e){ return 1;}
		return 0;
		
	}
}
