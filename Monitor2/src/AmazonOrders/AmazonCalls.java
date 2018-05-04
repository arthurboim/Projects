package AmazonOrders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class AmazonCalls  {
	
	public static Connection con ;
	public static java.sql.Statement statement ;
	public static String Connection = null;
	public static String scham = null;
	public static String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static String AWS_ACCESS_KEY_ID_FROM_FILE  = null;
	public static String AWS_SECRET_KEY_FROM_FILE = null;
	public static String ENDPOINT_FROM_FILE = null;

	public AmazonCalls() {

		//System.out.println("Constractor of Database");
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
				//	System.out.println("AWS_ACCESS_KEY_ID_FROM_FILE = "+AWS_ACCESS_KEY_ID_FROM_FILE);
				}
				
				if (sCurrentLine.contains("AWS_SECRET_KEY:"))
				{
					AWS_SECRET_KEY_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("AWS_SECRET_KEY:")+16);
				//	System.out.println("AWS_SECRET_KEY_FROM_FILE = "+AWS_SECRET_KEY_FROM_FILE);
				}
				
				if (sCurrentLine.contains("ENDPOINT:"))
				{
					ENDPOINT_FROM_FILE = sCurrentLine.substring(sCurrentLine.indexOf("ENDPOINT:")+10);
				//	System.out.println("ENDPOINT = "+ENDPOINT_FROM_FILE);
				}
				
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				//	System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
				//	System.out.println("Schame = "+scham);
					Connection =Connection+scham;
				//	System.out.println("Connection = "+Connection);
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

	public int  Items_LookUp(String Code, String CodeType) throws InterruptedException, ParserConfigurationException, SAXException, IOException, SQLException
	{
		 con = DriverManager.getConnection(Connection,"root","root");
		 statement = con.createStatement();//

	    /*
	     * Your AWS Access Key ID, as taken from the AWS Your Account page.
	     */
	     final String AWS_ACCESS_KEY_ID = AWS_ACCESS_KEY_ID_FROM_FILE;

	    /*
	     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	     * Your Account page.
	     */
	     String AWS_SECRET_KEY = AWS_SECRET_KEY_FROM_FILE;
	 	
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
	 
	        
	        Map<String, String> params = new HashMap<String, String>();
	       
	        Thread.sleep(3000);
	        params.put("Service", "AWSECommerceService");
	        params.put("Operation", "ItemLookup");
	        params.put("AWSAccessKeyId", "AKIAIN5OVJJQ2PRPII2Q");
	        params.put("AssociateTag", "choices29-20");
	        params.put("ItemId", Code);
	        params.put("IdType", CodeType);
	        params.put("ResponseGroup", "Offers,SalesRank");
	        params.put("Condition", "New");
	       // params.put("SearchIndex", "All");
	        int loopstate = 0;
	     while(loopstate==0)
	     {
	     try{
	     Thread.sleep(2000);
	     requestUrl = helper.sign(params);
		 return fetch(requestUrl,Code);
	     }catch(Exception e){System.out.println("Exception fetch"); loopstate = 1;}
	     }
		return -1;

	}

    private static int fetch(String requestUrl,String Code) throws ParserConfigurationException, SAXException, IOException, SQLException 
    {
    		con = DriverManager.getConnection(Connection,"root","root");
    		statement = con.createStatement();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            return GetSalesRank(doc,Code,statement);
    }
    
    public static int GetSalesRank(Document doc,String Code,java.sql.Statement statement_update)
    {
    	NodeList Nodelist =  doc.getElementsByTagName("SalesRank");
    	Node node = Nodelist.item(0).getFirstChild();
    	if (node.getNodeValue()!=null)
    	{
    		return Integer.parseInt(node.getNodeValue());
    	}
    	return -1;
    }
   
}
