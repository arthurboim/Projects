package eBayOptimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetSetDB {

	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;

	public GetSetDB() 
	{
		System.out.println("GetSetDB constractor");
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

	public void GetNotOptimizedItems(ArrayList<Item> ItemsList)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where Optimized = 0 and startdate = '2017-07-05' ;");
			while (res.next())
			{
				Item item = new Item();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
		} catch (SQLException e) {e.printStackTrace();}
	}

	public void GetNotOptimizedItemsGui(ArrayList<Item> ItemsList,String quary)
	{
	try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery(quary);
			while (res.next())
			{
				Item item = new Item();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
	} catch (SQLException e) {e.printStackTrace();}
	}

	public void GetAllOnline(ArrayList<Item> ItemsList)
	{
		try {
			
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online;");
			while (res.next())
			{
				Item item = new Item();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void GetAllOnlineItems(ArrayList<Item> ItemsList)
	{

		try {
			
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where ItemSpecific = 0;");
			while (res.next())
			{
				Item item = new Item();
				item.EbayId = res.getString("EbayId");
				ItemsList.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void SetOptimizedItems(Item item) throws SQLException
	{

		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		try{
			

			String WriteToData;
			WriteToData = "UPDATE  "+scham+".online SET Optimized = 1 where EbayId = '"+item.EbayId+"';";		  
			statement.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
			}

	public void GetCodes(ArrayList<Item> List,Connection con)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".items where date_scan >= '2017-09-24' and Amazon_price>7  and Deepsearch = 0 and placeinlowestprice<12;"); //and sold_on_ebay>1
			int counter =0;
			while (res.next())
			{
				Item item = new Item();
				//item.EbayId = res.getString("EbayId");
				item.BestResults = res.getString("bestresult");
				if (counter<50) 
					{
					counter++;
					//SetChecking(item);
					List.add(item);
					}
				else break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void GetAllAsins(ArrayList<Item> ItemsList)
	{

		try {
			
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online;");
			while (res.next())
			{
				Item item = new Item();
				item.Asin = res.getString("AmazonAsin");
				ItemsList.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		
	}
	
	public void SetChecking(ArrayList<Item> ItemList,Connection con) throws SQLException
	{

		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		for(Item ele: ItemList)
		{
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".items SET Deepsearch = "+10+" where bestresult = '"+ele.BestResults+"';";		  
			statement.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		}
	}
	
	public void GetAllItemsFromSellersTable(ArrayList<Item> list)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".productfromsellers;");
			while (res.next())
			{
				Item item = new Item();
				item.Asin = res.getString("ASIN");
				item.id = res.getInt("id");
				list.add(item);
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public void removeid(int id) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "DELETE FROM "+scham+".productfromsellers where id = "+id+";";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e){e.printStackTrace();}	
	}
	
	public void SetResults(Item item,Connection con) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		 try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".items SET ebayResults = "+item.EbayResults+",Deepsearch = "+1+", Sale_true  = "+item.sale_ture+" ,sold_on_ebay  = "+item.Sold+", SoldLastWeek = "+item.TransactionsLastWeek+" where bestresult = '"+item.BestResults+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e){e.printStackTrace();}			
	}

	public void SetItemUpdatedWIthItemspesific(Item item) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".online SET ItemSpecific = 1 where EbayId = '"+item.EbayId+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e){e.printStackTrace();}			
	}

	public void SetItemsOnlineHistory(Item item) throws SQLException
	{

		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".online SET TransactionsLastMonth = "+item.TransactionsLastMonth+" , LastDateSold = '"+item.LastSaleDate+"' where EbayId = '"+item.EbayId+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		
	}

	public void GetAndSetTax(Item item)
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

	public void SetCategory(ArrayList<Item> ItemList) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		for(Item ele: ItemList)
		{
		 try{
			String WriteToData;
			WriteToData = "UPDATE "+scham+".online SET Category='"+ele.Category+"' WHERE `AmazonAsin`='"+ele.Asin+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e){e.printStackTrace();}	
		}
	}

}
