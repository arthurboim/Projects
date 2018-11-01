package MainPackage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScrapInfo {

	public static double Total_Added = 0;
	public static String category;
	public  static Connection con = null;
	public  static String  Connection= null;
	public  static String  User= null;
	public  static String  Pass= null;
	public  static java.sql.Statement statement= null;
	public  ResultSet res2 = null;
	public  String FILENAME = "C:\\keys\\ConfigFile-Keys.txt";
	public  static String scham = null;
	
	public ScrapInfo() throws IOException {
		
		BufferedReader br = null;
		FileReader fr = null;
		fr = new FileReader(FILENAME);
		br = new BufferedReader(fr);
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) 
		{

			if (sCurrentLine.contains("User:")) User = "root";
			if (sCurrentLine.contains("Pass:")) Pass = "root";
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
		br.close();
	}

	public void Items_info(List<product> ItemsList,String Path1) throws InterruptedException, SQLException, IOException {
		ArrayList<product> ItemsList_new = new ArrayList<product>();
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		FileInputStream fis = null;
		Database Db = new Database();
		BufferedReader reader = null;
		EbaySearch Search = new EbaySearch();
		ChromeDriver Driver = new ChromeDriver();
		Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String path = Path1;
		FileOp Op = new FileOp();
		
		try {
			fis = new FileInputStream(path);
			reader = new BufferedReader(new InputStreamReader(fis));
			System.out.println("Reading File line by line using BufferedReader");
			String line = reader.readLine();
			if (Op.CheckIfFileContainsHere(path))
			{
				while(!line.contains("HERE-->"))
				{
					line = reader.readLine();
				}
				Op.RemoveHere(path, line);
				line = reader.readLine();
				if(line == null)
				{
					Op  = null;
					System.gc();
					fis = new FileInputStream(path);
					reader = new BufferedReader(new InputStreamReader(fis));
					Op = new FileOp();
					line = reader.readLine();
				}
				Op.SetHere(path, line);
			}else
			{
				Op.SetHere(path, line);
			}
			
			System.out.println("Start running...");
			while (line != null) 
			{
				ItemsList_new.removeAll(ItemsList_new);
				Amazon_Get_Items_Movers_And_Shakers_Items_info(Driver,line,ItemsList_new);
				try{
					Search.Finditem(ItemsList_new);
				}catch(Exception e)
				{
					System.out.println("eBay search faild");
				}
				
				Db.Set_product_from_database(ItemsList_new);
				Op.RemoveHere(path,line);
				line = reader.readLine();
				if (line == null)
				{
					Op  = null;
					System.gc();
					fis = new FileInputStream(path);
					reader = new BufferedReader(new InputStreamReader(fis));
					Op = new FileOp();
					line = reader.readLine();
				}
				
				Op.SetHere(path, line);
				ItemsList_new.removeAll(ItemsList_new);
			}
			line = null;
			System.gc();

	} catch (FileNotFoundException ex) 
	{
		System.out.println("Can't read from file ");
	} catch (IOException ex) 
	{
		System.out.println("Can't read from file ");
	}
}

	public  static double Min_price_to_sale(double price) {
		double ebay_fees = 0.09;
		double paypal_fees = 0.039;
		double paypal_fixed = 0.3;
		double my_percent = 1.06;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}

	void Amazon_Get_Items_Movers_And_Shakers_Items_info(ChromeDriver driver, String url, List<product> ItemsList_new) throws InterruptedException 
	{
		driver.get(url);
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Thread.sleep(1000);
		Webelements = driver.findElements(By.className("zg_itemImmersion"));
		
		if(Webelements.size() > 0)
		{
			driver.get(url);
			for (int i = 0; i < 5; i++) 
			{
				Add_Product_To_List(driver, ItemsList_new);
				Next_Page_Amazon1(driver,i+1);//
				Thread.sleep(1000);
			}
		}else
		{
			try{
			for (int i = 0; i < 2; i++) {
				Add_Product_To_List(driver, ItemsList_new);
				Next_Page_Amazon(driver);//
				Thread.sleep(1000);
			}
			}catch(Exception e){}
		}

	}
	
	void Add_Product_To_List(ChromeDriver Driver,List<product> ItemsList_new) throws InterruptedException 
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();

		product temp_product = new product();//
		Thread.sleep(1000);

		Webelements = Driver.findElements(By.className("zg_itemImmersion"));
		if (Webelements.size() == 0)
		{
			Webelements = Driver.findElements(By.className("zg-item-immersion"));
		}
		
		for (WebElement ele : Webelements) 
		{
			try{
				temp_product.link = ele.findElement(By.tagName("a")).getAttribute("href");
				temp_product.ASIN = temp_product.link.substring(temp_product.link.indexOf("/dp/") + 4,temp_product.link.indexOf("/ref"));	
				temp_product.bestresult = temp_product.link.substring(temp_product.link.indexOf(".com/") + ".com/".length(),temp_product.link.indexOf("/dp/"));
				temp_product.bestresult = temp_product.bestresult.replace("-", " ");
				temp_product.price  = GetPrice(ele);
				temp_product.prime  = isPrime(ele);
				
				if ((temp_product.prime == 1) && !IsItemExist(temp_product)) 
				{
					ItemsList_new.add(temp_product);
					temp_product = new product();
				}
			}catch(Exception e)
			{
			}
		}
		
		Webelements = Driver.findElements(By.className("zg_itemImmersion"));
	}

	int isPrime(WebElement outerele)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = outerele.findElements(By.tagName("i"));
		for(WebElement ele : Webelements)
		{
			if (ele.getAttribute("class").equals("a-icon a-icon-prime a-icon-small"))
			{
				return 1;
			}
		}
		
		return 0;
	}
	
	double GetPrice(WebElement outerele)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = outerele.findElements(By.tagName("span"));
		String price = null;
		double finalPrice = -1;
		
		for(WebElement ele : Webelements)
		{
			if (ele.getAttribute("class").equals("p13n-sc-price"))
			{
				price = ele.getText();
				break;
			}
		}
		
		if (price.contains("-"))
		{
			return -1;
		}
		
		if (price.contains("$"))
		{
			price = price.replace("$", " ");
		}
		
		try{
			finalPrice = Double.parseDouble(price);
		}catch(Exception e)
		{	
		}
		
		return finalPrice;
	}

	String GetLink(WebElement outerele)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = outerele.findElements(By.tagName("a"));
		
		for(WebElement ele : Webelements)
		{
			if (ele.getAttribute("class").equals("a-link-normal"))
			{
				return ele.getAttribute("href");
			}
		}
		return null;
	}

	boolean IsItemExist(product temp_product) throws SQLException 
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		ResultSet res = statement.executeQuery("SELECT count(*) FROM amazon.items where ASIN = '"+temp_product.ASIN+"';");
		res.last();
		if(res.getString(1).equals("1"))
		{
			return true;
		}

		return false;
	}

	void print_items_list(List<product> ItemsList) {
		for (product ele : ItemsList) {
			System.out.println("Results = " + ele.ebayResults + " arbitraje = " + ele.arbitraje + " percent = "
					+ ele.MAS_percent + "%" + " results code = " + ele.bestresult);
			// System.out.println("eBay price = "+ele.ebayLowestPrice+" Amazon
			// price without fees = "+ele.price);

		}
	}

	void Next_Page_Amazon(ChromeDriver Driver) 
	{
		try{
			Driver.findElementByXPath("//*[@id='zg-center-div']/div[2]/div/ul/li[4]/a").click();
		}catch(Exception e){
			GetNextPageBackUp(Driver);
		}
	}
	
	public void GetNextPageBackUp(ChromeDriver Driver)
	{
		String s = Driver.getCurrentUrl();
		String Temp = Driver.getCurrentUrl();
		
		Temp = Driver.findElementByXPath("//*[@id='zg-center-div']/div[2]/div/ul/li[2]/a").getAttribute("href");
		s = Temp.substring(Temp.lastIndexOf("/"));
		s = s.substring(0, s.length()-1);
		s+="2";
		Temp = Temp.substring(0, Temp.lastIndexOf("/"));
		Temp+=s;
		System.out.println(Temp);
		Driver.get(Temp);
	
	}
	
	void Next_Page_Amazon1(ChromeDriver Driver, int pageIndex) 
	{
		try{
			Driver.findElementByXPath("//*[@id='zg_page"+(pageIndex+1)+"']/a").click();
		}catch(Exception e)
		{
			GetNextPageBackUp1(Driver);
		}
	}
	
	public void GetNextPageBackUp1(ChromeDriver Driver)
	{
		String s = Driver.getCurrentUrl();
		String Temp;

		if (s.contains("#")) {
			s = s.substring(s.indexOf("#") + 1);
			int i = Integer.parseInt(s);
			i++;
			if (i == 6)
				i = 5;
			Temp = Driver.getCurrentUrl();
			Temp = Temp.substring(0, Temp.length() - 1);
			s = Temp + i;
		} else {
			s = s + "#2";
		}

		Driver.get(s);
	}
	
	
	
	
}
