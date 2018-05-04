package AmazonOrders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TaxChecker {

		public static String Asin = null;
		public static double Tax = -1;
		public static double Price = -1;
		public static Connection con ;
		public static java.sql.Statement statement ;
		public static String Connection = null;
		public static String scham = null;
		public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
		public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
		public static String AWS_SECRET_KEY_FROM_FILE = null;
		public static String ENDPOINT_FROM_FILE = null;
		
		public TaxChecker() 
		{
				System.out.println("Constractor of TaxChecker");
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
						//	System.out.println("Schame = "+scham);
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
	
		public void TaxCheckerMain2(String Asin)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.DATE, -10); //2 weeks
		    System.out.println(dateFormat.format(calendar));
			
			System.out.println(dateFormat.format(date));
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("--user-data-dir=C:\\User Data");
			System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
			ChromeDriver Driver = new ChromeDriver(options);
			Driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Driver.get("https://www.amazon.com/gp/offer-listing/"+Asin);
			try{
			Driver.findElementByXPath("//*[@id='olpRefinements']/fieldset[1]/ul/li[1]/span/span/div/label/input").click();
			Driver.findElementByXPath("//*[@id='olpRefinements']/fieldset[2]/ul/li[1]/span/span/div/label/input").click();
			System.out.println(Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]").getText());
			System.out.println(Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span").getText());
			}catch(Exception e){}
		}
	
		public void TaxCheckerScraper(String Asin,ChromeDriver Driver) throws InterruptedException
		{
			String EstimatedTax = null;
			String Price = null;
			Driver.get("https://www.amazon.com/gp/offer-listing/"+Asin);
			try{
			Driver.findElementByXPath("//*[@id='olpRefinements']/fieldset[1]/ul/li[1]/span/span/div/label/input").click();
			Driver.findElementByXPath("//*[@id='olpRefinements']/fieldset[2]/ul/li[1]/span/span/div/label/input").click();
			
			Price = Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]").getText();
			System.out.println("Price = "+Price);
			EstimatedTax = Driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span").getText();
			 //$0.00 estimated tax
			if (!EstimatedTax.contains("shipping")&&EstimatedTax.contains("estimated tax")) 
			{
			try{
			EstimatedTax = EstimatedTax.substring(EstimatedTax.indexOf("$")+1, EstimatedTax.indexOf("estimated tax")-1);
			Tax = Double.parseDouble(EstimatedTax);
			}catch(Exception e){
				Tax =-1;
				Asin = null;
				}
			}

			}catch(Exception e){}
		}
	
		public void SetAsinsForTaxChecking() throws SQLException
		{

			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();
			try{
				String WriteToData;
				WriteToData = "UPDATE "+scham+".online SET Tax="+Tax+" WHERE amazonasin='"+Asin+"';";		  
				statement.executeUpdate(WriteToData);
				}catch(SQLException e)
				{
				e.printStackTrace();
				}			
		
		
			
		
			
		}
		
}
