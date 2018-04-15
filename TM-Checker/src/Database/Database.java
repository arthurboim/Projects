package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

	
	Connection con = null;
	java.sql.Statement statement = null;
	String WriteToData = null;
	
	public Database() 
	{
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:4444/","root","root");
			statement = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/*check brand if excist */
	public int isExcist(String BrandName) throws SQLException
	{
		ResultSet res = null;
		
		if (BrandName.contains("'"))
		{
			BrandName = BrandName.replace("'", "\\'");
		}
		BrandName = BrandName.trim();
		try{
		res = statement.executeQuery("SELECT * FROM amazon.vero where Brand = '"+BrandName+"'");
		}catch(Exception e)
		{
			/*System.out.println(e);*/
		}
		
		if(res == null)
		{
			return 1;
		}
		return res.getRow();
	}
	
	
	public int AddToDb(String BrandName) throws SQLException
	{
		if (BrandName.contains("'"))
		{
			BrandName = BrandName.replace("'", "\\'");
		}
		BrandName = BrandName.trim();
		WriteToData = "INSERT INTO amazon.vero (Brand) VALUES ('"+BrandName+"')";
		
		try {
			statement.executeUpdate(WriteToData);
			/*System.out.println(BrandName);*/
		} catch (SQLException e) {
			/*System.out.println(e);*/
			return 0;
		}
		return 1;
	}
	
	/* Add brand to Db*/
	public void AddBrandToDb(Connection con,java.sql.Statement statement, ArrayList<String> BrandsName) throws SQLException
	{
		int i,counter = 0;
		
		for(i=0;i<BrandsName.size();i++)
		{
			try{
				//if(isExcist(BrandsName.get(i)) == 0)

			if(BrandsName.get(i).contains("'"))	
			{
				System.out.println(BrandsName.get(i));
				counter+=AddToDb(BrandsName.get(i));
				
			}
			}catch(Exception e){}
		}
		
		System.out.println(counter+" brands add to database");
		
		return;
	}
	
	
}
