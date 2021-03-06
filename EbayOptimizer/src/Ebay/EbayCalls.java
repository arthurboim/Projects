package Ebay;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetApiAccessRulesCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.sdk.call.ReviseFixedPriceItemCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TransactionType;
import Database.Item;
import Main.EbayOptimizerMain;



public class EbayCalls {
	
	public static ClientConfig config = null;
	public void Get_Item_Info(Item item) throws ApiException, SdkException, Exception
	{
		ApiContext apiContext = new ApiContext();
	    ApiCredential cred = apiContext.getApiCredential(); //
	    cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**lLmaWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
	    apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
	    apiContext.setSite(com.ebay.soap.eBLBaseComponents.SiteCodeType.US);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(item.EbayId);
		Call.setIncludeItemSpecifics(true);
		try{
		ItemType Item = Call.getItem();
		
		item.PicturesString = Item.getPictureDetails().getPictureURL();
		
		
		System.out.println("Quantity Sold = "+Item.getSellingStatus().getQuantitySold());
		item.QuantitySold = Item.getSellingStatus().getQuantitySold();
		item.Title = Item.getTitle();
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
			System.out.println(Item.getItemSpecifics().getNameValueList(i).getValue(0));
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("UPC") && !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		item.BestResults = Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		else if (Item.getItemSpecifics().getNameValueList(i).getName().equals("ISBN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		item.BestResults = Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		
		}
		}catch(Exception e)
		{
			System.out.println(e);
			print_error("Get_Item_Info ERROR \n"+e.toString());//Log
			EbayOptimizerMain.ErrorCount++;
			if (EbayOptimizerMain.ErrorCount>2) return;
		}

        
	}

	public void callsLimit()
	{
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**U1I5WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCodeType.US); 

		GetApiAccessRulesCall Call = new GetApiAccessRulesCall(apiContext);
		try {
			Call.getApiAccessRules();
			System.out.println("Daily Hard Limit = "+Call.getReturnedApiAccessRules()[0].getDailyHardLimit());
			System.out.println("Hourly Hard Limit = "+Call.getReturnedApiAccessRules()[0].getDailyHardLimit());
			System.out.println("Now Limit = "+Call.getReturnedApiAccessRules()[0].getDailyUsage());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void Get_All_Items_Info(Item item) throws ApiException, SdkException, Exception
	{
		ApiContext apiContext = new ApiContext();
	    ApiCredential cred = apiContext.getApiCredential();
	    cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**lLmaWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
	    apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
	    apiContext.setSite(com.ebay.soap.eBLBaseComponents.SiteCodeType.US);
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(item.EbayId);
		try{
		ItemType Item = Call.getItem();
		item.QuantitySold = Item.getSellingStatus().getQuantitySold();
		item.Title = Item.getTitle();
		item.PicturesString = Item.getPictureDetails().getPictureURL();
		}catch(Exception e)
		{
			System.out.println(e);
			print_error("Get_All_Items_Info ERROR \n"+e.toString());//Log
			EbayOptimizerMain.ErrorCount++;
			if (EbayOptimizerMain.ErrorCount>2) return;
		}
        
	}

	

	
	
	public void Find_Items_Info(Item item) throws FileNotFoundException
	{
				ArrayList<Item> ItemsList = new ArrayList<Item>();
	        	config = new ClientConfig();
	        	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");

	            FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
	            FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();

	    		ItemFilter filter2 = new ItemFilter();
	    		filter2.setName(ItemFilterType.CONDITION);
	    		filter2.getValue().add("1000");
	    		request.getItemFilter().add(filter2);  

	    		ItemFilter filter = new ItemFilter();
	    		filter.setName(ItemFilterType.LISTING_TYPE);
	    		filter.getValue().add("FixedPrice");
	    		request.getItemFilter().add(filter);  
	    		
	    		ItemFilter filter5 = new ItemFilter();
	    		filter5.setName(ItemFilterType.AVAILABLE_TO);
	    		filter5.getValue().add("US");
	    		request.getItemFilter().add(filter5);  
	    		
	    		ItemFilter filter6 = new ItemFilter();
	    		filter6.setName(ItemFilterType.LOCATED_IN);
	    		filter6.getValue().add("US");
	    		request.getItemFilter().add(filter6);  

	            try
	            {
	            System.out.println("Key word = "+item.BestResults);
	            request.setKeywords(item.BestResults);

	            request.setSortOrder(SortOrderType.BEST_MATCH);

	            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//

	            System.out.println("Finditem =  " + result.getSearchResult().getCount());

	            List<SearchItem> items = result.getSearchResult().getItem();
	            
	            int soldlastweekcounter = 0;
	            int SellersAmountSold = 0;
	            for (SearchItem ele:items)
	            {
	            	Item TempItem = new Item();
	            	TempItem.EbayId = ele.getItemId();
	            	Get_All_Items_Info(TempItem);
	            	if (TempItem.QuantitySold>0) 
	            	{
	            	SellersAmountSold++;
	            	GetItemTransactions(TempItem, 7);
	            	soldlastweekcounter+=TempItem.TransactionsLastWeek;
	            	}
	            	ItemsList.add(TempItem);
	            }
	            Collections.sort(ItemsList,new CustomComparator());
	            int soldcounter = 0;
	            int ResultsCounter = 0;
	            for (Item ele:ItemsList)
	            {
	            	System.out.println();
	            	System.out.println(ele.EbayId);
	            	System.out.println(ele.Title);
	            	System.out.println(ele.QuantitySold);
	            	soldcounter+=ele.QuantitySold;
	            	ResultsCounter++;
	            }
	           // print_to_file(ItemsList,item);
	            item.Sold = soldcounter;
	            item.EbayResults = ResultsCounter;
	            item.TransactionsLastWeek = soldlastweekcounter;
	            if (item.EbayResults>0)
	            item.sale_ture = (double)((double)item.Sold/(double)item.EbayResults)*100;
	            else item.sale_ture = -1;
	            System.out.println("Sold = "+item.Sold);
	            System.out.println("Ebay Results = "+item.EbayResults);
	            System.out.println("Transactions Last Week = "+item.TransactionsLastWeek);
	            System.out.println("Sell through = "+(double)((double)item.Sold/(double)item.EbayResults)*100);
	            Update_Sellers_Amount_Sold(SellersAmountSold,item.BestResults);
	            
	            result = null;
	            System.gc();
	            }
	            catch(Exception ex)
	            {
	    			System.out.println(ex);
	    			print_error("Find_Items_Info ERROR \n"+ex.toString()); //Log
	    			EbayOptimizerMain.ErrorCount++;
	    			if (EbayOptimizerMain.ErrorCount>2) return;
	            }
	            filter2 = null;
	            filter = null;
	            filter5 = null;
	            filter6 = null;
	            request = null;
	            ItemsList = null;
	            System.gc();
	            
	            
	            
	}

	
	
	
	
	
	public ArrayList<Item> Find_Items_Info_Gui(Item item) throws FileNotFoundException
	{
				ArrayList<Item> ItemsList = new ArrayList<Item>();
	        	ClientConfig config = new ClientConfig();
	        	config.setApplicationId("Arthurbo-Getprodu-PRD-c45f6444c-b98e8933");

	            FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
	            FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();

	    		ItemFilter filter2 = new ItemFilter();
	    		filter2.setName(ItemFilterType.CONDITION);
	    		filter2.getValue().add("1000");
	    		request.getItemFilter().add(filter2);  

	    		ItemFilter filter = new ItemFilter();
	    		filter.setName(ItemFilterType.LISTING_TYPE);
	    		filter.getValue().add("FixedPrice");
	    		request.getItemFilter().add(filter);  
	    		
	    		ItemFilter filter5 = new ItemFilter();
	    		filter5.setName(ItemFilterType.AVAILABLE_TO);
	    		filter5.getValue().add("US");
	    		request.getItemFilter().add(filter5);  
	    		
	    		ItemFilter filter6 = new ItemFilter();
	    		filter6.setName(ItemFilterType.LOCATED_IN);
	    		filter6.getValue().add("US");
	    		request.getItemFilter().add(filter6);  

	            try
	            {
	            System.out.println("Key word = "+item.BestResults);
	            request.setKeywords(item.BestResults);

	            request.setSortOrder(SortOrderType.BEST_MATCH);

	            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//

	            System.out.println("Finditem =  " + result.getSearchResult().getCount());

	            List<SearchItem> items = result.getSearchResult().getItem();
	            int counter =0;
	            int soldlastweekcounter = 0;
	            int SellersAmountSold = 0;
	            for (SearchItem ele:items)
	            {
	            	Item TempItem = new Item();
	            	TempItem.EbayId = ele.getItemId();
	            	Get_All_Items_Info(TempItem);
	            	if (TempItem.QuantitySold>0) 
	            	{
	            	SellersAmountSold++;
	            	GetItemTransactions(TempItem, 7);
	            	soldlastweekcounter+=TempItem.TransactionsLastWeek;
	            	if (counter>3) break;
	            	else counter++;
	            	}
	            	ItemsList.add(TempItem);
	            }
	            Collections.sort(ItemsList,new CustomComparator());
	            int soldcounter = 0;
	            int ResultsCounter = 0;
	            for (Item ele:ItemsList)
	            {
	            	System.out.println();
	            	System.out.println(ele.EbayId);
	            	System.out.println(ele.Title);
	            	System.out.println(ele.QuantitySold);
	            	soldcounter+=ele.QuantitySold;
	            	ResultsCounter++;
	            }
	           // print_to_file(ItemsList,item);
	            item.Sold = soldcounter;
	            item.EbayResults = ResultsCounter;
	            item.TransactionsLastWeek = soldlastweekcounter;
	            item.sale_ture = (double)((double)item.Sold/(double)item.EbayResults)*100;
	            System.out.println("Sold = "+item.Sold);
	            System.out.println("Ebay Results = "+item.EbayResults);
	            System.out.println("Transactions Last Week = "+item.TransactionsLastWeek);
	            System.out.println("Sell through = "+(double)((double)item.Sold/(double)item.EbayResults)*100);
	            
	            }
	            catch(Exception ex)
	            {
	    			System.out.println(ex);
	    			print_error("Find_Items_Info ERROR \n"+ex.toString()); //Log
	    			EbayOptimizerMain.ErrorCount++;
	            }
	            return ItemsList;
	}

	
	
	public void Update_Sellers_Amount_Sold(int SellersAmountSold, String bestresults) throws SQLException
	{


		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		java.sql.Statement statement_update = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.items SET SellersAmountSold = "+SellersAmountSold+"  where Bestresult = '"+bestresults+"';";		  
			statement_update.executeUpdate(WriteToData);
			}catch(SQLException e)
			{
			e.printStackTrace();
			}			
	}
	

	public void GetItemTransactions(Item product,int days) throws ApiException, SdkException, Exception
	{	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**U1I5WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCodeType.US); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext);
		call.setItemID(product.EbayId);
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);
	    call.setNumberOfDays(days);
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf);
	    try{
		TransactionType[] transactions = call.getItemTransactions();
		if (transactions==null)
		{
		product.TransactionsLastWeek = 0;
		System.out.println("Transactions amount = "+product.TransactionsLastWeek);
		}
		else
		{
		product.TransactionsLastWeek  = transactions.length;
		System.out.println("Transactions amount = "+product.TransactionsLastWeek);
		for (TransactionType ele:transactions)
		{
			System.out.println(dateFormat.format(ele.getCreatedDate().getTime()));
		}
		}
	    }catch(Exception e)
	    {
			System.out.println(e);
			print_error("GetItemTransactions ERROR \n"+e.toString()); //Log
			EbayOptimizerMain.ErrorCount++;
			//if (EbayOptimizerMain.ErrorCount>2) return;
	    }
		
		
	}
	
	
	public void AddItemSpecifics(String EbayID) throws ApiException, SdkException, Exception
	{	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**U1I5WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCodeType.US); 
	    ReviseFixedPriceItemCall reviseFP = new ReviseFixedPriceItemCall(apiContext);
        ItemType item = new ItemType();

        // We are using SKU to identify items
        item.setItemID(EbayID); 

        // Create 4 item specifics for item which was listed on category 63889          
        NameValueListArrayType itemSpec = new NameValueListArrayType();
        NameValueListArrayType temp = new NameValueListArrayType();
        temp =  GetItemSpecifics(EbayID);
        NameValueListType[] array = new NameValueListType[temp.getNameValueListLength()+25];
        int counter = 0;
        for (int i=0;i<temp.getNameValueListLength();i++)
        {
        	array[i] = temp.getNameValueList(i);
        	counter = i;
        }
        NameValueListType nv1 = new NameValueListType();
        nv1.setName("Ships from");
        nv1.setValue(new String[]{"USA"});
    	array[counter+1] =  nv1;
    	counter++;
    	NameValueListType nv2 = new NameValueListType();
    	nv2.setName("Seller");
    	nv1.setValue(new String[]{"USA Seller"});
    	array[counter+1] =  nv2;
    	counter++;
    	NameValueListType nv3 = new NameValueListType();
    	nv3.setName("Money Back");
    	nv3.setValue(new String[]{"14 Days"});
    	array[counter+1] =  nv3;
    	counter++;
    	NameValueListType nv4 = new NameValueListType();
    	nv4.setName("Shipping");
    	nv4.setValue(new String[]{"Free"});
    	array[counter+1] =  nv4;
    	counter++;
    	
    	
    	
    	NameValueListType nv5 = new NameValueListType();
    	nv5.setName("Satisfaction");
    	nv5.setValue(new String[]{"100%"});
    	array[counter+1] =  nv5;
    	counter++;
    	

    	NameValueListType nv7 = new NameValueListType();
    	nv7.setName("Gift Option");
    	nv7.setValue(new String[]{"FREE"});
    	array[counter+1] =  nv7;
    	counter++;
    	
    	NameValueListType nv8 = new NameValueListType();
    	nv8.setName("Delivery");
    	nv8.setValue(new String[]{"Fast"});
    	array[counter+1] =  nv8;
    	counter++;
    	
    	NameValueListType nv9 = new NameValueListType();
    	nv9.setName("Customer Service");
    	nv9.setValue(new String[]{"Professional Service, We Will Solve Any Problem"});
    	array[counter+1] =  nv9;
    	counter++;
    	
    	NameValueListType nv10 = new NameValueListType();
    	nv10.setName("Handling Time");
    	nv10.setValue(new String[]{"Up to 3 Business Days (Usually same day)"});
    	array[counter+1] =  nv10;
    	counter++;
    	
    	NameValueListType nv11 = new NameValueListType();
    	nv11.setName("Questions");
    	nv11.setValue(new String[]{"ASK SELLER"});
    	array[counter+1] =  nv11;
    	counter++;
    
    	
    	
    	NameValueListType nv6 = new NameValueListType();
    	nv6.setName("Tax");
    	nv6.setValue(new String[]{"No Tax Additoin"});
    	array[counter+1] =  nv6;
    	
        itemSpec.setNameValueList(array);
        item.setItemSpecifics(itemSpec);
        reviseFP.setItemToBeRevised(item);

 
        try {
            reviseFP.reviseFixedPriceItem();
            
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (SdkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	

	public String ChangeTitle(String EbayId,String NewTitle) throws ApiException, SdkException, Exception
	{	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**U1I5WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCodeType.US); 
	    ReviseFixedPriceItemCall reviseFP = new ReviseFixedPriceItemCall(apiContext);
        ItemType item = new ItemType();

        // We are using SKU to identify items
        item.setItemID(EbayId); 

        item.setTitle(NewTitle);
        reviseFP.setItemToBeRevised(item);

        try {
            reviseFP.reviseFixedPriceItem();
            return reviseFP.getResponseObject().getAck().value();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (SdkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviseFP.getResponseObject().getAck().value();
	}
	
	
	public void GetMyItemsHistory(Item product,int days)
	{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**U1I5WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
		ApiLogging apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);       
		apiContext.setSite(SiteCodeType.US); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext);
		call.setItemID(product.EbayId);
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);
	    call.setNumberOfDays(days);
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf);
	    try{
		TransactionType[] transactions = call.getItemTransactions();
		
		if (transactions==null)
		{
		product.TransactionsLastMonth = 0;
		product.LastSaleDate = "2001-01-01";
		}
		else
		{
		product.TransactionsLastMonth  = transactions.length;
		List<TransactionType> list = Arrays.asList(transactions);
		Collections.sort(list, new Comparator<TransactionType>() {
			  public int compare(TransactionType o1, TransactionType o2) {
			      return o2.getCreatedDate().getTime().compareTo(o1.getCreatedDate().getTime());
			  }
			});
		product.LastSaleDate = dateFormat.format(list.get(0).getCreatedDate().getTime());
		}
	    }catch(Exception e)
	    {
			System.out.println(e);
	    }
		
		
	
	}
	
	public int Getwatchers(String EbayID)
	{
		ApiContext apiContext = new ApiContext();
	    ApiCredential cred = apiContext.getApiCredential(); //
	    cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**lLmaWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
	    apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
	    apiContext.setSite(com.ebay.soap.eBLBaseComponents.SiteCodeType.US);
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayID);
		Call.setIncludeWatchCount(true);
		try{
		ItemType Item = Call.getItem();
		System.out.println(Item.getWatchCount().intValue());
		return Item.getWatchCount().intValue();
		}catch(Exception e){}
		return -1;
	}
	

	
	public NameValueListArrayType GetItemSpecifics(String EbayID)
	{
		ApiContext apiContext = new ApiContext();
	    ApiCredential cred = apiContext.getApiCredential(); //
	    cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**lLmaWA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlYCpDpeFow6dj6x9nY+seQ**ro4DAA**AAMAAA**HtzFwhLFbBcGw+uCK/yI2xOZYneRGJ1fjrfZ7HvBreMtCw0I0WnHNth01LrCG4C9ZYs+UZPW4GbkSQLhrC2+wTT2+vcfv39ZSg3QejhSqxHLFxwE6DxKpyBYaVi+Xnq3zyAdSALDrKSvMtlxE9oY8IUmVSCETRlFjYPwtG99DKCAUq3y9t1BvoDTfg1SNhFY7aDRxvZc0lVO0rUnXIXGZ9Y+FoDVdbBH9HhTjBU3qchEKrqimaUjbKb7HNQqQcnwnsPWM4657Qt4UB0Ysc+48q9rwe0DDNq3XUyx7ApXhFgV0l97MW+XzANLcm9liYYty4pXa4oxFrg4pgLBki7EoD1kxsUO8nHt2QWCFMsslh3goEjAJAKvGmSEX8YZ0JLa5g/JQLSpSm3Vyp15F2nR5s3HPZFegfVxWoeMMfbAn4r9dsWWq300Gn4ipx5YOCfRUR6yTPfTRiahGZPeHoYZuq8pVWEsihdFmO2k9mMz+GvRl9FsV0rdITtFBDubCam2g+ZjB7XH6eSUANspwh6T3D9F+vtzcJgBFxXO20R3H3owG+aBweOrTRa2ENAL+BR+nq02Yyf9GjDMvmDnnT9qWJba7iCWsyhq24wXB6pDhgK8/YmrYIg8FfMspjiF/rYUSQStRCOFrN1h9PKT5rxwOWuhQ0YcKjBCQWQGBVFqIUs78zaFX40PNoW8PKc0W9ookeqyfRoiK3bvAZOWtaoyGnLC665GFBgo23Q3M5GZqeDpltScfnS8DDC4kHhWC0r3");
	    apiContext.setApiServerUrl("https://api.ebay.com/wsapi");
	    apiContext.setSite(com.ebay.soap.eBLBaseComponents.SiteCodeType.US);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayID);
		Call.setIncludeItemSpecifics(true);
		try{
		ItemType Item = Call.getItem();
		Item.getWatchCount();
		NameValueListArrayType list =Item.getItemSpecifics();
		return list;
		}catch(Exception e)
		{

		}
		return null;

        
	}
	
	public ArrayList<String> Getimages (String Ebayid)
	{
		ArrayList<String> ImagesLinks = new ArrayList<String>();
		
		
		return null;
	}

	public class CustomComparator implements Comparator<Item> {
	    @Override
	    public int compare(Item o1, Item o2) 
	    {
	        return Integer.compare(o2.QuantitySold,o1.QuantitySold);
	    }
	}
	
	public void print_to_file(ArrayList<Item> itemsList,Item item) throws FileNotFoundException
	{
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {


			File file = new File("C:\\Users\\Noname\\Desktop\\OptimizerOutput.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			int counter = 0;
			bw.write("\n"+"My id = "+item.EbayId);
			bw.newLine();
			for (Item ele:itemsList)
			{
				bw.write("\n"+ele.EbayId);	
				bw.newLine();
				bw.write("\n"+ele.Title);
				bw.newLine();
				bw.write("\n"+ele.QuantitySold);
				bw.newLine();
				if (counter<4) counter++;
				else break;
			}
			bw.newLine();
			bw.write("----------------------");
			bw.newLine();

		} catch (IOException e) {

			e.printStackTrace();

		}  finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}

	public void print_error(String e) throws FileNotFoundException
	{
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {


			File file = new File("C:\\Users\\Noname\\Desktop\\Error.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(e);
			bw.newLine();

		} catch (IOException e1) {

			e1.printStackTrace();

		}  finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}


}
