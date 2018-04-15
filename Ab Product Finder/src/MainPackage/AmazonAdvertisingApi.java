package MainPackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

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

	        
	       if( fetch(requestUrl,"Item",List).equals("OK")) return 1;
	       else return 0;
	       
	        
	        
	        
	        

	}
	       
    private static String fetch(String requestUrl,String detail,ArrayList<product> list) throws ParserConfigurationException, SAXException, IOException {
	            String title = null; //https://www.youtube.com/watch?v=HfGWVy-eMRc
	            ArrayList<product> list2 = new ArrayList<product>();
	           
	                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                DocumentBuilder db = dbf.newDocumentBuilder();
	                Document doc = db.parse(requestUrl);
	                NodeList Node = doc.getElementsByTagName(detail);
	              
	                if (Node.getLength()==0) 
	                {
	                	System.out.println("Item lookup Fail");
	                	return "Fail";
	                }
	                
	              
	                for(int i=0;i<Node.getLength();i++) // all items //
	                {
	                	product pro = new product();
	                try{
	                	Node Item = Node.item(i);
	                	NodeList Childs  = Item.getChildNodes();
	                	System.out.println("--------------------------------------------");
	                	for(int j=0;j<Childs.getLength();j++)
	                	{
	                		if (Childs.item(j).getNodeName().equals("ASIN"))
	                		{
		                		try{
			                		Element e = (Element) Childs.item(j);
			                		
			                		System.out.println(e.getTagName()+" = "+e.getTextContent());
			                		pro.ASIN = e.getTextContent();
		                		}catch(Exception e){}
	                		}
	                		
	                		if (Childs.item(j).getNodeName().equals("SalesRank"))
	                		{
		                		try{
			                		Element e = (Element) Childs.item(j);
			                		System.out.println(e.getTagName()+" = "+e.getTextContent());
			                		pro.Amazon_Rank= Integer.parseInt(e.getTextContent());
		                		}catch(Exception e){}
	                		}
	                		
	                		if (Childs.item(j).getNodeName().equals("ItemAttributes")) //ItemAttributes//
	                		{
		                		
	                			Node ItemAttributes = Childs.item(j);
	                			NodeList ItemAttributesChilds  = ItemAttributes.getChildNodes();
	                			for(int k=0;k<ItemAttributesChilds.getLength();k++)
	                			{
	    	                		if (ItemAttributesChilds.item(k).getNodeName().equals("UPC"))
	    	                		{
	    		                		try{
	    			                		Element e = (Element) ItemAttributesChilds.item(k);
	    			                		System.out.println(e.getTagName()+" = "+e.getTextContent());
	    			                		pro.UPC= e.getTextContent();
	    			                		pro.bestresult = pro.UPC;
	    		                		}catch(Exception e){}
	    	                		}
	                			}
	                			for(int k=0;k<ItemAttributesChilds.getLength();k++)
	                			{
	    	                		if (ItemAttributesChilds.item(k).getNodeName().equals("ISBN"))
	    	                		{
	    		                		try{
	    			                		Element e = (Element) ItemAttributesChilds.item(k);
	    			                		System.out.println(e.getTagName()+" = "+e.getTextContent());
	    			                		pro.Isbn= e.getTextContent();
	    			                		pro.bestresult = pro.Isbn;
	    		                		}catch(Exception e){}
	    	                		}
	                			}
	                			
	                		}
	                		
	                		if (Childs.item(j).getNodeName().equals("Offers"))
	                		{

		                		
	                			Node ItemAttributes = Childs.item(j);
	                			NodeList ItemAttributesChilds  = ItemAttributes.getChildNodes();
	                			for(int k=0;k<ItemAttributesChilds.getLength();k++)
	                			{
	    	                		if (ItemAttributesChilds.item(k).getNodeName().equals("Offer"))
	    	                		{ 
	    	                			Node ItemAttributesChildsOffer = ItemAttributesChilds.item(k);
    		                			NodeList ItemAttributesChildsOfferChilds  = ItemAttributesChildsOffer.getChildNodes();
    		                			
    		                			
		    		                	for (int z=0;z<ItemAttributesChildsOfferChilds.getLength();z++)
		    		                	{
		    		                		if (ItemAttributesChildsOfferChilds.item(z).getNodeName().equals("OfferListing"))
		    		                		{
		    		                			
		    		                			Node ItemAttributesChildsOfferPrice = ItemAttributesChildsOfferChilds.item(z);
		    		                			NodeList ItemAttributesChildsOfferPriceChilds  = ItemAttributesChildsOfferPrice.getChildNodes();
		    		                			
		    		                			for (int q=0;q<ItemAttributesChildsOfferPriceChilds.getLength();q++)
		    		                			{
		    			                		if (ItemAttributesChildsOfferPriceChilds.item(q).getNodeName().equals("Price"))
		    			                		{
		    			                			Node ItemAttributesChildsOfferPriceFormattedPrice = ItemAttributesChildsOfferPriceChilds.item(q);
			    		                			NodeList ItemAttributesChildsOfferPriceFormattedPriceChilds  = ItemAttributesChildsOfferPriceFormattedPrice.getChildNodes();
		    			                			
			    		                			
			    		                			for (int p=0;p<ItemAttributesChildsOfferPriceFormattedPriceChilds.getLength();p++)
			    		                			{
			    		                				if (ItemAttributesChildsOfferPriceFormattedPriceChilds.item(p).getNodeName().equals("FormattedPrice"))
			    		                				{
			    		    		                		try{
			    		    			                		Element e = (Element) ItemAttributesChildsOfferPriceFormattedPriceChilds.item(p);
			    		    			                		String price  = e.getTextContent();
			    		    			                		price = price.substring(price.lastIndexOf("$")+1);
			    		    			                		pro.price = Double.parseDouble(price);
			    		    			                		System.out.println("Price = "+pro.price);
			    		    		                		}catch(Exception e){}
			    		    	                		}
			    		                				
			    		                				
			    		                			}
			    		                		}
			    		                			
		    			                		if (ItemAttributesChildsOfferPriceChilds.item(q).getNodeName().equals("IsEligibleForPrime"))	
		    			                		{
		    			                			int prime = 0;
		    			                			try{
	    		    			                		Element e = (Element) ItemAttributesChildsOfferPriceChilds.item(q);
	    		    			                		prime = Integer.parseInt(e.getTextContent());
	    		    			                		pro.prime = prime;
	    		    			                		System.out.println("prime = "+pro.prime);
	    		    		                		}catch(Exception e){}
		    			                			
		    			                		}
			    		                			
		    			                		}
		    		                		}
		    		                			
		    		                	}
		    		                	}
	    	                			
	    	                			
	    	                		}
	                			}
	                		

}
	                	pro.time_to_send = GetMaximumHours(Childs);
                        System.out.println("time_to_send = "+pro.time_to_send);
                        pro.AvailabilityType = GetAvailabilityType(Childs);
                        System.out.println("Availability Type = "+pro.AvailabilityType);

	                	
	                	System.out.println("--------------------------------------------");
	                	System.out.println();

	                	
	                	if (pro!=null&&pro.bestresult!=null&&pro.prime==1&&pro.price>0)
	                	{
	                		list.add(pro);
	                		list2.add(pro);
	                	}
	                	
	                
	                
	                }catch(Exception e1)
	                {
	                	System.out.println("Items not fit");
	                }
	                }
	                
	                
	                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	               for (product ele:list2)
	               {
	            	   System.out.println();
	            	   if (ele.ASIN!=null)
	            	   System.out.println("ASIN = "+ele.ASIN);
	            	   System.out.println("SalesRank = "+ele.Amazon_Rank);
	            	   if (ele.Isbn!=null)
	            	   System.out.println("ISBN = "+ele.Isbn);
	            	   if (ele.UPC!=null)   
	            	   System.out.println("UPC = "+ele.UPC);
	            	   System.out.println("Prime = "+ele.prime);
	            	   System.out.println("Price = "+ele.price);
	            	   if (!ele.bestresult.equals(null))
	            	   System.out.println("bestresult = "+ele.bestresult);
	            	  System.out.println();
	               }
	               System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	                
	               System.out.println("list2 = "+list2.size());
	               System.out.println("list = "+list.size());
	                
	                
	            return "OK";
}

    public static int GetMaximumHours(NodeList node)
    {
    
    	try{

       	Element e = (Element) node;
       	NodeList  list_temp  = e.getElementsByTagName("AvailabilityType");
       	
       	list_temp  = e.getElementsByTagName("MaximumHours");
    	return (Integer.parseInt(list_temp.item(0).getTextContent()));

    	}catch(Exception e)
    	{
    		System.out.println("%&^^%&^%&^$^Error%&^^%&^%&^$^");
    		return -1;
    	}

    }

    public static String GetAvailabilityType(NodeList node)
    {
    
    	try{

       	Element e = (Element) node;
       	NodeList  list_temp  = e.getElementsByTagName("AvailabilityType");
       	
       	list_temp  = e.getElementsByTagName("AvailabilityType");
    	return (list_temp.item(0).getTextContent());

    	}catch(Exception e)
    	{
    		System.out.println("Availability Type Error");
    		return "No info";
    	}

    }
    
}

	
	
