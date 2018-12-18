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

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;

import Config.Config;
import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Ebay.eBayAPI;
import Item.Item;
import Item.Item.PriceChangeStatus;
import Item.Item.QuantitiyChangeStatus;
import MarketPlace.MarketPlaceInterface;
import MarketPlace.eBayMarketPlace;
import Supplier.AmazonSupplier;


public class MonitorManager {

	private static int size = 100;
	private static int SuppliyerthreadAmount = 1;
	private static int MarketPlaceScanThreadAmount =1;
	private static int OutOfStockLimit = 2;
	
	eBayAPI 			 ebay; 
	SQLDataBase 	  	 SQLDb;
	ArrayList<Item> 	 ListOfItems;
	ArrayList<Item> 	 ListOfItemsForSuppliyer;
	List<List<Item>> 	 alistSuppliyer;
	ArrayList<Item> 	 ListOfItemsForMarketPlace;
	List<List<Item>> 	 alistMarketPlace;
	
 	public MonitorManager() {
 		
 		try {
 			// Read configurations
			ReadFileConfigurations(Config.KeysFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		// List to hold the items from database
 		ListOfItemsForSuppliyer = new ArrayList<Item>();
 		alistSuppliyer = new ArrayList<List<Item>>();
 		ListOfItemsForMarketPlace = new ArrayList<Item>();
 		alistMarketPlace = new ArrayList<List<Item>>();
 		ListOfItems  = new ArrayList<Item>();
 		
		SQLDb  = new SQLDataBase();
 		//UpdateDatabase();
 		
 		ebay = new eBayAPI();
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
					SuppliyerthreadAmount = Integer.parseInt(s);
				}
				
				if (sCurrentLine.contains("MarketPlaceScanThreadAmount: "))
				{
					String s = sCurrentLine.substring(sCurrentLine.indexOf("MarketPlaceScanThreadAmount: ")+"MarketPlaceScanThreadAmount: ".length());
					MarketPlaceScanThreadAmount = Integer.parseInt(s);
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
	protected void UpdateDatabase()
	{
		/* Variables */
		ResultSet res; 
		String EANCode;
		String UPCCode;
		String ISBNCode;
		String supplierCode;
		
		/* Body of function */
		
		try {
			
			res = SQLDb.Read("SELECT * FROM amazon.online;");
			
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
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}

	
	
	/* Public functions */
	public void MonitorScannerRun()
	{
		AmazonToeBayScanner();
		OptimizerChange();
		//NormalChanges();
	}
	

	
	
	
	/* Inner functions */
	
	protected  void PriceChangingDecision()
	{
		for(Item ele:ListOfItemsForSuppliyer)
		{
			ele.CalculateMinPriceToSale();
			ele.CalculateCurrentProfitPersent();
			ele.setPriceStatus(PriceChangeStatus.NoChange);
			
			// Case when i'm lowestprice and want to increase the price untill the second
			if (ele.getPlaceInLowestPrice() == 1 && 
				ele.getMarketPlaceResults() > 1  &&	
				ele.getCurrentMarketPlacePrice()+0.01 < ele.getMarketPlaceSecondLowestPrice())
			{
				ele.CalculateNewProfitPersentFromSpecificPrice(ele.getMarketPlaceSecondLowestPrice());
				ele.CalculateNewMarketPlacePrice(ele.getMarketPlaceSecondLowestPrice());
				ele.setPriceStatus(PriceChangeStatus.PriceIncrease);
			}
			
			else if (ele.getPlaceInLowestPrice() > 1)
			{
				for(Double ele1:ele.getPricesList())
				{
					if (ele.getMinPriceToSale() <= ele1 && 
							ele.getCurrentMarketPlacePrice() != ele1)
					{
						ele.CalculateNewProfitPersentFromSpecificPrice(ele1-0.01);
						ele.CalculateNewMarketPlacePrice(ele1-0.01);
						ele.setPriceStatus(PriceChangeStatus.PriceReduce);
						break;
					}
				}
			}else
			{
				ele.setMyProfitPercent(1.1);
				ele.CalculateNewMarketPlacePrice(ele.getMyProfitPercent());
			}
		}
	}
	
	protected  void StockChangingDecision()
	{
		for(Item ele:ListOfItemsForSuppliyer)
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
	
	protected  void UpdateChanges(Item item)
	{
		ListOfItemsForSuppliyer.add(item);
		UpdateChanges();
	}
	
	protected  void UpdateChanges()
	{
		for(Item ele:ListOfItemsForSuppliyer)
		{
			if (ele.getPriceStatus() != PriceChangeStatus.NoChange)
			{
				//Ebay chang price 
				ebay.ChangePrice(ele);
				// Update DB
				SQLDb.UpdateProfitPercent(ele);
				SQLDb.UpdatePrice(ele);
				SQLDb.UpdateTax(ele);
				SQLDb.UpdateLastTimeUpdated(ele);
			}
		}
	}
	
	private void DevideArrayList(ArrayList<Item> ListOfItemsToDivide, List<List<Item>> alist, int splitMount)
	{
		int i=0;
		
		if (ListOfItemsToDivide.size()<splitMount)
		{
			alist.add(ListOfItemsToDivide.subList(i, ListOfItemsToDivide.size()));

		}else
		{
			size = ListOfItemsToDivide.size()/splitMount;
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
			e.printStackTrace();
		}
	}
	
	private void UpdateUniversalCode(String supplierCode)
	{
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
		
	}

	private void MultiThreadScanner()
	{
		//Thread for marketplace scan
		
		Iterator<List<Item>> marketplaceIt = alistMarketPlace.listIterator();
		Thread marketplaceScanThread = null;
		for(int i = 0; i < MarketPlaceScanThreadAmount ;i++)
		{
			marketplaceScanThread = new Thread(new Runnable() {
		         @Override
		         public void run() {
		        	 new eBayMarketPlace().GetItemsUpdate(marketplaceIt.next());
		         }
			});
			marketplaceScanThread.start();
		}
		
		//Thread branch for supplier scan 
		Iterator<List<Item>> supplierIt = alistSuppliyer.listIterator();
		Thread supplierScanThread = null;
		for(int i = 0; i < SuppliyerthreadAmount ;i++)
		{
			supplierScanThread = new Thread(new Runnable() {
		         @Override
		         public void run() {
		        	 new AmazonSupplier().GetItemsUpdate(supplierIt.next());
		         }
			});
			supplierScanThread.start();
		}
		
		
		// Join the threads (supplier and marketplace)
		try {
			marketplaceScanThread.join();
			supplierScanThread.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//MergeArrayList(alistMarketPlace);
	}

	private void OptimizerChange()
	{
		// Make change decision
		PriceChangingDecision();
		
		// Stock decision
		//StockChangingDecision();
		
		// Execute changes
		UpdateChanges();
	}
	
	private void NormalChanges()
	{
		// Make change decision
//		StockChangingDecision(ListOfItems);
//		PriceChangingDecision(ListOfItems);
//		
//		 Execute changes
//		UpdateChanges(ListOfItems);
	}
	
	void printList(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			System.out.println(ele.toString());
		}
	}
	
	void AmazonToeBayScanner()
	{
		// Read data from database
		ResultSet res = SQLDb.Read("SELECT * FROM amazon.online;");
		
		// Arrange results
		ArrangeResultSet(res,ListOfItemsForSuppliyer);
		DevideArrayList(ListOfItemsForSuppliyer,alistSuppliyer,SuppliyerthreadAmount);
		DevideArrayList(ListOfItemsForSuppliyer,alistMarketPlace,MarketPlaceScanThreadAmount);

		// Execute scan marketplace and supplier
		MultiThreadScanner();

//		printList(ListOfItems);
	}



}
