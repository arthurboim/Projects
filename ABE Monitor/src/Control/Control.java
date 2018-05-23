package Control;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import Amazon.Amazon;
import Database.Item;
import Ebay.Ebay;


public class Control {

	public static ArrayList<Item> ListOfItems = new ArrayList<Item>();
	
	public void CheckItemBeforeUpload(String Asin)
	{
		
		Amazon AmazonApi = new Amazon();
		Item Newitem = AmazonApi.BuildItem("B00LSP68KM");
		
		Newitem.ItemPrint();
		Ebay ebay;
		try {
			ebay = new Ebay();
			try{
			if (!ebay.IsVeroBrand(Newitem)&&!ebay.IsAlreadyExcist(Newitem)&&Newitem.AvailabilityType.equals("now")&&Newitem.MaximumDaysToShip<=2)
			{
				Newitem.ReadyToUpload = true;
				System.out.println(Asin);
			}
			}catch(Exception e){}
			ListOfItems.add(Newitem);
			/*
			System.out.println("Checking Vero...");
			System.out.println("Is Vero ? "+ebay.IsVeroBrand(Newitem));
			System.out.println("Excisting list check...");
			System.out.println("Is Excisting ? "+ebay.IsAlreadyExcist(Newitem));
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void BulkCheckItemsBeforeUpload()
	{
		ArrayList<String> ListOfAsins = new ArrayList<String>();

		System.out.println("Reading from file...");

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader("C:\\Users\\Noname\\Desktop\\AsinForUplode.txt");
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				ListOfAsins.add(sCurrentLine);
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
		System.out.println("Reading from file ended...");
		
		System.out.println("Items checking start...");
		for (String ele:ListOfAsins)
		{
			CheckItemBeforeUpload(ele);
		}
		
		for (Item ele:ListOfItems)
		{
			if (ele.ReadyToUpload)
				System.out.println(ele.SupplierCode);
		}
		
		System.out.println("Items checking ended...");
	
	}

	private void GetResults0_7to30(ArrayList<Item> ListOfItemsAsins)
	{
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and (Uploaded = 0 or Uploaded is null)  and Amazon_Rank<50000 and Amazon_price>7 and Amazon_price<=30 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "GetResults0_7to30";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("GetResults0_7to30");}
	}

	private void GetResults0_3030AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where ebayResults=0 and Amazon_Rank<50000 and (Uploaded = 0 or Uploaded is null)   and Amazon_price>30 and Amazon_price<=2000 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "GetResults0_3030AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("GetResults0_3030AndHigher");}
	
	}
	private void Lowestprice1_7to30(ArrayList<Item> ListOfItemsAsins)
	{


		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where  placeinlowestprice = 1 and Uploaded = 0  and Amazon_Rank>0 and Amazon_Rank<50000 and Amazon_price>10 and Amazon_price<=30 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "Lowestprice1_7to30";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();
		} catch (SQLException e) {System.out.println("Lowestprice1_7to30");}
	
	
		
	}
	private void Lowestprice1_30AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where  placeinlowestprice = 1 and Uploaded = 0 and Amazon_Rank>0 and Amazon_Rank<50000 and Amazon_price>30 and Amazon_price<=2000 GROUP BY asin;");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "Lowestprice1_30AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	
	private void ProductFromSellers_10to30(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();// "SELECT * FROM amazon.productfromsellers where amazon_price>10 and amazon_price<30 and  (uploaded is null or  uploaded = 0 ) and (((Tax = 0 and breakevenforlowest>1.15) or (Tax >0 and breakevenforlowest>1.20)) and ((amazon_price>70  and amazon_price<1000 and soldlastweekall>0) or(amazon_price>8  and amazon_price<30 and soldlastweekall>3) or (amazon_price>30  and amazon_price<70 and soldlastweekall>1)));"
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.productfromsellers where  (uploaded is null or  uploaded = 0 ) and  (amazon_price>8  and amazon_price<30 and soldlastweekall>2);");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "ProductFromSellers_10to30";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	private void ProductFromSellers_30AndHigher(ArrayList<Item> ListOfItemsAsins)
	{

		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//"SELECT * FROM amazon.productfromsellers where amazon_price>30 and amazon_price<700 and breakevenforlowest<1.5 and (uploaded is null or  uploaded = 0 ) and (((Tax = 0 and breakevenforlowest>1.15) or (Tax >0 and breakevenforlowest>1.20)) and ((amazon_price>70  and amazon_price<1000 and soldlastweekall>1) or(amazon_price>8  and amazon_price<30 and soldlastweekall>3) or (amazon_price>30  and amazon_price<70 and soldlastweekall>1)));"
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.productfromsellers where (uploaded is null or  uploaded = 0 ) and ((amazon_price>70  and amazon_price<4000 and soldlastweekall>0) or (amazon_price>30  and amazon_price<70 and soldlastweekall>0));");

			while(res.next())
			{
				Item temp = new Item();
				temp.SupplierCode = res.getString("asin");
				temp.PathFolder = "ProductFromSellers_30AndHigher";
				ListOfItemsAsins.add(temp);
			}
			 con.close();
			 statement = null;
			 res = null;
			 System.gc();

		} catch (SQLException e) {System.out.println("Lowestprice1_30AndHigher");}

	}
	
	
	public void BulkCheckItemBeforeUpload() throws IOException, ParserConfigurationException, SAXException
	{

		ArrayList<Item> ListOfItemsAsins = new ArrayList<Item>();
		System.out.println("Reading files from database...");
		
		GetResults0_7to30(ListOfItemsAsins);
		
		GetResults0_3030AndHigher(ListOfItemsAsins);
		
		Lowestprice1_7to30(ListOfItemsAsins);
		
		Lowestprice1_30AndHigher(ListOfItemsAsins);
		//ProductFromSellers_10to30(ListOfItemsAsins);
		//ProductFromSellers_30AndHigher(ListOfItemsAsins);
		
		/*
		Item temp = new Item();
		temp.SupplierCode = "B00VQW8NDW";
		temp.PathFolder = "GetResults0_7to30";
		ListOfItemsAsins.add(temp);
		*/
		System.out.println("Reading finished...");
		System.out.println("Items checking start...");
		System.out.println("Amount of files before check is "+ListOfItemsAsins.size()); 
		RemoveDuplicatedItems(ListOfItemsAsins);
		
		
		RemoveAlreadyExcistitems(ListOfItemsAsins);
		System.out.println("Items amount after duplicate and excist check "+ListOfItemsAsins.size()); 
		AmazonApiCheck(ListOfItemsAsins);
		//check forbidden category //
		ItemsCheckingBeforeUpload(ListOfItemsAsins); /* BUG in preorder */
		RemoveNotReadyFiles(ListOfItemsAsins);
		PrintReadyToUploadItems(ListOfItemsAsins);
		System.out.println("Amount of files after check is "+ListOfItemsAsins.size());
		System.out.println("Items checking ended...");
		
		System.out.println("Getting images...");
		SaveImagesInFolders(ListOfItemsAsins);
		System.out.println("Getting images ended...");
		
	}
	
	private void SaveImagesInFolders(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		for(Item ele:ListOfItemsAsins)
		{
			try{
			System.out.println(ele.PathFolder);
			switch(ele.PathFolder)
			{
			case "GetResults0_7to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_7to30\\"+ele.SupplierCode+".png");
			break;
			
			case "GetResults0_3030AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\GetResults0_3030AndHigher\\"+ele.SupplierCode+".png");
			break;
			
			case "Lowestprice1_7to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_7to30\\"+ele.SupplierCode+".png");
			break;
			
			case "Lowestprice1_30AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\Lowestprice1_30AndHigher\\"+ele.SupplierCode+".png");
			break;
			case "ProductFromSellers_10to30":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_10to30\\"+ele.SupplierCode+".png");
			break;
			case "ProductFromSellers_30AndHigher":
			saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ItemsPictures\\Ubuythebest4u\\ProductFromSellers_30AndHigher\\"+ele.SupplierCode+".png");
			break;
			default :
				break;
			}
			}catch(Exception e){System.out.println(ele.PathFolder);}
		}
		
	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException 
	{
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile);

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) 
	    {
	    	os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}
	
	private void RemoveNotReadyFiles(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		Ebay ebay = new Ebay();
		Iterator<Item> i = ListOfItemsAsins.iterator();
		while (i.hasNext()) {
			if (i.next().ReadyToUpload==false) i.remove();
		}
		ebay = null;
		System.gc();
	}
	
	private void RemoveAlreadyExcistitems(ArrayList<Item> ListOfItemsAsins) throws IOException
	{
		Ebay ebay = new Ebay();
		/*
		Iterator<Item> i = ListOfItemsAsins.iterator();
		while (i.hasNext()) {
			if (ebay.IsAlreadyExcist(i.next())) i.remove();
		}
		*/
		ebay.IsAlreadyExcistBulk(ListOfItemsAsins);
		ebay = null;
		System.gc();
	}
	
	private void RemoveDuplicatedItems(ArrayList<Item> ListOfItemsAsins)
	{

		Set<Item> hs = new HashSet<>();
		hs.addAll(ListOfItemsAsins);
		ListOfItemsAsins.clear();
		ListOfItemsAsins.addAll(hs);
	}
	
	public  void AmazonApiCheck(ArrayList<Item> List) throws ParserConfigurationException, SAXException, IOException
	{
		int counter = 0;
		String Codes ="";
		Amazon AmazonApiCall = new Amazon();
		
		for (Item ele:List)
		{
			Codes+=ele.SupplierCode+",";
			if (counter==9)
			{
				counter++;	
				System.out.println(Codes);
				AmazonApiCall.BulkInfoRequest(Codes,List,counter);
				counter=0;
				Codes = "";
			}else 
			{
				counter++;	
			}
		}
		if (counter>0)
		{
			AmazonApiCall.BulkInfoRequest(Codes,List,counter);
		}
		
	}
	
	private void PrintReadyToUploadItems(ArrayList<Item> List)
	{
		System.out.println("--------------------------Report---------------------------");
		for(Item ele:List)
		{
			if (ele.ReadyToUpload)System.out.println(ele.SupplierCode);
			//else ele.ItemPrint();
		}
		System.out.println("--------------------------Report---------------------------");
	}
	
	private void ItemsCheckingBeforeUpload(ArrayList<Item> List) throws IOException
	{
		Ebay ebay = new Ebay();
		for (Item ele:List)//
		{
		try{
			ebay.IsVeroBrand(ele);
			ebay.ForbiddenWordsCheck(ele); /* It's checking the title */
			/*
			ebay.BrandCheckInContent(ele);
			ebay.BrandCheckInFeatures(ele);
			*/
			ebay.LinksCheck(ele);
			if (ele.prime == false)
			{
				ele.ReadyToUpload = false;
			}
			
			if (ele.IsPreorder == true) /* BUG */ 
			{
				ele.ReadyToUpload = false;
			}
			
			if (ele.IsNew == false)
			{
				ele.ReadyToUpload = false;
			}
			
			if (ele.MaximumDaysToShip >4)
			{
				ele.ReadyToUpload = false;
			}
			
			if (ele.AvailabilityType.equals("Not available"))
			{
				ele.ReadyToUpload = false;
				System.out.println("Not available "+ele.SupplierCode);
			}
			
			}
			catch(Exception e)
			{
				ele.ReadyToUpload = false;
			}
		}
	}
	
}
