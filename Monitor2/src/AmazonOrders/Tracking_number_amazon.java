package AmazonOrders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import Database.DatabaseOp;
import Ebay.Tracking_number_ebay;
import Main.DatabaseMain;
import Main.Order;

public class Tracking_number_amazon extends Thread
{
	public static String userName = null;
	public static String Pass = null;
	public static final String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;
	public static ApiContext apiContext = new ApiContext();
	public static ApiCredential cred = apiContext.getApiCredential(); //
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;
	
	public void run() {
		try {
			Get_tracking_from_amazon1();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public Tracking_number_amazon() {

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				//System.out.println(sCurrentLine);
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
				//	System.out.println("Schame = "+scham);
					Connection =Connection+scham;
				//	System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
					//System.out.println("eBay_token = "+eBay_token);
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
					//System.out.println("Server_url = "+Server_url);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
					//System.out.println("Application_id = "+Application_id);
				}
				
				if (sCurrentLine.contains("Acid:"))
				{
					userName = sCurrentLine.substring(sCurrentLine.indexOf("Acid:")+"Acid:".length());
				}
				
				if (sCurrentLine.contains("APass:"))
				{
					Pass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+"APass: ".length());
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					//System.out.println("Site code --> US");
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					//System.out.println("Site code --> UK");
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
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



	public Tracking_number_amazon(Runnable arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(Runnable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}



	public Tracking_number_amazon(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}



	public synchronized void Get_tracking_from_amazon1() throws InterruptedException, SQLException
	{

	DatabaseMain Db = new DatabaseMain();
	
	List<WebElement> list = new ArrayList<WebElement>();

	ArrayList<Order> List = new ArrayList<Order>();
	String temp= null;
	
	ChromeOptions options = new ChromeOptions();
	options.addArguments("--start-maximized");
	//options.addArguments("user-data-dir=C:\\User Data2");
	System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
	ChromeDriver Driver = new ChromeDriver(options);
	
	//Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	System.out.println("Tracking update process starts!");
	try{
	Driver.get("https://www.amazon.com/");
	Thread.sleep(1000);
	Driver.findElement(By.xpath("//*[@id='nav-orders']/span[2]")).click();
	Thread.sleep(2000); 
	
	try{
	if (Driver.findElement(By.xpath("//*[@id='ap_email']")).isDisplayed())
	{
		Driver.findElement(By.xpath("//*[@id='ap_email']")).click();
		Driver.findElement(By.xpath("//*[@id='ap_email']")).clear();
		Driver.findElement(By.xpath("//*[@id='ap_email']")).sendKeys(userName);
		Driver.findElement(By.xpath("//*[@id='ap_password']")).clear();
		Driver.findElement(By.xpath("//*[@id='ap_password']")).sendKeys(Pass);
		Driver.findElement(By.xpath("//*[@id='signInSubmit']")).click();
		Thread.sleep(8000);
	}
	}catch(Exception e1)
	{
		Login2(Driver);
	}
		

	
	Thread.sleep(2000);

	}catch(Exception e2)
	{
		System.out.println(e2);
		System.out.println("Exception can't loginto the orders");	
	}
	
	
	Db.Get_Orders_without_tracking(List);
	if (List.size() ==0){
		return;
	}
	
	
	
	
	for (Order ele:List)
	{
		temp = null;
		try{ 
		Driver.findElement(By.xpath("//*[@id='searchOrdersInput']")).click();
		Driver.findElement(By.xpath("//*[@id='searchOrdersInput']")).clear();
		Driver.findElement(By.xpath("//*[@id='searchOrdersInput']")).sendKeys(ele.Amazon_OrderNumber);
		}catch(Exception e){}
		
		try{ 

			Driver.findElement(By.xpath("//*[@id='a-autoid-7']/span/input")).click();

		}catch(Exception e){
		list = Driver.findElements(By.tagName("span"));
		for (WebElement ele1:list)
		{
			if (ele1.getText().contains("Search Orders")) 
				{
				ele1.click();
				break;
				}
		}
		}

		try{
		Thread.sleep(1000);  
		try{							     		 			
				try{							     							   				 
				temp  = Driver.findElement(By.xpath("//*[@id='ordersContainer']/div[1]/div[2]/div/div[1]/div[1]/div[2]")).getText(); // here is possibly can be an error need to make the catch with loop //
				}catch(Exception e){temp = null;}
				try{
				temp  = Driver.findElement(By.xpath("//*[@id='ordersContainer']/div[1]/div[3]/div/div[1]/div[1]/div[2]")).getText(); // here is possibly can be an error need to make the catch with loop //
				}catch(Exception e){temp = null;}

		}catch(Exception e1){temp = null;}       
		if (temp == null)
		{
		try{		
		temp  = Driver.findElement(By.xpath("//*[@id='ordersContainer']/div[1]/div[2]/div/div[1]/div[1]/div[2]/div")).getText(); 
		}catch(Exception e1){temp = null;}
		}
		if (temp == null)
		{
		try{	
		temp  = Driver.findElement(By.xpath("//*[@id='ordersContainer']/div[1]/div[3]/div/div[1]/div[1]/div/div/span")).getText(); 
		}catch(Exception e1){temp = null;}
		}
		
		System.out.println(temp);
		}catch(Exception e)
		{
		try{
			list = Driver.findElements(By.tagName("span"));
			for (WebElement ele1:list)
			{
				if (ele1.getAttribute("class").contains("a-color-success")||ele1.getText().contains("Delivery estimate: We need a little more")) 
					{
					
					temp = ele1.getText();
					break;
					}
			}
		}catch(Exception e1){}
		}

		JavascriptExecutor jse = (JavascriptExecutor)Driver;
		try
		{
		if ((temp!=null)&&(temp.contains("package was left")||(temp.contains("delivered")|| temp.contains("Your package was left in the mail room.")||temp.contains("Your package was left near the front door or porch.")||temp.contains("Package was left in a secure location")||temp.contains("Your package was delivered.")||temp.contains("Your package was delivered. It was handed directly to a resident.")||temp.contains("Package was left inside the residence’s mailbox")||temp.contains("On the way")||temp.contains("Your package was delivered")||temp.contains("Shipped")||temp.contains("Out for delivery")))) 
		{
			
			try{							
				Driver.findElement(By.xpath("//*[@id='a-autoid-8-announce']")).click();
				Thread.sleep(1000);
			}catch(Exception e){
			list = Driver.findElements(By.tagName("a"));
			for (WebElement ele1:list)
			{
				if (ele1.getText().contains("Track package")) 
					{
					ele1.click();
					break;
					}
			}
			Thread.sleep(1000);
			}
			
		    jse.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
		
			try{								
				
				
				list = Driver.findElements(By.tagName("h1"));
				for (WebElement ele1:list)
				{
					if (ele1.getText().contains("Shipped with")) 
						{
						temp = ele1.getText();
						temp = temp.substring(temp.indexOf("Shipped with ")+13);
						ele.Carrier = temp;
						break;
						}
				}
				if (temp.contains("UPS")||temp.contains("USPS")||temp.contains("UPS SurePost")||temp.contains("FedEx")||temp.contains("AMZL US")||temp.contains("ONTRAC"))
				{
				if (temp.contains("Shipped with "))
				temp = temp.substring(temp.indexOf("Shipped with ")+13);
				ele.Carrier = temp;
				}else{
					
					temp = Driver.findElementByXPath("//*[@id='carrierRelatedInfo-container']/div/h1[2]").getText();
					temp = temp.substring(temp.indexOf("Shipped with ")+13);
					ele.Carrier = temp;
				}


			}catch(Exception e){
				try{
				list = Driver.findElements(By.tagName("h1"));
				for (WebElement ele1:list)
				{
					if (ele1.getText().contains("Shipped with")&&ele1.getAttribute("class").equals("a-spacing-small widgetHeader")) 
						{
						temp = ele1.getText();
						temp = temp.substring(temp.indexOf("Shipped with ")+13);
						ele.Carrier = temp;
						break;
						}
				}
				
				}catch(Exception E1){
					ele.Carrier = null;
				}
				}
			
			try{

				list = Driver.findElements(By.tagName("a"));
				for (WebElement ele1:list)
				{
					if (ele1.getText().contains("Tracking ID ")&& ele1.getAttribute("class").equals("a-spacing-medium a-link-normal tracking-events-modal-trigger carrierRelatedInfo-trackingId-text")) 
						{
						temp = ele1.getText();
						temp = temp.substring(temp.indexOf("Tracking ID ")+12);
						ele.Tracking = temp;
						break;
						}
				}
				
				ele.Tracking = temp;
				}catch(Exception e){
				try{
				list = Driver.findElements(By.tagName("a"));
				for (WebElement ele1:list)
				{
					if (ele1.getText().contains("Tracking ID ")&& ele1.getAttribute("class").equals("a-spacing-medium a-link-normal tracking-events-modal-trigger carrierRelatedInfo-trackingId-text")) 
						{
						temp = ele1.getText();
						temp = temp.substring(temp.indexOf("Tracking ID ")+12);
						ele.Tracking = temp;
						break;
						}
				}
				}catch(Exception e1){ele.Tracking = null;}
			}

			if (ele.Tracking!=null && ele.Carrier!=null)
			{
			System.out.println("Carrier = "+ele.Carrier+" Tracking = "+ele.Tracking);
			Db.Set_tracking_to_database(ele.Tracking,ele.Carrier,ele.Amazon_OrderNumber);
			String ItemID = ele.OrderId;
			ItemID = ItemID.substring(0,ItemID.indexOf("-"));
			String TransactionID = ele.OrderId;
			TransactionID = TransactionID.substring(TransactionID.indexOf("-")+1);
			Tracking_number_ebay tracking_number_ebay = new Tracking_number_ebay(Db.GetStoreInfoByName(GetStoreNameByAmazonOrderNumber(ele.Amazon_OrderNumber)));
			if (tracking_number_ebay.Update_tracking_and_feedback_in_ebay(ItemID ,TransactionID,ele.Tracking,ele.Carrier,ele.Buyer_User_ID).equals("Success")) Db.Set_OrderStatus_Complete_to_database(ele.Amazon_OrderNumber);
			tracking_number_ebay = null;
			
			}else {
				Db.Set_OrderStatus_Tracking_Update_Fail(ele.Amazon_OrderNumber);
			}
			
			Thread.sleep(1000);
			Driver.navigate().back();
		}
		}catch(Exception e){System.out.println("Error in Amazon_Order Number "+ele.Amazon_OrderNumber);}

		try{
		if((temp!=null)&&(Driver.findElementByXPath("//*[@id='ordersContainer']/div[1]/div[3]/div/div[1]/div[1]/div/div/span").isDisplayed())) 
		{
			if (Driver.findElementByXPath("//*[@id='ordersContainer']/div[1]/div[3]/div/div[1]/div[1]/div/div/span").getText().toLowerCase().contains("cancelled"))
			{
				System.out.println(Driver.findElementByXPath("//*[@id='ordersContainer']/div[1]/div[3]/div/div[1]/div[1]/div/div/span").getText());
				Db.Set_OrderStatus_Cancelled_to_database(ele.Amazon_OrderNumber);
			}
		}
		}catch(Exception e){}
		
	}
	System.out.println("Tracking update process ended!");
	Driver.close();
	

	list = null;
	Db = null;
	List = null;
	temp= null;
	options = null;
	Driver = null;
	Driver = null;
	System.gc();

	}
	
	public void  Login2(ChromeDriver Driver)
	{
										
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(userName);
			Driver.findElementByXPath("//*[@id='continue']").click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(Pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		
		
	}
	
	public String GetStoreNameByAmazonOrderNumber(String AmazonOrderNumber)
	{
			try {
				
				Connection con = DriverManager.getConnection(Connection,"root","root");
				java.sql.Statement statement = con.createStatement();//
				ResultSet res = statement.executeQuery("select * from amazon.orders where Amazon_OrderNumber = '"+AmazonOrderNumber+"';");
				res.next();
				//con.close();
				statement = null;
				System.gc();
				//System.out.println(res.getString("StoreName"));
				return (res.getString("StoreName"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

}
