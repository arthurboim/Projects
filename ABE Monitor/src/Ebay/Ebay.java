package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import Abstracts.MarketPlace;
import Database.Item;

public class Ebay extends MarketPlace
{
	public  static Connection con = null;
	public  static String  Connection= null;
	public  static String  User= null;
	public  static String  Pass= null;
	public  static java.sql.Statement statement= null;
	public  ResultSet res2 = null;
	public  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public  static String scham = null;
	
	public Ebay() throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		fr = new FileReader(FILENAME);
		br = new BufferedReader(fr);
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) 
		{

			if (sCurrentLine.contains("User:")) User = "root";
			if (sCurrentLine.contains("Pass:")) Pass = "root";
			if (sCurrentLine.contains("Connection:")) Connection = "jdbc:mysql://localhost:4444/";
			
			if (sCurrentLine.contains("Schame:"))
			{
				scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+"Schame:".length()+1);
				Connection =Connection+scham;
	
			}
		}
		br.close();
	
		
	}

	@Override
	public boolean RemoveItem(Item item) {
		
		return false;
	}

	@Override
	public boolean UpdatePrice(Item item) {
		
		return false;
	}

	@Override
	public boolean UpdateItemQuantity(Item item, int Quantity) {
		
		return false;
	}

	@Override
	public boolean IsVeroBrand(Item item) 
	{
		try {
		ResultSet res;
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		item.Brand = item.Brand.trim();
		if (item.Brand.contains("'"))
		{
			item.Brand = item.Brand.replace("'", "\\'");
		}
		res = statement.executeQuery("SELECT count(*) FROM amazon.vero where Brand = '"+item.Brand.toLowerCase()+"';");
		res.last();
		if(res.getString(1).equals("1"))
		{
			System.out.println("Asin = "+item.SupplierCode+" "+item.Brand+" ? True");
			item.ReadyToUpload = false;
			return true;
		}
		
		if (item.Brand.contains("*"))
		{
			item.Brand = item.Brand.replace("*", "'");
		}
		res = statement.executeQuery("SELECT count(*) FROM amazon.vero where Brand = '"+item.Brand.toLowerCase()+"';");
		res.last();
		if(res.getString(1).equals("1"))
		{
			System.out.println("Asin = "+item.SupplierCode+" "+item.Brand+" ? True");
			item.ReadyToUpload = false;
			return true;
		}

		} catch (SQLException e) {e.printStackTrace();}
		System.out.println("Asin = "+item.SupplierCode+" "+item.Brand+" ? False");
		item.ReadyToUpload = true;
		return false;
	}

	@Override
	public boolean ForbiddenWordsCheck(Item item) 
	{
		try {
		ResultSet res;
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		System.out.println(item.Title);
		String[] arr = item.Title.split("[ |\\-|\\.|\\?|\\,|\\)|\\(|\\{|\\}|\\+|\\']");
		
		for(String ele:arr)
		{
			if (ele.contains("'"))
			{
				ele = ele.replace("'", "\\'");
			}
			System.out.println(ele);
		}
		
		
		for(int i=0;i<arr.length;i++)
		{
			res = statement.executeQuery("SELECT count(*) FROM amazon.forbiddenwords where Word = '"+arr[i].toLowerCase()+"';");
			res.last();
			if(res.getString(1).equals("1"))
			{
				System.out.println("Asin = "+item.SupplierCode+" Forbbiden word in title -> "+arr[i].toLowerCase());
				item.ReadyToUpload = false;
				/*return true;*/
			}
		}

		}catch(Exception e)
		{
			System.out.println("Forbbiden words error");
			System.out.println(e);
		}

		return false;
	}

	public void LinksCheck(Item item)
	{

		for(String ele:item.Features)
		{
			if(ele.toLowerCase().contains("http") || ele.toLowerCase().contains(".com") ||ele.toLowerCase().contains("www.")
					|| ele.toLowerCase().contains("www.amazon.com"))
			{
				System.out.println("------Link contenet found ------");
				System.out.println(ele);
				item.ReadyToUpload = false;
			}
		}
		
		if(item.Content.toLowerCase().contains("http") || item.Content.toLowerCase().contains(".com") ||item.Content.toLowerCase().contains("www.")
				|| item.Content.toLowerCase().contains("www.amazon.com"))
		{
			System.out.println("------Link contenet found ------");
			System.out.println(item.Content);
			item.ReadyToUpload = false;
		}
		
	}
	
	@Override
	public boolean ListItem(Item item) {
		
		return false;
	}

	public boolean CheckBrandIsVero(String Brand,String ForrbidenWord)
	{
		try{
		if (Brand.toLowerCase().equals(ForrbidenWord.toLowerCase())) return true;
		else return false;
		}catch(Exception e){return true;}
	}
	
	
	public void BrandCheckInContent(Item item) 
	{	   
		if (item.Content == null)
		{
			return;
		}
		try {
		ResultSet res;
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		
		
		item.Content = item.Content.replace("'", "\\'");
		String[] arr = item.Content.split(" ");
		
		
		for(int i=0;i<arr.length;i++)
		{
			res = statement.executeQuery("SELECT count(*) FROM amazon.forbiddenwords where Word = '"+arr[i].toLowerCase()+"';");
			res.last();
			if(res.getString(1).equals("1"))
			{
				System.out.println("Asin = "+item.SupplierCode+" Forbbiden word in content -> "+arr[i].toLowerCase());
				item.ReadyToUpload = false;
				return ;
			}
		}
		}catch(Exception e)
		{
			System.out.println("Forbbiden words error");
			System.out.println(e);
		}
		return ;
	}

	
	

	public void BrandCheckInFeatures(Item item) 
	{	   
		if (item.Content == null)
		{
			return;
		}
		try {
		ResultSet res;
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		String temp = " ";
		
		for(String ele:item.Features)
		{
			temp+=ele;
			temp+= " ";
		}
		temp = temp.replace("'", "\\'");
		String[] arr = temp.split(" ");
		
		for(int i=0;i<arr.length;i++)
		{
			res = statement.executeQuery("SELECT count(*) FROM amazon.forbiddenwords where Word = '"+arr[i].toLowerCase()+"';");
			res.last();
			if(res.getString(1).equals("1"))
			{
				System.out.println("Asin = "+item.SupplierCode+" Forbbiden word features -> "+arr[i].toLowerCase());
				item.ReadyToUpload = false;
				return ;
			}
		}
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return ;
	}

	
	
	
	public HashMap<String,Integer> DivideStringToWords(String str)
	{
		HashMap<String,Integer> Map = new HashMap<String,Integer>();
		String[] Parts = str.split(" ");
		
		   for (int i = 0; i < Parts.length; i++) 
		   {
			   Parts[i] = Parts[i].trim();
			   Parts[i] = Parts[i].toLowerCase();
			   Parts[i] = Parts[i].replace(".", "");
			   Parts[i] = Parts[i].replace(",", "");
			   Parts[i] = Parts[i].replace("\\?", "");
			   Parts[i] = Parts[i].replace("\\+", "");
			   Parts[i] = Parts[i].replace("!", "");
			   Parts[i] = Parts[i].replace("\\:", "");
			   Parts[i] = Parts[i].replace("'s", "");
			   Parts[i] = Parts[i].replace("'", "");
			   Parts[i] = Parts[i].replace("#", "");
			   Parts[i] = Parts[i].replace("@", "");
			   Parts[i] = Parts[i].replace("$", "");
			   Parts[i] = Parts[i].replace("%", "");
			   Parts[i] = Parts[i].replace("^", "");
			   Parts[i] = Parts[i].replace("&", "");
			   Parts[i] = Parts[i].replace("*", "");
			   Parts[i] = Parts[i].replace("(", "");
			   Parts[i] = Parts[i].replace(")", "");
			   Parts[i] = Parts[i].replace("~", "");
			   Parts[i] = Parts[i].replace("`", "");
			   Parts[i] = Parts[i].replace("{", "");
			   Parts[i] = Parts[i].replace("}", "");
			   Parts[i] = Parts[i].replace("-", "");
			   Parts[i] = Parts[i].replace("=", "");
			   Parts[i] = Parts[i].replace("|", "");
			   Parts[i] = Parts[i].replace(">", "");
			   Parts[i] = Parts[i].replace("<", "");
			   Map.put(Parts[i], 0);
		   }
		/*
		   for (Entry<String, Integer> entry : Map.entrySet()) 
		   { 
			  System.out.println(entry.getKey()+" "+entry.getValue());
		   }
		*/
		
		
		return Map;
	}
	
	public boolean CheckIfMapHasVero(HashMap<String,Integer> Map,String ForrbidenWord)
	{
		   for (Entry<String, Integer> entry : Map.entrySet()) 
		   { 
			  if (ForrbidenWord.toLowerCase().equals(entry.getKey().toLowerCase())) return true;
		   }
		return false;
	}
	

	
	@Override
	public  boolean IsAlreadyExcist(Item item) 
	{
		try {
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM amazon.online;");
		while(res.next())
		{
			if (res.getString("AmazonAsin").equals(item.SupplierCode)) 
				{
				System.out.println("Already excist "+res.getString("AmazonAsin"));
				return true;
				}
		}
		return false;
		} catch (SQLException e) {e.printStackTrace();return false;}
	}
	
	public  void IsAlreadyExcistBulk(ArrayList<Item> ListOfItemsAsins) 
	{
		try {
		Iterator<Item> i = ListOfItemsAsins.iterator();
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM amazon.online;");

		String AsinTocheck = null;
	
			while (i.hasNext()) 
			{
				AsinTocheck= i.next().SupplierCode;
				while(res.next())
				{
					
				if (res.getString("AmazonAsin").equals(AsinTocheck)) 
					{
					i.remove();
					System.out.println("Already excist "+res.getString("AmazonAsin"));
					}
				}
				res.first();
			}
			

		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	
	public boolean IsAvailable(Item item) 
	{
		try {
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM amazon.online;");
		while(res.next())
		{
			if (res.getString("AmazonAsin").equals(item.SupplierCode)) return true;
		}
		return false;
		} catch (SQLException e) {e.printStackTrace();return false;}
	}
}
