package Supplier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import Config.Config;
import Item.Item;
import Monitor.Manager;



public class AmazonSupplier implements SupplierInterface{

	 static int numberOfItemsInCall = 9;
	 
	enum CallStatus
	{
	    SUCCESS, 
	    FAIL;
	}
	
	private String AWS_ACCESS_KEY_ID;
	private String AWS_SECRET_KEY;
	private String AssociateTag;
	private String AWSAccessKeyId;
	private String ENDPOINT;
		
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
	public void GetItemsUpdate(ArrayList<Item> ListOfItems) 
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
						// TODO Auto-generated catch block
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
	
	
	
	/* Inner use functions */
	
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
					System.out.println("Watting...");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
        	//System.out.println(e.toString());
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
