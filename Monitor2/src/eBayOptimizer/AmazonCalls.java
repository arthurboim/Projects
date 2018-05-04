package eBayOptimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
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


public class AmazonCalls {

	public static Connection con ;
	public static java.sql.Statement statement ;
	public static String Connection = null;
	public static String scham = null;
	public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public static String AWS_SECRET_KEY_FROM_FILE = null;
	public static String ENDPOINT_FROM_FILE = null;
	
	public AmazonCalls() 
	{

		System.out.println("Constractor of Database");
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
					//System.out.println("AWS_ACCESS_KEY_ID_FROM_FILE = "+AWS_ACCESS_KEY_ID_FROM_FILE);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
					//System.out.println("AWS_SECRET_KEY_FROM_FILE = "+AWS_SECRET_KEY_FROM_FILE);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
					//System.out.println("ENDPOINT = "+ENDPOINT_FROM_FILE);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
					//System.out.println("Connection = "+Connection);
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

    public static String GetImageSets(Document doc,Item item)
    {

    	String Status = "Ok";
    	try{
    	NodeList Nodelist = doc.getElementsByTagName("ImageSet");
    	String name = null;
    	System.out.println(Nodelist.getLength()); 
    	if (Nodelist.getLength()==0) return "Error";
    	for (int i=0;i<Nodelist.getLength();i++)
    	{
    		Element e = (Element)Nodelist.item(i);
    		name = e.getAttribute("Category");
    		if (name.equals("variant"))
    		name = name+(i+1);
    		System.out.println(name);
    		NodeList Nodelist2 =  e.getElementsByTagName("LargeImage");
    		System.out.println(Nodelist2.item(0).getFirstChild().getTextContent());
    		if (name.contains("variant") || name.equals("primary") || name.equals("swatch"))  
    		{
    		item.MainImage = Nodelist2.item(0).getFirstChild().getTextContent();
    		break;
    		}
    		//item.Images.put(name, Nodelist2.item(0).getFirstChild().getTextContent());
    		name = null;
    	}
    	}catch(Exception e)
    	{
    		Status = "Error";
    		throw new RuntimeException(e);
    	}
		return Status;

    }
     
	public String Items_LookUp_10(String Code, String CodeType , ArrayList<Item> Itemlist,int k) throws InterruptedException, ParserConfigurationException, SAXException, IOException
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
	        } catch (Exception e) {
	            e.printStackTrace();
	            
	        }

	        String requestUrl = null;
	        //Online_product product = new Online_product();
	        
	        Map<String, String> params = new HashMap<String, String>();
	        //ArrayList<product> List = new ArrayList<product> ();

	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAJ2NQFWXCDNJESKLQ");
	        params.put("AssociateTag", "bchoices-20");
	        params.put("ItemId", Code);
	        params.put("IdType", CodeType);
	        params.put("ResponseGroup", "Images");
	        params.put("Condition", "New");
	      
	        

