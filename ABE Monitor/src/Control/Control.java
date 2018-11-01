package Control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.xml.sax.SAXException;
import Amazon.Amazon;
import Database.Item;
import Ebay.Ebay;

public class Control {

	public static ArrayList<Item> ListOfItems = new ArrayList<Item>();
	
	private static int  NItemsInstock = 10;
	private static int  RankCreteria = 30000;
	Connection con;
	java.sql.Statement statement;
	
	static enum StockStatusEnum
	{
		InStock,
		NotInStock,
		OnlyNLeftOrderSoon,
		OnlyNLeftOrderMoreOntheWay,
		UsuallyShipsWithinN,
		AvailableToShip,
		AvailableFromTheseSellers,
		OOS,
		PreOrder,
		Unknown
	};
	
	String user = null;
	String pass = null;
	
	public String FILENAME = "C:\\keys\\ConfigFile-Keys.txt";
	public String PathForImages = null;
	private ChromeDriver Driver;
	String User;
	String Pass;
	public  static String  Connection= null;
	public  ResultSet res2 = null;
	public  static String scham = null;
	
	public Control(){

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br = new BufferedReader(fr);
		String sCurrentLine = null;
		try {
			while ((sCurrentLine = br.readLine()) != null) 
			{
				/* Change the code here */
				if (sCurrentLine.contains("Acid: ")) 
				{
					user = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ")+ "Acid: ".length());
				}
				
				if (sCurrentLine.contains("APass: ")) 
				{
					pass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+ "APass: ".length());
				}
				
				if (sCurrentLine.contains("PathForImages: ")) 
				{
					PathForImages = sCurrentLine.substring(sCurrentLine.indexOf("PathForImages: ")+ "PathForImages: ".length());
				}
			
				
				if (sCurrentLine.contains("Connection:")) 
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("jdbc:"));
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+"Schame:".length()+1);
					Connection =Connection+scham;
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver = new ChromeDriver();
		
		
	}

	public void BulkCheckItemBeforeUpload() throws IOException, ParserConfigurationException, SAXException
	{
		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		System.out.println("Reading files from database...");
		
		GetResults0_7to30(ListOfItemsAsins);
		
		GetResults0_3030AndHigher(ListOfItemsAsins);
		
		//Lowestprice1_7to30(ListOfItemsAsins);
		
		//Lowestprice1_30AndHigher(ListOfItemsAsins);
		//ProductFromSellers_10to30(ListOfItemsAsins);
		//ProductFromSellers_30AndHigher(ListOfItemsAsins);
		
		System.out.println("Reading finished...");
		System.out.println("Items checking start...");
		System.out.println("Amount of files before check is "+ListOfItemsAsins.size()); 
		UpdateUploadedSet1(ListOfItemsAsins);
		RemoveDuplicatedItems(ListOfItemsAsins);
		RemoveAlreadyExcistitems(ListOfItemsAsins);
		System.out.println("Items amount after duplicate and excist check "+ListOfItemsAsins.size()); 
		AmazonApiCheck(ListOfItemsAsins);
		//check forbidden category //
		ItemsCheckingBeforeUpload(ListOfItemsAsins); /* BUG in preorder */
		RemoveNotReadyFiles(ListOfItemsAsins);
		PrintReadyToUploadItems(ListOfItemsAsins);
		System.out.println("Amount of files after check is "+ListOfItemsAsins.size());
		System.out.println("Items checking ended...");
		
		System.out.println("Getting images...");
		SaveImagesInFolders(ListOfItemsAsins);
		System.out.println("Getting images ended...");
	}
	
	public void WebDriverCheckItemBeforeUpload() throws IOException, ParserConfigurationException, SAXException
	{

		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		System.out.println("Reading files from database...");
		
		GetResults0_7to30(ListOfItemsAsins);
		
		GetResults0_3030AndHigher(ListOfItemsAsins);
		
		//Lowestprice1_7to30(ListOfItemsAsins);
		
		//Lowestprice1_30AndHigher(ListOfItemsAsins);
		//ProductFromSellers_10to30(ListOfItemsAsins);
		//ProductFromSellers_30AndHigher(ListOfItemsAsins);
		
		System.out.println("Reading finished...");
		System.out.println("Items checking start...");
		System.out.println("Amount of files before check is "+ListOfItemsAsins.size()); 
		//UpdateUploadedSet1(ListOfItemsAsins);
		RemoveDuplicatedItems(ListOfItemsAsins);
		RemoveAlreadyExcistitems(ListOfItemsAsins);
		System.out.println("Items amount after duplicate and excist check "+ListOfItemsAsins.size()); 
		
		/* WebDriver check*/
		WebDriverScrapInfoBeforUpload(ListOfItemsAsins);
		
		//check forbidden category //
		//ItemsCheckingBeforeUpload(ListOfItemsAsins); /* BUG in preorder */
		//RemoveNotReadyFiles(ListOfItemsAsins);
		PrintReadyToUploadItems(ListOfItemsAsins);
		System.out.println("Amount of files after check is "+ListOfItemsAsins.size());
		System.out.println("Items checking ended...");
		
//		System.out.println("Getting images...");
//		SaveImagesInFolders(ListOfItemsAsins);
//		System.out.println("Getting images ended...");
	}
	
	private void GetResults0_7to30(ArrayList<Item> ListOfItemsAsins)
	{
		try {

			//ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and (Uploaded = 0 or Uploaded is null)  and Amazon_Rank<15000  and Amazon_Rank>0 and Amazon_price>7 and Amazon_price<=30 GROUP BY asin;");
			ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and (Uploaded = 0 or Uploaded is null)  and Amazon_price>7 and Amazon_price<=30 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "GetResults0_7to30";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("GetResults0_7to30");}
	}

	private void GetResults0_3030AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			//ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and Amazon_Rank<15000  and Amazon_Rank>0 and (Uploaded = 0 or Uploaded is null)   and Amazon_price>30 and Amazon_price<=2000 GROUP BY asin;");
			ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and (Uploaded = 0 or Uploaded is null) and Amazon_price>30 and Amazon_price<=2000 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "GetResults0_3030AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("GetResults0_3030AndHigher");}
	
	}
	
	private void Lowestprice1_7to30(ArrayList<Item> ListOfItemsAsins)
	{


		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where  placeinlowestprice = 1 and Uploaded = 0  and Amazon_Rank>0 and Amazon_Rank<30000 and Amazon_price>10 and Amazon_price<=30 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "Lowestprice1_7to30";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("Lowestprice1_7to30");}
	
	
		
	}
	
	private void Lowestprice1_30AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM items where  placeinlowestprice = 1 and Uploaded = 0 and Amazon_Rank>0 and Amazon_Rank<30000 and Amazon_price>30 and Amazon_price<=2000 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "Lowestprice1_30AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	
	private void ProductFromSellers_10to30(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.productfromsellers where  (uploaded is null or  uploaded = 0 ) and  (amazon_price>8  and amazon_price<30 and soldlastweekall>2);");
			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "ProductFromSellers_10to30";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	
	private void ProductFromSellers_30AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.productfromsellers where (uploaded is null or  uploaded = 0 ) and ((amazon_price>70  and amazon_price<4000 and soldlastweekall>0) or (amazon_price>30  and amazon_price<70 and soldlastweekall>0));");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "ProductFromSellers_30AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	
	public void UpdateUploadedSet1(ArrayList<Item> ListOfItemsAsins)
	{
		try {
			for(Item ele:ListOfItemsAsins)
			{
				statement.executeUpdate("update items set Uploaded = 1 where ASIN = '"+ele.SupplierCode+"';");				
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void UpdateUploadedSet1(Item item)
	{
		try {
			statement.executeUpdate("update items set Uploaded = 1 where ASIN = '"+item.SupplierCode+"';");				
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WebDriverScrapInfoBeforUpload(ArrayList<Item> ListOfItemsAsins)
	{
		UniversalCodeCheckerFromAsinLab CodeChecker = new UniversalCodeCheckerFromAsinLab();
		// TODO Login
		Login(Driver);
		
		while(ListOfItemsAsins.size() > 0)
		{
			for(Item ele:ListOfItemsAsins)
			{
				UpdateUploadedSet1(ele);
				Driver.get("https://www.amazon.com/dp/"+ele.SupplierCode+"/");
				ele.Title 	= GetTitle(Driver);
				ele.Brand 	= GetBrand(Driver);
				ele.Rank 	= GetRank(Driver);
				ele.Content = GetContent(Driver);
				GetFeatures(Driver,ele);
				ele.inStock = GetInstock(Driver,ele);
				ItemCheckBeforeUpload(ele);
				CheckItemCreteria(ele);
				if (true == ele.ReadyToUpload)
				{
					if (CodeChecker.isBook(ele) == false)
					{
						CodeChecker.GetItemInfo(ele);						
					}
					CodeChecker.GetResultsByCode(ele);
					
					if (ele.eBayResults == 0)
					{
						GetImageLink(ele);
						SaveImageInFolder(ele);
					}
				}
				
				
				// For debuging only 
				System.out.println("Asin = "+ele.SupplierCode);
				System.out.println("Rank = "+ele.Rank);
				System.out.println("In stock? "+ele.inStock);
				System.out.println("In prime? "+ele.prime);
				System.out.println("is Vero? " +ele.isVero);
				System.out.println("Ready to upload? "+ele.ReadyToUpload);
				System.out.println("***********************");
				break;
			}
			ListOfItemsAsins.removeAll(ListOfItemsAsins);
			GetResults0_7to30(ListOfItemsAsins);
			GetResults0_3030AndHigher(ListOfItemsAsins);
		}
		

		Driver.close();
		Driver.quit();
		CodeChecker = null;
		System.gc();
	}
	
	
	
	
	private void GetImageLink(Item ele)
	{
		int counter =0;
		ele.PicturesString[0] = GetMainPicture(Driver);	
		while((ele.PicturesString[0] == null || !ele.PicturesString[0].contains(".jpg")) && (counter < 20))
		{
			ele.PicturesString[0] = GetMainPicture(Driver);		
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
		}
		System.out.println("Counter = "+ counter);
		System.out.println(ele.PicturesString[0]);
	}
	
	private void CheckItemCreteria(Item ele)
	{
		if (ele.Rank > RankCreteria)
		{
			ele.ReadyToUpload = false;
		}
		if (ele.prime == false)
		{
			ele.ReadyToUpload = false;
		}
		
		if (ele.inStock == false)
		{
			ele.ReadyToUpload = false;
		}
	}
	
	private void Login(ChromeDriver Driver)
	{
		Driver.get("https://www.amazon.com/");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElement(By.id("nav-link-accountList")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElementByXPath("//*[@id='ap_email']").click();
		Driver.findElementByXPath("//*[@id='ap_email']").clear();
		Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
		Driver.findElementByXPath("//*[@id='continue']").click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElementByXPath("//*[@id='ap_password']").click();
		Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
		Driver.findElementByXPath("//*[@id='signInSubmit']").click();
}
	
	private String GetTitle(ChromeDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		
		Webelements = Driver.findElements(By.cssSelector("span[id='productTitle']"));
		for(WebElement ele:Webelements)
		{
			if (ele.getAttribute("id").equals("productTitle"))
			{
				return ele.getText();
			}
		}
		
		return null;
	}
	
	private int GetRank(ChromeDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = Driver.findElements(By.cssSelector("td"));
		int tempRank = 0;
//
		for(WebElement ele:Webelements)
		{
			if (ele.getText().contains("#") && ele.getText().contains("in"))
			{
				String temp =  ele.getText();
				temp = temp.replace("#", " ");
				temp = temp.replace(",", "");
				temp = temp.substring(0, temp.indexOf("in"));
				try{
					temp = temp.trim();
					tempRank = Integer.parseInt(temp);
					break;
				}catch(Exception e )
				{
					tempRank =  -1;
				}
			}
		}
//
		if (tempRank == -1)
		{
			Webelements = Driver.findElements(By.id("SalesRank"));
			for(WebElement ele:Webelements)
			{
				String temp =  ele.getText();
				temp = temp.substring(temp.indexOf("#"));
				temp = temp.replace("#", " ");
				temp = temp.replace(",", "");
				temp = temp.substring(0, temp.indexOf("in"));
				try{
					temp = temp.trim();
					tempRank = Integer.parseInt(temp);
					break;
				}catch(Exception e )
				{
					tempRank =  -1;
				}
			}
		
		}

		return tempRank;	
	}
	
	private String GetFeatures(ChromeDriver Driver,Item item)
	{
		List<WebElement> Webelements = Driver.findElements(By.id("feature-bullets"));
		for(WebElement ele:Webelements)
		{
			if (!ele.getText().isEmpty())
			{
				String text = ele.getText();
				text = text.replace("› See more product details", "");
				String[] data = text.split("\n");
				for(String ele1:data)
				{
					item.Features.add(ele1);
				}	
				break;
			}
		}
		

		return null;
	}
	
	private boolean GetInstock(ChromeDriver Driver, Item ele)
	{
		IsPrime(Driver,ele);
		List<WebElement> Webelements = Driver.findElements(By.id("availability"));
		for(WebElement ele1:Webelements)
		{
			if ( InStockFilter(ele1.getText()) == true)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean InStockFilter(String text)
	{
		StockStatusEnum StockStatus =  null;
		StockStatus = StockStatusEnum.Unknown;
		boolean status = false;
		int N = -1;
		
		if (text.contains("In Stock"))
		{
			StockStatus = StockStatusEnum.InStock;
		}else if(text.contains("Only") && text.contains("left in stock") && text.contains("more on the way"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderMoreOntheWay;
		}else if (text.contains("Only") && text.contains("left in stock") && text.contains("order soon"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderSoon;
		}else if(text.contains("Usually ships within"))
		{
			StockStatus = StockStatusEnum.UsuallyShipsWithinN;
		}else if(text.contains("Available to ship in"))
		{
			StockStatus = StockStatusEnum.AvailableToShip;
		}else if(text.contains("Available from these sellers"))
		{
			StockStatus = StockStatusEnum.AvailableFromTheseSellers;
		}else if(text.contains("Temporarily out of stock"))
		{
			StockStatus = StockStatusEnum.OOS;
		}else if (text.contains("This title will be released on"))
		{
			StockStatus = StockStatusEnum.PreOrder;
		}else if (text.contains("Currently unavailable"))
		{
			StockStatus = StockStatusEnum.OOS;
		}

		if (StockStatusEnum.Unknown == StockStatus)
		{
			System.out.println("Parsing failes...");
			System.out.println("Text = \n"+text);
		}
		
		switch (StockStatus)
		{
			case InStock:
				status = true;
				break;
				
			case OnlyNLeftOrderMoreOntheWay:  
				text = text.substring(text.indexOf("Only ")+"Only ".length(), text.indexOf(" left in stock (more on the way)."));
				N = Integer.parseInt(text);
				if (N < NItemsInstock )
				{
					status = false;
				}else
				{
					status = true;
				}
				break;
				
			case OnlyNLeftOrderSoon:
				text = text.substring(text.indexOf("Only ")+"Only ".length(), text.indexOf(" left in stock - order soon."));
				N = Integer.parseInt(text);
				if (N < NItemsInstock )
				{
					status = false;
				}else
				{
					status = true;
				}
				break;
				
			case UsuallyShipsWithinN:
					status = false;
				break;
				
			case NotInStock:
					status = false;
				break;
				
			case AvailableToShip:
				status = false;
				break;
			case AvailableFromTheseSellers:
				status = false;
				break;
			case OOS:
				status = false;
				break;
			case PreOrder:
				status = false;
				break;
			case Unknown:
					status = false;
				break;
		}
		
		return status;		
	}
	
	private boolean IsPrime(ChromeDriver Driver,Item ele)
	{
		boolean status = false;
		List<WebElement> Webelements = Driver.findElements(By.xpath("//*[@id='priceBadging_feature_div']/i"));
		if (Webelements.size() > 0)
		{
			if (Driver.findElement(By.xpath("//*[@id='priceBadging_feature_div']/i")).getAttribute("class").equals("a-icon a-icon-prime"))
			{
				status = true;
			}else
			{
				status = false;
			}
		}
		ele.prime = status;
		
		return status;
	}
	
	private String GetBrand(ChromeDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = Driver.findElements(By.cssSelector("a[id='brand']"));

		
		for(WebElement ele:Webelements)
		{
			if (ele.getAttribute("id").equals("brand"))
			{
				return ele.getText();
			}
		}
		
		Webelements = Driver.findElements(By.id("bylineInfo"));
		for(WebElement ele:Webelements)
		{
			if (ele.getAttribute("id").equals("bylineInfo"))
			{
				return ele.getText();
			}
		}
		
		return null;
	}
	
	private String GetMainPicture(ChromeDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		
		try {
			Thread.sleep(500);
			Webelements.clear();
			Webelements = Driver.findElements(By.cssSelector("img[class='fullscreen']"));
			for(WebElement ele:Webelements)
			{
				if (ele.getAttribute("class").equals("fullscreen"))
				{
					return ele.getAttribute("src");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		try {
			Thread.sleep(500);
			Webelements.clear();
			Webelements = Driver.findElements(By.cssSelector("img[class='a-dynamic-image  a-stretch-vertical']"));
			for(WebElement ele:Webelements)
			{
				if (ele.getAttribute("class").equals("a-dynamic-image  a-stretch-vertical"))
				{
					String s = ele.getAttribute("src");
					return s;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(500);
			Webelements.clear();
			Webelements = Driver.findElements(By.id("imgBlkFront"));
			for(WebElement ele:Webelements)
			{
				return ele.getAttribute("src");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(500);
			Webelements.clear();
			Webelements = Driver.findElements(By.id("landingImage"));
			for(WebElement ele:Webelements)
			{
				return ele.getAttribute("src");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String GetContent(ChromeDriver Driver)
	{

		List<WebElement> Webelements = Driver.findElements(By.id("productDescription"));
		for(WebElement ele:Webelements)
		{
			if (ele.getAttribute("class").equals("a-section a-spacing-small"))
			{
				return ele.getText();
			}
		}
		

		return null;
	}
	
	private void SaveImagesInFolders(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		for(Item ele:ListOfItemsAsins)
		{
			try{
			System.out.println(ele.PathFolder);
			switch(ele.PathFolder)
			{
			case "GetResults0_7to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_7to30\\"+ele.SupplierCode+".png");
			break;
			
			case "GetResults0_3030AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_3030AndHigher\\"+ele.SupplierCode+".png");
			break;
			
			case "Lowestprice1_7to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_7to30\\"+ele.SupplierCode+".png");
			break;
			
			case "Lowestprice1_30AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_30AndHigher\\"+ele.SupplierCode+".png");
			break;
			case "ProductFromSellers_10to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_10to30\\"+ele.SupplierCode+".png");
			break;
			case "ProductFromSellers_30AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_30AndHigher\\"+ele.SupplierCode+".png");
			break;
			default :
				break;
			}
			}catch(Exception e){System.out.println(ele.PathFolder);}
		}
		
	}
	
	public void SaveImageInFolder(Item ele) 
	{
		try{
		saveImage(ele.PicturesString[0],PathForImages+ele.SupplierCode+".png");

//		System.out.println(ele.PathFolder);
//		switch(ele.PathFolder)
//		{
//		case "GetResults0_7to30":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_7to30\\"+ele.SupplierCode+".png");
//		break;
//		
//		case "GetResults0_3030AndHigher":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_3030AndHigher\\"+ele.SupplierCode+".png");
//		break;
//		
//		case "Lowestprice1_7to30":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_7to30\\"+ele.SupplierCode+".png");
//		break;
//		
//		case "Lowestprice1_30AndHigher":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_30AndHigher\\"+ele.SupplierCode+".png");
//		break;
//		case "ProductFromSellers_10to30":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_10to30\\"+ele.SupplierCode+".png");
//		break;
//		case "ProductFromSellers_30AndHigher":
//		saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_30AndHigher\\"+ele.SupplierCode+".png");
//		break;
//		default :
//			break;
//		}
		}catch(Exception e)
		{
			System.out.println(ele.PathFolder);
		}
	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException 
	{
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile);
	    

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) 
	    {
	    	os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}
	
	private void RemoveNotReadyFiles(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		Iterator<Item> i = ListOfItemsAsins.iterator();
		while (i.hasNext()) 
		{
			if (i.next().ReadyToUpload==false) i.remove();
		}
	}
	
	private void RemoveAlreadyExcistitems(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		Ebay ebay = new Ebay();
		/*
		Iterator<Item> i = ListOfItemsAsins.iterator();
		while (i.hasNext()) {
			if (ebay.IsAlreadyExcist(i.next())) i.remove();
		}
		*/
		ebay.IsAlreadyExcistBulk(ListOfItemsAsins);
		ebay = null;
		System.gc();
	}
	
	private void RemoveDuplicatedItems(ArrayList<Item> ListOfItemsAsins)
	{

		Set<Item> hs = new HashSet<>();
		hs.addAll(ListOfItemsAsins);
		ListOfItemsAsins.clear();
		ListOfItemsAsins.addAll(hs);
	}
	
	public  void AmazonApiCheck(ArrayList<Item> List) throws ParserConfigurationException, SAXException, IOException
	{
		int counter = 0;
		String Codes ="";
		Amazon AmazonApiCall = new Amazon();
		
		for (Item ele:List)
		{
			Codes+=ele.SupplierCode+",";
			if (counter==9)
			{
				counter++;	
				Codes = Codes.substring(0, Codes.length()-1);
				System.out.println(Codes);
				AmazonApiCall.BulkInfoRequest(Codes,List,counter);
				counter=0;
				Codes = "";
			}else 
			{
				counter++;	
			}
		}
		if (counter>0)
		{
			AmazonApiCall.BulkInfoRequest(Codes,List,counter);
		}
		
	}
	
	private void PrintReadyToUploadItems(ArrayList<Item> List)
	{
		System.out.println("--------------------------Report---------------------------");
		for(Item ele:List)
		{
			if (ele.ReadyToUpload)
			{
				System.out.println(ele.SupplierCode);
			}
		}
		System.out.println("--------------------------Report---------------------------");
	}
	
	public void ItemsCheckingBeforeUpload(ArrayList<Item> List) throws IOException
	{
		Ebay ebay = new Ebay();
		for (Item ele:List)//
		{
		try{
			ebay.IsVeroBrand(ele);
			ebay.ForbiddenWordsCheck(ele); /* It's checking the title */
			ebay.BrandCheckInContent(ele);
			ebay.BrandCheckInFeatures(ele);
			
			ebay.LinksCheck(ele);
			if (ele.prime == false)
			{
				ele.ReadyToUpload = false;
			}
			
//			if (ele.IsPreorder == true) 
//			{
//				ele.ReadyToUpload = false;
//			}
			
//			if (ele.IsNew == false)
//			{
//				ele.ReadyToUpload = false;
//			}
			
//			if (ele.MaximumDaysToShip >4)
//			{
//				ele.ReadyToUpload = false;
//			}
			
//			if (ele.AvailabilityType.equals("Not available"))
//			{
//				ele.ReadyToUpload = false;
//				System.out.println("Not available "+ele.SupplierCode);
//			}
			
			}
			catch(Exception e)
			{
				ele.ReadyToUpload = false;
			}
		}
	}
	
	public void ItemCheckBeforeUpload(Item item)
	{
		try{
			Ebay ebay = new Ebay();
			ebay.IsVeroBrand(item);//
			ebay.ForbiddenWordsCheck(item); /* It's checking the title */
			ebay.BrandCheckInContent(item);
			ebay.BrandCheckInFeatures(item);
			ebay.LinksCheck(item);
			if (item.prime == false)
			{
				item.ReadyToUpload = false;
			}
		}
		catch(Exception e)
		{
			item.ReadyToUpload = false;
		}
		
	}
	
}
