package PriceChanger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import AmazonOrders.SignedRequestsHelper;



public class AmazonCall {

	public static Connection con ;
	public static java.sql.Statement statement ;
	public static String Connection = null;
	public static String scham = null;
	public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public static String AWS_SECRET_KEY_FROM_FILE = null;
	public static String ENDPOINT_FROM_FILE = null;

	public AmazonCall() {

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("AWS_ACCESS_KEY_ID:"))
				{
					AWS_ACCESS_KEY_ID_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_ACCESS_KEY_ID:")+19);
					System.out.println("AWS_ACCESS_KEY_ID_FROM_FILE = "+AWS_ACCESS_KEY_ID_FROM_FILE);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
					System.out.println("AWS_SECRET_KEY_FROM_FILE = "+AWS_SECRET_KEY_FROM_FILE);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
					System.out.println("ENDPOINT = "+ENDPOINT_FROM_FILE);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					System.out.println("Schame = "+scham);
					Connection =Connection+scham;
					System.out.println("Connection = "+Connection);
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

	public int GetItemCodeFromAmazon(String Code, String CodeType , ArrayList<ListInformation> List,int Length) throws InterruptedException, ParserConfigurationException, SAXException, IOException, SQLException
	{


	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */								  
	    final String AWS_ACCESS_KEY_ID = AWS_ACCESS_KEY_ID_FROM_FILE;

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	    final String AWS_SECRET_KEY = "3ig4cJYuigSPmQV5TuaVl0dfJau+AhtP3b3ZQKUQ";

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
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJYGD72A2U4KSBUZQ");
	        params.put("AssociateTag", "bchoices-20");
	        params.put("ItemId", Code);
	        //params.put("SearchIndex", "All");
	        params.put("IdType", CodeType);
	        params.put("ResponseGroup", "ItemAttributes");


	        Thread.sleep(4000);
	        requestUrl = helper.sign(params);

	        
	       if( fetch(requestUrl,List, Length).equals("OK")) return 1;
	       else return 0;
	       
	        
	        
	        
	        

	}
	
    private static String fetch(String requestUrl, ArrayList<ListInformation> List,int Length) throws ParserConfigurationException, SAXException, IOException, SQLException {
	                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                DocumentBuilder db = dbf.newDocumentBuilder();
	                Document doc = db.parse(requestUrl);
	                NodeList Node = doc.getElementsByTagName("Item");
	                NodeList Node2 = doc.getElementsByTagName("UPC");
	                NodeList Node3 = doc.getElementsByTagName("EAN");
	                int ItemIndex = -1;
	                DatabasePriceChanger Db = new DatabasePriceChanger();
	                for (int i=0;i<Length;i++)
	                {
	                	ItemIndex =getItemIndex(List,Node.item(i).getFirstChild().getFirstChild().getNodeValue());
	                	ListInformation temp = List.get(ItemIndex);
	                	System.out.println(Node.item(i).getFirstChild().getNodeName());
		              	System.out.println(Node.item(i).getFirstChild().getFirstChild().getNodeValue());
		              	try{
		              	if (Node2!=null)
		              	{
		              	if (Node2.item(i).getFirstChild().getNodeValue()!=null)
		              	{
		              	
	                	System.out.println(Node2.item(i).getNodeName());
		              	System.out.println(Node2.item(i).getFirstChild().getNodeValue());
		              	temp.Code = Node2.item(i).getFirstChild().getNodeValue();
		              	List.set(ItemIndex, temp);
		              	Db.UpdateCode(List.get(ItemIndex).EbayId, List.get(ItemIndex).Code); 
		              	continue;
		              	}
		              	}
		              	
		              	if (Node3!=null)
		              	{
		              	if (Node3!=null && Node3.item(i).getFirstChild().getNodeValue()!=null )
		              	{
		              		
		                	System.out.println(Node3.item(i).getNodeName());
			              	System.out.println(Node3.item(i).getFirstChild().getNodeValue());
			              	temp.Code = Node2.item(i).getFirstChild().getNodeValue();
			              	List.set(ItemIndex, temp);
			              	Db.UpdateCode(List.get(ItemIndex).EbayId, List.get(ItemIndex).Code);  
		              	}
		              	}
		              //	List.set(ItemIndex, temp);
		              //	Db.UpdateCode(List.get(ItemIndex).EbayId, List.get(ItemIndex).Code);  
		              	}catch(Exception e){
		              		System.out.println(e);
		              	}
	                }
	                Db = null;
	                System.gc();
	                return "OK";
}
    
    private static int getItemIndex(ArrayList<ListInformation> List,String Asin)
    {
    	
    	for(int i=0;i<List.size();i++)
    	{
    		if (List.get(i).Asin.equals(Asin))
    		{
    			System.out.println("Index "+i);
    			return i;
    		}
    	}
    	
		return 0;
    }
    
    
    
    
    
    
    
    public static String GetCode(NodeList node)
    {
    	try{
       	Element e = (Element) node;
       	NodeList  list_temp  = e.getElementsByTagName("UPC");
     
     
       	return list_temp.item(0).getTextContent();

    	}catch(Exception e){return null;}

    }

    
}

	
	
