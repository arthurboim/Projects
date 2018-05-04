package PriceChanger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DatabasePriceChanger {
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  ResultSet res = null;
	public static Connection con = null;
	public static  java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;
	public static SimpleDateFormat  format= null;
	public static Date date = new Date();
	
	public DatabasePriceChanger() throws SQLException {
		format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
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
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					Connection =Connection+scham;
					con = DriverManager.getConnection(Connection,"root","root");
					statement = con.createStatement();
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

	public int CheckBestMatchOptimizedFlag(String Asin)
	{

		try {
			
			 
			 res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"';");
			 res.next();
			return res.getInt("BestMatchOptimizedFlag");
		} catch (SQLException e) {}
		return -1;
	}
	
	public int GetLowestpricePosition(String Asin)
	{

		try {
		
			
			res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"';");
			res.next();
			return res.getInt("LowestPricePosition");
		} catch (SQLException e) {}
		return -1;
	}
	
	public void UpdateBestmatchFlag(String Asin,int flag) throws SQLException
	{
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET BestMatchOptimizedFlag = "+flag+" WHERE AmazonAsin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	
	}
	
	public void UpdateRemoveItemFlag(String Asin,int flag) throws SQLException
	{
	
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET RemoveItemFlag = "+flag+" WHERE AmazonAsin = '"+Asin+"';";
		System.out.println(WriteToData);
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	
	}
	
	public void UpdateSecondLowestPrice(String asin,double SecondLowestPrice) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET eBaySecondLowestprice = "+SecondLowestPrice+" WHERE AmazonAsin = '"+asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	}
	
	public void UpdateBreakeven(String asin,double breakevennow) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET BreakEvenNow = "+breakevennow+" WHERE AmazonAsin = '"+asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	}
	
	public void UpdateCode(String EbayId,String Code) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET Bestresults = '"+Code+"' WHERE EbayId = '"+EbayId+"';";
		System.out.println(WriteToData);		
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	}
	
	public void ProfitPersent(String asin , double ProfitPercent) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET ProfitPercent = "+ProfitPercent+" WHERE AmazonAsin = '"+asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
	}
	
	public double GeteBayLowestLowest(String Asin)
	{
		try {

			res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"';");
			res.next();
			//System.out.println(res.getDouble("eBaylowestprice"));
			return res.getDouble("eBaylowestprice");
		} catch (SQLException e) {}
		return -1;
	}
	
	public double CurrentAmazonPrice(String Asin)
	{
		try {

			res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"';");
			res.next();
			//System.out.println(res.getDouble("CurrentAmazonPriceWithScraper"));
			return res.getDouble("CurrentAmazonPriceWithScraper");
		} catch (SQLException e) {}
		return -1;
	}
	
	public double CurrentAmazonTax(String Asin)
	{
		try {

			 res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"';");
			res.next();
			//System.out.println(res.getDouble("Tax"));
			return res.getDouble("Tax");
		} catch (SQLException e) {}
		return -1;
	}
	
	
	public void UpdateCalcualteBreakEvenLowest(String Asin, double BreakEvenLowest) throws SQLException
	{
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET BreakEvenForLowest = "+BreakEvenLowest+" WHERE AmazonAsin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void UpdateAmazonPriceScraper(String Asin, double CurrentAmazonPrice) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET CurrentAmazonPriceWithScraper = "+CurrentAmazonPrice+" WHERE AmazonAsin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void UpdateTaxScraper(String Asin, double CurrentAmazonTax) throws SQLException
	{

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET Tax = "+CurrentAmazonTax+" WHERE AmazonAsin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}

	public void SetItemInfoToDb(int BestMatchPosition ,int lowestpricePosition, int Ebayresults ,String Code , double EbayLowestprice, double EbaySecondLowestprice , double CurrenteBayPrice , String EbayId) throws SQLException
	{

		 	try{
			String WriteToData;
			WriteToData = "UPDATE "+scham+".online SET BestMatchPosition = "+BestMatchPosition+" , LowestPricePosition = "+lowestpricePosition+" , ebayresults = "+Ebayresults+" , Bestresults = '"+Code+"' , eBaylowestprice = "+EbayLowestprice+" , eBaySecondLowestprice = "+EbaySecondLowestprice+", LastTimeScan = '"+format.format(date.getTime())+"' , CurrenteBayPrice = "+CurrenteBayPrice+" WHERE EbayId = '"+EbayId+"';";		  
			System.out.println(WriteToData);
			statement.executeUpdate(WriteToData);
			}catch(SQLException e){System.out.println("Error in SetItemInfoToDb");e.printStackTrace();}		
		 	
	}
	
	
	public void SetListInfoToDb(ArrayList<ListInformation> List) throws SQLException
	{
		for (ListInformation ele:List)
		{
			SetItemInfoToDb(ele.BestMatchPosition, ele.lowestpricePosition, ele.Ebayresults, ele.Code, ele.EbayLowestprice, ele.EbaySecondLowestprice, ele.CurrenteBayPrice, ele.EbayId);
		}
	}
	

	public void UpdateDateSoldAndSoldAmount(String Date ,int SoldAmount, String ebayid) throws SQLException
	{

		
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET TransactionsLastMonth = "+SoldAmount+" , LastDateSold = '"+Date+"' WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	
		
	
	}
	
	public void UpdatePriceChangeTo0() throws SQLException
	{
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  PriceChange = 0;";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	
	
	
	
	public void GetOnlineItemsInfo(ArrayList<ListInformation> List,String StoreName) throws SQLException
	{
		res = statement.executeQuery("SELECT * FROM "+scham+".online where StoreName = '"+StoreName+"';"); 
		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			List.add(item);
		}
		
	}

	public void GetOnlineItemsToUpdate(ArrayList<ListInformation> List) throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -20);
	    date =  cal.getTime();

		res = statement.executeQuery("SELECT * FROM "+scham+".online where ebayResults>1 and (LastDateSold<'"+dateFormat.format(date)+"' or LastDateSold is null) and LowestPricePosition>1 and ProfitPercent !=2 and  BestMatchPosition>1;"); 

		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			item.BreakEvenForLowest = res.getDouble("BreakEvenForLowest");
			item.Tax = res.getDouble("Tax");
			item.AmazonPrice = res.getDouble("CurrentAmazonPriceWithScraper");
			item.EbayLowestprice = res.getDouble("eBaylowestprice");
			item.EbaySecondLowestprice = res.getDouble("eBaySecondLowestprice");
			item.Asin = res.getString("AmazonAsin");
			item.PriceChangeFlag = res.getInt("PriceChange");
			List.add(item);
		}
		
	}
	
	public void GetOnlineItemsToUpdate1(ArrayList<ListInformation> List) throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -20);
	    date =  cal.getTime();
		
		res = statement.executeQuery("SELECT * FROM "+scham+".online where PriceChange = 0 and	LastTimePriceUpdate is  null and ebayResults>1 and (LastDateSold<'"+dateFormat.format(date)+"' or LastDateSold is null) and LowestPricePosition>1 and ProfitPercent !=2 and  BestMatchPosition>1;"); 

		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			item.BreakEvenForLowest = res.getDouble("BreakEvenForLowest");
			item.Tax = res.getDouble("Tax");
			item.AmazonPrice = res.getDouble("CurrentAmazonPriceWithScraper");
			item.EbayLowestprice = res.getDouble("eBaylowestprice");
			item.EbaySecondLowestprice = res.getDouble("eBaySecondLowestprice");
			item.Asin = res.getString("AmazonAsin");
			List.add(item);
		}
		
	}
	
	public void GetOnlineItemsToUpdate2(ArrayList<ListInformation> List) throws SQLException
	{
		
		res = statement.executeQuery("SELECT * FROM "+scham+".online where LowestPricePosition=1;"); 
		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			item.BreakEvenForLowest = res.getDouble("BreakEvenForLowest");
			item.Tax = res.getDouble("Tax");
			item.AmazonPrice = res.getDouble("CurrentAmazonPriceWithScraper");
			item.EbayLowestprice = res.getDouble("eBaylowestprice");
			item.EbaySecondLowestprice = res.getDouble("eBaySecondLowestprice");
			item.Asin = res.getString("AmazonAsin");
			item.lowestpricePosition = res.getInt("LowestPricePosition");
			List.add(item);
		}
		
	}
	
	
	public void GetOnlineItemsInfoThatNotSold(ArrayList<ListInformation> List,String StoreName) throws SQLException
	{		
		res = statement.executeQuery("SELECT * FROM "+scham+".online where StoreName = '"+StoreName+"';"); 
		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			item.Asin = res.getString("AmazonAsin");
			List.add(item);
		}
		
	}

	public void GetOnlineItemsWithOutCode(ArrayList<ListInformation> List,String StoreName) throws SQLException
	{		
		res = statement.executeQuery("SELECT * FROM "+scham+".online where Bestresults is null and StoreName = '"+StoreName+"';"); 
		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			item.Asin = res.getString("AmazonAsin");
			List.add(item);
		}
		
	}
	
	
	
	public void GetOnlineItemsThatCanImprovePossition(ArrayList<ListInformation> List) throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -10);
	    date =  cal.getTime();
		res = statement.executeQuery("SELECT * FROM "+scham+".online where  ebayResults>1 and (LastDateSold<'"+dateFormat.format(date)+"' or LastDateSold is null) and LowestPricePosition>1 and BestMatchPosition>1;"); 
		while(res.next())
		{
			ListInformation item = new ListInformation();
			item.Code = res.getString("Bestresults");
			item.EbayId = res.getString("EbayId");
			List.add(item);
		}
		
	}

	public void UpdateFlagChange(String ebayid) throws SQLException
	{
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  PriceChange = 1 WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	
	
	public void UpdateTimeChange(String ebayid) throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    date =  cal.getTime();

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  LastTimePriceUpdate = '"+dateFormat.format(date)+"' WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void UpdateProfitPercent(String ebayid, double ProfitPercent) throws SQLException
	{
		
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  ProfitPercent = "+ProfitPercent+" WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void UpdateStoreName(String ebayid,String StoreName) throws SQLException
	{
		
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  StoreName = '"+StoreName+"' WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	public void UpdateAmazonPrice(String ebayid, double AmazonPrice) throws SQLException
	{
		
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  CurrentAmazonPriceWithScraper = "+AmazonPrice+" WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
	
	
	
	public void UpdateBreakEven(String ebayid, double BreakEvenNow) throws SQLException
	{
		
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET  BreakEvenNow = "+BreakEvenNow+" WHERE EbayId = '"+ebayid+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
	}
	
}
