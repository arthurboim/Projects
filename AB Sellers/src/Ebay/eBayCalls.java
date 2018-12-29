package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.AckValue;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.GranularityLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.TransactionType;

import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;

public class eBayCalls implements Runnable{

	private   String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	private   int pageSize = 200;
	private   int minmumSoldItemsThreshold = 2;
	private   String scham = null;
	private   ApiContext apiContext;
	private   ApiCredential cred; 
	private   String eBay_token = null;
	private   String Server_url = null;
	private   String Application_id= null;
	private   SiteCodeType SiteCode = null;
	private   String Contry = null;
	private   PaginationType pt;
	private	  PaginationInput ptInput; 
	private   ApiLogging apiLogging;
	private   ClientConfig config; 
	private   TimeFilter tf;
	private   Calendar calFrom;
	private   Calendar calTo;
	private    static int threadCounter = 0;
	private  FindingServicePortType serviceClient;
	private  GetSellerListCall GetSellerListCall;
	private  GetItemCall GetItemCall;
	private  GetItemTransactionsCall GetItemTransactionsCall;
	private  FindItemsByKeywordsRequest  FindItemsbykeywordRequest;
	private  FindItemsByKeywordsResponse  FindItemsbykeywordRespond;
	private  IDataBase DB; 
	private  ResultSet Res;
	private DateFormat dateFormat;
	private Date date;
	private ArrayList<ItemType> sellerItemsList;
	private Item newItem;
	private ItemType[] activeItems;
	private int ThreadId =0;
	
	/* Contractor */
	public eBayCalls() {
		
		/* Load configurations */
		InitFromFile();
		
		/* Load Database */
		DB = new SQLDataBase();
		
		date = new Date();
		dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		sellerItemsList = new ArrayList<ItemType>();
		newItem = new Item();
		apiContext = new ApiContext();
		cred = apiContext.getApiCredential(); 
		
		/* eBay API Initialization */
		eBayAPIGeneralinitialization();
		InitializationSellerListCall();
		InitializationGetItemCall();
		InitializationFindByKeyWordRequest();
		InitializationOfGetItemTransactions();
		ThreadId = threadCounter;
	}

	/* Initialization */
	private void InitFromFile()
	{

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+"Schame: ".length());
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
	
