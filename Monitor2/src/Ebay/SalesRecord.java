package Ebay;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import AmazonOrders.AmazonCalls;
import Database.DatabaseOp;


public class SalesRecord {

	static String EbayId = null;
	static String Asin = null;
	static String Code = null;
	static int placeinlowestprice = 1;
	static int Placeinbestmatch = 1;
	static int placeinfeedbackamount = 1;
	static int Ebayresults = -1;
	static double arbitraje = 0;
	static double Sale_true = -1;
	static int Rank  = 0;
	static int differenceDays = -1;
	
	public static final String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;
	public static ApiContext apiContext = new ApiContext();
	public static ApiCredential cred = apiContext.getApiCredential(); //
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;
	
	
	
	public SalesRecord(String Asin) throws SQLException {

		DatabaseOp Db = new DatabaseOp();
		EbayId = Db.GetEbayId(Asin);
		
		System.out.println("Constractor of Sales Record");
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
				}
				
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
					//System.out.println("eBay_token = "+eBay_token);
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
					//System.out.println("Server_url = "+Server_url);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
					//System.out.println("Application_id = "+Application_id);
				}
				//
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					//System.out.println("Site code --> US");
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					//System.out.println("Site code --> UK");
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
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

	
	public void SalesRecordMain(String Asin) throws InterruptedException, ParserConfigurationException, SAXException, IOException, SQLException, ParseException
	{
		
		System.out.println("Sale record getting information...");
		Code = BestMatchCode(EbayId);
		placeinlowestprice = Placeinlowestprice(Code);
		System.out.println("Place in lowest price = "+placeinlowestprice);
		Placeinbestmatch = PlaceInBestMatch(Code);
        System.out.println("Place in best match = "+Placeinbestmatch);
		placeinfeedbackamount = PlaceInFeedbackAmount(Code);
		System.out.println("Place in feedback amount = "+placeinfeedbackamount);
		differenceDays = (int)GetDaysForTheFirstSale(Code);
		System.out.println("differenceDays = "+differenceDays);
		

		// Here need to put the Sale through //
		
		try {

			AmazonCalls call = new AmazonCalls();
			Rank = call.Items_LookUp(Asin,"ASIN");
			System.out.println("Rank = "+Rank);
			call = null;
			System.gc();

		} catch (SQLException e) {e.printStackTrace();}
		
		try {SetRecord();} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	public int Placeinlowestprice(String Code)
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
		
		ItemFilter filter5 = new ItemFilter();
		filter5.setName(ItemFilterType.AVAILABLE_TO);
		filter5.getValue().add("US");
		request.getItemFilter().add(filter5);  
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add("US");
		request.getItemFilter().add(filter6);  

        List<OutputSelectorType> opSelector = request.getOutputSelector();
        opSelector.add(OutputSelectorType.SELLER_INFO);
        opSelector.add(OutputSelectorType.STORE_INFO);
		
		
        try
        {
        //System.out.println("Key word = "+Code);
        request.setKeywords(Code);

        request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);

        FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//

        System.out.println("Finditem =  " + result.getSearchResult().getCount());
        Ebayresults = result.getSearchResult().getCount();
        List<SearchItem> items = result.getSearchResult().getItem();
        
        int placeinlowestprice = 1;
        for (SearchItem ele:items)
        {
        	try{
        	System.out.println(ele.getStoreInfo().getStoreName());
        	if (ele.getStoreInfo().getStoreName().equals("ConfigFile"))
        	{
        		this.placeinlowestprice = placeinlowestprice;
        		break;
        	}
        	else placeinlowestprice++;
        	}catch(Exception e){}
        }
        }
        catch(Exception ex)//
        {	
        	System.out.println("Excption");
        	placeinlowestprice = -1;
        }

		return placeinlowestprice;	
	
		
	}
	
	public int PlaceInBestMatch(String Code)
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
		
		ItemFilter filter5 = new ItemFilter();
		filter5.setName(ItemFilterType.AVAILABLE_TO);
		filter5.getValue().add("US");
		request.getItemFilter().add(filter5);  
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add("US");
		request.getItemFilter().add(filter6);  

        List<OutputSelectorType> opSelector = request.getOutputSelector();
        opSelector.add(OutputSelectorType.SELLER_INFO);
        opSelector.add(OutputSelectorType.STORE_INFO);
		
		
        try
        {
       // System.out.println("Key word = "+Code);
        request.setKeywords(Code);

        request.setSortOrder(SortOrderType.BEST_MATCH);

        FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//

        //System.out.println("Finditem =  " + result.getSearchResult().getCount());

        List<SearchItem> items = result.getSearchResult().getItem();
        
        int Placeinbestmatch = 1;
        for (SearchItem ele:items)
        {
        	try{
        	System.out.println(ele.getStoreInfo().getStoreName());
        	if (ele.getStoreInfo().getStoreName().equals("ConfigFile"))
        	{
        		this.Placeinbestmatch = Placeinbestmatch;
        		break;
        	}
        	else Placeinbestmatch++;
        	}catch(Exception e){}
        }
        }
        catch(Exception ex)//
        {	
        	System.out.println("Excption");
        	Placeinbestmatch = -1;
        }

		return Placeinbestmatch;	
	}
	
	public int PlaceInFeedbackAmount(String Code)
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
		
		ItemFilter filter5 = new ItemFilter();
		filter5.setName(ItemFilterType.AVAILABLE_TO);
		filter5.getValue().add("US");
		request.getItemFilter().add(filter5);  
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add("US");
		request.getItemFilter().add(filter6);  

        List<OutputSelectorType> opSelector = request.getOutputSelector();
        opSelector.add(OutputSelectorType.SELLER_INFO);
        opSelector.add(OutputSelectorType.STORE_INFO);
		
		
        try
        {
       // System.out.println("Key word = "+Code);
        request.setKeywords(Code);

        request.setSortOrder(SortOrderType.BEST_MATCH);

        FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//

        //System.out.println("Finditem =  " + result.getSearchResult().getCount());

        List<SearchItem> items = result.getSearchResult().getItem();
        
        int placeinfeedbackamount = 1;
        long myfeedbackamount = -1;
        for (SearchItem ele:items)
        {
        	try{
        	//System.out.println(ele.getStoreInfo().getStoreName());
        	if (ele.getStoreInfo().getStoreName().equals("ConfigFile"))
        	{
        		myfeedbackamount = ele.getSellerInfo().getFeedbackScore();
        		break;
        	}
        	}catch(Exception e){
        		System.out.println("Excption");
        	}
        }
        
        for (SearchItem ele:items)
        {
        	try{
        	//System.out.println(ele.getStoreInfo().getStoreName());
        	if (ele.getSellerInfo().getFeedbackScore()>myfeedbackamount){placeinfeedbackamount++;}
        	}catch(Exception e){}
        }
        this.placeinfeedbackamount = placeinfeedbackamount;
        }
        catch(Exception ex)//
        {}

		return  placeinfeedbackamount;	

	}
	
	public String BestMatchCode(String EbayId)
	{

		ApiContext apiContext = new ApiContext();
	    ApiCredential cred = apiContext.getApiCredential(); //
	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(com.ebay.soap.eBLBaseComponents.SiteCodeType.US);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(EbayId);
		Call.setIncludeItemSpecifics(true);
		try{
		ItemType Item = Call.getItem();
		
		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		//System.out.println(Item.getItemSpecifics().getNameValueList(i).getValue(0));
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("UPC") && !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		Code = Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		else if (Item.getItemSpecifics().getNameValueList(i).getName().equals("ISBN")&& !Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("Does Not Apply"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName()+" = "+Item.getItemSpecifics().getNameValueList(i).getValue(0));
		Code =  Item.getItemSpecifics().getNameValueList(i).getValue(0);
		break;
		}
		
		}
		}catch(Exception e)
		{}

		return Code;	
	}
	
	public void SetRecord() throws SQLException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement_update = con.createStatement();


				try{
					String WriteToData;
					WriteToData = "INSERT INTO "+scham+".salesrecord (Asin,EbayId,Date,Placeinbestmatch,Placeinlowestprice,Placeinfeedbackamount,Ebayresults,Rank,DaysToFirstSale)"+
					"VALUES ('"+Asin+"','"+EbayId+"','"+dateFormat.format(date)+"',"+Placeinbestmatch+","+placeinlowestprice+","+placeinfeedbackamount+","+Ebayresults+","+Rank+","+differenceDays+");";
					statement_update.executeUpdate(WriteToData);
					}catch(SQLException e) //sdfs
					{
						System.out.println(e);
					}				
	}
	
	public long GetDaysForTheFirstSale(String Asin) throws SQLException, ParseException
	{
		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("select * from amazon.orders where AmazonAsin = '"+Asin+"';");
			res.last();
			if (res.getRow()==1)
			{
				String FirstSaleDate = res.getString("Sale_date");
				DateFormat format = new SimpleDateFormat("EEE MMM dd hh:kk:ss z yyyy", Locale.ENGLISH);
				Date date2 = format.parse(FirstSaleDate);
				int difference = (int) (date2.getTime() - GetDateByAsin(Asin).getTime());
				return difference / (1000 * 60 * 60 * 24);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public Date GetDateByAsin(String Asin) throws SQLException
	{
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.online where AmazonAsin = '"+Asin+"';");
			return res.getDate(7);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
