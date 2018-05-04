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
		ArrayList<product> ItemsListToAmazon = new ArrayList<product>();
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		FileInputStream fis = null;
		int SearchCodesCounter=1;	
		String SearchCodes = "";
		Database Db = new Database();
		BufferedReader reader = null;
		AmazonAdvertisingApi AmazonApi = new  AmazonAdvertisingApi();
		EbaySearch Search = new EbaySearch();
		ChromeDriver Driver = new ChromeDriver();
		Driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String path = Path1;
		int Counter = 0;
		FileOp Op = new FileOp();
		
		if (path.contains("Movers-and-shakers")) 
		category = path.substring(path.indexOf("Shakers\\")+8,path.indexOf(".txt") );
		else
		{
		try{
		category = path.substring(path.indexOf("Sellers-")+8,path.indexOf(".txt") );
		 }catch(Exception e) {}
		}
		System.out.println("We check category "+category);
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
			
			while (line != null) 
			{

				ItemsList_new.removeAll(ItemsList_new);
				//Db.Get_product_from_database(ItemsList);
				System.out.println(line);
				Amazon_Get_Items_Movers_And_Shakers_Items_info(Driver,line,ItemsList_new);
				Counter+=ItemsList_new.size();
				try{
					System.out.println("ItemsList_new = "+ItemsList_new.size());
					for (product ele:ItemsList_new)
					{
					if ((SearchCodesCounter % 11)==0)
					{
						System.out.println("SearchCodes = "+SearchCodes);
						AmazonApi.Get_Items_LookUp(SearchCodes,"ASIN",ItemsListToAmazon);
						SearchCodes = "";
						SearchCodesCounter = 1;
					}
				   else {
			     			SearchCodes = SearchCodes+ele.ASIN+",";
			     			SearchCodesCounter++;
			     			System.out.println("SearchCodesCounter = "+SearchCodesCounter+" SearchCodes = "+SearchCodes);
						}
					}
					
					System.out.println("Tail SearchCodesCounter = "+SearchCodesCounter);
					if (SearchCodesCounter>0) // if the is a tail //
					{
						System.out.println("SearchCodes = "+SearchCodes);
						AmazonApi.Get_Items_LookUp(SearchCodes,"ASIN",ItemsListToAmazon);
						SearchCodes = "";
						SearchCodesCounter = 1;
					}
					Search.Finditem(ItemsListToAmazon);


				}catch(Exception e){System.out.println("eBay search faild");}
				
				System.out.println("Adding "+ItemsListToAmazon.size()+" elements");
				Db.Set_product_from_database(ItemsListToAmazon);

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
				ItemsListToAmazon.removeAll(ItemsListToAmazon);
			}
			line = null;
			System.gc();

	} catch (FileNotFoundException ex) {
		System.out.println("Can't read from file ");
	} catch (IOException ex) {

		System.out.println("Can't read from file ");
	}
		System.out.println("Items added = "+Counter);
		Driver.close();
		Driver = null;
		ItemsListToAmazon = null;
		ItemsList_new = null;
		fis = null;
		SearchCodes = null;
		Db = null;
		SearchCodes = null;
		reader = null;
		AmazonApi = null;
		Search = null;;
		path = null;
		System.gc();
	}

	public  double Min_price_to_sale(double price) {
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
			for (int i = 0; i < 5; i++) {
				Add_Product_To_List(driver, ItemsList_new);
				Next_Page_Amazon1(driver);//
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
	
	void Add_Product_To_List(ChromeDriver Driver,List<product> ItemsList_new) throws InterruptedException {
		List<WebElement> Webelements = new ArrayList<WebElement>();
		String s = null;
		product temp_product = new product();//
		String Temp;
		Thread.sleep(1000);

		Webelements = Driver.findElements(By.className("zg-item-immersion"));
		if (Webelements.size() == 0)
		{
			Webelements = Driver.findElements(By.className("zg_itemImmersion"));
		}
		for (WebElement ele : Webelements) 
		{
			try {
				System.out.println(ele.getText());//
				temp_product.link = ele.findElement(By.tagName("a")).getAttribute("href");
				if (category.equals("books")) 
				{
					temp_product.ASIN = temp_product.link.substring(temp_product.link.indexOf("/dp/") + 4,temp_product.link.indexOf("/ref"));
					temp_product.bestresult = temp_product.ASIN;
				}
				else 
				{
				try{
				temp_product.ASIN = temp_product.link.substring(temp_product.link.indexOf("/dp/") + 4,temp_product.link.indexOf("/ref"));
				temp_product.bestresult = temp_product.link.substring(temp_product.link.indexOf(".com/") + 5,temp_product.link.indexOf("/dp/"));
				}catch(Exception e){}
				}
				
				if (temp_product.bestresult.contains("-")) temp_product.bestresult = temp_product.bestresult.replace("-", " ");
				s = ele.getText();
				if (s.contains("Kindle Edition") || s.contains("Audible Audio Edition")|| s.contains("Audio CD")) 
					System.out.println("Books not fit");
				else
				{
				s = s.substring(s.indexOf("\n") + 1);
				if (s.contains("%")) 
				{
				Temp = s.substring(0, s.indexOf("%"));
				if (Temp.contains(","))Temp = Temp.replace(",", "");
				temp_product.MAS_percent = Integer.parseInt(Temp);
				}

				s = s.substring(s.indexOf("$") + 1);
				if (s.contains("\n")) s = s.substring(0, s.indexOf("\n")); 
				
				try{
				temp_product.price = Double.parseDouble(s);
				}catch (Exception e)
				{
					System.out.println(s);
					System.out.println("parseDouble error");
				}
				////
				
				if (!IsItemExist(temp_product)) 
				{
					ItemsList_new.add(temp_product);
					temp_product = new product();
				}
				
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			Webelements = null;
			s = null;
			System.gc();
		}

	}

	boolean IsItemExist(product temp_product) throws SQLException 
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".items;");
		
		res = statement.executeQuery("SELECT count(*) FROM amazon.items where ASIN = '"+temp_product.ASIN+"';");
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

	void Next_Page_Amazon(ChromeDriver Driver) {
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
	
	void Next_Page_Amazon1(ChromeDriver Driver) {
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
