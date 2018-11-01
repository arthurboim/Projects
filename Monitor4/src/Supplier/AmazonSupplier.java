package Supplier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import Config.Config;
import Item.Item;
import WebDriver.JSOPWebDriver;
import WebDriver.SelenumWebDriver;
import WebDriver.WebDriverInterface;


public class AmazonSupplier implements SupplierInterface {
	
	static int numberOfItemsInCall = 9;
	private static int  NItemsInstock = 10;
	
	enum CallStatus
	{
	    SUCCESS, 
	    FAIL;
	}
	
	static enum StockStatusEnum
	{
		InStock,
		NotInStock,
		OnlyNLeftOrderSoon,
		OnlyNLeftOrderMoreOntheWay,
		UsuallyShipsWithinN,
		AvailableToShip,
		AvailableFromTheseSellers,
		OOS,
		PreOrder,
		Unknown
	};
	
	
	private String AWS_ACCESS_KEY_ID;
	private String AWS_SECRET_KEY;
	private String AssociateTag;
	private String AWSAccessKeyId;
	private String ENDPOINT;
	private ChromeDriver Driver;
	private String AmazonUser;
	private String AmazonPass;

	/* Contractors */
	
	public AmazonSupplier() {
		try {
			ReadFileConfigurations(null);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public AmazonSupplier(String KeysFilePath)  {
		try {
			ReadFileConfigurations(KeysFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public AmazonSupplier(List<Item> ListOfItems)
	{
		try {
			ReadFileConfigurations(null);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
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
				if (sCurrentLine.contains("AWS_ACCESS_KEY_ID:"))
				{
					AWS_ACCESS_KEY_ID = sCurrentLine.substring(sCurrentLine.indexOf("AWS_ACCESS_KEY_ID:")+19);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
				}
				
				if (sCurrentLine.contains("AssociateTag:"))
				{
					AssociateTag = sCurrentLine.substring(sCurrentLine.indexOf("AssociateTag: ")+"AssociateTag: ".length());
				}
				
				if (sCurrentLine.contains("AWSAccessKeyId:"))
				{
					AWSAccessKeyId = sCurrentLine.substring(sCurrentLine.indexOf("AWSAccessKeyId: ")+"AWSAccessKeyId: ".length());
				}

				if (sCurrentLine.contains("Acid: ")) 
				{
					AmazonUser = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ")+ "Acid: ".length());
				}
				
				if (sCurrentLine.contains("APass: ")) 
				{
					AmazonPass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+ "APass: ".length());
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


	
	
	
	/* Interface implementations */
	
	/*
	 * PORPUSE	  : Update all the items given in the list
	 * 
	 * DESCRIPTION:	Make an API call for all items given in the ArrayList.
	 * 
	 * INPUT	  :	ArrayList of Items.
	 * 
	 * RETURN	  : NONE.
	 * 
	 * Test status: NOT Tested.
	 */
	
	@Override
	public void GetItemsUpdate(List<Item> ListOfItems) 
	{
		UpdateItemViaWebSelenum(ListOfItems);
	}
	
	
	
	
	
	
	/* Inner use functions */
	
	/*------------------------------ AMAZON WEB ------------------------------*/

	
	protected void UpdateItemViaWebSelenum(List<Item> listOfItems)
	{
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		Driver = new ChromeDriver();
		
		Login(Driver);
		
		for(Item ele:listOfItems)
		{
			System.out.println("Item number: "+listOfItems.indexOf(ele));
			Driver.get("https://www.amazon.com/dp/"+ele.getSupplierCode()+"/");
			
			//ele.setInStock(GetInstock(Driver,ele));
			ele.setTitle(GetTitle(Driver));
			
			Driver.get("https://www.amazon.com/gp/offer-listing/"+ele.getSupplierCode()+"/ref=olp_f_new?ie=UTF8&f_all=true&f_new=true&f_primeEligible=true");
			ele.setCurrentTax(GetTax(Driver));
			ele.setCurrentPrice(GetPrice(Driver));
			if (ele.getCurrentTax() == -1 || ele.getCurrentPrice() == -1)
			{
				ele.setInStock(false);
			}else
			{
				ele.setInStock(true);

			}
		}
		
		Driver.close();
		Driver.quit();
	}
	
	private void Login(ChromeDriver Driver)
	{
		Driver.get("https://www.amazon.com/");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElement(By.id("nav-link-accountList")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElementByXPath("//*[@id='ap_email']").click();
		Driver.findElementByXPath("//*[@id='ap_email']").clear();
		Driver.findElementByXPath("//*[@id='ap_email']").sendKeys(AmazonUser);
		Driver.findElementByXPath("//*[@id='continue']").click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Driver.findElementByXPath("//*[@id='ap_password']").click();
		Driver.findElementByXPath("//*[@id='ap_password']").sendKeys(AmazonPass);
		Driver.findElementByXPath("//*[@id='signInSubmit']").click();
	}
	
	private boolean GetInstock(ChromeDriver Driver, Item ele)
	{
		IsPrime(Driver,ele);
		List<WebElement> Webelements = Driver.findElements(By.id("availability"));
		for(WebElement ele1:Webelements)
		{
			if ( InStockFilter(ele1.getText()) == true)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean InStockFilter(String text)
	{
		StockStatusEnum StockStatus =  null;
		StockStatus = StockStatusEnum.Unknown;
		boolean status = false;
		int N = -1;
		
		if (text.contains("In Stock"))
		{
			StockStatus = StockStatusEnum.InStock;
		}else if(text.contains("Only") && text.contains("left in stock") && text.contains("more on the way"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderMoreOntheWay;
		}else if (text.contains("Only") && text.contains("left in stock") && text.contains("order soon"))
		{
			StockStatus = StockStatusEnum.OnlyNLeftOrderSoon;
		}else if(text.contains("Usually ships within"))
		{
			StockStatus = StockStatusEnum.UsuallyShipsWithinN;
		}else if(text.contains("Available to ship in"))
		{
			StockStatus = StockStatusEnum.AvailableToShip;
		}else if(text.contains("Available from these sellers"))
		{
			StockStatus = StockStatusEnum.AvailableFromTheseSellers;
		}else if(text.contains("Temporarily out of stock"))
		{
			StockStatus = StockStatusEnum.OOS;
		}else if (text.contains("This title will be released on"))
		{
			StockStatus = StockStatusEnum.PreOrder;
		}else if (text.contains("Currently unavailable"))
		{
			StockStatus = StockStatusEnum.OOS;
		}

		if (StockStatusEnum.Unknown == StockStatus)
		{
			System.out.println("Parsing failes...");
			System.out.println("Text = \n"+text);
		}
		
		switch (StockStatus)
		{
			case InStock:
				status = true;
				break;
				
			case OnlyNLeftOrderMoreOntheWay:  
				text = text.substring(text.indexOf("Only ")+"Only ".length(), text.indexOf(" left in stock (more on the way)."));
				N = Integer.parseInt(text);
				if (N < NItemsInstock )
				{
					status = false;
				}else
				{
					status = true;
				}
				break;
				
			case OnlyNLeftOrderSoon:
				text = text.substring(text.indexOf("Only ")+"Only ".length(), text.indexOf(" left in stock - order soon."));
				N = Integer.parseInt(text);
				if (N < NItemsInstock )
				{
					status = false;
				}else
				{
					status = true;
				}
				break;
				
			case UsuallyShipsWithinN:
					status = false;
				break;
				
			case NotInStock:
					status = false;
				break;
				
			case AvailableToShip:
				status = false;
				break;
			case AvailableFromTheseSellers:
				status = false;
				break;
			case OOS:
				status = false;
				break;
			case PreOrder:
				status = false;
				break;
			case Unknown:
					status = false;
				break;
		}
		
		return status;		
	}
	
	private boolean IsPrime(ChromeDriver Driver,Item ele)
	{
		boolean status = false;
		List<WebElement> Webelements = Driver.findElements(By.xpath("//*[@id='priceBadging_feature_div']/i"));
		if (Webelements.size() > 0)
		{
			if (Driver.findElement(By.xpath("//*[@id='priceBadging_feature_div']/i")).getAttribute("class").equals("a-icon a-icon-prime"))
			{
				status = true;
			}else
			{
				status = false;
			}
		}
		ele.setPrime(status); 
		
		return status;
	}
	
	private double GetPrice(ChromeDriver driver)
	{
		try{
			return Double.parseDouble(driver.findElementByXPath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/span[1]").getText().replace("$",""));
		}catch(Exception e)
		{
			return -1;
		}
	}
	
	private double GetTax(ChromeDriver driver)
	{
		try{
		String CurrenteTax = driver.findElement(By.xpath("//*[@id='olpOfferList']/div/div/div[2]/div[1]/p/span/span")).getText();
		CurrenteTax = CurrenteTax.substring(CurrenteTax.indexOf("+")+3, CurrenteTax.indexOf("estimated tax")-1);
		return Double.parseDouble(CurrenteTax);
		}catch(Exception e)
		{
			return -1;
		}
	}
	
	private String GetTitle(ChromeDriver Driver)
	{
		List<WebElement> Webelements = new ArrayList<WebElement>();
		
		Webelements = Driver.findElements(By.cssSelector("span[id='productTitle']"));
		for(WebElement ele:Webelements)
		{
			if (ele.getAttribute("id").equals("productTitle"))
			{
				return ele.getText();
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	/*------------------------------ AMAZON API ------------------------------*/
	
	protected void UpdateItemViaAPI(ArrayList<Item> ListOfItems)
	{
		int numberOfRuns =0; 
		int i,attampt=0;
		String Codes = null;
		Map<String, String> params = new HashMap<String, String>();
		
		do
		{
			numberOfRuns = (RunUntillIndex(ListOfItems)/(numberOfItemsInCall+1));
			for(i=0;numberOfRuns>0;i+=(numberOfItemsInCall+1))
			{
				Codes = BuildCodesString(ListOfItems,i,i+numberOfItemsInCall);
				InitAmazonGetItemApiCall(Codes,params);
				try {
					AmazonLookUpCall(params,ListOfItems,i,i+numberOfItemsInCall);
				} catch (Exception e) {
					System.out.println(e.toString());
					
					try {
						System.out.println("Wating...");
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				numberOfRuns--;
			}
			SortItemsListByPrice(ListOfItems);

			if (RunUntillIndex(ListOfItems)<10)
			{
				Codes = BuildCodesString(ListOfItems,0,RunUntillIndex(ListOfItems));	
				InitAmazonGetItemApiCall(Codes,params);
				try {
					AmazonLookUpCall(params,ListOfItems,0,RunUntillIndex(ListOfItems));
				} catch (Exception e) 
				{
					System.out.println(e.toString());
				}
				SortItemsListByPrice(ListOfItems);
			}
			
			attampt++;
			//Manager.PrintItems(ListOfItems);
			System.out.println("attampt = " +attampt);
		}while(CheckIfAllItemsUpdated(ListOfItems) == false && attampt<20);
		
	
	}
	
	/*
	 * PORPUSE	  : Initialize Amazon API Call.
	 * 
	 * DESCRIPTION:	Initialize Amazon API Call by setting keys.
	 * 
	 * INPUT	  :	String Codes, Map<String, String> params.
	 * 
	 * RETURN	  : NONE.
	 * 
	 * Test status: Not Tested.
	 */
	
	protected void InitAmazonGetItemApiCall(String Codes, Map<String, String> params)
	{
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("AWSAccessKeyId", AWSAccessKeyId);
        params.put("AssociateTag", AssociateTag);
        params.put("ItemId", Codes);
        params.put("IdType", "ASIN"); 
        params.put("ResponseGroup", "ItemAttributes,Offers");
	}
	
	/*
	 * PORPUSE	  : Make an amazon call for N items.
	 * 
	 * DESCRIPTION:	Make an amazon call for N items from startIndex to endIndex.
	 * 
	 * INPUT	  :	ArrayList of Items, start index and end index.
	 * 
	 * RETURN	  : CallStatus.
	 * 
	 * Test status: Not Tested.
	 */

	protected CallStatus AmazonLookUpCall(Map<String, String> params, ArrayList<Item> List ,int startIndex, int endIndex) throws ParserConfigurationException, SAXException, IOException
	{
			CallStatus status = CallStatus.SUCCESS;
			String requestUrl = null;
			SignedRequestsHelper helper = null;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        try {
				Thread.sleep(0);
			} catch (InterruptedException e1)
	        {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return CallStatus.FAIL;
			}
	        
	        requestUrl = helper.sign(params);
	        status = Parser(requestUrl,List,startIndex, endIndex);
	        
	        
	        return status;
	}
	
	/*
	 * PORPUSE	  : Parse the URL.
	 * 
	 * DESCRIPTION:	Calling to parsing function.
	 * 
	 * INPUT	  :	URL from amazon, List of items to update, index indication.
	 * 
	 * RETURN	  : CallStatus.
	 * 
	 * Test status: Not Tested.
	 */
	
	protected CallStatus Parser(String requestUrl,ArrayList<Item> listOfItems,int startIndex, int endIndex) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(requestUrl).openStream());
        NodeList Nodelist =  doc.getElementsByTagName("Item");
        int initIndex = startIndex;
        
        try{
        /* Check if call is valid */
        if(CallStatus.FAIL == CheckFailMessage(doc))
        {
        	return CallStatus.FAIL;
        }
        
        }catch(Exception e)
        {
        	try {
				System.out.println("Watting...");
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
        	return CallStatus.FAIL;
        }
    
        /* Parsing data */
        
        for(;startIndex < endIndex;++startIndex)
        {
        	GetAsin(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	GetSalesRank(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	IsPrime(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	Instock(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	IsPreorder(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	GetPrice(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        	GetSalePrice(Nodelist,listOfItems.get(startIndex),startIndex-initIndex);
        }
        
		return CallStatus.SUCCESS;
	}

	/*
	 * PORPUSE	  : Check if API call succeed.
	 * 
	 * DESCRIPTION:	Check if API call succeed.
	 * 
	 * INPUT	  :	Document doc.
	 * 
	 * RETURN	  : CallStatus.
	 * 
	 * Test status: Not Tested.
	 */
	
	protected CallStatus CheckFailMessage(Document doc) throws ParserConfigurationException, SAXException, IOException
	{
//		int i=0;
		NodeList List = doc.getElementsByTagName("Error");
		
//		for(;i<List.getLength();i++)
//		{
//			System.out.println(List.item(i).getTextContent());
//		}
		
		if (List.getLength()>0)
		{
			return CallStatus.FAIL;
		}
		
		return CallStatus.SUCCESS;
	}
	
	public void bulkfetch(String requestUrl,ArrayList<Item> List,int startIndex, int endIndex) throws ParserConfigurationException, SAXException, IOException
	{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(requestUrl);
        NodeList Nodelist =  doc.getElementsByTagName("Item");
        
        for(int i=0;i<Nodelist.getLength();i++)
        {
	        try{
	        	Item item = new Item();
	            GetAsin(Nodelist,item,i);
		        GetSalesRank(Nodelist,item,i);
		        IsPrime(Nodelist,item,i);
		        Instock(Nodelist,item,i);
        	}catch(Exception e){}
        } 
	}
	
    protected  void GetAsin(NodeList list, Item item,int i)
    {
    	try{
    	Element ele = (Element)list.item(i);
    	NodeList Nodelist =  ele.getElementsByTagName("ASIN");
    	item.setSupplierCode(Nodelist.item(0).getTextContent());
    	}catch(Exception e)
    	{
    		item.setSupplyerRequestSuccess(false);
    	}
    	
		return;
    }
	  
    protected  void GetSalesRank(NodeList list,Item item,int i)
    {
		try{									
		Element ele = (Element)list.item(i);
		NodeList Nodelist =  ele.getElementsByTagName("SalesRank");
		item.setItemRank(Integer.parseInt(Nodelist.item(0).getTextContent()));
		}catch(Exception e)
		{
    		item.setSupplyerRequestSuccess(false);
		}
		
		return;
    }
    
    protected  void IsPrime(NodeList list,Item item,int i)
    {
    	try{
			Element ele = (Element)list.item(i);
			NodeList Nodelist =  ele.getElementsByTagName("IsEligibleForPrime");
	        if (Nodelist.item(0).getTextContent().equals("1")) 
	        {
	        	item.setPrime(true);
	        }
	        else 
	        {
	        	item.setPrime(false);
	        }
    	}catch(Exception e)
    	{
        	item.setPrime(false);
    		item.setSupplyerRequestSuccess(false);
    	}
    	
		return;
    }

    protected  void Instock(NodeList list,Item item,int i)
    {
    	try{
	    	Element ele = (Element)list.item(i);
	    	NodeList Nodelist =  ele.getElementsByTagName("AvailabilityType");
	        if (Nodelist.item(0).getTextContent().equals("now")) 
	        {
	        	item.setInStock(true);
	        }
	        else 
	        {
	        	item.setInStock(false);
	        }
    	}catch(Exception e)//
    	{
    		item.setInStock(false);
    		item.setSupplyerRequestSuccess(false);
    	}
    	
		return;
    }
    
    protected  void IsPreorder(NodeList list,Item item,int i)
    {
	    Element ele = (Element)list.item(i);
		Node node;
		
		try{
			NodeList Nodelist =  ele.getElementsByTagName("AvailabilityAttributes");
			node = Nodelist.item(0).getFirstChild();
			item.setPreorder(false);
			while(node.getNextSibling()!=null)
			{
				if(node.getNodeName() == "IsPreorder")
				{
			    	item.setPreorder(true);	
			    	break;
				}
				node = node.getNextSibling();
			}
		}catch(Exception e)
		{
    		item.setSupplyerRequestSuccess(false);
		}
		
		return;
    }
    
    protected  void GetPrice(NodeList list, Item item,int i)
    {

    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("Price");
    	Node node = Nodelist.item(0).getFirstChild();
    	String sPrice = null;
    	while(node != null)
    	{
    		if (node.getNodeName().equals("FormattedPrice"))
    		{
    			sPrice = node.getTextContent();
    			sPrice = sPrice.replace("$", "");
    			item.setPrice(Double.parseDouble(sPrice));
    			break;
    		}
    		node = node.getNextSibling();
    	}

    	}catch(Exception e)
    	{
    		item.setSupplyerRequestSuccess(false);
    	}
		return;
    }
    
    protected  void GetSalePrice(NodeList list, Item item,int i)
    {
    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("SalePrice");
    	Node node = Nodelist.item(0).getFirstChild();
    	String sPrice = null;
    	while(node != null)
    	{
    		if (node.getNodeName().equals("FormattedPrice"))
    		{
    			sPrice = node.getTextContent();
    			sPrice = sPrice.replace("$", "");
    			item.setPrice(Double.parseDouble(sPrice));
    			break;
    		}
    		node = node.getNextSibling();
    	}

    	}catch(Exception e)
    	{
    		item.setSupplyerRequestSuccess(false);
    	}
		return;
    }
    
    protected void SetAllCallSuccessStatus(ArrayList<Item> listOfItems)
    {
    	for(Item ele:listOfItems)
    	{
    		ele.setSupplyerRequestSuccess(true);
    	}
    }
    
  
	/*
	 * PORPUSE	  : Generate one string of codes
	 * 
	 * DESCRIPTION:	Generate one string of codes
	 * 				for using in the API(Max 10 codes in one call).
	 * 
	 * INPUT	  :	ArrayList of Items, start index and end index.
	 * 
	 * RETURN	  : String of codes.
	 * 
	 * Test status: Tested.
	 */
	protected  static String BuildCodesString(ArrayList<Item> ListOfItems, int startIndex, int endIndex)
	{
		String codeString = new String();
		
		for(;startIndex<=endIndex;startIndex++)
		{
			codeString+=ListOfItems.get(startIndex).getSupplierCode();
			codeString+=',';
		}
		
		codeString = codeString.substring(0, codeString.length()-1);
		
		return codeString;
	}
	
	protected  boolean CheckIfAllItemsUpdated(ArrayList<Item> List)
	{
		int counter = 0;
		for(Item  ele:List)
		{
			if(ele.getPrice() == -1)
			{
				counter++;
			}
		}
		
		if ( counter >0 )
		{
			System.out.println("Left = "+counter);
			return false;
		}
		
		System.out.println("Left = "+counter);
		
		return true;
	}
	
	protected void SortItemsListByPrice(ArrayList<Item> List)
	{
		Collections.sort(List,new CustomComparator());
	}
	
	public  class CustomComparator implements Comparator<Item> {
	    @Override
	    public int compare(Item o1, Item o2) {
	        return (int) (o1.getPrice() - o2.getPrice());
	    }
	}
	
	protected int RunUntillIndex(ArrayList<Item> List)
	{
		int i = 0;
		while(i < List.size() && List.get(i).getPrice() == -1)
		{
			i++;
		}
		
		return i;
	}
	
	
	
	
	
	
	/* Getters and setters */
	
	public String getAWS_ACCESS_KEY_ID() {
		return AWS_ACCESS_KEY_ID;
	}

	public String getAWS_SECRET_KEY() {
		return AWS_SECRET_KEY;
	}

	public String getENDPOINT() {
		return ENDPOINT;
	}


}
