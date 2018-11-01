package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
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

public class Ebay implements Runnable {
	
	String ItemEbayId = null;
	double EbayLowestprice = -1;
	double SellerPrice = -1;
	String CodeType = null;
	String Code = null;
	int SellerAmountSold = 0;
	int SoldLastWeekAll = 0;
	int SoldBySellerLastWeek = 0;
	int SoldBySeller = 0;
	int ebayResults = 0;
	double Seller_items_amount = 0;
	double Seller_Sold_Amount = 0;
	double SaleThrogh = 0;
	int SalesCounter = 0;
	int SellersAmountSold = 0;
	static int thradAmount =0;

	public  static Connection con ;
	public  static java.sql.Statement statement_update ;
	public  static java.sql.Statement statement ;
	public  static String Connection = null;
	public  static String scham = null;
	public  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public  String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public  String AWS_SECRET_KEY_FROM_FILE = null;
	public  String ENDPOINT_FROM_FILE = null;
	public  ApiContext apiContext = new ApiContext();
	public  ApiCredential cred = apiContext.getApiCredential(); //
	public  String eBay_token = null;
	public  String Server_url = null;
	public  String Application_id= null;
	public  SiteCodeType SiteCode = null;
	public  String Contry = null;
	public  ResultSet res = null;
	public  ApiCredential cred2 = apiContext.getApiCredential(); //
	public  ApiContext    apiContext2 = new ApiContext();
	public  ApiCredential cred3 = apiContext.getApiCredential(); //
	public  ApiContext    apiContext3 = new ApiContext();
	
