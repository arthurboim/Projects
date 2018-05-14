package PriceChanger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TaxUpDateNewItems {

	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  ResultSet res = null;
	public static Connection con = null;
	public static  java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;
	public static ChromeOptions options = new ChromeOptions();
	public static ChromeDriver Driver2 = null;
	public DatabasePriceChanger Db = null;
	public ItemsPosition ItemsPosition = null;
	
	public TaxUpDateNewItems() throws InterruptedException, SQLException {
		ItemsPosition = new ItemsPosition();
		Db = new DatabasePriceChanger();

		//System.out.println("Constractor of Database");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				//	System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
				//	System.out.println("Connection = "+Connection);
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

	public void GetItemsForTaxChecking()
	{
		if (Driver2==null)
		{
		options.addArguments("--start-maximized");
		options.addArguments("--user-data-dir=C:\\User Data2");// remove //
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver2 = new ChromeDriver(options);
		}
		
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			 res = statement.executeQuery("SELECT * FROM amazon.items where Date_scan>'2017-10-15' and breakevenlowestprice>1.14 and breakevenlowestprice<1.60 and deepsearch =1 and ((amazon_price>70  and amazon_price<300 and soldlastweek>0) or(amazon_price>5  and amazon_price<30 and soldlastweek>2) or (amazon_price>30  and amazon_price<70 and soldlastweek>1));");
			while(res.next())
			{
				ItemsPosition.GetAmazonPriceScraper(res.getString("asin"),Driver2);
				UpdateTaxItems(res.getString("asin"),ItemsPosition.GetAmazonTaxScraper(res.getString("asin"),Driver2));
			}
			
			con = null;
			statement =null;
			System.gc();
			
		
		} catch (SQLException e) {}
		Driver2.close();
		Driver2 = null;
		System.gc();
	}
	
	public void UpdateTaxItems(String Asin, double CurrentAmazonTax  ) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "Update amazon.items set Tax = "+CurrentAmazonTax+" where asin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void GetProductFromSellers()
	{
		if (Driver2==null)
		{
		options.addArguments("--start-maximized");
		options.addArguments("--user-data-dir=C:\\User Data2");// remove //
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver2 = new ChromeDriver(options);
		}

		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			 res = statement.executeQuery("SELECT * FROM amazon.productfromsellers where scan_date >= '2017-10-13'  and ((tax is null) or (tax = -1)) and uploaded is null and (breakevenforlowest>1.14  and ((amazon_price>70  and amazon_price<300 and soldlastweekall>0) or(amazon_price>5  and amazon_price<30 and soldlastweekall>2) or (amazon_price>30  and amazon_price<70 and soldlastweekall>1)))");
			while(res.next())
			{
				try{
				ItemsPosition.GetAmazonPriceScraper(res.getString("asin"),Driver2);
				UpdateTaxScraperProductFromSellers(res.getString("asin"),ItemsPosition.GetAmazonTaxScraper(res.getString("asin"),Driver2));
				}catch(Exception e){}
			}
			con = null;
			statement =null;
			System.gc();
		} catch (SQLException e) {}
		Driver2.close();
		Driver2 = null;
		System.gc();
	}
	
	public void UpdateTaxScraperProductFromSellers(String Asin, double CurrentAmazonTax ) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "Update amazon.productfromsellers set Tax = "+CurrentAmazonTax+" where asin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
}
