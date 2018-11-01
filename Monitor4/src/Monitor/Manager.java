package Monitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Config.Config;
import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;
import Item.Item.PriceChangeStatus;
import Item.Item.QuantitiyChangeStatus;
import MarketPlace.MarketPlaceInterface;
import MarketPlace.eBayMarketPlace;
import Supplier.AmazonSupplier;


public class Manager {

	private static int size = 100;
	private static int threadAmount = 1;
	private static int OutOfStockLimit = 2;
	MarketPlaceInterface ebay; 
	
	
	
	
 	public Manager() {
 		try {
			ReadFileConfigurations(Config.KeysFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		InitDatabase();
 		ebay = new eBayMarketPlace();
	}
 	
	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try {
			
			if(null == KeysFilePath)
			{
				fr = new FileReader(Config.KeysFilePath);
			}else
			{
				fr = new FileReader(KeysFilePath);
			}
			
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{

				if (sCurrentLine.contains("SupplierScanThreadAmount: "))
				{
					String s = sCurrentLine.substring(sCurrentLine.indexOf("SupplierScanThreadAmount: ")+"SupplierScanThreadAmount: ".length());
					threadAmount = Integer.parseInt(s);
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
	}

	/* Sync universal codes */
	protected void InitDatabase()
	{
		/* Variables */
		IDataBase 	  SQLDb	    = new SQLDataBase();
		ResultSet res; 
		String EANCode;
		String UPCCode;
		String ISBNCode;
		String supplierCode;
		
		
		/* Body of function */
		res = SQLDb.Read("SELECT * FROM amazon.online;");
		try {
			
			while(res.next())
			{
				EANCode 	  = res.getString("EAN");
				UPCCode 	  = res.getString("UPC");
				ISBNCode 	  = res.getString("ISBN");
				supplierCode  = res.getString("AmazonAsin");
				if (null == EANCode &&
					null == UPCCode &&	
					null == ISBNCode )
				{
					UpdateUniversalCode(supplierCode);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/* Release resources */
		SQLDb = null;
		System.gc();
	}

	
	
	
	/* Public functions */
	
	public void AmazonToeBay()
	{
		ArrayList<Item> ListOfItems = new ArrayList<Item>();
		List<List<Item>> alist = new ArrayList<List<Item>>();

		
		IDataBase 	  SQLDb	    = new SQLDataBase();
		ResultSet res = SQLDb.Read("SELECT * FROM amazon.online;");
		
		ArrangeResultSet(res,ListOfItems);
		DevideArrayList(ListOfItems,alist);
		
		//Thread for marketplace scan

		Thread t2 = null;
		t2 = new Thread(new Runnable() {
	         @Override
	         public void run() {
	        	 WrapFunctionForMarketPlaceInterface(ListOfItems);
	         }
		});
		t2.start();
		
		

		//Thread branch for supplier scan 
		Iterator<List<Item>> it = alist.listIterator();
		Thread t = null;
		for(int i = 0; i < threadAmount ;i++)
		{
			 t = new Thread(new Runnable() {
		         @Override
		         public void run() {
		        	 new AmazonSupplier().GetItemsUpdate(it.next());
		         }
			});
			t.start();
		}
		
		try {
			t.join();
			t2.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		
		StockChangingDecision(ListOfItems);
		PriceChangingDecision(ListOfItems);
		UpdateChanges(ListOfItems);
		
		//printList(ListOfItems);
		SQLDb = null;
		System.gc();
	}
	
	public void AmazonToeBay(ArrayList<Item> ListOfItems)
	{
		List<List<Item>> alist = new ArrayList<List<Item>>();
		DevideArrayList(ListOfItems,alist);
		
		//Thread for marketplace scan

		Thread t2 = null;
		t2 = new Thread(new Runnable() {
	         @Override
	         public void run() {
	        	 WrapFunctionForMarketPlaceInterface(ListOfItems);
	         }
		});
		t2.start();
		
		

		//Thread branch for supplier scan 
		Iterator<List<Item>> it = alist.listIterator();
		Thread t = null;
		if (alist.size() >= threadAmount)
		{
			for(int i = 0; i < threadAmount ;i++)
			{
				 t = new Thread(new Runnable() {
			         @Override
			         public void run() {
			        	 new AmazonSupplier().GetItemsUpdate(it.next());
			         }
				});
				t.start();
			}
		}else
		{
			 t = new Thread(new Runnable() {
		         @Override
		         public void run() {
		        	 new AmazonSupplier().GetItemsUpdate(it.next());
		         }
			});
			t.start();
		}
		
		try {
			t.join();
			t2.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		
		StockChangingDecision(ListOfItems);
		PriceChangingDecision(ListOfItems);
		UpdateChanges(ListOfItems);

		System.gc();
	}
	
	
	public void printList(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			System.out.println(ele.toString());
		}
	}
	

	
	/* Inner functions */
	
	
	protected  void PriceChangingDecision(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			ele.CalculateMinPriceToSale();
			ele.CalculateCurrentProfitPersent();
			
			if (ele.getPlaceInLowestPrice() == 1 && 
				ele.getMarketPlaceResults() > 1  &&	
				ele.getCurrentMarketPlacePrice()+0.01 < ele.getMarketPlaceSecondLowestPrice())
			{
				ele.CalculateNewProfitPersentFromSpecificPrice(ele.getMarketPlaceSecondLowestPrice());
				ele.CalculateNewMarketPlacePrice(ele.getMarketPlaceSecondLowestPrice());
				ele.setPriceStatus(PriceChangeStatus.PriceIncrease);

			}else if (ele.getPlaceInLowestPrice() > 1 && 
					ele.getMinPriceToSale() < ele.getMarketPlaceLowestPrice())
			{
				ele.CalculateNewProfitPersentFromSpecificPrice(ele.getMarketPlaceLowestPrice());
				ele.CalculateNewMarketPlacePrice(ele.getMarketPlaceLowestPrice());
				ele.setPriceStatus(PriceChangeStatus.PriceReduce);

			}else if (ele.getMarketPlaceResults() == 1)
			{
				ele.setMyProfitPercent(1.1);
				ele.CalculateNewMarketPlacePrice(ele.getMyProfitPercent());
			}
		}
	}
	
	protected  void StockChangingDecision(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			if (ele.getQuantity() >= OutOfStockLimit)
			{
				ele.setQuantitiyStatus(QuantitiyChangeStatus.Instock);
			}else
			{
				ele.setQuantitiyStatus(QuantitiyChangeStatus.OutOfStock);
			}
		}
	}
	
	protected  void UpdateChanges(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			if (ele.getPriceStatus()     != PriceChangeStatus.NoChange ||
				ele.getQuantitiyStatus() != QuantitiyChangeStatus.Instock)
			{
				System.out.println(ele.toString());
			}
		}
	}
	
	private void DevideArrayList(ArrayList<Item> ListOfItemsToDivide, List<List<Item>> alist)
	{
		int i=0;
		
		if (ListOfItemsToDivide.size()<threadAmount)
		{
			alist.add(ListOfItemsToDivide.subList(i, ListOfItemsToDivide.size()));

		}else
		{
			size = ListOfItemsToDivide.size()/threadAmount;
			while(i + size < ListOfItemsToDivide.size())
			{
				alist.add(ListOfItemsToDivide.subList(i, i + size));
				i+=size;
			}
			alist.add(ListOfItemsToDivide.subList(i, ListOfItemsToDivide.size()));
		}
		
		System.out.println("Divided into: "+alist.size());
	}
	
	private void ArrangeResultSet(ResultSet res,ArrayList<Item> ListOfItems)
	{
		try {
			while(res.next())
			{
				Item tempItem = new Item();
				tempItem.setSupplierCode(res.getString("AmazonAsin"));
				tempItem.setMarketPlaceCode(res.getString("EbayId"));	
				tempItem.setUniversalCode(res.getString("Bestresults"));	
				ListOfItems.add(tempItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void UpdateUniversalCode(String supplierCode)
	{
		IDataBase 	  SQLDb	    = new SQLDataBase();
		ResultSet 	  res; 
		String 		  codeType;
		String 		  code;
		
		try {
			res = SQLDb.Read("SELECT * FROM amazon.productfromsellers where ASIN = '"+supplierCode+"';");
			res.last();
	
			if (res.getRow()>0)
			{
				codeType = res.getString("CodeType");
				code 	 = res.getString("Code");
				switch(codeType)
				{
					case("UPC"):
						SQLDb.Write("UPDATE amazon.online SET UPC='"+code+"' where AmazonAsin = '"+supplierCode+"';");
						break;
						
					case("EAN"):
						SQLDb.Write("UPDATE amazon.online SET EAN='"+code+"' where AmazonAsin = '"+supplierCode+"';");
						break;
						
					case("ISBN"):
						SQLDb.Write("UPDATE amazon.online SET ISBN='"+code+"' where AmazonAsin = '"+supplierCode+"';");
						break;
						
					default:
						break;
				}
			}else
			{
				// TODO put title to search
			}
			
		} catch (SQLException e) {
			System.out.println("Exception in UpdateUniversalCode: "+e.getMessage());
		}
		
		/* Release resources */
		SQLDb = null;
		System.gc();
	}

	private void WrapFunctionForMarketPlaceInterface(ArrayList<Item> ListOfItems)
	{

		for(Item ele:ListOfItems)
		{
			ebay.PlaceInSearchLowestFirst(ele);
		}
	}


}
