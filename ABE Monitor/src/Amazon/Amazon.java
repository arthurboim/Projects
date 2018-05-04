package Amazon;

import java.io.IOException;
import java.util.ArrayList;
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

import Abstracts.Supplier;
import Database.Item;


public class Amazon extends Supplier{
	
	
	public void BulkInfoRequest(String Code,ArrayList<Item> List,int counter) throws ParserConfigurationException, SAXException, IOException
	{

	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	    final String AWS_ACCESS_KEY_ID = "AKIAJI6TKSBFN3ADN2LA";

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	    final String AWS_SECRET_KEY = "BgtiuFnLkwiD+gl+GizOIQe+glJqEET3KiRfvgyG";

	    /*
	     * Use the end-point according to the region you are interested in.
	     */
	    final String ENDPOINT = "webservices.amazon.com";

	        /*
	         * Set up the signed requests helper.
	         */
	        SignedRequestsHelper helper = null;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String requestUrl = null;

	        Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJI6TKSBFN3ADN2LA");
	        params.put("AssociateTag", "ubuythebest01-20");
	        params.put("ItemId", Code);
	        params.put("IdType", "ASIN"); 
	        params.put("ResponseGroup", "BrowseNodes,Images,ItemAttributes,Offers,SalesRank,EditorialReview");

	      
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				System.out.println("Sleeping error");
				return;
			}
	        
