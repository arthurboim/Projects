package Controller;

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

import Amazon.AmazonCalls;
import Database.GetSetDB;
import Database.Item;
import Ebay.EbayCalls;

public class Control implements Runnable {
	
	private static final String FILENAME = "C:\\Users\\Noname\\Desktop\\AsinForUplode.txt";
	public static String ImageDirectory = "C:\\Users\\Noname\\Desktop\\ImageBeforUplode";
	public static Connection con = null;
	public static ResultSet res = null;
	public static java.sql.Statement statement = null;//
	

	public Control(Connection con) {
		this.con = con;
	}
	
	public void ItemsDeepResearch()  throws ApiException, SdkException, Exception
	{
		EbayCalls Call = new EbayCalls();
		GetSetDB Db = new GetSetDB();
		ArrayList<Item> List = new ArrayList<Item>();
		GetCodes(List);

		System.out.println("Calls amount = "+List.size());
		while (List.size()>0)
		{
		for(Item ele:List)
		{
		if (!ele.BestResults.equals(null)&&!ele.BestResults.equals("NA")
				&&!ele.BestResults.equals("Does not apply"))
		{
			System.out.println("Checkign code = "+ele.BestResults);
			Call.Find_Items_Info(ele);
			ele.optimized = 1;
			Db.SetResults(ele,con);
		}
		else 
			System.out.println("No code to scan");

		}
		List.removeAll(List);
		List = null;
		List = new ArrayList<Item>();
		System.gc();
		Db.GetCodes(List);
		}
		Call = null;
		Db  = null;
		List  = null;
		System.gc();
		
	}

	public void GetImageBeforeUpload() throws InterruptedException, ParserConfigurationException, SAXException, IOException
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


		System.out.println("size = "+ItemList.size());
		
		int success = 0;
		for (Item ele:ItemList)
		{
			while(success==0){
			try{
			AmazonCalls.Items_LookUp(ele.Asin,"ASIN",ele);
			success = 1;
			}catch(Exception e1){success=0;}
			}

			for (String ele1:ele.Categorys)
			{
				if (ele1.equals("3760931")||ele1.equals("133141011")||ele1.equals("599872")||ele1.equals("2350150011")||ele1.equals("624868011")||ele1.equals("3222119011"
						)||ele1.equals("409488")||ele1.equals("2858778011")||ele1.equals("3760931")||ele1.equals("3412851")||ele1.equals("3760911")||ele1.equals("3222111011")||ele1.equals("16310101"
						)||ele1.equals("3480662011")||ele1.equals("4078751")||ele1.equals("3764421")||ele1.equals("3764411")||ele1.equals("3764401")||ele1.equals("3764411")||ele1.equals("3400861")||ele1.equals("3760901")||ele1.equals("13280251")
						||ele1.equals("3225960011")||ele1.equals("13364369011")||ele1.equals("13364359011")||ele1.equals("6973718011")||ele1.equals("")) //3400861
				{
					ele.upload = 0;
				}
				System.out.println(ele1);
			}
			if (ele.upload==-1) ele.upload=1;
			if (ele.upload==1)
			{
			try {
				saveImage(ele.PicturesString[0],"C:\\Users\\Noname\\Desktop\\ImageBeforUplode\\"+ele.Asin+".png");
				//here we need to se the value to upload
				UpdateUploaded(ele.Asin);
				System.out.println(ele.Asin);
			} catch (Exception e) {
				System.out.println("Error Asin = "+ele.Asin);
				System.out.println(e);
			}
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
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		for (Item ele1:list1)
		{
			for (Item ele2:list2)
			{
				if (ele1.Asin!=null &ele2.Asin!=null)
				{
				if(ele1.Asin.equals(ele2.Asin) &&ele1.id!=ele2.id)	
					{
					System.out.println("Remove");
					db.removeid(ele2.id,con);
					}
				}
			}
			
		}
		
	}
	
	public void UpdateUploaded(String Asin) throws SQLException
	{
		con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "Update amazon.productfromsellers set uploaded = "+1+" where asin = '"+Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		} catch (SdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Ebay id ="+ele.EbayId+" Fail");
		}
		try {
			Db.SetItemUpdatedWIthItemspesific(ele);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		    	
//		    	aFile = aFile.substring(0, aFile.indexOf("."));
//		        System.out.println(aFile);
		       
		    	if (aFile.contains("=")){
		    		aFile = aFile.substring(aFile.lastIndexOf("=")+1);
		    	}
		        System.out.println("http://freshrecords.ru/gmp3.php?fid="+aFile);
		        
		    }
		}
	}
	
	public synchronized void GetCodes(ArrayList<Item> List) throws SQLException, InterruptedException
	{
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			statement = con.createStatement();//
			res = statement.executeQuery("SELECT * FROM amazon.items where  breakevenlowestprice>1.14  and  breakevenlowestprice<1.8 and tax is null and (uploaded is null or uploaded = 0);"); //and sold_on_ebay>1
			int counter =0;
			while (res.next())
			{
				Item item = new Item();
				item.BestResults = res.getString("bestresult");
				if (counter<1) 
					{
					counter++;
					SetChecking(item);
					Thread.sleep(1000);
					List.add(item);
					}
				else break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		con.close();
		statement = null;

	}
	
	public static void SetChecking(Item item) throws SQLException
	{
		con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
		statement = con.createStatement();

		try{
			String WriteToData;
			WriteToData = "UPDATE  amazon.items SET Deepsearch = "+10+" where bestresult = '"+item.BestResults+"';";		  
			statement.executeUpdate(WriteToData);
			}
			catch(SQLException e)
			{
			e.printStackTrace();
			}	
		
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			while(true)
			{
			ItemsDeepResearch();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