	private void eBayAPIGeneralinitialization()
	{
		/*eBay API calls general initialization*/
		apiContext = new ApiContext();
	    cred = apiContext.getApiCredential();
	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(SiteCode);
	    pt = new PaginationType();
	    pt.setEntriesPerPage(pageSize);   
	    apiLogging = new ApiLogging();
	    apiContext.setApiLogging(apiLogging);
	    config = new ClientConfig();
	    config.setApplicationId(Application_id);
	}
	private void InitializationSellerListCall()
	{
	    /* initialization Seller list call */
	    GetSellerListCall = new GetSellerListCall (apiContext);
		GetSellerListCall.setGranularityLevel(GranularityLevelCodeType.COARSE);
	    calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH, 0);
	    calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,30);
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    GetSellerListCall.setEndTimeFilter(tf);
	    GetSellerListCall.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
	}
	private void InitializationGetItemCall()
	{
	    /* initialization Get Item call */
	    GetItemCall = new GetItemCall(apiContext);
		GetItemCall.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
	}

	private void InitializationOfGetItemTransactions()
	{
	    /* initialization of get item transactions */
		GetItemTransactionsCall = new GetItemTransactionsCall(apiContext);
	    tf = new TimeFilter(calFrom, calTo);
	    GetItemTransactionsCall.setModifiedTimeFilter(tf);
	}
	private void InitializationFindByKeyWordRequest()
	{

	    /* initialization of find item advanced request */
		serviceClient =  FindingServiceClientFactory.getServiceClient(config);
		FindItemsbykeywordRequest = new FindItemsByKeywordsRequest();
	    ItemFilter filter2 = new ItemFilter();
		filter2.setName(ItemFilterType.CONDITION);
		filter2.getValue().add("1000");
		FindItemsbykeywordRequest.getItemFilter().add(filter2); 
		
		ItemFilter filter = new ItemFilter();
		filter.setName(ItemFilterType.LISTING_TYPE);
		filter.getValue().add("FixedPrice");
		FindItemsbykeywordRequest.getItemFilter().add(filter);  
	
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add(Contry);
		FindItemsbykeywordRequest.getItemFilter().add(filter6);  
		
		List<OutputSelectorType> outputSelector = FindItemsbykeywordRequest.getOutputSelector();
		OutputSelectorType outputSelectorType = OutputSelectorType.SELLER_INFO;
		outputSelector.add(outputSelectorType);
		ptInput = new PaginationInput();
		ptInput.setEntriesPerPage(8);   
		ptInput.setPageNumber(1);
		FindItemsbykeywordRequest.setPaginationInput(ptInput);
		FindItemsbykeywordRequest.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
		
	
	}
	
	
	/* Public functions */
	public void RunProductFinder()
	{
		System.out.println("I'm thread id: "+ThreadId+" start call GetSellerItems");
		GetSellerItems(GetNextSeller());
	}
	
	
	private void ItemResearch()
	{
		for(ItemType ele:sellerItemsList)
		{
			if (ele.getSellingStatus().getQuantitySold() > minmumSoldItemsThreshold)
			{
				newItem.setMarketPlaceCode(ele.getItemID());
				GetItemUniversalCode(newItem); 
				
				if (newItem.getUniversalCode() != null && 
					newItem.getCodeType() 	   != null)
				{
					newItem.setAmountSoldByTheSeller(ele.getSellingStatus().getQuantitySold());
					newItem.setSellerPrice(ele.getSellingStatus().getCurrentPrice().getValue());
					if ((DB.IsExcist("select * from "+scham+".productfromsellers where Code = '"+newItem.getUniversalCode()+"';") == false) && 
						newItem.getAmountSoldByTheSeller() > 0)
					{
						UpdateSoldAmount(newItem);
					}
					
					if(true == newItem.IsEnoughDemand())
					{
						DB.Write("INSERT INTO "+scham+".productfromsellers (scan_date,SellerPrice,Code,CodeType,SoldBySeller,EbayLowestprice,SoldLastWeekAll,SellersAmountSold)"+
								"VALUES ('"+dateFormat.format(date)+"',"+newItem.getSellerPrice()+",'"+newItem.getUniversalCode()+"','"+newItem.getCodeType()+"',"+newItem.getAmountSoldByTheSeller()+","+newItem.getMarketPlaceLowestPrice()+","+newItem.getQuantitySold()+","+newItem.getSellerSoldTheItem()+");");	
					}
				}
				newItem.rest();
			}
		}
		
		sellerItemsList.clear();
		//System.out.println("Number of eBay live threads = "+threadCounter);
	}
	
	
	
	public void GetSellerItems(String sellerName)
	{
		int pageCounter = 1;
		Boolean hasMorePages = true; 
		
		GetSellerListCall.setUserID(sellerName);
		
		do
		{
		    try {
		    	pt.setPageNumber(pageCounter); 
		    	GetSellerListCall.setPagination(pt);
		    	activeItems = GetSellerListCall.getSellerList();
		    	for(int i=0 ; i < activeItems.length;i++)
		    	{
		    		sellerItemsList.add(activeItems[i]);
		    	}
		    	
		    	if (activeItems.length == pageSize)
		    	{
		    		pageCounter++;	
		    		hasMorePages = true;
		    	}else
		    	{
		    		hasMorePages = false;
		    	}

		    	ItemResearch();
		    	
			} catch (Exception e) 
		    {
				System.out.println("Exception GetSellerItems: "+e.getMessage());
				hasMorePages = false;
				DB.Write("DELETE FROM amazon.sellers WHERE SellerId='"+sellerName+"';");
				System.out.println("Seller: "+sellerName+" removed!");
			}

		}while(hasMorePages);
		activeItems = null;
	}

	public void GetItemUniversalCode(Item newItem)
	{
		try {
			GetItemCall.setItemID(newItem.getMarketPlaceCode());
			if (null == GetItemCall.getItem())
			{
				return;
			}
			
			if (GetItemCall.getItem().getProductListingDetails().getISBN()!=null && !GetItemCall.getItem().getProductListingDetails().getISBN().toLowerCase().contains("apply") && !GetItemCall.getItem().getProductListingDetails().getISBN().toLowerCase().contains("applicable")) 
			{
				 newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getISBN());
				 newItem.setCodeType("ISBN");
			}
			else if (GetItemCall.getItem().getProductListingDetails().getUPC()!=null&& !GetItemCall.getItem().getProductListingDetails().getUPC().toLowerCase().contains("apply")&& !GetItemCall.getItem().getProductListingDetails().getUPC().toLowerCase().contains("applicable"))
			{
				 newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getUPC());
				 newItem.setCodeType("UPC");
			}
			else if (GetItemCall.getItem().getProductListingDetails().getEAN()!= null && !GetItemCall.getItem().getProductListingDetails().getEAN().toLowerCase().contains("apply")&& !GetItemCall.getItem().getProductListingDetails().getEAN().toLowerCase().contains("applicable")) 
			{
				 newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getUPC());
				 newItem.setCodeType("EAN");
			}
			
		} catch (Exception e) 
		{
			
		}
		
	}

	// Using of findbykeyword request (in oder to change the service remove the 1 in GetAndUpdateSearchResults1)
	public void UpdateSoldAmount(Item newItem)
	{
		int soldAmount = 0;
		
		GetSearchResults(newItem);
	    List<SearchItem> items = FindItemsbykeywordRespond.getSearchResult().getItem();

	    /* Set lowest price */
    	if(items.size() > 0)
    	{
    		try{
    			newItem.setMarketPlaceLowestPrice(items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue());	    		
    		}
    		catch(Exception e1)
    		{
    			FindItemsbykeywordRespond = null;
    			System.gc();
    			return;
    		}
    	}
    	
    	for(int index = 0; index < items.size() && index < 8; index++) 
        {
    		/* Checking if he seller is Exist if not add to db */
        	if (DB.IsExcist("select 1 from amazon.sellers where SellerId = '"+items.get(index).getSellerInfo().getSellerUserName()+"';")== false &&
        			items.get(index).getSellerInfo().getFeedbackScore().intValue() > 200) 
    		{
         		DB.Write("INSERT INTO "+scham+".sellers (SellerId,Feedbackscore,PositiveFeedbackPercent,Scaned)"+
    	        		"VALUES ('"+items.get(index).getSellerInfo().getSellerUserName()+"',"+items.get(index).getSellerInfo().getFeedbackScore().intValue()+","+items.get(index).getSellerInfo().getPositiveFeedbackPercent().doubleValue()+","+0+");");
    		}
    		
    		/* Get the amount of sold items according to price and days range */
        	if (items.get(0).getSellingStatus().getCurrentPrice().getValue()<30)
        	{
        		soldAmount = GetItemTrasactions(items.get(index).getItemId(),7);
        	}else if (items.get(0).getSellingStatus().getCurrentPrice().getValue() >=30 &&
        			  items.get(0).getSellingStatus().getCurrentPrice().getValue() <70)
        	{
        		soldAmount = GetItemTrasactions(items.get(index).getItemId(),14);
        	}
        	else if (items.get(0).getSellingStatus().getCurrentPrice().getValue()>=70)
        	{
        		soldAmount = GetItemTrasactions(items.get(index).getItemId(),14);
        	}
        	
    		/* Update the amount of total amount of sells for this item */
    		newItem.setQuantitySold(soldAmount + newItem.getQuantitySold());
    		
    		/* Update the amount of sellers that sold the item */
        	if (soldAmount > 0)  
        	{
        		newItem.setSellerSoldTheItem(newItem.getSellerSoldTheItem()+1);
        	}
        }
    	
    	DB.Write("UPDATE "+scham+".productfromsellers SET SellersAmountSold="+newItem.getSellerSoldTheItem()+" , SoldLastWeekAll="+newItem.getQuantitySold()+" WHERE Code='"+newItem.getUniversalCode()+"' and Codetype = '"+newItem.getCodeType()+"';");
    	
    	items = null;
		FindItemsbykeywordRespond = null;
		System.gc();
	}
	
	// Update the item position in eBay search
	public void UpdateItemPosition(Item newItem)
	{
		int counter = 1;
		GetSearchResults(newItem);
		
		if (AckValue.FAILURE == FindItemsbykeywordRespond.getAck())
		{
			// Do nothing
		}
		else
		{
		    List<SearchItem> items = FindItemsbykeywordRespond.getSearchResult().getItem();
		    newItem.setMarketPlaceResults(items.size());
		    newItem.setMarketPlaceLowestPrice(items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue());
		    newItem.setArbitraje(newItem.getMarketPlaceLowestPrice() - newItem.getMinPriceToSale());
	
	        for(SearchItem item : items) 
	        {
	        	if (item.getSellingStatus().getCurrentPrice().getValue() < newItem.getMinPriceToSale())
	        	{
	        		counter++;
	        	}
	        	else 
	        	{
	        		newItem.setPlaceInLowestPrice(counter);
	        		break;
	        	}
	        }
	        
	        // Don't know if this is needed
	        if (newItem.getPlaceInLowestPrice() == 0) 
	        {
	        	newItem.setPlaceInLowestPrice(++counter);
	        }   
		}
	}
	
	/* Protected functions */
	protected int GetItemTrasactions(String itemId, int days)
	{
		int itemCount = 0;
		GetItemTransactionsCall.setItemID(itemId);
		GetItemTransactionsCall.setNumberOfDays(days);
		
		try {
			TransactionType[] items = GetItemTransactionsCall.getItemTransactions();
			if (null == items)
			{
				itemCount = 0;
			}else
			{			
				itemCount = items.length;
			}
			
			items = null;
			System.gc();
		} catch (Exception e) 
		{
			System.out.println("Exception in GetItemTrasactions: "+e.getMessage());
		}
		
		return itemCount;
	}

	/* Private functions */
	private String GetNextSeller()
	{
		String sellerName = null;
		Res = DB.Read("SELECT * FROM "+scham+".sellers where scaned = 0 and ((Sale_through>10 and Sale_through<200) or (Sale_through is null));");
		try{
		Res.next();
		while(Res.getString("SellerId")== null) 
		{
			Res.next();
		}
		sellerName = Res.getString("SellerId");
		DB.Write("UPDATE  amazon.sellers SET scaned = 1 where SellerId = '"+sellerName+"';");
		}catch(Exception e)
		{
			System.out.println("Exception in GetNextSeller: "+e.getMessage());
		}
		
		return sellerName;
	}

	private void GetSearchResults(Item newItem)
	{
		try{
			/* eBay call */
			FindItemsbykeywordRequest.setKeywords(newItem.getUniversalCode());
			FindItemsbykeywordRespond = serviceClient.findItemsByKeywords(FindItemsbykeywordRequest);
		}catch(Exception e)
		{
			System.out.println("Exception in GetSearchResults");
			System.out.println(e.getMessage());
			StackTraceElement[] elements = e.getStackTrace(); 
			for (int iterator=1; iterator<=elements.length; iterator++)  
                System.out.println("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
		}
	}
	
	@Override
	public void run() {
		
		try{
			threadCounter++;
			while(true)
			{
				RunProductFinder();
			}
		}catch(Exception e)
		{
			System.out.println(e.getStackTrace());
			StackTraceElement[] elements = e.getStackTrace(); 
			for (int iterator=1; iterator<=elements.length; iterator++)  
                System.out.println("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
		}finally{
			System.out.println("Thread is dead!");
			threadCounter--;
		}
	}
}
