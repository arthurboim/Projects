package MainPackage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class ScrapInfo {

	public static double currency = 0;
	public static double Total_Added = 0;
	public static String category;
	public  static Connection con = null;
	public  static String  Connection= null;
	public  static String  User= null;
	public  static String  Pass= null;
	public  static java.sql.Statement statement= null;
	public  ResultSet res2 = null;
	public  String FILENAME = "C:\\keys\\ubuythebest4u-Keys.txt";
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
				//Connection = "jdbc:mysql://localhost:444/";
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

	void Date_Convert(product temp_product)
	{	
	String month = temp_product.Date;
	String Day = temp_product.Date;
	Day = Day.substring(Day.indexOf(" "));
	Day = Day.substring(1,Day.indexOf(","));
	if (Day.length()==1) Day = "0"+Day;
	month = month.substring(0,month.indexOf(" "));
	int year;
	String temp_year = null;
	if (temp_product.Date.contains("."))
	{
		temp_year = temp_product.Date.substring(temp_product.Date.indexOf(",")+2,temp_product.Date.indexOf("."));
		
		year = Integer.parseInt(temp_year);
	}
	else
	year = Calendar.getInstance().get(Calendar.YEAR);
	
	switch(month)
		{
        case "Jan":
        	temp_product.Date = year+"-01-"+Day;
            break;
        case "Feb":
        	temp_product.Date = year+"-02-"+Day;
            break;
        case "Mar":
        	temp_product.Date = year+"-03-"+Day;
            break;
        case "Apr":
        	temp_product.Date = year+"-04-"+Day;
            break;
        case "May":
        	temp_product.Date = year+"-05-"+Day;
            break;
        case "June":
        	temp_product.Date = year+"-06-"+Day;
            break;
        case "July":
        	temp_product.Date = year+"-07-"+Day;
            break;
        case "Aug":
        	temp_product.Date = year+"-08-"+Day;
            break;
        case "Sep":
        	temp_product.Date = year+"-09-"+Day;
            break;
        case "Oct":
        	temp_product.Date = year+"-10-"+Day;
            break;
        case "Nov":
        	temp_product.Date = year+"-11-"+Day;
            break;
        case "Dec":
        	temp_product.Date = year+"-12-"+Day;
            break;
            
        case "January":
        	temp_product.Date = year+"-01-"+Day;
            break;
        case "February":
        	temp_product.Date = year+"-02-"+Day;
            break;
        case "March":
        	temp_product.Date = year+"-03-"+Day;
            break;
        case "April":
        	temp_product.Date = year+"-04-"+Day;
            break;
        case "August":
        	temp_product.Date = year+"-08-"+Day;
            break;
        case "September":
        	temp_product.Date = year+"-09-"+Day;
            break;
        case "October":
        	temp_product.Date = year+"-10-"+Day;
            break;
        case "November":
        	temp_product.Date = year+"-11-"+Day;
            break;
        case "December":
        	temp_product.Date = year+"-12-"+Day;
            break;
		
		
		}
	}

	void print_to_file_items(List<product> ItemsList, String Path, int added)
			throws FileNotFoundException, UnsupportedEncodingException {
		Total_Added += added;
		PrintWriter writer = new PrintWriter(Path, "UTF-8");
		writer.println("New Items From the begining = " + Total_Added);
		writer.println("New Items added on last hour = " + added);
		Sort_by_persent(ItemsList);
		writer.println("\n\nSort_by_persent\n");

		for (product ele : ItemsList) {
			if (ele.ebayResults != -1) {
				writer.println("Persent = " + ele.MAS_percent + " Arbitraje = " + ele.arbitraje + "   ResultsEbay = "
						+ ele.ebayResults + " Asin = " + ele.ASIN + " bestresult = " + ele.bestresult);
			}
		}
		Sort_by_arbitraje(ItemsList);
		writer.println("\n\nSort_by_arbitraje at " + current_time());
		for (product ele : ItemsList) {
			if (ele.ebayResults != -1) {
				writer.println("Persent = " + ele.MAS_percent + " Arbitraje = " + ele.arbitraje + "   ResultsEbay = "
						+ ele.ebayResults + " Asin = " + ele.ASIN);
			}
		}
		writer.close();
	}

	void Sort_by_persent(List<product> ItemsList) {
		Collections.sort(ItemsList, new Comparator<product>() {
			@Override
			public int compare(product o1, product o2) {
				return ((Integer) o1.MAS_percent).compareTo(o2.MAS_percent);
			}
		});
		Collections.reverse(ItemsList);
	}

	void Sort_by_arbitraje(List<product> ItemsList) {
		Collections.sort(ItemsList, new Comparator<product>() {
			@Override
			public int compare(product o1, product o2) {
				return Double.compare(o1.arbitraje, o2.arbitraje);
			}
		});

		Collections.reverse(ItemsList);
	}

	void Sort_by_sold(List<book> ItemsList) {
		Collections.sort(ItemsList, new Comparator<product>() {
			@Override
			public int compare(product o1, product o2) {
				return Double.compare(o1.sold, o2.sold);
			}
		});

		Collections.reverse(ItemsList);

	}

	void sort_by_arbitraje_books(List<book> ItemsList) {

		Collections.sort(ItemsList, new Comparator<product>() {
			@Override
			public int compare(product o1, product o2) {
				return Double.compare(o1.arbitraje, o2.arbitraje);
			}
		});

		Collections.reverse(ItemsList);

	}

	void Pre_order_books(String url) throws FileNotFoundException, UnsupportedEncodingException {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver driverprReOrder = new ChromeDriver();
		driverprReOrder.get(url);
		PrintWriter writer = new PrintWriter("C:\\Users\\Noname\\Desktop\\booksPreorder.txt", "UTF-8");
		List<WebElement> Webelements = new ArrayList<WebElement>();
		Webelements = driverprReOrder.findElements(By.tagName("li"));
		List<WebElement> Webelements2 = driverprReOrder.findElements(By.tagName("li"));
		for (WebElement ele : Webelements) {
			WebElement temp = ele;
			Webelements2 = ele.findElements(By.tagName("span"));
			try {
				if (temp.findElement(By.tagName("span")).getText().equals("Best Seller")) {
					System.out.println("Best Seller");
					String s = temp.findElement(By.tagName("a")).getAttribute("href");
					s = s.substring(s.indexOf("dp/") + 3, s.indexOf("/ref"));
					System.out.println(s);
					writer.print(s);
					for (WebElement ele1 : Webelements2) {
						if (ele1.findElement(By.tagName("span")).getAttribute("class")
								.equals("a-size-small a-color-secondary")) {
							s = ele1.getText();
							writer.println(" " + s);
							System.out.println(s);
						}
					}
				}
			} catch (Exception e) {
			}

		}
		writer.close();
	}
	

	void Books_info(String url, List<book> Bookslist) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		Amazon_Get_Items_Movers_And_Shakers_books(url, Bookslist);
		driver.close();
	}

	
	////////////////// EBAY //////////////////////


	void Ebay_items_Check(List<product> ItemsList) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver();
		Driver.get("http://www.ebay.com/");

		Ebay_Search(Driver, "Iphone");
		//Buy_it_now(Driver);
		
		//Search_by_Lowest_Price(Driver);
		//Set_Country(Driver);
		//if (currency == 0) currency = check_currency(driver);
		//Driver.findElement(By.xpath("//*[@id='e1-151']")).click();
		//Ebay_items_List_Search_by_sold(Driver, ItemsList);

	}


	String Get_8words_from_title(product ele) {
		String temp = null;
		String temp2 = null;
		temp = ele.title;
		for (int i = 0; i < 8; i++) {
			try {
				if (i == 0)
					temp2 = temp.substring(0, temp.indexOf(" ") + 1);
				else {
					temp2 = temp2 + temp.substring(0, temp.indexOf(" ") + 1);
				}
				temp = temp.substring(temp.indexOf(" ") + 1);
			} catch (Exception e) {
				return ele.title;
			}
		}
		return temp2;

	}

	String Get_string_from_Url_tosearch(product ele) {
		String Temp = ele.link;
		Temp = Temp.substring(Temp.indexOf(".com/") + 5, Temp.indexOf("/dp/"));
		Temp = Temp.replace("-", " ");
		ele.bestresult = Temp;
		return Temp;
	}

	String get_upcs_string(product ele) {
		String temp;
		try {

			temp = ele.UPC;
			temp = temp.substring(0, temp.indexOf(" "));
			ele.UPC = ele.UPC.substring(ele.UPC.indexOf(", ") + 2);
		} catch (Exception e) {
			temp = ele.UPC;
			ele.UPC = null;
			return temp;
		}
		return temp;
	}

	void Ebay_get_search_results_for_Items(ChromeDriver driver, product product, String temp1) {
		String temp;
		try {
			temp = driver.findElement(By.className("rsHdr")).getText();
			temp = temp.substring(0, temp.indexOf(" "));
			if (product.ebayResults <= Integer.parseInt(temp)) {
				product.ebayResults = Integer.parseInt(temp);
				product.bestresult = temp1;
				temp = driver.findElement(By.className("bold")).getText();
				temp = temp.substring(temp.indexOf(" ") + 1, temp.length());
				if (temp.contains("\n"))
					temp = temp.substring(0, temp.indexOf("\n"));
				product.ebayLowestPrice = Double.parseDouble(temp);
				product.ebayLowestPrice = product.ebayLowestPrice / currency;
				temp = driver.findElement(By.className("lvshipping")).getText();
				if (temp.equals("Free shipping"))
					product.Ebay_shipping = 0.0;
				else {
					temp = temp.substring(5, temp.indexOf("shipping") - 1);
					product.Ebay_shipping = Double.parseDouble(temp);
					product.Ebay_shipping = product.Ebay_shipping / currency;
				} // +ILS 19.50 shipping
			}
		} // try
		catch (Exception e) {
			System.out.println("Can't get ditalest Items");
		}
	}

	void Ebay_Book_List_Search_by_sold(ChromeDriver driver, List<book> Bookslist) throws InterruptedException {
		String temp = null;
		List<WebElement> Webelements = new ArrayList<WebElement>();
		try {
			Webelements = driver.findElements(By.tagName("a"));
		} catch (Exception e) {
		}
		for (WebElement ele : Webelements) {
			try {
				temp = ele.getAttribute("href");
				if (temp.contains("&LH_Complete=1&LH_Sold=1&rt=nc")) {
					ele.click();;
					break;
				}
			} catch (Exception e) {
			}
		}


		for (book ele : Bookslist) {//
			try {
				if (ele.sold==-1)
				{
				Ebay_Search(driver, ele.ASIN);
				temp = driver.findElement(By.className("rsHdr")).getText();
				temp = temp.substring(0, temp.indexOf(" "));
				ele.sold = Integer.parseInt(temp);
				}
			} catch (Exception e) {
				ele.sold = 0;
			}
		}

	}


	public  double Min_price_to_sale(double price) {
		double ebay_fees = 0.09;
		double paypal_fees = 0.039;
		double paypal_fixed = 0.3;
		double my_percent = 1.06;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}

	void Ebay_get_search_results(ChromeDriver driver, book Book) throws InterruptedException 
	{
			String temp;
			temp = driver.findElement(By.className("rsHdr")).getText();
			temp = temp.substring(0, temp.indexOf(" "));
			Book.ebayResults = Integer.parseInt(temp);
			temp = driver.findElement(By.className("bold")).getText();
			temp = temp.substring(temp.indexOf(" ") + 1, temp.length());
			if (temp.contains("\n"))
				temp = temp.substring(0, temp.indexOf("\n"));
			Book.ebayLowestPrice = Double.parseDouble(temp);
			Book.ebayLowestPrice = Book.ebayLowestPrice / currency;
			temp = driver.findElement(By.className("lvshipping")).getText();
			if (temp.equals("Free shipping"))
				Book.Ebay_shipping = 0.0;
			else {
				temp = temp.substring(5, temp.indexOf("shipping") - 1);
				Book.Ebay_shipping = Double.parseDouble(temp);
				Book.Ebay_shipping = Book.Ebay_shipping / currency;
			} // +ILS 19.50 shipping
		
	}

	void Ebay_Search(ChromeDriver driver, String Tosearch) throws InterruptedException {
		try {
			driver.findElement(By.xpath("//*[@id='gh-ac']")).click();
			driver.findElement(By.xpath("//*[@id='gh-ac']")).clear();
			driver.findElement(By.xpath("//*[@id='gh-ac']")).sendKeys(Tosearch);
			driver.findElement(By.xpath("//*[@id='gh-btn']")).click();
		} catch (Exception e) {
			System.out.println("Cant search");
		}
	}

	void Search_by_Lowest_Price(ChromeDriver driver) {
		driver.findElement(By.xpath("//*[@id='DashSortByContainer']/ul[1]/li/a")).click();
		driver.findElement(By.xpath("//*[@id='SortMenu']/li[3]/a")).click();
	}

	void Set_Country(ChromeDriver driver) {
		driver.findElement(By.xpath("//*[@id='loczip']/a")).click();
		driver.findElement(By.xpath("//*[@id='zip_e1-5']")).click();
		driver.findElement(By.xpath("//*[@id='zip_e1-5']/option[202]")).click();
		driver.findElement(By.xpath("//*[@id='zip_e1-8']")).click();
	}

	double check_currency(ChromeDriver driver) throws InterruptedException {
		String currency_type;
		currency_type = driver.findElement(By.className("bold")).getText();
		if (currency_type.contains("$"))
			return 1;
		else {
			return get_convetion_rate();
		}

	}

	double get_convetion_rate() throws InterruptedException {
		// System.setProperty("webdriver.chrome.driver",
		// "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		driver.get("http://pages.ebay.com/services/buyandsell/currencyconverter.html");
		driver.findElement(
				By.xpath("/html/body/table[3]/tbody/tr[2]/td[3]/form/ul[1]/table/tbody/tr[2]/td[5]/select/option[61]"))
				.click();
		driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[2]/td[3]/form/ul[1]/table/tbody/tr[3]/td/input"))
				.click();
		Thread.sleep(2000);
		String s = driver
				.findElement(By
						.xpath(" /html/body/p/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[2]/td/table/tbody/tr[1]/td[5]/font/font[1]/b"))
				.getText();
		s = s.substring(0, s.indexOf(" "));
		driver.close();
		return Double.parseDouble(s);

	}

	int Buy_it_now(ChromeDriver driver) {
		List<WebElement> list = new ArrayList<WebElement>();
		list = driver.findElements(By.tagName("a"));
		for (WebElement ele : list) {
			if (ele.getText().contains("Buy It Now")) {
				ele.click();
				return 1;
			}
		}
		return 0;
	}

	////////////////// EBAY //////////////////////


	////////////////// AMAZON //////////////////////

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
	String Get_full_title(ChromeDriver Driver)
	{
		return (Driver.findElement(By.xpath("//*[@id='productTitle']")).getText());
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

	void Amazon_Get_Items_Movers_And_Shakers_books(String url, List<book> Bookslist) {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver();
		Driver.get(url);
		for (int i = 0; i < 5; i++) {
			Add_Books_To_List(Driver, Bookslist);
			Next_Page_Amazon(Driver);
		}
		Driver.close();
	}

	void Amazon_Get_Items_bestsellers_books(ChromeDriver Driver, List<book> Bookslist,List<book> Bookslist_new) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			add_book_to_list_bestseller(Driver, Bookslist,Bookslist_new);
			Next_Page_Amazon(Driver);
		}
	}

	void Add_Books_To_List(ChromeDriver Driver, List<book> Bookslist) {
		List<WebElement> Webelements = new ArrayList<WebElement>();
		String s;
		book temp_book = new book();

		Webelements = Driver.findElements(By.className("zg_itemImmersion"));
		for (WebElement ele : Webelements) {
			s = ele.getText();
			temp_book.link = ele.findElement(By.className("a-link-normal")).getAttribute("href");
			temp_book.ASIN = Get_Asin(temp_book.link);
			temp_book.isbn = Get_isbn(ele);
			s = s.substring(s.indexOf("\n") + 1);
			String Temp = s.substring(0, s.indexOf("%"));
			Temp = Temp.replace(",", "");
			temp_book.MAS_percent = Integer.parseInt(Temp);
			s = s.substring(s.indexOf("\n") + 1);
			temp_book.Amazon_Rank = Integer.parseInt(s.substring(s.indexOf(":") + 2, s.indexOf("(was") - 1));
			s = s.substring(s.indexOf("\n") + 1);
			temp_book.title = s.substring(0, s.indexOf("\n"));
			s = s.substring(s.indexOf("\n") + 1);
			temp_book.by = s.substring(0, s.indexOf("\n"));
			s = s.substring(s.indexOf("\n") + 1);
			try {
				temp_book.price = Double.parseDouble(s.substring(s.indexOf("$") + 1));
			} catch (Exception e) {
				temp_book.price = 0;
			}
			if (Check_Book(temp_book, Bookslist) == true) {
				Bookslist.add(temp_book);
				temp_book = new book();
			}

		}
		// print__book_list(Bookslist);
	}

	void add_book_to_list_bestseller(ChromeDriver Driver, List<book> Bookslist,List<book> Bookslist_new) {

		List<WebElement> Webelements = new ArrayList<WebElement>();
		String s;
		book temp_book = new book();

		Webelements = Driver.findElements(By.className("zg_itemImmersion"));
		for (WebElement ele : Webelements) {

			try {
				s = ele.getText();//
				//System.out.println(s);
				temp_book.link = ele.findElement(By.className("a-link-normal")).getAttribute("href");
				temp_book.ASIN = Get_Asin(temp_book.link);
				temp_book.isbn = Get_isbn(ele);
				s = s.substring(s.indexOf("\n") + 1);
				temp_book.title = s.substring(0, s.indexOf("\n"));
				s = s.substring(s.indexOf("\n") + 1);
				temp_book.by = s.substring(0, s.indexOf("\n"));
				s = s.substring(s.indexOf("\n") + 1);
				s = s.substring(s.indexOf("\n") + 1);
				temp_book.Type = s.substring(0, s.indexOf("\n"));
				try {
					temp_book.price = Double.parseDouble(s.substring(s.indexOf("$") + 1));
				} catch (Exception e) {
					temp_book.price = 0;
				}
				if (Check_Book_for_bestsellers(temp_book, Bookslist) == true) {
					Bookslist_new.add(temp_book);
					temp_book = new book();
				}
			} catch (Exception e) {

			}

		}
		// print__book_list(Bookslist);

	}
	
	boolean Check_Book_for_bestsellers(book temp_book, List<book> Bookslist) {
		if (temp_book.Type.contains("Kindle Edition") || temp_book.Type.contains("Audible Audio Edition")|| temp_book.Type.contains("Audio CD"))
			return false;
		for (book ele : Bookslist)
			if (ele.ASIN.equals(temp_book.ASIN)) {
				ele.price = temp_book.price; // update the price//
				System.out.println("found in list");
				System.out.println("ele.ASIN = "+ele.ASIN+"temp_book.ASIN = "+temp_book.ASIN);
				return false;
			}

		return true;

	}

	boolean Check_Book(book temp_book, List<book> Bookslist) {
		for (book ele : Bookslist) {
			if (ele.isbn.equals(temp_book.isbn)) {
				try {
					if (ele.MAS_percent < temp_book.MAS_percent)
						ele.MAS_percent = temp_book.MAS_percent;
					ele.price = temp_book.price;
					return false;
				} catch (Exception e) {
					return false;
				}
			}

		}
		try {
			if (temp_book.MAS_percent > 100)
				return true;
			else
				return false;
		} catch (Exception e) {
			return true;
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

	
	
	String Get_Item_model_number(ChromeDriver Driver) {
		List<WebElement> elements = Driver.findElements(By.tagName("tr"));
		for (WebElement ele : elements) {
			if (ele.getText().contains("Item model number")) {
				WebElement temp = ele;
				String S = temp.getText();
				S = S.substring(18);
				return S;
			}

		}
		return null;
	}

	String Get_UPC(ChromeDriver Driver) {
		List<WebElement> elements = Driver.findElements(By.tagName("tr"));
		for (WebElement ele : elements) {
			if (ele.getText().contains("UPC")) {
				WebElement temp = ele;
				String S = temp.getText();
				S = S.substring(4);
				return S;
			}

		}
		return null;
	}

	String Get_isbn(WebElement ele) {
		String s;
		s = ele.findElement(By.className("a-link-normal")).getAttribute("href");
		s = s.substring(s.indexOf("dp/") + 3, s.indexOf("/ref"));
		return s;
	}

	String Get_Item_Brand_name(ChromeDriver Driver) {
		String brand = null;
		try {
			brand = Driver.findElement(By.xpath("//*[@id='brand']")).getText();
			brand = brand.trim();
			return brand;
		} catch (Exception e) {
			System.out.println("can't get the brand");
			return "";
		}

	}

	String Get_Asin(String s) {
		String Asin = s;
		Asin = Asin.substring(Asin.indexOf("dp/") + 3, Asin.indexOf("/ref"));
		return Asin;

	}
	////////////////// AMAZON //////////////////////

	String current_time() {
		return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
	}

}
