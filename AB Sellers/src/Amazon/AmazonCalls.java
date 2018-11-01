package Amazon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;



public class AmazonCalls implements Runnable {

	public static Connection con ;
	public static java.sql.Statement statement_update ;
	public static String FILENAMEIPS = null;
	public static java.sql.Statement statement ;
	public static String Connection = null;
	public static String scham = null;
	public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public static String AWS_SECRET_KEY_FROM_FILE = null;
	public static String ENDPOINT_FROM_FILE = null;
	public static ApiContext apiContext;
	public static  ApiCredential cred;
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;
	public static  String user = null;
	public static  String pass = null;
	public static double  Amazon_Price = -1;
	public static String webdriverPath = null;
		
	public AmazonCalls() {



		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("FILENAMEIPS:"))
				{
					FILENAMEIPS = sCurrentLine.substring(sCurrentLine.indexOf("FILENAMEIPS:")+13);
				
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
				
				}

				if (sCurrentLine.contains("APass: "))
				{
					pass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+"APass: ".length());
				
				}
				
				if (sCurrentLine.contains("Acid: "))
				{
					user = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ")+"Acid: ".length());
				
				}
				if (sCurrentLine.contains("webdriver.chrome.driver:"))
				{
					webdriverPath = sCurrentLine.substring(sCurrentLine.indexOf("webdriver.chrome.driver:")+"webdriver.chrome.driver: ".length());
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
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
			
				}
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
		} catch (IOException e) {} 
		finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {}
			}
	
	
	}

	public synchronized String Items_LookUp(String Code, String CodeType) throws InterruptedException, ParserConfigurationException, SAXException, IOException, SQLException
	{


	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	    final String AWS_ACCESS_KEY_ID = AWS_ACCESS_KEY_ID_FROM_FILE;

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	    final String AWS_SECRET_KEY = AWS_SECRET_KEY_FROM_FILE;

	    /*
	     * Use the end-point according to the region you are interested in.
	     */
	    final String ENDPOINT = ENDPOINT_FROM_FILE;



	        /*
	         * Set up the signed requests helper.
	         */
	        SignedRequestsHelper helper = null;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {}

	        String requestUrl = null;
	        //Online_product product = new Online_product();
	        
	        Map<String, String> params = new HashMap<String, String>();
	        //ArrayList<product> List = new ArrayList<product> ();
	        Thread.sleep(500);
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAIN5OVJJQ2PRPII2Q");
	        params.put("AssociateTag", "choices29-20");
	        params.put("ItemId", Code);
	        params.put("IdType", CodeType);
	        params.put("ResponseGroup", "Offers,SalesRank,OfferListings,OfferSummary");
	        params.put("Condition", "New");
	        params.put("SearchIndex", "All");
	       

	        	requestUrl = helper.sign(params);
	        	return fetch(requestUrl,Code);

	}

    private static String fetch(String requestUrl,String Code) throws ParserConfigurationException, SAXException, IOException, SQLException 
    {
    		con = DriverManager.getConnection(Connection,"root","root");
    		statement_update = con.createStatement();
    		String Fatching_status = "Fetching success";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
        	NodeList Nodes = doc.getElementsByTagName("Item");
        //	int prime = 0;
        	
   
        	
        	//prime = IsEligibleForPrime(doc,Code,statement_update);
        	if (Nodes.getLength()>0)
        	{
        		AmazonAvailable(Code,statement_update);
            	if (GetAsin(doc,Code,statement_update).equals("Ok")){}
                	//System.out.println("GetSalesRank = Ok\n");
                    else 
                    {
                    	Fatching_status = "GetSalesRank Error";
                    	//System.out.println(Fatching_status);
                    }
            	
                if (GetSalesRank(doc,Code,statement_update).equals("Ok")){}
                	//System.out.println("GetSalesRank = Ok\n");
                    else 
                    {
                    	Fatching_status = "GetSalesRank Error";
                    	//System.out.println(Fatching_status);
                    }
                
                if (GetOfferListing(doc,Code,statement_update).equals("Ok"))
                	System.out.println("GetOfferListing = Ok\n");
                    else 
                    {
                    	Fatching_status = "GetOfferListing Error";
                    	System.out.println(Fatching_status);
                    }
        	}else return "No item";
        	con.close();
        	return Fatching_status;
    }

    public static int CheckMessage(Document doc)
    {
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("Message");
    	Node node = Nodelist.item(0).getFirstChild();
    	if (node.getTextContent().contains("You are submitting requests too quickly.")) 
    	{
    		return -1;
    	}
    	}catch(Exception e)
    	{
    		return 1;
    	}
    	
		return 1;
    }

    
    
    public static int IsEligibleForPrime(Document doc,String Code,java.sql.Statement statement_update)
    {
    	
    	try{ //
    	
    	//System.out.println("PrimeLength  = "+doc.getElementsByTagName("IsEligibleForPrime").getLength());
    	for (int i=0;i<doc.getElementsByTagName("IsEligibleForPrime").getLength();i++)
    	{
            //System.out.println("Prime"+i+" = "+doc.getElementsByTagName("IsEligibleForPrime").item(i).getTextContent());

        if (doc.getElementsByTagName("IsEligibleForPrime").item(i).getTextContent().equals("1")) {return 1;}
        
    	}
    	 return 0;
    	}catch(Exception e){
    		//System.out.println("Not prime for this "+Code);
    		return 0;}
    }

    public static String GetSalesRank(Document doc,String Code,java.sql.Statement statement_update)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("SalesRank");
    	Node node = Nodelist.item(0).getFirstChild();

    	try{
    	String WriteToData;
    	WriteToData = "UPDATE amazon.productfromsellers SET Rank = "+Integer.parseInt(node.getNodeValue())+" where Code = '"+Code+"';";		  
    	statement_update.executeUpdate(WriteToData);	
    	}
    	catch(SQLException e)
    	{}			
    	
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
    
    public static String GetAsin(Document doc,String Code,java.sql.Statement statement_update)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("ASIN");
    	Node node = Nodelist.item(0).getFirstChild();

    	try{
    	String WriteToData;
    	WriteToData = "UPDATE amazon.productfromsellers SET ASIN = '"+node.getNodeValue()+"' where Code = '"+Code+"';";		  
    	statement_update.executeUpdate(WriteToData);	
    	}
    	catch(SQLException e)
    	{}			
    	
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
    
    public static String GetOfferListing(Document doc,String Code,java.sql.Statement statement_update)
    {
    	double Price = -1;
    	String Status = "Ok";
    	try{
    	NodeList Nodelist = doc.getElementsByTagName("OfferListing");
    	Node node = Nodelist.item(0).getFirstChild();
    	while (node!= Nodelist.item(0).getLastChild())
       	{
       	if (node.getNodeName().equals("SalePrice"))
       	{
       	String price_temp = null;
       	Element e = (Element) node;
       	NodeList  list_temp  = e.getElementsByTagName("FormattedPrice");
       	price_temp = list_temp.item(0).getTextContent();
       	price_temp = price_temp.replace("$","");
       	Price = Double.parseDouble(price_temp);
       	}else if (node.getNodeName().equals("Price"))
       	{
           	String price_temp = null;
           	Element e = (Element) node;
           	NodeList  list_temp  = e.getElementsByTagName("FormattedPrice");
           	price_temp = list_temp.item(0).getTextContent();
           	price_temp = price_temp.replace("$","");
           	Price = Double.parseDouble(price_temp);
           	Amazon_Price = Price;
       	}

       	node = node.getNextSibling();
       	}
    	}catch(Exception e){
    		//System.out.println(e);
    		Status = "Error";
    		}
    		
    	try{
    		System.out.println("Item price = "+Price);
        	String WriteToData;
        	WriteToData = "UPDATE amazon.productfromsellers SET Amazon_price = "+Price+" where Code = '"+Code+"';";		  
        	statement_update.executeUpdate(WriteToData);	
        	}
        	catch(SQLException e)
        	{}		
    	
    	
    	
    	
		return Status;
    }
    
    public static void AmazonAvailable(String Code,java.sql.Statement statement_update)
    {
     	try{
     		con = DriverManager.getConnection(Connection,"root","root");
     		statement_update = con.createStatement();//
        	String WriteToData;
        	WriteToData = "UPDATE amazon.productfromsellers SET AmazonAvailavle = "+1+" where Code = '"+Code+"';";		  
        	statement_update.executeUpdate(WriteToData);	
        	con = null;
        	statement_update = null;
        	System.gc();
        	}
        	catch(SQLException e){System.out.println(e);}		
    }
    
    public static void DeleteItem(String Code,java.sql.Statement statement_update)
    {
     	try{
        	String WriteToData;
        	WriteToData = "DELETE FROM "+scham+".productfromsellers WHERE Code='"+Code+"';";		  
        	statement_update.executeUpdate(WriteToData);
         	WriteToData = null;
         	statement_update = null;
         	//System.out.println("Item deleted");
         	System.gc();
        	}
        	catch(SQLException e)
        	{System.out.println(e);}	
    }

    public static void GetItemsToUpdate(ArrayList<UpdateStract> list)
    {
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement_update = con.createStatement();//
			ResultSet res = statement_update.executeQuery("SELECT * FROM productfromsellers where AmazonAvailavle = 0;");
			while (res.next())
			{
				UpdateStract Stract = new UpdateStract();
				Stract.Code = res.getString("Code");
				Stract.CodeType = res.getString("CodeType");
				list.add(Stract);
			}
			con = null;
			res = null;
			System.gc();
		} catch (SQLException e) {
			
		}
    }
    
	public static void SetItemsToUpdate(UpdateStract ele) throws SQLException
	{

		con = DriverManager.getConnection(Connection,"root","root");
		statement_update = con.createStatement();
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".productfromsellers SET asin = '"+ele.asin+"' , Amazon_price = "+ele.AmazonPrice+", AmazonAvailavle = 1 , placeinlowestprice = "+ele.placeinlowestprice+" , BreakEvenForlowest = "+(ele.EbayLowestprice)/(ele.AmazonPrice+ele.Tax+0.3)+" , arbitrajeLowestprice = "+ele.arbitrajelowestprice+" , EbayLowestprice = "+(ele.EbayLowestprice)+" , DiffrenceInLowestPricesPercent  = "+ele.DiffrenceInLowestPricesPercent+" , EbaySecondLowestprice = "+ele.EbaySecondLowestPrice+" ,Tax = "+ele.Tax+" where Code = '"+ele.Code+"';";		  
			System.out.println(WriteToData);
			statement_update.executeUpdate(WriteToData);
			}
			catch(SQLException e){System.out.println(e);}		
		con = null;
	}
    
	public static double GetItemPrice(String Code,String CodeType) throws SQLException
	    {
			con = DriverManager.getConnection(Connection,"root","root");
			statement_update = con.createStatement();//
			try {

				ResultSet res = statement_update.executeQuery("SELECT * FROM "+scham+".productfromsellers where Code = '"+Code+"' and CodeType = '"+CodeType+"';");
				res.next();
				return res.getDouble("Amazon_price");
			} catch (SQLException e) {
			}
		
			con = null;
			statement_update = null;
			System.gc();
			return 0;
	    }
	    
	public static void EbayUpdate(UpdateStract UpdateStract)
	{
//
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
		    		filter5.getValue().add(Contry);
		    		request.getItemFilter().add(filter5);  
		    		
		    		
		    		ItemFilter filter6 = new ItemFilter();
		    		filter6.setName(ItemFilterType.LOCATED_IN);
		    		filter6.getValue().add(Contry);
		    		request.getItemFilter().add(filter6);  


		            try
		            {
		     
		            request.setKeywords(UpdateStract.Code);

		            request.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);

		            FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);//
		            

		            List<SearchItem> items = result.getSearchResult().getItem();//
		            UpdateStract.EbayResultsAmount = items.size();
		          //  System.out.println("Ebay lowestprice = "+items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue());
		            UpdateStract.EbayLowestprice = items.get(0).getSellingStatus().getCurrentPrice().getValue()+items.get(0).getShippingInfo().getShippingServiceCost().getValue();
		           // UpdateStract.arbitrajelowestprice =  UpdateStract.EbayLowestprice - Min_price_to_sale(GetItemPrice(UpdateStract.Code,UpdateStract.CodeType));
		            //Amazon_Price
			        UpdateStract.arbitrajelowestprice =  UpdateStract.EbayLowestprice - Min_price_to_sale(UpdateStract.AmazonPrice);

		            if (UpdateStract.EbayResultsAmount>1)
		            {//
		            UpdateStract.EbaySecondLowestPrice = items.get(1).getSellingStatus().getCurrentPrice().getValue()+items.get(1).getShippingInfo().getShippingServiceCost().getValue();
		           
		            UpdateStract.DiffrenceInLowestPricesPercent = ((UpdateStract.EbaySecondLowestPrice-UpdateStract.EbayLowestprice)/UpdateStract.EbayLowestprice)*100;
		            }
		          

		            int counter = 1;
		            if (result.getAck().toString()!="FAILURE") 
		            {
		            for(SearchItem item : items) 
		            {
		            	if (item.getSellingStatus().getCurrentPrice().getValue()<Min_price_to_sale(UpdateStract.AmazonPrice))
		            		counter++;
		            	else 
		            		{
		            		UpdateStract.placeinlowestprice = counter;
		            		break;
		            		}
		            }//for
		            if (UpdateStract.placeinlowestprice==0) UpdateStract.placeinlowestprice = ++counter;
		            }//if 

		            }
		            catch(Exception ex){System.out.println(ex);}
		            
		    		
		
			
	}
	
	public static  double Min_price_to_sale(double price) {
		double ebay_fees = 0.09;
		double paypal_fees = 0.039;
		double paypal_fixed = 0.3;
		double my_percent = 1.06;
		return ((price * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}
	
	public void CheckWithAPI(UpdateStract ele ) throws InterruptedException, SQLException
	{

		try {
			int counter = 0;
			while(counter<2)
			{
				if (counter==0) ele.CodeType = "UPC";
				if (counter==1) ele.CodeType = "ISBN";
				System.out.println("Checking code type = "+ele.CodeType+" by value = "+ele.Code);
				if (Items_LookUp(ele.Code,ele.CodeType)!="No item") 
					{
					EbayUpdate(ele);
					SetItemsToUpdate(ele);
					break;
					}
				else counter++;
			}
			
			if (counter==2) 
			{
				//System.out.println("Item not found... delete");
				DeleteItem(ele.Code,statement_update);
			}
			
		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {

		} catch (IOException e) {

		}

	
	}
	
	public void CheckWithScraper(UpdateStract ele ,ChromeDriver driver) throws InterruptedException, SQLException
	{
		String Asin = null;
		try{
			ele.Code = ele.Code.replaceAll(" ", "");
			//System.out.println(ele.Code);
			driver.get("https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="+ele.Code+"");
			Asin = driver.findElementByXPath("//*[@id='result_0']/div/div/div/div[2]/div[1]/div/a").getAttribute("href");
			Asin = Asin.substring(Asin.lastIndexOf("dp/")+3, Asin.lastIndexOf("/ref="));
			driver.get("https://www.amazon.com/gp/offer-listing/"+Asin+"/ref=olp_f_new?ie=UTF8&f_all=true&f_new=true&f_primeEligible=true");
			ele.AmazonPrice = Double.parseDouble(driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]").getText().replace("$",""));
			
			try{
			String CurrenteTax = driver.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();
			//System.out.println("Asin = "+Asin+" Tax = "+CurrenteTax);
			CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+")+3, CurrenteTax.indexOf("estimated tax")-1);
			ele.Tax  = Double.parseDouble(CurrenteTax);
			}catch(Exception e)
			{
				
			}
			
			if (driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[2]/i").getAttribute("aria-label").contains("Eligible for free shipping with Amazon Prime."))
			{
				ele.asin = Asin;
				ele.prime = 1;
			}else 
			{
				ele.prime = 0;
			}


		}catch(Exception e){ele.prime = 0;ele.AmazonPrice = -1;};
														  //*[@id="olpOfferList"]/div/div/div[2]/div[1]/span[2]/i/span
	
	}
	
	
	
    @Override
	public void run() {
    	
    	ArrayList<UpdateStract> list = new ArrayList<UpdateStract> ();
    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--start-maximized");
    	System.out.println("Amazon running...");
    	System.setProperty("webdriver.chrome.driver", webdriverPath);
		ChromeDriver Driver = new ChromeDriver(options);
		
		// login here 
		Boolean success = Login(Driver);
		if (false == success)
		{
			Login2(Driver);
		}
			try {
				
				while (true)
				{
					list = new ArrayList<UpdateStract> ();
					GetItemsToUpdate(list);
					if (list!=null)
					{
						for (UpdateStract ele:list)
						{
							CheckWithScraper(ele,Driver);
							if (ele.prime==1)
							{
								EbayUpdate(ele);
								//System.out.println("Code = "+ele.Code);
								//System.out.println("Ebay lowestprice = "+ele.EbayLowestprice);
								//System.out.println("Amazon Price = "+ele.AmazonPrice);
								//System.out.println("Amazon Tax = "+ele.Tax);
								//System.out.println("Break even lowestprice = "+(ele.EbayLowestprice)/(ele.AmazonPrice+ele.Tax+0.3));
								if ((((ele.EbayLowestprice)/(ele.AmazonPrice+0.3)		 > 1.14)   && (ele.Tax == 0)) ||
									(((ele.EbayLowestprice)/(ele.AmazonPrice+ele.Tax+0.3) > 1.185) && (ele.Tax > 0)))
								{
									System.out.println("Item added Final");
									SetItemsToUpdate(ele);
								}else 
								{
									DeleteItem(ele.Code, statement_update);
								}
							}else 
							{
								DeleteItem(ele.Code, statement_update);
							}
						}
					}
					
						list = null;
						System.gc();
						//Thread.sleep(1000*60*3);
				}
			
			} catch (InterruptedException e) {

			} catch (SQLException e) {

			}
		
		
	}
    
	private boolean Login(ChromeDriver Driver)
	{
		Driver.get("https://www.amazon.com/");
		try {
			Thread.sleep(1000);
			Driver.findElement(By.id("nav-link-accountList")).click();
			Thread.sleep(500);
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
			Driver.findElementByXPath("//*[@id='continue']").click();
			Thread.sleep(2000);
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
}
	
	public void  Login2(ChromeDriver Driver)
	{
										
			Driver.findElementByXPath("//*[@id='ap_email']").click();
			Driver.findElementByXPath("//*[@id='ap_email']").clear();
			Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(user);
			//Driver.findElementByXPath("//*[@id='continue']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").click();
			Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(pass);
			Driver.findElementByXPath("//*[@id='signInSubmit']").click();

	}
    
}
