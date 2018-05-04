package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import Ebay.ItemForUpdate;
import Main.ProductOnline;

public class DatabaseOp {

	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;

	
	
	public DatabaseOp() throws SQLException {

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

	public void RemoveItemFromOnlineTable(String Asin) throws SQLException, InterruptedException
	{
	///kjfhbsdfjf
		try{
		//con = DriverManager.getConnection(Connection,"root","root");
		 //statement = con.createStatement();//
		System.out.println("DELETE FROM "+scham+".online WHERE AmazonAsin = '"+Asin+"';");
		statement.executeUpdate("DELETE FROM "+scham+".online WHERE AmazonAsin = '"+Asin+"';");
		}catch(Exception e)
		{
			System.out.println(e);
			System.out.println("RemoveItemFromOnlineTable");
		}
	}

	public void GetAllOnlineItems(ArrayList<ItemForUpdate> ItemsList,String Storename)
	{

		try {
			
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			String DatabaseQuary = null;
			if (Storename.equals("All")) DatabaseQuary = "SELECT * FROM "+scham+".online where ItemSpecific = 0;";
			else DatabaseQuary = "SELECT * FROM "+scham+".online where ItemSpecific = 0 and Storename = '"+Storename+"';";
			ResultSet res = statement.executeQuery(DatabaseQuary);
			while (res.next())
			{
				ItemForUpdate item = new ItemForUpdate();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void SetNewItemToDb(ProductOnline  ele) throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		//con = DriverManager.getConnection(Connection,"root","root");
		//statement = con.createStatement();
				try{
					String WriteToData;
					WriteToData = "INSERT INTO "+scham+".online (AmazonAsin,EbayId,Price,StartDate,ProfitPercent,BreakEvenNow)"+
					"VALUES ('"+ele.getAmazonAsin()+"','"+ele.getEbayId()+"',"+ele.getPrice()+",'"+dateFormat.format(date)+"',"+ele.ProfitPersent+","+ele.BreakEven+");";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e) 
					{
						System.out.println("set error in product "+ele.getAmazonAsin());
						System.out.println(e);
					}		
	}
	
	public String GetEbayId(String Asin)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where amazonasin = '"+Asin+"';"); 
			res.next();
			return res.getString("EbayId");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void GetAllOnline(ArrayList<ItemForUpdate> ItemsList)
	{
		try {
			
			 con = DriverManager.getConnection(Connection,"root","root");
		     statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online;");
			while (res.next())
			{
				ItemForUpdate item = new ItemForUpdate();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	public void GetAndSetTax(ItemForUpdate item)
	{
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where EbayId = '"+item.EbayId+"';");
			while (res.next())
			{
				item.Tax = res.getDouble("Fees");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("No id");
		}

		
		if (item.Tax>=0)
		{
		try{
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();
			String WriteToData;
			WriteToData = "UPDATE "+scham+".online SET Tax="+item.Tax+" WHERE EbayId = '"+item.EbayId+"';";
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
		}
	
	}

	public void SetWatcher(String Ebayid,int watchers) throws SQLException
	{

		 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".online SET Watchers = "+watchers+" where EbayId = '"+Ebayid+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
			
	}

	public void SetItemsOnlineHistory(ItemForUpdate item) throws SQLException
	{

		 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.online SET TransactionsLastMonth = "+item.TransactionsLastMonth+" , LastDateSold = '"+item.LastSaleDate+"' where EbayId = '"+item.EbayId+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		
	}

	public String GetStoreName()
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".store;");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getString(1);
		} catch (SQLException e) {System.out.println("Get store name error");}
		return null;
	}

	public double Get_Monthly_profit(String StoreName)
	{
		Date date = new Date();
		DateFormat fmt = new SimpleDateFormat("MMM", Locale.US);
		try {
			String Quary = null;
			if (StoreName.equals("All")) 
				Quary = "SELECT sum(Profit) FROM "+scham+".orders where Sale_date like '%"+fmt.format(date)+"%' and (OrderStatus = 'Complete' or OrderStatus = 'Orderd');";
			else 
				Quary = "SELECT sum(Profit) FROM "+scham+".orders where Sale_date like '%"+fmt.format(date)+"%' and (OrderStatus = 'Complete' or OrderStatus = 'Orderd') and StoreName = '"+StoreName+"';";
	
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery(Quary);
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getDouble(1);
		} catch (SQLException e) {System.out.println("Get_Monthly_profit");}
		return -1;
	
	}
	
	public int MonthlyOrders(String StoreName)
	{
		Date date = new Date();
		DateFormat fmt = new SimpleDateFormat("MMM", Locale.US);
		try {
			
			String Quary = null;
			if (StoreName.equals("All")) 
			Quary = "SELECT count(id) FROM "+scham+".orders where Sale_date like '%"+fmt.format(date)+"%';";
			else 
			Quary = "SELECT count(id) FROM "+scham+".orders where Sale_date like '%"+fmt.format(date)+"%' and StoreName = '"+StoreName+"';";
			System.out.println(Quary);
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery(Quary);
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("Monthly Orders Error");
			}
		return -1;
	
	
		
	}
	
	public int OnlineItemsAmount(String StoreName)
	{
		try {
			
			
			
			String Quary = null;
			if (StoreName.equals("All")) 
			Quary = "SELECT count(id) FROM "+scham+".online;";
			else 
			Quary = "SELECT count(id) FROM "+scham+".online where StoreName = '"+StoreName+"';";
			System.out.println(Quary);
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery(Quary);
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return -1;

	}
	
	public int OnlineItemsAmount0to30()
	{
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT count(id) FROM "+scham+".online where CurrentAmazonPriceWithScraper<30;");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return -1;

	}
	
	public int OnlineItemsAmount30to70()
	{
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT count(id) FROM "+scham+".online where CurrentAmazonPriceWithScraper<70 and CurrentAmazonPriceWithScraper>30;");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return -1;

	}

	public int OnlineItemsAmount70to300()
	{
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT count(id) FROM "+scham+".online where CurrentAmazonPriceWithScraper<1000 and CurrentAmazonPriceWithScraper>70;");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return -1;

	}

	public double AvgDayProfit()
	{
		Date date = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		date = cal.getTime();
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			System.out.println("SELECT sum(profit) FROM "+scham+".orders where Sale_date >='"+date+"';");
			ResultSet res = statement.executeQuery("SELECT sum(profit) FROM "+scham+".orders where Sale_date >='"+date+"';");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return (res.getDouble(1))/30;
		} catch (SQLException e) {System.out.println("Avg Day Sales Val exception");}
		return -1;
	
	
		
	
		
	}
	
	public double AvgDaySalesVal()
	{
		Date date = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		date = cal.getTime();
		try {
			 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();//
			System.out.println("SELECT count(id) FROM "+scham+".orders where Sale_date >='"+date+"';");
			ResultSet res = statement.executeQuery("SELECT count(id) FROM "+scham+".orders where Sale_date >='"+date+"';");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return ((double)res.getInt(1))/30;
		} catch (SQLException e) {System.out.println("Avg Day Sales Val exception");}
		return -1;
	
	
		
	
		
	}

	public int SoldLast30Days()
	{
		Date date = new Date(); //Tue Aug 22 07:45:55 IDT 2017
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		date = cal.getTime();
		System.out.println(date);
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			System.out.println("SELECT count(id) FROM "+scham+".orders where Sale_date >='"+date+"';");
			ResultSet res = statement.executeQuery("SELECT count(id) FROM "+scham+".orders where Sale_date >='"+date+"';");
			res.next();
			con = null;
			statement =null;
			System.gc();
			return res.getInt(1);
		} catch (SQLException e) {System.out.println("SoldLast30Days exception");}
		return -1;

	}

	public double SaleThrough(String StoreName)
	{
		try{
		return ((double)SoldLast30Days()/OnlineItemsAmount(StoreName))*100;
		}catch(Exception e){System.out.println("SaleThrough Exception");}
		return -1;
	}

	public void UpdateItemSpecific(String EbayId) throws SQLException, InterruptedException
	{
Thread.sleep(100);
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET ItemSpecific = "+1+" WHERE EbayId = '"+EbayId+"';";
		statement.executeUpdate(WriteToData);
		WriteToData =null;
		System.gc();
		}catch(SQLException e){e.printStackTrace();}		
	
	}

	public Object[][] GetTable(String CompoboxChoice, String ValueTosearch) throws ParseException
	{
		
		switch(CompoboxChoice)
		{
		
		case "All":
			System.out.println("All");
			//getting all the sales //
			return GetAllSalesRecords();
			

		case "Buyer username":
			System.out.println("Buyer username"); //"Order status", "Sale date", "Amazon order id", "eBay id ", "Asin"
			return GetRecordsByUsername(ValueTosearch);
			
		case "Order status":
			System.out.println("Order status");
			return GetRecordsByOrderStatus(ValueTosearch);

		case "Amazon order id":
			System.out.println("Amazon order id");
			return GetRecordsByAmazonOrderId(ValueTosearch);
			
		case "eBay id":
			System.out.println("eBay id");
			return GetRecordsByEbayId(ValueTosearch);
			
		case "Asin":
			System.out.println("Asin");
			return GetRecordsByAsin(ValueTosearch);
			default:
			System.out.println("No compobox choice");
			break;
			
		}
		return null;
	}

	public Object[][] GetAllSalesRecords() throws ParseException
	{

		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders;");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	}
	
	public Object[][] GetRecordsByUsername(String BuyerUserName)
	{


		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where Buyer_User_ID = '"+BuyerUserName+"';");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	
		
	}
	
	public Object[][] GetRecordsByOrderStatus(String OrderStatus)
	{


		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where OrderStatus = '"+OrderStatus+"';");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	
		
	}

	public Object[][]  GetRecordsByAmazonOrderId(String AmazonOrderId)
	{



		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where Amazon_OrderNumber = '"+AmazonOrderId+"';");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	
		
	
	}
	
	public Object[][]  GetRecordsByEbayId(String EbayId)
	{



		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where EbayId = '"+EbayId+"';");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	
		
	
	}
	
	public Object[][]  GetRecordsByAsin (String Asin)
	{



		try {
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders where Amazonasin = '"+Asin+"';");
			
			res.last(); 
			res.getRow();
			System.out.println("Table size = "+res.getRow());
			Object[][] records = new Object[res.getRow()][17];
			//res.first();
			int row = 0;
			do
			{ 
				records[row][0] = res.getString("OrderId");
				records[row][1] = res.getString("Buyer_User_ID");
				records[row][2] = res.getString("OrderStatus");
				records[row][3] = res.getString("Sale_date");
				records[row][4] = String.valueOf(res.getDouble("Amazon_price_before_tax"));
				records[row][5] = String.valueOf(res.getDouble("Fees"));
				records[row][6] = String.valueOf(res.getDouble("Profit"));
				records[row][7] = String.valueOf(res.getDouble("Total_price"));
				records[row][8] = res.getString("Amazon_OrderNumber");
				records[row][9] = res.getString("Tracking");
				records[row][10] = res.getString("Carrier");
				records[row][11] = String.valueOf(res.getInt("Feedback_left")); 
				records[row][12] = res.getString("CheckoutStatus");
				records[row][13] = res.getString("EbayId");
				records[row][14] = res.getString("AmazonAsin");
				row++;
				System.out.println("row = "+row);
			}while(res.previous());
			con = null;
			statement = null;
			System.gc();
			return records;
		} catch (SQLException e) {System.out.println("Monthly Orders Error");}
		return null;
	
		
	
	}
	
}
