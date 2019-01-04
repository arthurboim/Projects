package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetSetDB {

	
	public static Connection con = null;
	
	public void GetSetDB(Connection con)
	{
		this.con = con;
	}
	
	public void GetNotOptimizedItems(ArrayList<Item> ItemsList)
	{

		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.online where Optimized = 0 and startdate = '2017-07-05' ;");
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

	public void GetNotOptimizedItemsGui(ArrayList<Item> ItemsList,String quary)
	{

		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery(quary);
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

	public void GetAllOnline(ArrayList<Item> ItemsList)
	{
		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.online;");
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


		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.online SET Watchers = "+watchers+" where EbayId = '"+Ebayid+"';";		  
			statement_update.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
			
	}
	
	public void GetAllOnlineItems(ArrayList<Item> ItemsList)
	{

		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.online where ItemSpecific = 0;");
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

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			

			String WriteToData;
			WriteToData = "UPDATE  amazon.online SET Optimized = 1 where EbayId = '"+item.EbayId+"';";		  
			statement_update.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
			}

	public void GetCodes(ArrayList<Item> List)
	{
		try {
			 con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.items where Date_scan>'2017-10-05' and breakevenlowestprice>1.13 and breakevenlowestprice<1.40 and deepsearch =0;"); //and sold_on_ebay>1
			int counter =0;
			while (res.next())
			{
				Item item = new Item();
				//item.EbayId = res.getString("EbayId");
				item.BestResults = res.getString("bestresult");
				if (counter<3) 
					{
					counter++;
					SetChecking(item);
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
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.online;");
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
	
	
	public void SetChecking(Item Item) throws SQLException
	{

		 con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();

		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.items SET Deepsearch = "+10+" where bestresult = '"+Item.BestResults+"';";		  
			statement_update.executeUpdate(WriteToData);
			}
			catch(SQLException e){e.printStackTrace();}	
	}
	
	public void GetAllItemsFromSellersTable(ArrayList<Item> list)
	{

		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.productfromsellers;");
			while (res.next())
			{
				Item item = new Item();
				item.Asin = res.getString("ASIN");
				item.id = res.getInt("id");
				list.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void removeid(int id,Connection con) throws SQLException
	{
		//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();

		try{
			String WriteToData;
			WriteToData = "DELETE FROM amazon.productfromsellers where id = "+id+";";		  
			statement_update.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
	}
	
	public void SetResults(Item item,Connection con) throws SQLException
	{

		 con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			

			String WriteToData;
			WriteToData = "UPDATE  amazon.items SET ebayResults = "+item.EbayResults+",Deepsearch = "+1+", Sale_true  = "+item.sale_ture+" ,sold_on_ebay  = "+item.Sold+", SoldLastWeek = "+item.TransactionsLastWeek+"  where bestresult = '"+item.BestResults+"';";		  
			statement_update.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
	}

	public void SetItemUpdatedWIthItemspesific(Item item) throws SQLException
	{

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			

			String WriteToData;
			WriteToData = "UPDATE  amazon.online SET ItemSpecific = 1 where EbayId = '"+item.EbayId+"';";		  
			statement_update.executeUpdate(WriteToData);
			
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
			}

	public void SetItemsOnlineHistory(Item item) throws SQLException
	{

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.online SET TransactionsLastMonth = "+item.TransactionsLastMonth+" , LastDateSold = '"+item.LastSaleDate+"' where EbayId = '"+item.EbayId+"';";		  
			statement_update.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		
	}


	public void GetAndSetTax(Item item)
	{
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.orders where EbayId = '"+item.EbayId+"';");
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
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement_update = con.createStatement();
			String WriteToData;
			WriteToData = "UPDATE amazon.online SET Tax="+item.Tax+" WHERE EbayId = '"+item.EbayId+"';";
			statement_update.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}			
		}
	
	}

	


}
