package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseOp 
{
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static Connection con2 = null;
	public static java.sql.Statement statement = null;
	public static java.sql.Statement statement2 = null;
	public static String Connection = null;
	public static String scham = null;

	public DataBaseOp() // constractor //
	{


		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					Connection =Connection+scham;
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
				System.out.println(ex);
				ex.printStackTrace();

			}
			}
	
		
	

	}

	public int Get_eBay_Results_0()
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT count(*) FROM "+scham+".items where ebayResults=0 and  amazon_rank<100000 and Uploaded = 0;");
			res.last();
			//res.getRow();
			System.out.println(res.getInt(1));
			return res.getInt(1);
		} catch (SQLException e) {System.out.println(e);e.printStackTrace();return -1;}
		
	}
	
	public int Get_eBay_Results_More_Than_0_Results_And_Place_In_lowest_Price_1()
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT count(*) FROM "+scham+".items where ebayResults>0 and amazon_rank>0 and amazon_rank<20000 and Placeinlowestprice=1 and Uploaded = 0;");
			res.last();
			System.out.println(res.getInt(1));
			return res.getInt(1);
		} catch (SQLException e) {System.out.println(e);e.printStackTrace();return -1;}
	}

	public void UpdateUploadedSet1()
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			statement.executeUpdate("update "+scham+".items set Uploaded = 1;");
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public void  GetItemsFromAbFinder () throws InterruptedException
	{
	try 
	{
		Connection con3 = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement3 = con3.createStatement();//
		ResultSet res3 = statement3.executeQuery("SELECT * FROM "+scham+".abfinderips;");
		String s = null;
		
		while(res3.next())
		{
			s = res3.getString("Ip");
			System.out.println("Getting items from "+s);
			GetResults0AbFinder(s);
			GetItemsThatIsCheapest(s);
			UpdateRemoteDBWith1(s);
		}
		
		res3.close();
		res3 = null;
		System.gc();
	} catch (SQLException e) {System.out.println("GetItemsFromAbFinder");System.out.println(e);}

	
	}
	
	public void  GetResults0AbFinder(String ip)
	{
		try 
		{				
			System.out.println("GetResults0AbFinder Start...");
			System.out.println("jdbc:mysql://"+ip+":3306/amazon");
			con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where Amazon_price>10 and amazon_rank>0 and amazon_rank<20000 and ebayResults = 0 and uploaded =0 GROUP BY asin");
			while(res.next())
			{
				SetResultsAbFinder(res.getDouble("Amazon_price"),res.getString("ASIN"),res.getInt("Amazon_Rank"),res.getInt("ebayResults"),res.getInt("Placeinlowestprice"));
			}
			res.close();
			System.out.println("GetResults0AbFinder ended...");
			res = null;
			System.gc();
		} catch (SQLException e) {System.out.println("GetResults0AbFinder");e.printStackTrace();}
	}
	
	public void  GetItemsThatIsCheapest(String ip)
	{
		try 
		{
			System.out.println("GetItemsThatIsCheapest start...");
			System.out.println("jdbc:mysql://"+ip+":3306/amazon");
			con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM items where Amazon_price>10 and amazon_rank>0 and amazon_rank<20000 and Placeinlowestprice = 1 and uploaded =0 GROUP BY asin");
			while(res.next())
			{
				SetResultsAbFinder(res.getDouble("Amazon_price"),res.getString("ASIN"),res.getInt("Amazon_Rank"),res.getInt("ebayResults"),res.getInt("Placeinlowestprice"));
			}
			res.close();
		} catch (SQLException e) {System.out.println("GetItemsThatIsCheapest");e.printStackTrace();}

		
	}
	
	public void  UpdateRemoteDBWith1(String ip)
	{
		try 
		{
			System.out.println("Setting 1 to "+ip);
			System.out.println("jdbc:mysql://"+ip+":3306/amazon");
			con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
			statement = con.createStatement();//
			statement.executeUpdate("update "+scham+".items set Uploaded = 1;");

		} catch (SQLException e) 
		{
			System.out.println("UpdateRemoteDBWith1");
			e.printStackTrace();
		}

		
	}
	
	public void  SetResultsAbFinder(double Amazon_price ,String ASIN ,int Amazon_Rank,int ebayResults,int Placeinlowestprice) throws SQLException
	{
		
		 con2 = DriverManager.getConnection(Connection,"root","root");
		 statement2 = con2.createStatement();
		try{
				String WriteToData;
				WriteToData = "INSERT INTO "+scham+".items (Amazon_price,ASIN,Amazon_Rank,ebayResults,Placeinlowestprice)"+
				"VALUES ("+Amazon_price+",'"+ASIN+"',"+Amazon_Rank+","+ebayResults+","+Placeinlowestprice+");";
				System.out.println(WriteToData);
				statement2.executeUpdate(WriteToData);
		}catch(SQLException e){e.printStackTrace();}			
	}
}
