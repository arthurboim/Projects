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
import Database.Item;


public class Amazon{
	
	
	public void BulkInfoRequest(String Code,ArrayList<Item> List,int counter) throws ParserConfigurationException, SAXException, IOException
	{

	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	    final String AWS_ACCESS_KEY_ID = "AKIAJI6TKSBFN3ADN2LA";

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     *///
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
	        params.put("ResponseGroup", "Images,ItemAttributes,Offers,SalesRank,EditorialReview");

	      
	        try {
				Thread.sleep(3000);
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
        NodeList Nodelist =  doc.getElementsByTagName("Item");
        
        for(int i=0;i<Nodelist.getLength();i++)
        {
	        try{
	        	Item item = new Item();
	            GetAsin(Nodelist,item,i);
		        GetSalesRank(Nodelist,item,i);
		        GetItemAttributes(Nodelist,item,i);
		        IsPrime(Nodelist,item,i);
		        GetImageSets(Nodelist,item,i);
		        Instock(Nodelist,item,i);
		        GetShippingTime(Nodelist,item,i);
		        IsNew(Nodelist,item,i);
		        IsPreorder(Nodelist,item,i);
		        GetTitle(Nodelist,item,i);
		        GetItemFeature(Nodelist,item,i);
		        GetContent(Nodelist,item,i);
		        GetPrice(Nodelist,item,i);
		        GetSalePrice(Nodelist,item,i);
		    	System.out.println("\n---------------------\n");
		    	item.PathFolder = List.get(GetItemIndex(List,item.SupplierCode)).PathFolder;
		    	List.set(GetItemIndex(List,item.SupplierCode), item);
        	}catch(Exception e){}
        } 
	}
	
    public static String GetAsin(NodeList list, Item item,int i)
    {
    	
    	String Status = "Ok";
    	try{
    	Element ele = (Element)list.item(i);
    	NodeList Nodelist =  ele.getElementsByTagName("ASIN");
    	item.SupplierCode = Nodelist.item(0).getTextContent();
    	}catch(Exception e)
    	{
        	item.ReadyToUpload = false;
        	System.out.println("GetAsin Null");
        	Status = "Error";
    	}
		return Status;
    }
	  
    public static String GetSalesRank(NodeList list,Item item,int i)
    {
		String Status = "Ok";
		try{									
			Element ele = (Element)list.item(i);
			NodeList Nodelist =  ele.getElementsByTagName("SalesRank");
			item.Rank = Integer.parseInt(Nodelist.item(0).getTextContent());
		}catch(Exception e)
		{
			Status = "Error";
		}
		
		return Status;
    }
    
    public static String GetItemAttributes(NodeList list,Item item,int i)
    {
    	String Status = "Ok";
    	
		try{
			Element ele = (Element)list.item(i);
			NodeList Nodelist =  ele.getElementsByTagName("ItemAttributes");
			NodeList Nodelist2 = Nodelist.item(0).getChildNodes();
			for(int j=0;j<Nodelist2.getLength();j++)
			{
			
			if(Nodelist2.item(j).getNodeName() == "Publisher" )
			{
				item.Brand = Nodelist2.item(j).getTextContent();
				System.out.println(item.SupplierCode +" = "+item.Brand);
				break;
			}
			
			if(Nodelist2.item(j).getNodeName().equals("Brand"))
			{
				item.Brand = Nodelist2.item(j).getTextContent();
				System.out.println(item.SupplierCode +" = "+item.Brand);
				break;
			}
			
//			if(Nodelist2.item(j).getNodeName().equals("Label"))
//			{
//				item.Brand = Nodelist2.item(j).getTextContent();
//				System.out.println("Label");
//				System.out.println(item.SupplierCode +" = "+item.Brand);
//				break;
//			}
		}
			
		}catch(Exception e1)
		{  
			System.out.println("Item attributes fail "+item.SupplierCode);
		}
    		
    	try{
		Element ele = (Element)list.item(i);
		NodeList Nodelist =  ele.getElementsByTagName("EAN");
		System.out.println("EAN = "+Nodelist.item(0).getTextContent());
    	item.EAN = Nodelist.item(0).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	
    	try{
	        Element ele = (Element)list.item(i);
	        NodeList Nodelist =  ele.getElementsByTagName("Model");
			System.out.println("Model = "+Nodelist.item(0).getTextContent());

	    	item.Model = Nodelist.item(0).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    	
    	try{
	        Element ele = (Element)list.item(i);
	        NodeList Nodelist =  ele.getElementsByTagName("MPN");
			System.out.println("MPN = "+Nodelist.item(0).getTextContent());

	        item.MPN = Nodelist.item(0).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}
    
    	try{
	        Element ele = (Element)list.item(i);
	        NodeList Nodelist =  ele.getElementsByTagName("UPC");
			System.out.println("UPC = "+Nodelist.item(0).getTextContent());
	        item.UPC = Nodelist.item(0).getTextContent();
    	}catch(Exception e1)
    	{
    		
    	}

    	
		return Status;
    
    }
    
    public static String IsPrime(NodeList list,Item item,int i)
    {
    	String Status = "Ok";
    	try{					
		Element ele = (Element)list.item(i);
		NodeList Nodelist =  ele.getElementsByTagName("IsEligibleForPrime");
        if (Nodelist.item(0).getTextContent().equals("1")) 
        	item.prime = true;
        else 
        	item.prime = false;
    	}catch(Exception e)
    	{
    		System.out.println("No prime info");
        	item.prime = false;
    		return "Error";
    	}
    	
		return Status;
    }
	
    public static String GetImageSets(NodeList list,Item Item,int i)
    {

    	String Status = "Ok";
		Element ele = (Element)list.item(i);
		NodeList Childs =  ele.getElementsByTagName("LargeImage");
		
		for(int j=0;j<Childs.getLength();j++)
    	{
			if (Childs.item(j).getNodeName().equals("LargeImage"))
			{
			
	    		Element e = (Element) Childs.item(j);
	    		String[] TempImageLink = new String[1];
	    		TempImageLink[0] = e.getTextContent();
	    		try{
	    		TempImageLink[0] = TempImageLink[0].substring(0, TempImageLink[0].indexOf(".jpg")+4);
	    		}catch(Exception e1)
	    		{
	    			System.out.println("substring error");
	    		}
	    		Item.PicturesString[0] = TempImageLink[0];
	    		break;
			}
    	}
    

	return Status;
}
     
    public static String Instock(NodeList list,Item item,int i)
    {
    	try{
    	Element ele = (Element)list.item(i);
    	NodeList Nodelist =  ele.getElementsByTagName("AvailabilityType");
        if (Nodelist.item(0).getTextContent().equals("now")) 
        {
        	item.AvailabilityType = "now";
        }
        else 
        {
        	item.AvailabilityType = "Not available";
        }
    	}catch(Exception e)//
    	{
    		System.out.println("No availability info");
    		item.ReadyToUpload = false;//
        	item.AvailabilityType = "NOT AVAILABLE";
    		return "Error";
    	}
    	
		return "Ok";
		
    }
    
    public static String GetShippingTime(NodeList list ,Item item,int i)
    {
    	String Status = "Ok";
    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("AvailabilityAttributes");
        Node node =  Nodelist.item(0);
    	item.MinimumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getTextContent().toString());
    	item.MinimumDaysToShip /=24;
    	item.MaximumDaysToShip = Integer.parseInt(node.getFirstChild().getNextSibling().getNextSibling().getTextContent());
    	item.MaximumDaysToShip /=24;
    	}catch(Exception e)
    	{
    		System.out.println("No shipping time info");
        	item.ReadyToUpload = false;
        	item.AvailabilityType = "NOT AVAILABLE";
    		return "Error";
    	}
		return Status;
    }

    public static String IsNew(NodeList list,Item item,int i)
    {
    	try{
            Element ele = (Element)list.item(i);
            NodeList Nodelist =  ele.getElementsByTagName("OfferAttributes");
            Node node =  Nodelist.item(0);
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
    
    public static String IsPreorder(NodeList list,Item item,int i)
    {
	    Element ele = (Element)list.item(i);
		Node node;
		
		try{
			NodeList Nodelist =  ele.getElementsByTagName("AvailabilityAttributes");
			node = Nodelist.item(0).getFirstChild();
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
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return "Ok";
    }
    
    public static String GetTitle(NodeList list, Item item,int i)
    {

    	String Status = "Ok";
    	try{
    	Element ele = (Element)list.item(i);
    	NodeList Nodelist =  ele.getElementsByTagName("Title");
    	Node node = Nodelist.item(0).getFirstChild();
    	item.Title = node.getNodeValue();
    	}catch(Exception e)
    	{
        	item.AvailabilityType = "NOT AVAILABLE";
        	System.out.println("GetTitle Null");
        	item.ReadyToUpload = false;
    	}
		return Status;

    }

    public static String GetItemFeature(NodeList list,Item item,int i)
    {
    	String Status = "Ok";
    	Element ele = (Element)list.item(i);
    	NodeList Nodelist =  ele.getElementsByTagName("ItemAttributes");
    	Node node = Nodelist.item(0).getFirstChild();
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

    public static String GetContent(NodeList list, Item item,int i)
    {
    	int counter = 0;
    	String Status = "Ok";
    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("EditorialReview");
    	Node node = Nodelist.item(0).getFirstChild();
    	
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
    
    //Price
    

    public static String GetPrice(NodeList list, Item item,int i)
    {

    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("Price");
    	Node node = Nodelist.item(0).getFirstChild();
    	
    	while(node != null)
    	{
    		if (node.getNodeName().equals("FormattedPrice"))
    		{
    			System.out.println("Node value = "+node.getTextContent());
    		}
    		node = node.getNextSibling();
    	}

    	}catch(Exception e)
    	{

    	}
		return "OK";
    }
    
    

    public static String GetSalePrice(NodeList list, Item item,int i)
    {

    	try{
        Element ele = (Element)list.item(i);
        NodeList Nodelist =  ele.getElementsByTagName("SalePrice");
    	Node node = Nodelist.item(0).getFirstChild();
    	
    	while(node != null)
    	{
    		if (node.getNodeName().equals("FormattedPrice"))
    		{
    			System.out.println("Sale price = "+node.getTextContent());
    		}
    		node = node.getNextSibling();
    	}

    	}catch(Exception e)
    	{

    	}
		return "OK";
    }
    
    
    
	private int GetItemIndex(ArrayList<Item> List,String Asin)
	{
		for(int i=0;i<List.size();i++)
		{
			if (List.get(i).SupplierCode.equals(Asin)) 
			{
				return i;
			}
		}
		
		return -1;
	}
	
}
