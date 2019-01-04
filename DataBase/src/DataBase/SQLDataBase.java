package DataBase;

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
import java.util.Date;

import Config.Config;
import Item.Item;

public class SQLDataBase implements IDataBase{

	private  Connection con ;
	private  java.sql.Statement statement;
	private  String Scham;
	private  String Connection;
	private  String User;
	private  String Pass;
	private  String Port;
	private  ResultSet res;
	java.util.Date date ;
	DateFormat dateFormat;
	
	/* Contractor */
	
	public SQLDataBase() {
		try{
		ReadDataBaseConfigurations(Config.KeysFilePath);
		}catch(Exception e)
		{
			System.out.println("Exception in SQLDataBase: "+e.getMessage() );
		};
		
		OpenConnection();
		date = new Date();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public SQLDataBase(String KeysFilePath) {
		try{
			ReadDataBaseConfigurations(KeysFilePath);
		}catch(Exception e)
		{
			System.out.println("Exception in SQLDataBase: "+e.getMessage() );
		};
		OpenConnection();
	}
	
	private void ReadDataBaseConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(KeysFilePath);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("Connection: "))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+"Connection: ".length());
				}
				
				if (sCurrentLine.contains("Schame: "))
				{
					Scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame: ")+"Schame: ".length());
				}
				
				if (sCurrentLine.contains("DataBaseUser: "))
				{
					User = sCurrentLine.substring(sCurrentLine.indexOf("DataBaseUser: ")+"DataBaseUser: ".length());
				}
				
				if (sCurrentLine.contains("DataBasePass: "))
				{
					Pass = sCurrentLine.substring(sCurrentLine.indexOf("DataBasePass: ")+"DataBasePass: ".length());
				}
				
				if (sCurrentLine.contains("Port: "))
				{
					Port = sCurrentLine.substring(sCurrentLine.indexOf("Port: ")+"Port: ".length());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			
			try {
				
				if (br != null)
					br.close();
				
				if (fr != null)
					fr.close();
				
			} catch (IOException ex) {
				
				ex.printStackTrace();
				throw ex;
			}
		}

		Connection+=Port;
		Connection+="/";
	
	}
	
	/* Public  functions */
	
	// Tracking numbers 
	
	public void UpDateTrackingNumber(ArrayList<Item> ListOfItems)
	
	{
		for(Item ele: ListOfItems)
		{
			if (null != ele.getTracking() && null != ele.getCarrier())
			{
				Write("UPDATE "+Scham+".orders SET OrderStatus='Complete',Tracking='"+ele.getTracking()+"',Carrier='"+ele.getCarrier()+"' WHERE Amazon_OrderNumber='"+ele.getOrderNumber()+"';");				
			}
		}
	}
	
	public void GetItemsForUpdateTrackingNumber(ArrayList<Item> ListOfItems)
	{
		ResultSet res = Read("select * from " + Scham
				+ ".orders WHERE OrderStatus = 'Orderd' or OrderStatus = 'TrackingUpdateFail';");
		try
		{
			while (res.next())
			{
				Item newItem = new Item();
				newItem.setOrderNumber(res.getString("Amazon_OrderNumber"));
				newItem.setOrderId(res.getString("OrderId"));
				newItem.setBuyerUserID(res.getString("Buyer_User_ID"));
				ListOfItems.add(newItem);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	
	// Online items 
	
	public void GetItemForImagesGrab(ArrayList<Item> ListOfItems)
	{
		ResultSet res = Read("SELECT * FROM amazon.productfromsellers where BreakEvenForLowest >0 and  Uploaded = 0;");
		try
		{
			while (res.next())
			{
				Item newItem = new Item();
				newItem.setSupplierCode(res.getString("ASIN").trim());
				ListOfItems.add(newItem);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	
	}

	
	
	public void GetOnlineItems(ArrayList<Item> ListOfItems)
	{
		ResultSet res = Read("SELECT * FROM amazon.online;");
		
		try
		{
			while (res.next())
			{
				Item newItem = new Item();
				newItem.setSupplierCode(res.getString("AmazonAsin").trim());
				newItem.setMarketPlaceCode(res.getString("EbayId").trim());
				ListOfItems.add(newItem);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void InsertItem(Item newItem)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		Write("INSERT INTO "+Scham+".online (AmazonAsin,EbayId,StartDate)"+
			  "VALUES ('"+newItem.getSupplierCode()+"','"+newItem.getMarketPlaceCode()+"','"+dateFormat.format(date)+"');");
	}
	
	public void RemoveItem(Item ItemToRemove)
	{
		Write("DELETE FROM "+Scham+".online WHERE AmazonAsin = '"+ItemToRemove.getSupplierCode()+"';");
	}
	
	public synchronized void SetUniversalCode(Item newItem)
	{
		if (null != newItem.getUniversalCode() && !newItem.getUniversalCode().contains("apply"))
		{
			Write("UPDATE "+Scham+".online SET '"+newItem.getCodeType()+"'='"+newItem.getUniversalCode()+"'  WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
		}
	}

	public synchronized void SetBestResults(Item newItem)
	{
		if (null != newItem.getUniversalCode() && !newItem.getUniversalCode().contains("apply"))
		{
			Write("UPDATE "+Scham+".online SET Bestresults ='"+newItem.getUniversalCode()+"'  WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
		}
	}
	
	public synchronized void SetPlaceInLowestPrice(Item newItem)
	{
		if (-1 != newItem.getPlaceInLowestPrice())
		{
			Write("UPDATE "+Scham+".online SET PlaceInLowestPrice='"+newItem.getPlaceInLowestPrice()+"' WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
		}
	}
	
	public synchronized void SetMarketPlaceResults(Item newItem)
	{
		if (-1 != newItem.getMarketPlaceResults())
		{
			Write("UPDATE "+Scham+".online SET ebayresults='"+newItem.getMarketPlaceResults()+"' WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
		}
	}
	
	public void UpdatePrice(Item newItem)
	{
		Write("UPDATE "+Scham+".online SET CurrentAmazonPriceWithScraper='"+newItem.getCurrentSupplierPrice()+"' WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");
	}
	
	public void UpdateTax(Item newItem)
	{
		Write("UPDATE "+Scham+".online SET Tax='"+newItem.getCurrentTax()+"' WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");
	}
	
	public void UpdateStock(Item newItem)
	{
		if (newItem.isInStock() ==true)
		{
			Write("UPDATE "+Scham+".online SET InStock=1 WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");
		}else
		{
			Write("UPDATE "+Scham+".online SET InStock=0 WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");
		}
	}
	
	
	public void UpdateProfitPercent(Item newItem)
	{
		if (newItem.getPriceStatus() != Item.PriceChangeStatus.NoChange)
		{
			Write("UPDATE "+Scham+".online SET ProfitPercent="+newItem.getMyProfitPercent()+" WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
		}
	}
	
	public void UpdateLastTimeUpdated(Item newItem)
	{
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		Write("UPDATE "+Scham+".online SET LastTimePriceUpdate = '"+currentTime+"' WHERE EbayId='"+newItem.getMarketPlaceCode()+"';");			
	}

	
	
	
	/* Private  functions */
	
	/* Database operations */
	
	public void OpenConnection()  
	{
		try {
			con = DriverManager.getConnection(Connection,User,Pass);
			statement = con.createStatement();//
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CloseConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Write(String statment)
	{
		try {
			statement.executeUpdate(statment);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}
	
	public ResultSet Read(String statment)
	{
		try {
			res = statement.executeQuery(statment);
		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}
		return res;
	}
	
	@Override
	public int GetResultsAmount(String statment) {
		int ResultsAmount = 0;
		
		try {
			res = Read(statment);
			res.beforeFirst();
			res.last();  
			ResultsAmount = res.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
		return ResultsAmount;
	}
	
	@Override
	public Boolean IsExcist(String statment) 
	{
		int size = 0;
		Boolean isExcist = false;
		size = GetResultsAmount(statment);
		if (size > 0)
		{
			isExcist = true;
		}
		
		return isExcist;
	}

	
	/* Getters and Setters */
	
	public ResultSet getRes() {
		return res;
	}


	
	/* Destractor override */
	
	@Override
	protected void finalize() throws Throwable {
		CloseConnection();
	}


}
