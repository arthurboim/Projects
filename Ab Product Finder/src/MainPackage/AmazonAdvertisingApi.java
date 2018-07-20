package MainPackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AmazonAdvertisingApi {

	public int Get_Items_LookUp(String Code, String CodeType , ArrayList<product> List) throws InterruptedException, ParserConfigurationException, SAXException, IOException
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
	        SignedRequestsHelper helper;

	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }

	        String requestUrl = null;

	        Map<String, String> params = new HashMap<String, String>();
	        //ArrayList<product> List = new ArrayList<product> ();
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJYGD72A2U4KSBUZQ");
	        params.put("AssociateTag", "bchoices-20");
	        params.put("ItemId", Code);
	        if (CodeType.equals("UPC")||CodeType.equals("ISBN"))
	        params.put("SearchIndex", "All");
	        params.put("IdType", CodeType);
	        params.put("ResponseGroup", "Images,ItemAttributes,Offers,SalesRank");


	        Thread.sleep(4000);
	        requestUrl = helper.sign(params);

	        
	       if( fetch(requestUrl,List).equals("OK")) return 1;
	       else return 0;
	       
	        
	        
	        
	        

	}
	 
    private static String fetch(String requestUrl,ArrayList<product> list) throws ParserConfigurationException, SAXException, IOException {	           
	                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                DocumentBuilder db = dbf.newDocumentBuilder();
	                Document doc = db.parse(requestUrl);
	                NodeList Node = doc.getElementsByTagName("Item");
	              
	                if (Node.getLength()==0) 
	                {
	                	System.out.println("Item lookup Fail");
	                	return "Fail";
	                }

	                
	                for(int i=0;i<Node.getLength();i++) // all items //
	                {
	                try{
	                	product pro = new product();
	                	
	                	if (false == IsNew(doc,i) || false == Instock(doc,i) || true == IsPreorder(doc,i))
	                	{
	                		continue;
	                	}
	                	
	                	pro.ASIN 		= GetAsin(doc,i);
	                	pro.bestresult 	= GetItemCode(doc,i);
	                	pro.price 		= Price(doc,i);
	                	pro.Amazon_Rank = GetSalesRank(doc,i);

	                	if (pro.ASIN!=null && pro.bestresult!=null && pro.prime==1 && pro.price>0)
	                	{
	                		list.add(pro);
	                	}
	                	
	                }catch(Exception e1)
	                {}
	                
	                } //Loop end
	                

	               for (product ele:list)
	               {
	            	   System.out.println("ASIN = "+ele.ASIN);
	               }

	            return "OK";
}

    private static boolean Instock(Document doc,int i)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("AvailabilityType");
        Node node =  Nodelist.item(i);
        if (node.getFirstChild().getTextContent().equals("now")) 
        {
        	return true;
        }
        else 
        {
        	return false;        
        }
    	}catch(Exception e){}
    	
    	return false;
    }
    
    private static boolean IsNew(Document doc, int i)
    {
    	try{
        NodeList Nodelist =  doc.getElementsByTagName("OfferAttributes");
        Node node =  Nodelist.item(i);
        
        if (node.getFirstChild().getTextContent().equals("New")) 
        {
        	return true;
        }
        else 
        {
        	return false;
        }
        }catch(Exception e){}
    	
    	return false;
    }
    
    private static double Price(Document doc, int i)
    {
    try{
        NodeList Nodelist =  doc.getElementsByTagName("Price");
        Node node =  Nodelist.item(i);
        return Double.parseDouble(node.getFirstChild().getNextSibling().getNextSibling().getTextContent().replace("$", "")); 
    }catch(Exception e){}
    	return -1;
    }
    
    private static boolean IsPreorder(Document doc, int i)
    {
    	try{
    		
    		NodeList Nodelist =  doc.getElementsByTagName("IsPreorder");
    		Node node =  Nodelist.item(i);
    		
    		if (node.getFirstChild().getTextContent().equals("1")) 
    		{
    			return true;
    		}
    		else 
    		{
    			return false;
    		}
    		
    	}catch(Exception e){}
    	
    	return false;
    }
    
    private static int GetSalesRank(Document doc, int i)
    {
    try{
    		
    	NodeList Nodelist =  doc.getElementsByTagName("SalesRank");
    	Node node = Nodelist.item(i).getFirstChild();
    	return Integer.parseInt(node.getNodeValue());
    	
    }catch(Exception e){}
    	
		return -1;
    }
    
    private static String GetItemCode(Document doc, int i)
    {
    	NodeList Nodelist = null;
    	String Code= null;
    	
        try{
        Nodelist = doc.getElementsByTagName("UPC");
        Code = Nodelist.item(i).getTextContent();
        if (null != Code)
        {
        	return Code;
        }
        }catch(Exception e1)
        {
        	Code= null;
        }
        
        try{
    	Nodelist = doc.getElementsByTagName("EAN");
    	Code = Nodelist.item(i).getTextContent();
        if (null != Code)
        {
        	return Code;
        }
        }catch(Exception e1)
        {
        	Code= null;
        }
        
        try{
    	Nodelist = doc.getElementsByTagName("ISBN");
    	Code = Nodelist.item(i).getTextContent();
        if (null != Code)
        {
        	return Code;
        }
        }catch(Exception e1)
        {
        	Code= null;
        }
        
        try{
    	Nodelist = doc.getElementsByTagName("MPN");
    	Code = Nodelist.item(i).getTextContent();
        if (null != Code)
        {
        	return Code;
        }
        }catch(Exception e1)
        {
        	Code= null;
        }
        
        return null;
    }
    
    private static String GetAsin(Document doc, int i)
    {
    	try{
    		
    	NodeList Nodelist =  doc.getElementsByTagName("ASIN");
    	Node node = Nodelist.item(i).getFirstChild();
    	return node.getNodeValue();
    	
    	}catch(Exception e){}
    	
		return null;
    }
     
}

	
	
