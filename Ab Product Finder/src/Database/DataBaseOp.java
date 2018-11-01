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
	public static Connection conOut = null;
	public static java.sql.Statement statementOut = null;
	public static java.sql.Statement statement = null;
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
	
		
		try {
			con = DriverManager.getConnection(Connection,"root","root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			statement = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//

	}

	public int Get_eBay_Results_0()
	{
		try {
			int local;
			ResultSet res = statement.executeQuery("SELECT count(*) FROM "+scham+".items where ebayResults=0 and  amazon_rank<100000 and Uploaded = 0;");
			res.last();
			local = res.getInt(1);
			res.close();
			return local;
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int Get_eBay_Results_More_Than_0_Results_And_Place_In_lowest_Price_1()
	{
		try {
			int local;
			ResultSet res = statement.executeQuery("SELECT count(*) FROM "+scham+".items where ebayResults>0 and amazon_rank>0 and amazon_rank<20000 and Placeinlowestprice=1 and Uploaded = 0;");
			res.last();
			local = res.getInt(1);
			res.close();
			return local;
		} catch (SQLException e) {System.out.println(e);e.printStackTrace();return -1;}
	}

	public void UpdateUploadedSet1()
	{
		try {
			statement.executeUpdate("update "+scham+".items set Uploaded = 1;");
		} catch (SQLException e) 
		{
			e.printStackTrace();	
		}
	}
	
	public void  GetItemsFromAbFinder () throws InterruptedException
	{
		try 
		{
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".abfinderips;");
			String s = null;
			while(res.next())
			{
				s = res.getString("Ip");
				System.out.println("Getting items from "+s);
				GetResults0AbFinder(s);
				GetItemsThatIsCheapest(s);
				UpdateRemoteDBWith1(s);
			}
			res.close();
		} catch (SQLException e) 
		{
			System.out.println(e);
		}

	
	}
	
	public void  GetResults0AbFinder(String ip) throws InterruptedException, SQLException
	{
		int flag = 0;
		conOut = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
		statementOut = conOut.createStatement();//
		
		while(flag == 0)
		{
			try 
			{		
				System.out.println("Reading from Remote DB -> "+ip);
				ResultSet res = statementOut.executeQuery("SELECT * FROM items where Amazon_price>7 and ebayResults = 0 and uploaded =0 GROUP BY asin");
				while(res.next())
				{
					try{
					
					SetResultsAbFinder(res.getDouble("Amazon_price"),res.getString("ASIN"),res.getInt("Amazon_Rank"),res.getInt("ebayResults"),res.getInt("Placeinlowestprice"));
					Thread.sleep(15);
					}catch(Exception e)
					{
					}
				}
				System.out.println("Reading from Remote DB success");
				res.close();
				conOut.close();
				statementOut.close();
				flag =1;
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void  GetItemsThatIsCheapest(String ip) throws InterruptedException, SQLException
	{
		int flag = 0;
		conOut = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
		statementOut = conOut.createStatement();//
		
		while(flag == 0)
		{
			try 
			{
				System.out.println("Reading from Remote DB -> "+ip);
				ResultSet res = statementOut.executeQuery("SELECT * FROM items where Amazon_price>7 and Placeinlowestprice = 1 and uploaded =0 GROUP BY asin");
				while(res.next())
				{
					try{
					SetResultsAbFinder(res.getDouble("Amazon_price"),res.getString("ASIN"),res.getInt("Amazon_Rank"),res.getInt("ebayResults"),res.getInt("Placeinlowestprice"));
					Thread.sleep(10);
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
				System.out.println("Reading from Remote DB success");
				flag =1;
				res.close();
				conOut.close();
				statementOut.close();
			} catch (SQLException e) 
			{
				System.out.println("Reading from Remote DB Fail");
				e.printStackTrace();
				flag =1;
			}
		}
		
	}
	
	public void  UpdateRemoteDBWith1(String ip) throws SQLException
	{
		try 
		{
			conOut = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/amazon","root","root");
			statementOut = conOut.createStatement();//
			System.out.println("Updating Remote DB -> "+ip);
			statementOut.executeUpdate("update "+scham+".items set Uploaded = 1;");
			System.out.println("Updating Remote DB success");
		} catch (SQLException e) 
		{
			System.out.println("Updating Remote DB Fail");
			e.printStackTrace();
		}

		
	}
	
	public void  SetResultsAbFinder(double Amazon_price ,String ASIN ,int Amazon_Rank,int ebayResults,int Placeinlowestprice) throws SQLException
	{
		try{
			String WriteToData = "INSERT INTO "+scham+".items (Amazon_price,ASIN,Amazon_Rank,ebayResults,Placeinlowestprice)"+
			"VALUES ("+Amazon_price+",'"+ASIN+"',"+Amazon_Rank+","+ebayResults+","+Placeinlowestprice+");";
			statement.executeUpdate(WriteToData);
		}catch(SQLException e)
		{
			e.printStackTrace();
		}			
	}
}