	        //Thread.sleep(4000);
	        requestUrl = helper.sign(params);
	        System.out.println("fetch_10");
			return fetch_10(requestUrl,"Item",Itemlist,k);

	}
	
    private static String fetch_10(String requestUrl,String detail,ArrayList<Item> Itemlist,int k) throws ParserConfigurationException, SAXException, IOException 
    {
    		String Fatching_status = "Fetching success";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            /*
            if (GetTitle(doc,item).equals("Ok"))
               	System.out.println("GetTitle = Ok\n");
                else 
                {
                	Fatching_status = "GetTitle Error";
                	System.out.println(Fatching_status);
                }
            
            if (GetItemAttributes(doc,item).equals("Ok"))
           	System.out.println("GetItemAttributes = Ok\n");
            else 
            {
            	Fatching_status = "GetItemAttributes Error";
            	System.out.println(Fatching_status);
            }
         
              */
           // for (Item ele:Itemlist)
          //  {
            System.out.println("Getting images...");
                if (GetImageSets_10(doc,Itemlist,k).equals("Ok"))
                   	System.out.println("GetImageSets = Ok\n");
                    else 
                    {
                    	Fatching_status = "GetImageSets Error";
                    	System.out.println(Fatching_status);
                    }
          //  }
             
                /*
                if (GetCategory_10(doc,Itemlist,k).equals("Ok"))
                   	System.out.println("GetCategory = Ok\n");
                    else 
                    {
                    	Fatching_status = "Category Error";
                    	System.out.println(Fatching_status);
                    }
            
                
            if (GetSalesRank(doc,item).equals("Ok"))
        	System.out.println("GetSalesRank = Ok\n");
            else 
            {
            	Fatching_status = "GetSalesRank Error";
            	System.out.println(Fatching_status);
            }
            
            if (GetPackageDimensions(doc,item).equals("Ok"))
        	System.out.println("GetPackageDimensions = Ok\n");
            else 
            {
            	Fatching_status = "GetPackageDimensions Error";
            	System.out.println(Fatching_status);
            }
            
            if (GetItemDimensions(doc,item).equals("Ok"))
        	System.out.println("GetItemDimensions = Ok\n");
            else 
            {
            	Fatching_status = "GetItemDimensions Error";
            	System.out.println(Fatching_status);
            }
            
            if (GetOfferListing(doc,item).equals("Ok"))
        	System.out.println("GetOfferListing = Ok\n");
            else 
            {
            	Fatching_status = "GetOfferListing Error";
            	System.out.println(Fatching_status);
            }
            
            if (GetContent(doc,item).equals("Ok"))
        	System.out.println("GetContent = Ok\n");
            else 
            {
            	Fatching_status = "GetContent Error";
            	System.out.println(Fatching_status);
            }
            	*/
        	return Fatching_status;
    }
   
	public static String GetCategory_10(Document doc,ArrayList<Item> Itemlist,int k)
	{

    	String Status = "Ok";
    	NodeList Node = doc.getElementsByTagName("Item");
    	try{
        if (Node.getLength()==0) 
        {
            	System.out.println("Item lookup Fail");
            	return "Fail";
        }
    	
        for(int i=0;i<Node.getLength();i++) // all items //
        {
        //System.out.println(Node.item(i).getTextContent());
        String Temp  = Node.item(i).getTextContent();
        int j = Temp.length()-1;
        int end = 1;
        while (end == 1)
        {
        	if (Character.isDigit(Temp.charAt(j))) end =0 ; else j--;
        }
        Temp = Temp.substring(j+1);
        System.out.println(Temp);
        Itemlist.get(k+i).Category = Temp;
        }
    	}catch(Exception e) {return "Fail";}
    	return Status;
    	
	}

    public static String GetImageSets_10(Document doc,ArrayList<Item> Itemlist,int k)
    {

    	String Status = "Ok";
        NodeList Node = doc.getElementsByTagName("Item");

        if (Node.getLength()==0) 
        {
        	System.out.println("Item lookup Fail");
        	return "Fail";
        }
        for(int i=0;i<Node.getLength();i++) // all items //
        {
        	Node Item = Node.item(i);
        	NodeList Childs  = Item.getChildNodes();
        	for(int j=0;j<Childs.getLength();j++)
        	{
    		if (Childs.item(j).getNodeName().equals("LargeImage"))
    		{
        		
            		Element e = (Element) Childs.item(j);
            		String[] TempImageLink = new String[1];
            		TempImageLink[0] = e.getTextContent();
            		System.out.println(TempImageLink[0]);
            		try{
            		TempImageLink[0] = TempImageLink[0].substring(0, TempImageLink[0].indexOf(".jpg")+4);
            		}catch(Exception e1)
            		{
            			System.out.println("substring error");
            		}
            		Itemlist.get(k+i).PicturesString = new String[1];
            		Itemlist.get(k+i).PicturesString[0] = TempImageLink[0];
            			
            		System.out.println(Itemlist.get(k+i).PicturesString[0]);
            		//pro.ASIN = e.getTextContent();
        		
    		}
        	}
        }

		return Status;
    }
     
	
	
}