	        requestUrl = helper.sign(params);

	        	
	      bulkfetch(requestUrl, List,counter);


	}

	public void bulkfetch(String requestUrl,ArrayList<Item> List,int counter) throws ParserConfigurationException, SAXException, IOException
	{
		
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(requestUrl);
    
        for (int i=0;i<counter;i++)
        {
        Item item = new Item();
	        try{
		        GetAsin(doc,item,i);
		        GetItemAttributes(doc,item,i);
		        IsPrime(doc,item,i);
		        GetImageSets(doc,item,i);
		        Instock(doc,item,i);
		        GetShippingTime(doc,item,i);
		        IsNew(doc,item,i);
		        IsPreorder(doc,item,i);
		        GetTitle(doc,item,i);
		        GetItemFeature(doc,item,i);
		        GetContent(doc,item,i);

		        item.PathFolder = List.get(GetItemIndex(List,item.SupplierCode)).PathFolder;
		        List.set(GetItemIndex(List,item.SupplierCode), item);
	        }catch(Exception e)
	        {
	        	item.ReadyToUpload = false;
	        	item.AvailabilityType = "NOT AVAILABLE";
	        	System.out.println("BulkInfoRequest error");
	        }
        }
        doc = null;
        System.gc();
	   
	}
	
    public static String GetImageSets(Document doc,Item Item1,int i)
    {

    	String Status = "Ok";
        NodeList Node = doc.getElementsByTagName("Item");

        if (Node.getLength()==0) 
        {
        	System.out.println("Item lookup Fail");
        	return "Fail";
        }

        	Node Item = Node.item(i);
        	NodeList Childs  = Item.getChildNodes();
        	for(int j=0;j<Childs.getLength();j++)
        	{
    		if (Childs.item(j).getNodeName().equals("LargeImage"))
    		{
        		
            		Element e = (Element) Childs.item(j);
            		String[] TempImageLink = new String[1];
            		TempImageLink[0] = e.getTextContent();
            		/*System.out.println(TempImageLink[0]);*/
            		try{
            		TempImageLink[0] = TempImageLink[0].substring(0, TempImageLink[0].indexOf(".jpg")+4);
            		}catch(Exception e1)
            		{
            			System.out.println("substring error");
            		}
            		Item1.PicturesString[0] = TempImageLink[0];
    		}
        	}
        

		return Status;
    }
     
    public static String GetTitle(Document doc, Item item,int i)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("Title");
    	Node node = Nodelist.item(i).getFirstChild();

    	item.Title = node.getNodeValue();
    	}catch(Exception e)
    	{
        	item.AvailabilityType = "NOT AVAILABLE";
        	System.out.println("GetTitle Null");
        	item.ReadyToUpload = false;
    	}
		return Status;

    }
    
    public static String GetAsin(Document doc, Item item,int i)
    {
    	/**/
    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("ASIN");
    	Node node = Nodelist.item(i).getFirstChild();
    	item.SupplierCode = node.getNodeValue();
    	}catch(Exception e)
    	{
        	item.ReadyToUpload = false;
        	System.out.println("GetAsin Null");
        	Status = "Error";
    	}
		return Status;

    }
	
    public static String GetContent(Document doc, Item item,int i)
    {
    	int counter = 0;
    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("EditorialReview");
    	Node node = Nodelist.item(i).getFirstChild();
    	
    	while(node != null)
    	{
    		if (node.getNodeName().equals("Content"))
    		{
    			if (counter ==0)
    			{
    				item.Content = node.getTextContent();
    				counter++;
    			}else
    			{
    				item.Content = item.Content+ " " +node.getTextContent();
    				counter++;
    			}
    		}
    		node = node.getNextSibling();
    	}
    	System.out.println();
    	}catch(Exception e)
    	{
    		item.Content = null;
    		System.out.println("Content is "+e.getMessage());
    	}
		return Status;

    }
    
    public static String GetItemAttributes(Document doc,Item item,int i)
    {
    	NodeList Nodelist = null;
    	Node node = null;
    	String Status = "Ok";
    	
		try{
			Nodelist = doc.getElementsByTagName("ItemAttributes");
			
			node = Nodelist.item(i).getFirstChild();
			while(node.getNodeName()!=null)
			{
				if(node.getNodeName().equals("Brand"))
				{
					item.Brand = node.getTextContent();
					System.out.println(item.SupplierCode +" = "+item.Brand);
					break;
				}
				
				if(node.getNodeName() == "Author" || node.getNodeName() == "ISBN")
				{
					item.Brand = node.getTextContent();
					System.out.println(item.SupplierCode +" = "+item.Brand);
					break;
				}
		
				if(node.getNodeName().equals("Label"))
				{
					item.Brand = node.getTextContent();
					System.out.println("Label");
					System.out.println(item.SupplierCode +" = "+item.Brand);
					break;
				}
				
				node = node.getNextSibling();
			}
			
		}catch(Exception e1)
		{  
			System.out.println("Item attributes fail "+item.SupplierCode);
		}
    		
    	try{
    	Nodelist = doc.getElementsByTagName("EAN");
    	item.EAN = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Color");
    	item.Color = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Format");
    	item.Format = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Manufacturer");

    	item.Manufacturer = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Model");
    	item.Model = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("MPN");
    	item.MPN = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("NumberOfItems");
    	item.NumberOfItems = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PartNumber");
    	item.PartNumber = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Publisher");
    	item.Publisher = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("UPC");
    	item.UPC = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PackageQuantity");
    	item.PackageQuantity = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Size");
    	item.Size = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	

    	try{
    	Nodelist = doc.getElementsByTagName("NumberOfPages");
    	item.NumberOfPages = Nodelist.item(i).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	

    	try{
    	Nodelist = doc.getElementsByTagName("Language");
    	node = Nodelist.item(0).getFirstChild();
       	item.Language = node.getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
   
    	
    	try{
    	Nodelist = doc.getElementsByTagName("ManufacturerMinimumAge");
    	node = Nodelist.item(0).getFirstChild();
    	item.ManufacturerMinimumAge = node.getTextContent()+" months";
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("ManufacturerMaximumAge");
    	node = Nodelist.item(0).getFirstChild();
    	item.ManufacturerMaximumAge = node.getTextContent()+" months";
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PublicationDate");
    	node = Nodelist.item(i).getFirstChild();
    	item.PublicationDate = node.getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("OperatingSystem");
    	node = Nodelist.item(0).getFirstChild();
    	item.OperatingSystem = node.getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
 
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Warranty");
    	node = Nodelist.item(i).getFirstChild();
  
    	item.Warranty = node.getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}


		return Status;
    
    }
    
    public static String GetItemFeature(Document doc,Item item,int i)
    {
    	String Status = "Ok";
    	NodeList Nodelist = doc.getElementsByTagName("ItemAttributes");
    	Node node = Nodelist.item(i).getFirstChild();
    	try{
		while(node!=null)
		{
    		if (node.getNodeName().equals("Feature"))
    		{
    			item.Features.add(node.getTextContent());
    		}
    		
			node = node.getNextSibling();
		}

    	}catch(Exception e)
    	{
    		System.out.println("GetItemFeature Error");
    		System.out.println(e);
    		return "Error";
    	}
		return Status;
    }
    
    public static String GetShippingTime(Document doc,Item item,int i)
    {
    	String Status = "Ok";
    	try{//
        NodeList Nodelist =  doc.getElementsByTagName("AvailabilityAttributes");
        Node node =  Nodelist.item(i);
    	item.MinimumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getTextContent().toString());
    	item.MinimumDaysToShip /=24;
    	item.MaximumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getNextSibling().getTextContent());
    	item.MaximumDaysToShip /=24;
    	}catch(Exception e)
    	{
        	item.ReadyToUpload = false;
        	item.AvailabilityType = "NOT AVAILABLE";
        	System.out.println("GetShippingTime null");
    		return "Error";
    	}
		return Status;
    }

    public static String IsPrime(Document doc,Item item,int i)
    {
    	String Status = "Ok";
    	try{										   
        NodeList Nodelist =  doc.getElementsByTagName("IsEligibleForPrime");
        Node node =  Nodelist.item(i);
        if (node.getFirstChild().getTextContent().equals("1")) 
        	item.prime = true;
        else 
        	item.prime = false;
    	}catch(Exception e)
    	{
    		System.out.println("IsEligibleForPrime Null"); 
        	item.prime = false;
    		return "Error";
    	}
    	
		return Status;
    }

    public static String Instock(Document doc,Item item,int i)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("AvailabilityType");
        Node node =  Nodelist.item(i);
        if (node.getFirstChild().getTextContent().equals("now")) 
        {
        	item.AvailabilityType = "now";
        }
        else 
        {
        	item.AvailabilityType = "Not available";
        }
    	}catch(Exception e)
    	{
    		System.out.println("AvailabilityType null");
    		item.ReadyToUpload = false;
        	item.AvailabilityType = "NOT AVAILABLE";
    		return "Error";
    	}
		return "Ok";
		
    }
    
    public static String IsNew(Document doc,Item item,int i)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("OfferAttributes");
        Node node =  Nodelist.item(i);
        if (node.getFirstChild().getTextContent().equals("New")) 
        {
        		item.IsNew = true;
        }
        else 
        {
        	item.IsNew = false;
        }
        }catch(Exception e)
    	{
        	item.IsNew = false;
        	return "Error";
        }
    		return "Ok";
    }
    
    public static String IsPreorder(Document doc,Item item,int i)
    {

    	NodeList Nodelist = doc.getElementsByTagName("AvailabilityAttributes");
    	Node node;
    	node = Nodelist.item(i).getFirstChild();
    	
    	item.IsPreorder = false;
    	while(node.getNextSibling()!=null)
    	{
    		if(node.getNodeName() == "IsPreorder")
    		{
    	    	item.IsPreorder = true;
    	    	break;
    		}
    		node = node.getNextSibling();
    	}
    	
    	return "Ok";
    }
    
	public Item BuildItem(String Code)
	{
	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	    final String AWS_ACCESS_KEY_ID = "AKIAITQMX5ZZNOJOVJFA";

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	    final String AWS_SECRET_KEY = "Qch13q5UmXUsHlAexaEWPwehrCYYU+QuXPkgHBPs";

	    /*
	     * Use the end-point according to the region you are interested in.
	     */
	    final String ENDPOINT = "webservices.amazon.com";

	        /*
	         * Set up the signed requests helper.
	         */
	        SignedRequestsHelper helper = null;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String requestUrl = null;

	        Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJYGD72A2U4KSBUZQ");
	        params.put("AssociateTag", "bchoices-20");
	        params.put("ItemId", Code);
	        params.put("IdType", "ASIN"); 
	        params.put("ResponseGroup", "Images,ItemAttributes,Offers,SalesRank,EditorialReview");

	        System.out.println("Getting item "+Code);
	        try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try{
	        requestUrl = helper.sign(params);
	        Item item = new Item();
	        item.SupplierCode = Code;
	        if( fetch(requestUrl,item).equals("OK"));
	        return item;
	        }catch(Exception e){
	        	System.out.println("Item build fail");
	        	return null;
	        }
	     

	
	}
	
	public Item ItemLookUp(String Code , Item item) throws InterruptedException, ParserConfigurationException, SAXException, IOException
	{
	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	    final String AWS_ACCESS_KEY_ID = "AKIAITQMX5ZZNOJOVJFA";

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	    final String AWS_SECRET_KEY = "Qch13q5UmXUsHlAexaEWPwehrCYYU+QuXPkgHBPs";

	    /*
	     * Use the end-point according to the region you are interested in.
	     */
	    final String ENDPOINT = "webservices.amazon.com";

	        /*
	         * Set up the signed requests helper.
	         */
	        SignedRequestsHelper helper = null;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String requestUrl = null;

	        Map<String, String> params = new HashMap<String, String>();
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJYGD72A2U4KSBUZQ");
	        params.put("AssociateTag", "bchoices-20");
	        params.put("ItemId", Code);
	        params.put("IdType", "ASIN"); 
	        params.put("ResponseGroup", "Images,ItemAttributes,Offers,SalesRank");

	        System.out.println("Getting item "+Code);
	        Thread.sleep(4000);
	        requestUrl = helper.sign(params);

	        
	       if( fetch(requestUrl,item).equals("OK"));
	       return item;
	     

	
	}
	
    private static String fetch(String requestUrl, Item item) throws ParserConfigurationException, SAXException, IOException 
    {
		String Fatching_status = "Fetching success";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(requestUrl);

        
        
        if (GetTitle(doc,item).equals("Ok"))
          	System.out.println("GetTitle = Ok");
            else 
            {
            	Fatching_status = "GetTitle Error";
            	System.out.println(Fatching_status);
            }
        
        if (GetAsin(doc,item).equals("Ok"))
          	System.out.println("GetAsin = Ok");
            else 
            {
            	Fatching_status = "GetAsin Error";
            	System.out.println(Fatching_status);
            }
        
        
        if (GetImageSets(doc,item).equals("Ok"))
          	System.out.println("GetImageSets = Ok");
            else 
            {
            	Fatching_status = "GetImageSets Error";
            	System.out.println(Fatching_status);
            }
        
        if (GetItemAttributes(doc,item).equals("Ok"))
       	System.out.println("GetItemAttributes = Ok");
        else 
        {
        	Fatching_status = "GetItemAttributes Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetSalesRank(doc,item).equals("Ok"))
    	System.out.println("GetSalesRank = Ok");
        else 
        {
        	Fatching_status = "GetSalesRank Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetPackageDimensions(doc,item).equals("Ok"))
    	System.out.println("GetPackageDimensions = Ok");
        else 
        {
        	Fatching_status = "GetPackageDimensions Error";
        	System.out.println(Fatching_status);
        }
        
        
        if (GetContent(doc,item).equals("Ok"))
    	System.out.println("GetContent = Ok");
        else 
        {
        	Fatching_status = "GetContent Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetItemFeature(doc,item).equals("Ok"))
    	System.out.println("GetItemFeature = Ok");
        else 
        {
        	Fatching_status = "GetItemFeature Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetShippingTime(doc,item).equals("Ok"))
        System.out.println("GetShippingTime = Ok");
        else 
        {
        	Fatching_status = "GetShippingTime Error";
        	System.out.println(Fatching_status);
        }
        
        if (IsPrime(doc,item).equals("Ok"))
        	System.out.println("IsPrime = Ok");
        else 
        {
        	Fatching_status = "IsPrime Error";
        	System.out.println(Fatching_status);
        }
        
        if (Instock(doc,item).equals("Ok"))
       	System.out.println("Instock = Ok");
        else 
        {
        	Fatching_status = "Instock Error";
        	System.out.println(Fatching_status);
        }
        
     
        if (GetPrice(doc,item).equals("Ok"))
       	System.out.println("GetPrice = Ok");
        else 
        {
        	Fatching_status = "GetPrice Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetSalePrice(doc,item).equals("Ok"))
       	System.out.println("GetSalePrice = Ok");
        else 
        {
        	Fatching_status = "GetSalePrice Error";
        	System.out.println(Fatching_status);
        }
        
        if (IsNew(doc,item).equals("Ok"))
           	System.out.println("IsNew = Ok");
            else 
            {
            	Fatching_status = "IsNew Error";
            	System.out.println(Fatching_status);
            }
        
        if (IsPreorder(doc,item).equals("Ok"))
       	System.out.println("IsPreorder = Ok");
        else 
        {
        	Fatching_status = "IsPreorder Error";
        	System.out.println(Fatching_status);
        }
        
        if (GetContent(doc,item).equals("Ok"))
       	System.out.println("IsPreorder = Ok");
        else 
        {
        	Fatching_status = "IsPreorder Error";
        	System.out.println(Fatching_status);
        }
        
    	return Fatching_status;
    }
     
    public static String GetContent(Document doc, Item item)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("EditorialReviews");
    	Node node = Nodelist.item(0).getFirstChild().getFirstChild().getNextSibling();
    	item.Content = node.getTextContent();
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
    
    public static String GetTitle(Document doc, Item item)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("Title");
    	Node node = Nodelist.item(0).getFirstChild();

    	item.Title = node.getNodeValue();
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
    
    public static String GetAsin(Document doc, Item item)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("ASIN");
    	Node node = Nodelist.item(0).getFirstChild();
    	item.SupplierCode = node.getNodeValue();
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
      
    public static String GetOfferListing(Document doc,Item item)
    {
    	String Status = "Ok";
    	try{
    	NodeList Nodelist = doc.getElementsByTagName("OfferListing");
    	Node node = Nodelist.item(0).getFirstChild();
    	while (node!= Nodelist.item(0).getLastChild())
       	{
       	//System.out.println(node.getNodeName()+" = "+node.getTextContent());
       	
       	if (node.getNodeName().equals("SalePrice"))
       	{
       	String price_temp = null;
       	Element e = (Element) node;
       	NodeList  list_temp  = e.getElementsByTagName("FormattedPrice");
     //  	System.out.println(list_temp.item(0).getNodeName()+"  = "+list_temp.item(0).getTextContent());
       	price_temp = list_temp.item(0).getTextContent();
       	price_temp = price_temp.replace("$","");
       	item.AmazonPrice = Double.parseDouble(price_temp);
       	}else if (node.getNodeName().equals("Price"))
       	{
           	String price_temp = null;
           	Element e = (Element) node;
           	NodeList  list_temp  = e.getElementsByTagName("FormattedPrice");
          // 	System.out.println(list_temp.item(0).getNodeName()+"  = "+list_temp.item(0).getTextContent());
           	price_temp = list_temp.item(0).getTextContent();
           	price_temp = price_temp.replace("$","");
        	item.AmazonPrice = Double.parseDouble(price_temp);
       	}
       	
       	
       	node = node.getNextSibling();
       	}
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
    		
    	
		return Status;
    }
      
    public static String GetImageSets(Document doc,Item item)
    {
    	String Status = "Ok";
    	try{
    	NodeList Nodelist = doc.getElementsByTagName("ImageSet");
    	Node node = null;
    	for (int i=0;i<Nodelist.getLength();i++)
    	{
    		if (Nodelist.item(i).getAttributes().getNamedItem("Category").getTextContent().equals("primary"))
    		{
    		node = Nodelist.item(i).getLastChild().getFirstChild();
    		item.Imeges.add(node.getTextContent());
    		}
    	}
    	
    	for (int i=0;i<Nodelist.getLength();i++)
    	{
    		if (!Nodelist.item(i).getAttributes().getNamedItem("Category").getTextContent().equals("primary"))
    		{
    		node = Nodelist.item(i).getLastChild().getFirstChild();
    		item.Imeges.add(node.getTextContent());
    		}
    	}
    	

    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
    		
    	
		return Status;
   
	}
     
    public static String GetItemAttributes(Document doc,Item item)
    {
    	NodeList Nodelist = null;
    	Node node = null;
    	String Status = "Ok";
    	try{
    		
    	try{
    	Nodelist = doc.getElementsByTagName("EAN");
    	item.EAN = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Brand");
    	item.Brand = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Color");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Color = "+Nodelist.item(0).getTextContent());
    	item.Color = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Format");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Format = "+Nodelist.item(0).getTextContent());
    	item.Format = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Manufacturer");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Manufacturer = "+Nodelist.item(0).getTextContent());
    	item.Manufacturer = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Model");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Model = "+Nodelist.item(0).getTextContent());
    	item.Model = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("MPN");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("MPN = "+Nodelist.item(0).getTextContent());
    	item.MPN = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("NumberOfItems");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("NumberOfItems = "+Nodelist.item(0).getTextContent());
    	item.NumberOfItems = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PartNumber");
    	////node = Nodelist.item(0).getFirstChild();
    	//System.out.println("PartNumber = "+Nodelist.item(0).getTextContent());
    	item.PartNumber = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Publisher");
    	//node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Publisher = "+Nodelist.item(0).getTextContent());
    	item.Publisher = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("UPC");
    	//node = Nodelist.item(0).getFirstChild();
    	//System.out.println("UPC = "+Nodelist.item(0).getTextContent());
    	item.UPC = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PackageQuantity");
    	//node = Nodelist.item(0).getFirstChild();
    	//System.out.println("PackageQuantity = "+Nodelist.item(0).getTextContent());
    	item.PackageQuantity = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Size");
    	//node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Size = "+Nodelist.item(0).getTextContent());
    	item.Size = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	

    	try{
    	Nodelist = doc.getElementsByTagName("NumberOfPages");
    	//node = Nodelist.item(0).getFirstChild();
    	//System.out.println("NumberOfPages = "+Nodelist.item(0).getTextContent());
    	item.NumberOfPages = Nodelist.item(0).getTextContent();
    	}catch(Exception e1){}
    	
    	
 
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Language");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Language = "+node.getTextContent());
       	item.Language = node.getTextContent();
    	}catch(Exception e1){}
    	
   
    	
    	try{
    	Nodelist = doc.getElementsByTagName("ManufacturerMinimumAge");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("ManufacturerMinimumAge = "+node.getTextContent()+" months");
    	item.ManufacturerMinimumAge = node.getTextContent()+" months";
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("ManufacturerMaximumAge");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("ManufacturerMaximumAge = "+node.getTextContent()+" months");
    	item.ManufacturerMaximumAge = node.getTextContent()+" months";
    	}catch(Exception e1){}
    	
    	
    	try{
    	Nodelist = doc.getElementsByTagName("PublicationDate");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("PublicationDate = "+node.getTextContent());
    	item.PublicationDate = node.getTextContent();
    	}catch(Exception e1){}
    	
    	try{
    	Nodelist = doc.getElementsByTagName("OperatingSystem");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Operating System = "+node.getTextContent());
    	item.OperatingSystem = node.getTextContent();
    	}catch(Exception e1){}
 
    	
    	try{
    	Nodelist = doc.getElementsByTagName("Warranty");
    	node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Warranty = "+node.getTextContent());
    	item.Warranty = node.getTextContent();
    	}catch(Exception e1){}
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}

		return Status;
    
    }
    
    public static String GetItemFeature(Document doc,Item item)
    {
    	String Status = "Ok";
    	NodeList Nodelist = null;
    	Nodelist = doc.getElementsByTagName("Feature");
    	try{
    		
    	for (int i=0;i<Nodelist.getLength();i++) 
    	{
    		System.out.println(Nodelist.item(i).getTextContent());
    		item.Features.add(Nodelist.item(i).getTextContent());
    	}
    	
    	}catch(Exception e)
    	{
    		return "Error";
    	}

		return Status;
    }
    
    public static String GetSalesRank(Document doc,Item item)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("SalesRank");
    	Node node = Nodelist.item(0).getFirstChild();
    	//System.out.println("Sales Rank = "+node.getNodeValue());
    	item.Rank = Integer.parseInt(node.getNodeValue());
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;

    }
    
    public static String GetPackageDimensions(Document doc,Item item)
    {
    	String Status = "Ok";
    	try{
    	NodeList Nodelist =  doc.getElementsByTagName("PackageDimensions");
    	double Height = Double.parseDouble(Nodelist.item(0).getFirstChild().getTextContent());
    	double Length = Double.parseDouble(Nodelist.item(0).getFirstChild().getNextSibling().getTextContent());
    	double Weight = Double.parseDouble(Nodelist.item(0).getFirstChild().getNextSibling().getNextSibling().getTextContent());
    	double Width  = Double.parseDouble(Nodelist.item(0).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getTextContent());

    	Height/=100;
    	Length/=100;
    	Weight/=100;
    	Width/=100;
    	//3.9 x 7.9 x 1.5 inches
    	//System.out.println(Height+" x "+Length+" x "+Width+" inches");
    	item.PackageDimensions = Height+" x "+Length+" x "+Width+" inches";
    	//System.out.println(Weight+" pounds");
    	item.PackageWeight = Weight+" pounds";
    	
    	}catch(Exception e)
    	{
    		Status = "Error";
    	}
		return Status;
    }
    
    public static String GetShippingTime(Document doc,Item item)
    {
    	String Status = "Ok";
    	try{//
        NodeList Nodelist =  doc.getElementsByTagName("AvailabilityAttributes");
        Node node =  Nodelist.item(0);
    	item.MinimumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getTextContent().toString());
    	item.MinimumDaysToShip /=24;
        System.out.println(node.getFirstChild().getNextSibling().getNextSibling().getNodeName());
    	item.MaximumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getNextSibling().getTextContent());
    	item.MaximumDaysToShip /=24;
    	}catch(Exception e){System.out.println(e); return "Error";}
		return Status;
    }

    public static String IsPrime(Document doc,Item item)
    {
    	String Status = "Ok";
    	try{//
        NodeList Nodelist =  doc.getElementsByTagName("IsEligibleForPrime");
        Node node =  Nodelist.item(0);
        if (node.getFirstChild().getTextContent().equals("1")) item.prime = true;
        else item.prime = false;
    	}catch(Exception e){System.out.println(e); return "Error";}
		return Status;
    }

    public static String Instock(Document doc,Item item)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("AvailabilityType");
        Node node =  Nodelist.item(0);
        if (node.getFirstChild().getTextContent().equals("now")) item.AvailabilityType = "now";
        else item.AvailabilityType = "Not available";
    	}catch(Exception e){System.out.println(e); return "Error";}
		return "Ok";
		
    }
    
    public static String GetPrice(Document doc,Item item)
    {

    	try{
            NodeList Nodelist =  doc.getElementsByTagName("Price");
            Node node =  Nodelist.item(0);
            
            item.AmazonPrice =  Double.parseDouble(node.getFirstChild().getNextSibling().getNextSibling().getTextContent().replace("$", "")); 
        	}catch(Exception e){System.out.println(e); return "Error";}
    		return "Ok";
    }
    
    public static String GetSalePrice(Document doc,Item item)
    {

    	try{
            NodeList Nodelist =  doc.getElementsByTagName("SalePrice");
            Node node =  Nodelist.item(0);
            
            item.AmazonSalePrice =  Double.parseDouble(node.getFirstChild().getNextSibling().getNextSibling().getTextContent().replace("$", "")); 
        	}catch(Exception e){return "Error";}
    		return "Ok";
    }
    
    public static String IsNew(Document doc,Item item)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("OfferAttributes");
        Node node =  Nodelist.item(0);
        if (node.getFirstChild().getTextContent().equals("New")) item.IsNew = true;
        else item.IsNew = false;
        }catch(Exception e){return "Error";}
    		return "Ok";
    }
    
    public static String IsPreorder(Document doc,Item item)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("IsPreorder");
        Node node =  Nodelist.item(0);
        if (node.getFirstChild().getTextContent().equals("1")) item.IsPreorder = true;
        else item.IsPreorder = false;
        }catch(Exception e){return "Error";}
    	return "Ok";
    
    }
    
	private int GetItemIndex(ArrayList<Item> List,String Asin)
	{
		for(int i=0;i<List.size();i++)
		{
			if (List.get(i).SupplierCode.equals(Asin)) return i;
		}
		return -1;
	}
	
	
    @Override
	public boolean StockCheck(Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean PriceCheck(Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ShippingTimeCheck(Item item) {
		// TODO Auto-generated method stub
		return false;
	}


}