	public Ebay() {
		//FILENAME = AB_Sellers_Gui.Path;
		//System.out.println("File name that loaded = "+FILENAME);
		//System.out.println("File name that loaded = "+AB_Sellers_Gui.Path);
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
				
			
				if (sCurrentLine.contains("AWS_ACCESS_KEY_ID:"))
				{
					AWS_ACCESS_KEY_ID_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_ACCESS_KEY_ID:")+19);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					Connection =Connection+scham;
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
				System.out.println("Ebay Constarctor ");
			}
			}
	
	
	
	}

	
	
	public void EbayMain() throws ApiException, SdkException, Exception
	{
		String Seller  = null;
		Seller = GetSellersToScrap();
		while (Seller!=null)
		{
			Get_Seller_Items(Seller);
			Seller = GetSellersToScrap();
		}
		
	}
	
	public void Get_Seller_Items(String SellerName) throws ApiException, SdkException, Exception
	{		     
	int returnItems = 0;
	int counter  = 1;
	int itemsamount = 0;
	int SoldAmount = 0;
	
	Date date;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date Today = new Date();
	Date DateBack = new Date();
	Calendar c = Calendar.getInstance(); 
	c.setTime(Today); 
	c.add(Calendar.DATE, -14);
	DateBack = c.getTime();
	
	ApiContext apiContext = new ApiContext();
    ApiCredential cred = apiContext.getApiCredential();
    cred.seteBayToken(eBay_token);
    apiContext.setApiServerUrl(Server_url);
    apiContext.setSite(SiteCode);
 
    GetSellerListCall Call = new GetSellerListCall (apiContext);
    Call.setUserID(SellerName);
    Call.setGranularityLevel(GranularityLevelCodeType.COARSE);
    
    Calendar calFrom = Calendar.getInstance();
    calFrom.add(Calendar.DAY_OF_MONTH, 0);
    Calendar calTo = Calendar.getInstance();
    calTo.add(Calendar.DAY_OF_MONTH,60);
    TimeFilter tf = new TimeFilter(calFrom, calTo);
    Call.setEndTimeFilter(tf);
    
    Call.addDetailLevel(DetailLevelCodeType.RETURN_ALL);

    
    while(returnItems!=-1)
	{
	    PaginationType pt = new PaginationType();
	    pt.setEntriesPerPage(200);   
	    pt.setPageNumber(counter); 
	    Call.setPagination(pt);
	    try{
	    ItemType[] activeItems = Call.getSellerList(); //Execute API call
	    if (activeItems.length<200) 
		{
			returnItems = -1;
			itemsamount+=activeItems.length;
		}
	    else
	    {
	    	counter++;
	    	itemsamount+=200;
	    }
	    
		for (ItemType ele:activeItems)
		{
			date  = ele.getListingDetails().getStartTime().getTime();
			SoldAmount+=ele.getSellingStatus().getQuantitySold();
	
			if (ele.getSellingStatus().getQuantitySold()>0) 
			{
				try{
				GetItem(ele.getItemID()); // getting code 
				}catch(Exception e1)
				{
			    	System.out.println(e1.getMessage());
			    	System.out.println("ele.getItemID() = "+ele.getItemID());
			    	System.out.println("Exception 3");
				}
				
				if (CodeType != null && Code != null)
				{
					SoldBySeller = ele.getSellingStatus().getQuantitySold();
					SellerPrice = ele.getSellingStatus().getCurrentPrice().getValue();
					ItemEbayId = ele.getItemID();
					if (Code!=null&&CheckCode(Code)==0&&SoldBySeller>0&&SellerPrice>10)
					{	
						GetAlltransactions(Code); //Code here is the original item that we found and check his competitors//
						//System.out.println("SellerName = "+SellerName+" SoldLastWeekAll = "+SoldLastWeekAll +" SellersAmountSold = "+SellersAmountSold +" SellerPrice = "+SellerPrice);
						if (((SoldLastWeekAll>3)   && (1<SellersAmountSold) && (SellerPrice<30)&& SellerPrice>10) 	
							||(SoldLastWeekAll>1 && 0<SellersAmountSold && SellerPrice>30 && SellerPrice<70)
							||(SoldLastWeekAll>0 && 0<SellersAmountSold  && SellerPrice>70 && SellerPrice<1000))
						{
							InsertNewItem();
						}
						
			            SalesCounter = 0; //counter //
			            SoldLastWeekAll = 0; //all the competitors transactions //
			            SellersAmountSold = 0; //how many sellers sale //
					}
				}
				InitVeribales();
			} 
			
		}
		
		activeItems = null;
		pt = null;
		System.gc();
	    }catch(Exception e)
	    {
	    	System.out.println("Exception 1");
	    	System.out.println(e.getMessage());
	    	String s = e.toString();
	    	try{
		    	s = s.substring(s.indexOf("The user")+10, s.indexOf("was not found in our database.")-2);
		    	DeleteSeller(s);
		    	returnItems = -1;
	    	}catch(Exception e1)
	    	{
	    		System.out.println(e1);
	    	}
	    }
	    
		Seller_items_amount = itemsamount;
		Seller_Sold_Amount = SoldAmount;
		SaleThrogh = (Seller_Sold_Amount/Seller_items_amount)*100;
		itemsamount = 0;
		SoldAmount = 0;
		if (Seller_items_amount>0)
		{
		if (SaleThrogh<10 ||SaleThrogh>250 )
		{
			DeleteSeller(SellerName);
		}
		else if ((SaleThrogh>30 && SaleThrogh<250 )&& Seller_Sold_Amount>200)
		{
			System.out.println("New seller added - "+SellerName+" Sale through  = "+SaleThrogh+" Seller_Sold_Amount = "+Seller_Sold_Amount);
	
			Update_Seller_Info(SellerName);
		}
	    }else
	    {
	    	DeleteSeller(SellerName);
	    }
	    }
    
	returnItems = 0;
	date = null;
	dateFormat = null;
	Today = null;
	DateBack = null;
	c = null; 
	DateBack = null;
	apiContext = null;
    cred = null;
    Call = null;
    System.gc();
    
    
	}
	
    public static void DeleteSeller(String SellerId)
    {
     	try{
        	String WriteToData;
    		con = DriverManager.getConnection(Connection,"root","root");
    		statement_update = con.createStatement();//
    		WriteToData = "DELETE FROM "+scham+".sellers  WHERE SellerId='"+SellerId+"';";	
    		statement_update.executeUpdate(WriteToData);
         	WriteToData = null;
         	statement_update = null;
         	System.gc();
        	}
        	catch(SQLException e)
     		{
        		e.printStackTrace();
        		System.out.println("DeleteSeller exception");
        	}	
    }
	  
	public void GetAlltransactions(String Code) //this is need to be coped to monitor for searching //
	{
		ClientConfig config = new ClientConfig();
		config.setApplicationId(Application_id);
	
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
		
		/*
		ItemFilter filter5 = new ItemFilter();
		filter5.setName(ItemFilterType.AVAILABLE_TO);
		filter5.getValue().add("US");
		request.getItemFilter().add(filter5);  
		*/
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add(Contry);
		request.getItemFilter().add(filter6);  
		
		List<OutputSelectorType> outputSelector = request.getOutputSelector();
		OutputSelectorType outputSelectorType = OutputSelectorType.SELLER_INFO;
		outputSelector.add(outputSelectorType);
	
	    try
	    {
	    PaginationInput pt = new PaginationInput();
	    pt.setEntriesPerPage(8);   
	    pt.setPageNumber(1);
	    request.setPaginationInput(pt);	
	    
	    //TODO check here!
	    
	    
	    request.setKeywords(Code); 
	    request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
	    FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
	    

	    if (result.getAck().toString()!="FAILURE") 
	    {
	    List<SearchItem> items = result.getSearchResult().getItem();
	    if (items.size()>0)
	    {
	    	double shipping;
	    	if (items.get(0).getShippingInfo().getShippingServiceCost() == null)
	    	{
	    		 shipping  = 0;  
	    	}else
	    	{
	    		 shipping  = items.get(0).getShippingInfo().getShippingServiceCost().getValue();  
	    	}
	    EbayLowestprice =  items.get(0).getSellingStatus().getCurrentPrice().getValue()+shipping;
	    
	    
	    int counter = 0;
	    if (items.size()>0)
	    {
	        	
	        for(SearchItem item : items) 
	        {
	        	if (CheckIfExcistingSeller(item.getSellerInfo().getSellerUserName())==0 &&
	        		item.getSellerInfo().getFeedbackScore().intValue() > 200) 
	    		{
	    			InsertNewSeller(item.getSellerInfo().getFeedbackScore().intValue(),item.getSellerInfo().getPositiveFeedbackPercent().doubleValue(),item.getSellerInfo().getSellerUserName());
	    		}
	        	
	        	if (items.get(0).getSellingStatus().getCurrentPrice().getValue()<30)
	        	{
	        		GetItemTransactions2(item.getItemId(),7);
	        	}else if (items.get(0).getSellingStatus().getCurrentPrice().getValue()>30&&items.get(0).getSellingStatus().getCurrentPrice().getValue()<70)
	        	{
	        		GetItemTransactions2(item.getItemId(),14);
	        	}
	        	else if (items.get(0).getSellingStatus().getCurrentPrice().getValue()>70)
	        	{
	        		GetItemTransactions2(item.getItemId(),30);
	        	}
	        	
	        	if (SalesCounter>0)  
	        	{
	        		SellersAmountSold++;
	        	}
	        	
	        	SalesCounter = 0;
	        	if (counter++>8) 
	    		{
	        		break;
	    		}
	        	
	        }
	        
	        Update_Sellers_Amount_Sold_and_Transcations_Last_Week();
	    }
	    
	    }
	    	items = null;
	    }
	
	    result = null;
	    System.gc();

	    }catch(Exception ex)
	    {
	    	System.out.println(ex);
	    	System.out.println("Exception 2 GetAlltransactions Exception");
	    	System.out.println("Code = "+Code);
	    	
	    }  
	
	    
	    config = null;
	    serviceClient = null;
	    request = null;
	    filter = null;
	    filter2 = null;
	   // filter5 = null;
	    filter6 = null;
	    outputSelector = null;
	    
		System.gc();
	}

	public void GetItemTransactions2(String EbayId,int days) throws ApiException, SdkException, Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		cred3 = apiContext3.getApiCredential();
		cred3.seteBayToken(eBay_token);
		apiContext3.setApiServerUrl(Server_url);
		ApiLogging apiLogging3 = new ApiLogging();
		apiContext3.setApiLogging(apiLogging3);       
		apiContext3.setSite(SiteCode); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext3);
		call.setItemID(EbayId);
		
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);
	 
	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf);
	    
	    call.setNumberOfDays(days);
	    try{
		TransactionType[] transactions = call.getItemTransactions();
		if (transactions==null)
		{
			SalesCounter = 0;
		}
		else
		{
			SalesCounter  = transactions.length;
		}
		
		transactions = null;
	    }catch(Exception e1)
	    {
	    	System.out.println("GetItemTransactions2");
	    }
		
		apiLogging3 = null;
		call= null;
		calFrom = null;
		calTo = null;
		tf = null;
		System.gc();

	}
	
	public  void GetItemTransactions(int days) throws ApiException, SdkException, Exception
	{

		cred2 = apiContext2.getApiCredential();
		cred2.seteBayToken(eBay_token);
		apiContext2.setApiServerUrl(Server_url);
		ApiLogging apiLogging2 = new ApiLogging();
		apiContext2.setApiLogging(apiLogging2);       
		apiContext2.setSite(SiteCode); 
		GetItemTransactionsCall call = new GetItemTransactionsCall(apiContext2);
		call.setItemID(ItemEbayId);
		
	    Calendar calFrom = Calendar.getInstance();
	    calFrom.add(Calendar.DAY_OF_MONTH,0);
	    Calendar calTo = Calendar.getInstance();
	    calTo.add(Calendar.DAY_OF_MONTH,60);

	    TimeFilter tf = new TimeFilter(calFrom, calTo);
	    call.setModifiedTimeFilter(tf);
	    
	    call.setNumberOfDays(days);
		TransactionType[] transactions = call.getItemTransactions();
		if (transactions==null)
		{
			SoldBySellerLastWeek = 0;
		}
		else
		{
			SoldBySellerLastWeek  = transactions.length;
		}
		transactions = null;
		apiLogging2 = null;
		call= null;
		calFrom = null;
		calTo = null;
		tf = null;
		System.gc();
	}
	
	public  void GetItem(String ebayId) throws ApiException, SdkException, Exception
	{
	cred.seteBayToken(eBay_token);
	apiContext.setApiServerUrl(Server_url);
	ApiLogging apiLogging = new ApiLogging();
	apiContext.setApiLogging(apiLogging);       
	apiContext.setSite(SiteCode); 
	
	GetItemCall call = new GetItemCall(apiContext);
	call.setItemID(ebayId);
	call.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
	ItemType item = call.getItem();
	try{
	if (item.getProductListingDetails().getISBN()!=null && !item.getProductListingDetails().getISBN().toLowerCase().contains("apply") && !item.getProductListingDetails().getISBN().toLowerCase().contains("applicable")) 
	{
		CodeType = "ISBN";
		Code = item.getProductListingDetails().getISBN();
	}
	else if (item.getProductListingDetails().getUPC()!=null&& !item.getProductListingDetails().getUPC().toLowerCase().contains("apply")&& !item.getProductListingDetails().getUPC().toLowerCase().contains("applicable"))
	{
		CodeType = "UPC";
		Code = item.getProductListingDetails().getUPC();
	}
	else if (item.getProductListingDetails().getEAN()!=null&& !item.getProductListingDetails().getEAN().toLowerCase().contains("apply")&& !item.getProductListingDetails().getEAN().toLowerCase().contains("applicable")) 
		{
		CodeType = "EAN";
		Code = item.getProductListingDetails().getEAN();
		}
	}catch(Exception e)
	{
		CodeType = null;
		Code = null;
	}
	
	
	item = null;
	call = null;
	apiLogging = null;
	System.gc();
	}

	public  synchronized String GetSellersToScrap() throws InterruptedException
	{
		try {
			String s = null;
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();
			
			res = statement.executeQuery("SELECT * FROM "+scham+".sellers where scaned = 0 and ((Sale_through>10 and Sale_through<200) or (Sale_through is null));"); 
			
			res.next();
			while(res.getString("SellerId")== null) 
			{
				res.next();
			}
			
			System.out.println("\nChecking seller "+res.getString("SellerId"));
			statement_update = con.createStatement();
			String WriteToData;
			WriteToData = "UPDATE  amazon.sellers SET scaned = 1 where SellerId = '"+res.getString("SellerId")+"';";		  
			statement_update.executeUpdate(WriteToData);
			WriteToData = "COMMIT;";
			statement_update.executeUpdate(WriteToData);
			s = res.getString("SellerId");
			WriteToData = null;
			statement = null;
			statement_update = null;
			con.close();
			con = null;
			System.gc();
			return s;
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("GetSellersToScrap");
		}
		return null;
	}

	public void InsertNewItem() throws SQLException //Only ebay check//
	{
	con = DriverManager.getConnection(Connection,"root","root");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Date date = new Date();
	statement_update = con.createStatement();
	if (Code.contains(" ")) Code = Code.replace(" ", "");
	try{
		String WriteToData; //SoldLastWeekAll //SellersAmountSold SoldLastWeekAll SellersAmountSold
		WriteToData = "INSERT INTO "+scham+".productfromsellers (scan_date,SellerPrice,Code,CodeType,SoldBySeller,EbayLowestprice,SoldLastWeekAll,SellersAmountSold)"+
		"VALUES ('"+dateFormat.format(date)+"',"+SellerPrice+",'"+Code+"','"+CodeType+"',"+SoldBySeller+","+EbayLowestprice+","+SoldLastWeekAll+","+SellersAmountSold+");";
		//System.out.println(WriteToData);
		statement_update.executeUpdate(WriteToData);
		WriteToData = null;
		System.gc();
		}catch(SQLException e)
		{
			System.out.println("InsertNewItem SQLException");
		}	
	dateFormat = null;
	date = null;
	statement_update = null;
	con.close();
	con = null;
	System.gc();
	}

	public int  CheckIfExcistingSeller(String SellerName) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		res = statement.executeQuery("select 1 from amazon.sellers where SellerId = '"+SellerName+"';"); 
		res.next();
		try{
			if (res != null)   
			{  
			res.beforeFirst();  
			res.last();   
			if (res.getRow()==0)
				{
				con = null;
				statement = null;
				res = null;
				System.gc();
				return 0;
				}
			else 
				{
				con = null;
				statement = null;
				res = null;
				System.gc();
				return 1;
				}
			}  
		}catch(Exception e){System.out.println(e);}
		con.close();
		con = null;
		statement = null;
		System.gc();
		return 1;
	}
	
	public void InsertNewSeller(int Feedbackscore, double PositiveFeedbackPercent ,String Seller_name) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement_update = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "INSERT INTO "+scham+".sellers (SellerId,Feedbackscore,PositiveFeedbackPercent,Scaned)"+
			"VALUES ('"+Seller_name+"',"+Feedbackscore+","+PositiveFeedbackPercent+","+0+");";
			statement_update.executeUpdate(WriteToData);
			WriteToData = null;
			System.gc();
			}catch(SQLException e){System.out.println(e);}	
		con.close();
		con = null;
		statement_update = null;
		System.gc();
	}

	public void Update_Sellers_Amount_Sold_and_Transcations_Last_Week() throws SQLException
	{
		 con = DriverManager.getConnection(Connection,"root","root");
		 statement_update = con.createStatement();//
		try{
			String WriteToData;
			WriteToData = "UPDATE "+scham+".productfromsellers SET SellersAmountSold="+SellersAmountSold+" , SoldLastWeekAll="+SoldLastWeekAll+" WHERE Code='"+Code+"' and Codetype = '"+CodeType+"';";
			statement_update.executeUpdate(WriteToData);
			WriteToData = null;
			System.gc();
			}catch(SQLException e)
			{
				System.out.println("Update_Sellers_Amount_Sold_and_Transcations_Last_Week SQLException");
			}
		con.close();
		con = null;
		statement_update = null;
		System.gc();
	}

	public void Update_Seller_Info(String SellerName) throws SQLException
	{
		if (Seller_items_amount!=0)
		{
			System.out.println("New seller added sale through = "+(Seller_Sold_Amount/Seller_items_amount)*100);
			con = DriverManager.getConnection(Connection,"root","root");
			statement_update = con.createStatement();//
			try{
			String WriteToData;
			WriteToData = "UPDATE "+scham+".sellers SET Items_amount="+Seller_items_amount+" , Sold_amount="+Seller_Sold_Amount+" ,Sale_through = "+(Seller_Sold_Amount/Seller_items_amount)*100+" WHERE SellerId='"+SellerName+"';";
			statement_update.executeUpdate(WriteToData);
			}catch(SQLException e){System.out.println(e);}
			con.close();
			con = null;
			statement_update = null;
			System.gc();
		}
	}
	
	public int CheckCode(String Code) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();//
		ResultSet res = statement.executeQuery("select 1 from "+scham+".productfromsellers where Code = '"+Code+"';"); 
		res.next();
		try{
			if (res != null)   
			{  
			res.beforeFirst();  
			res.last();   
			if (res.getRow()==0)return 0;
			else return 1;
			}  
		}catch(Exception e){System.out.println(e);}
		con.close();
		con = null;
		statement = null;
		System.gc();
		return 1;
	}
	
	public void InitVeribales()
	{
		ItemEbayId = null;
		EbayLowestprice = -1;
		SellerPrice = -1;
		CodeType = null;
		Code = null;
		SellersAmountSold = 0;
		SoldLastWeekAll = 0;
		SoldBySellerLastWeek = 0;
		SoldBySeller = 0;
		ebayResults = 0;
		System.gc();
	}

	@Override
	public void run() {
		
		try {
	
			String Seller  = null;
			Seller = GetSellersToScrap();
			thradAmount++;
			System.out.println("Runing threads -> "+thradAmount);
			while (true)
			{
				Get_Seller_Items(Seller);
				Seller = null;
				System.gc();
				Seller = GetSellersToScrap();
			}
		} catch (Exception e) 
		{
			thradAmount--;
			System.out.println("Runing threads -> "+thradAmount);
		}
	}
	
	
}
