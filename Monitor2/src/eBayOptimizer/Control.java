package eBayOptimizer;

import java.io.BufferedReader;
import java.io.File;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;


public class Control implements Runnable {
	
	public static String FILENAME = "C:\\Users\\Noname\\Desktop\\AsinForUplode.txt";
	public static String ImageDirectory = "C:\\Users\\Noname\\Desktop\\ImageBeforUplode";
	public static Connection con = null;
	public static ResultSet res = null;
	public static java.sql.Statement statement = null;//
	static final String FILENAMEINFO = "C:\\Keys\\ConfigFile-Keys.txt";
	static String Connection = null;
	static String scham = null;
	
	public Control(Connection con) {

		System.out.println("Constractor of Database");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAMEINFO);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				//System.out.println(sCurrentLine);
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("AsinForUplode:"))
				{
					FILENAME = sCurrentLine.substring(sCurrentLine.indexOf("AsinForUplode:")+15);
					//System.out.println("FILENAME = "+FILENAME);
				}
				
				if (sCurrentLine.contains("ImageDirectory:"))
				{
					ImageDirectory = sCurrentLine.substring(sCurrentLine.indexOf("ImageDirectory:")+16);
					//System.out.println("ImageDirectory = "+ImageDirectory);
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
	
	public void ItemsDeepResearch()  throws ApiException, SdkException, Exception
	{
		EbayCalls Call = new EbayCalls();
		GetSetDB Db = new GetSetDB();
		ArrayList<Item> List = new ArrayList<Item>();
		GetCodes(List);
		SetChecking(List);
		System.out.println("Calls amount = "+List.size());
		while (List.size()>0)
		{
		for(Item ele:List)
		{
		if (!ele.BestResults.equals(null)&&!ele.BestResults.equals("NA")
				&&!ele.BestResults.equals("Does not apply"))
		{
			System.out.println("Running...");
			System.out.println("------------------ Thread = "+Thread.currentThread().getId()+"-------------");
			
			Call.Find_Items_Info(ele);
			System.out.println("End...");
			ele.optimized = 1;
			Db.SetResults(ele,con);
		}
		else 
			System.out.println("No code to scan");

		}
		List.removeAll(List);
		Db.GetCodes(List,con);
		}
		Call = null;
		Db  = null;
		List  = null;
		System.gc();
		
	}

	public void GetImageBeforeUpload()
	{
		
		ArrayList<String> Asinlist = new ArrayList<String>(); 
		ArrayList<Item> ItemList = new ArrayList<Item>(); 
		
		ReadAsinsFromFile(Asinlist);
		for (int i=0;i<Asinlist.size();i++)
		{
			Item item = new Item();
			item.Asin = Asinlist.get(i);
			ItemList.add(item);
		}
		
		String Code = "";
		AmazonCalls AmazonCalls = new AmazonCalls();

		int counter = 0;
		try{

			for (int i=0;i<=ItemList.size();i++)
			{
			Code+=ItemList.get(i).Asin+",";
			if ((i%10)==9 || i==(ItemList.size()-1))
			{
			Code = Code.substring(0, Code.length()-1);
			System.out.println(Code);
			String Status = "Success";
			do
			{
				//Thread.sleep(2000);
				try{
					if (i==ItemList.size()-1)
					{
						System.out.println("Index = "+(ItemList.size()- ItemList.size()%10));
						Status = AmazonCalls.Items_LookUp_10(Code, "ASIN", ItemList,ItemList.size()- ItemList.size()%10);
					}
					else
					{
						System.out.println("Index = "+(i-9));
						Status = AmazonCalls.Items_LookUp_10(Code, "ASIN", ItemList,i-9);
					}

				}catch(Exception e){}
				if (Status.contains("Error")||Status.contains("error")) 
				{
					System.out.println("Retrying...");
					Thread.sleep(2000);
				}
				else 
					System.out.println("Success");
				Thread.sleep(4000);
			}while(Status.contains("Error"));

			Code = "";
			counter = 0;
			}else
			counter++;
			
			}
		}catch(Exception e){System.out.println("Global error");}

		System.out.println("******Saving photos******");
		/*
		for (int i=0;i<ItemList.size();i++)
		{
		try {
			System.out.println(ItemList.get(i).PicturesString[0]);
		} catch (Exception e) {System.out.println("Error Asin = "+ItemList.get(i).Asin);}
		}
		*/
		
		for (int i=0;i<ItemList.size();i++)
		{
		try {
			///Thread.sleep(500);
			saveImage(ItemList.get(i).PicturesString[0],"C:\\Users\\Noname\\Desktop\\ImageBeforUplode\\"+ItemList.get(i).Asin+".png");
			System.out.println(ItemList.get(i).Asin);
		} catch (Exception e) {
			System.out.println("Error Asin = "+ItemList.get(i).Asin);
			System.out.println(e);
		}
		}
		
		
	}
	
	public void RemoveDuplicats() throws SQLException
	{
		GetSetDB db = new GetSetDB();
		ArrayList<Item> list1 = new ArrayList<Item>();
		db.GetAllItemsFromSellersTable(list1);
		ArrayList<Item> list2 = new ArrayList<Item>();
		db.GetAllItemsFromSellersTable(list2);
		con = DriverManager.getConnection(Connection,"root","root");
		for (Item ele1:list1)
		{
			for (Item ele2:list2)
			{
				if (ele1.Asin!=null &ele2.Asin!=null)
				{
				if(ele1.Asin.equals(ele2.Asin) &&ele1.id!=ele2.id)	
					{
					System.out.println("Remove");
					db.removeid(ele2.id);
					}
				}
			}
			
		}
		
	}
	
	public void ReadAsinsFromFile(ArrayList<String> Asinlist)
	{


		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				Asinlist.add(sCurrentLine);
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
	
	public void GetSalesUpdate() throws ApiException, SdkException
	{
		ArrayList<Item> ItemsList = new ArrayList<Item> ();
		EbayCalls Call = new EbayCalls();
		GetSetDB Db = new GetSetDB();
		Db.GetAllOnline(ItemsList);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		for (Item ele:ItemsList)
		{
		try {
			Db.SetWatcher(ele.EbayId,Call.Getwatchers(ele.EbayId));
			Call.GetMyItemsHistory(ele, 30);
			Db.GetAndSetTax(ele);
			Db.SetItemsOnlineHistory(ele);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		System.out.println(dateFormat.format(date));
	}

	public void GetCategory()
	{
		AmazonCalls Calls = new AmazonCalls();
		String Code = "";
		String CodeType = "ASIN";
		ArrayList<Item> list = new ArrayList<Item>();
		GetSetDB Db = new GetSetDB();
		Db.GetAllAsins(list);
		System.out.println("List size = "+list.size());
		try {
			for (int i=0;i<list.size();i++)
			{
			if ((i%10==0&&i>0)||i ==list.size())
			{
			System.out.println(Code);
			Thread.sleep(1000);
			Calls.Items_LookUp_10(Code, CodeType, list, i-10);
			Code = "";
			}
			Code = Code+list.get(i).Asin+",";
			
			
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		} catch (SAXException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		for(Item ele:list)
		{
			System.out.println(ele.Asin+" = "+ele.Category);
		}
		
		try {
			Db.SetCategory(list);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

 	public void Update_All_Items_With_Item_Specific() 
	{
		GetSetDB Db = new GetSetDB();
		EbayCalls EbayCall = new EbayCalls();
		ArrayList<Item> ItemsList = new ArrayList<Item>() ;
		Db.GetAllOnlineItems(ItemsList); //all items without items specific full //
		System.out.println("Items List = "+ItemsList.size());
		for (Item ele:ItemsList)
		{
		try {
			EbayCall.AddItemSpecifics(ele.EbayId);
			System.out.println("Ebay id ="+ele.EbayId+" Success");
		} catch (ApiException e) {
			
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		} catch (SdkException e) {
			
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		}
		try {
			Db.SetItemUpdatedWIthItemspesific(ele);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		}
	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile);

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) {
	        os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}

	public void GetAsinAfterFilter()
	{
	
		File dir = new File(ImageDirectory);
		String[] files = dir.list();
		if (files.length == 0) {
		    System.out.println("The directory is empty");
		} else {
		    for (String aFile : files) {
		    	aFile = aFile.substring(0, aFile.indexOf("."));
		        System.out.println(aFile);
		    }
		}
	}
	
	public void GetCodes(ArrayList<Item> List)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			res = statement.executeQuery("SELECT * FROM "+scham+".items where date_scan >= '2017-09-20' and Amazon_price>7  and Deepsearch = 0 and placeinlowestprice<20;"); //and sold_on_ebay>1
			int counter =0;
			while (res.next())
			{
				Item item = new Item();
				item.BestResults = res.getString("bestresult");
				if (counter<50) 
					{
					counter++;
					//SetChecking(item);
					List.add(item);
					}
				else break;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}
	
	public void SetChecking(ArrayList<Item> ItemList) throws SQLException
	{
		con = DriverManager.getConnection(Connection,"root","root");
		statement = con.createStatement();
		for(Item ele: ItemList)
		{
		try{
			String WriteToData;
			WriteToData = "UPDATE  "+scham+".items SET Deepsearch = "+10+" where bestresult = '"+ele.BestResults+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		}
	}
	
	@Override
	public void run() {
		try {
			while(true)
			{
				ItemsDeepResearch();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	
}
