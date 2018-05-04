package MainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Database {

	public  static Connection con = null;
	public  static String  Connection= null;
	public  static String  User= null;
	public  static String  Pass= null;
	public  static java.sql.Statement statement= null;
	public  ResultSet res2 = null;
	public  String FILENAME = "C:\\keys\\ConfigFile-Keys.txt";
	public  static String scham = null;
	
	public Database() throws IOException, SQLException 
	{
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				/* Change the code here */
				if (sCurrentLine.contains("User:")) 
					{
						User = "root";
					}
				if (sCurrentLine.contains("Pass:")) 
					{
						Pass = "root";
					}
				if (sCurrentLine.contains("Connection:")) 
					{
						Connection = sCurrentLine.substring(sCurrentLine.indexOf("jdbc:mysql:"));
					}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+"Schame:".length()+1);
					Connection =Connection+scham;
				}
			}
			con = DriverManager.getConnection(Connection,"root","root");
			br.close();
	}

	public void Set_product_from_database(List<product> ItemsListToAmazon) throws SQLException
	{
		String WriteToData;
		statement = con.createStatement();//
		
		for(product ele:ItemsListToAmazon)
		{
		 try{
			WriteToData = "INSERT INTO "+scham+".items (Amazon_price,ASIN,Amazon_Rank,arbitraje,AvailabilityType,bestresult,ebayLowestPrice,ebayResults,UPC,Placeinlowestprice)"+
			"VALUES ("+ele.price+",'"+ele.ASIN+"',"+ele.Amazon_Rank+","+ele.arbitraje+",'"+ele.AvailabilityType+"','"+ele.bestresult+"',"+ele.ebayLowestPrice+","+ele.ebayResults+",'"+ele.UPC+"',"+ele.PlaceInlowestprice+");";
			statement.executeUpdate(WriteToData);
			}catch(SQLException e)
			{
				e.printStackTrace();
			}	
		}
		
	}
	
}
