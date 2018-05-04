package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WatchersWakeUp {

	public String EbayIdWachers = null;
	public double Breakeven = -1;
	static final String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	static Connection con = null;
	static java.sql.Statement statement = null;
	static String Connection = null;
	static String scham = null;
	
	public WatchersWakeUp() {
		System.out.println("Constractor of WatchersWakeUp");
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

	public void WatchersWakeUpMain() throws SQLException
	{
		System.out.println("Watchers wakeup started...");
		double breakevenchange = -0.1;
		ArrayList<WatchersWakeUp> List = new ArrayList<WatchersWakeUp>();
		GetWatchersWakeUp(List);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--user-data-dir=C:\\User Data");
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver Driver = new ChromeDriver(options);
		Driver.get("https://autodstools.com/active_listings/");
		System.out.println("List size = "+List.size());
		for(WatchersWakeUp ele:List)
		{
			WatchersWakeUpScraper(ele.EbayIdWachers,Driver,breakevenchange);
		}
		System.out.println("Watchers wakeup finished...");
	}
	
	public void WatchersWakeUpScraper(String Ebayid , ChromeDriver Driver,double breakevenchange)
	{
		String BreakEven = null;
		double BreakEven2 = -1;
		try{
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").clear();
		Driver.findElementByXPath("//*[@id='products_table_filter']/label/input").sendKeys(Ebayid);
		Thread.sleep(4000);
		Driver.findElementByXPath("//*[@id='products_table']/tbody/tr/td[2]/span/img").click();
		Thread.sleep(4000);
		BreakEven = Driver.findElementByXPath("//*[@id='new_break_even']").getAttribute("placeholder");
		BreakEven2 = Double.parseDouble(BreakEven);
		BreakEven2 = BreakEven2+breakevenchange;
		BreakEven = String.valueOf(BreakEven2);
		System.out.println("breakeven2 = "+BreakEven2);
		System.out.println("breakeven = "+BreakEven);

		Driver.findElementByXPath("//*[@id='new_break_even']").clear();
		Driver.findElementByXPath("//*[@id='new_break_even']").sendKeys(BreakEven);
		Driver.findElementByXPath("//*[@id='modal-change_active_product_details']/div/div/div[3]/button[6]").click();
		Thread.sleep(4000);
		try{
		Driver.findElementByXPath("/html/body/div[9]/div[7]/div/button").click();
		Thread.sleep(2500);
		SetBreakEven(Ebayid,BreakEven2);
		System.out.println("new break even set to db");
		}catch(Exception e1){
			Driver.findElementByXPath("/html/body/div[8]/div[7]/div/button").click();
			Thread.sleep(4000);
		}
		
		Thread.sleep(10000);
		}catch(Exception e){}
	}

	public void GetWatchersWakeUp(ArrayList<WatchersWakeUp> List) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where watchers>0 and breakeven = 19.0"); 
		
		while(res.next())
		{
		 WatchersWakeUp WatchersWakeUp = new WatchersWakeUp();
		 WatchersWakeUp.EbayIdWachers  = res.getString("EbayId");
		 WatchersWakeUp.Breakeven =  res.getDouble("breakeven");
		 List.add(WatchersWakeUp);
		}

	}
	
	public void SetBreakEven(String EbayId,double newbreakeven) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET breakeven = "+newbreakeven+" WHERE EbayId = '"+EbayId+"';";
		statement.executeUpdate(WriteToData);
		} catch(SQLException e){e.printStackTrace();}		
	}

}
