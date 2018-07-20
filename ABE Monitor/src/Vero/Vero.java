package Vero;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vero {

	
	
	public void AddNewVeroFromFile(String Path) throws IOException, SQLException
	{
		BufferedReader br = null;
		FileReader fr = null;
		fr = new FileReader(Path);
		br = new BufferedReader(fr);
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) 
		{
			if (!CheckIfExcist(sCurrentLine)) 
			{
				addToDb(sCurrentLine);
			}
		}
		br.close();
	}
	
	public void addToDb(String VeroBand) throws SQLException
	{
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/","root","root");
		java.sql.Statement statement = con.createStatement();
		statement.executeUpdate("INSERT INTO amazon.vero SET Brand = '"+VeroBand.replace("'", "").toLowerCase()+"';");
	}
	
	public Boolean CheckIfExcist(String VeroBand)
	{

		try {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/","root","root");
		java.sql.Statement statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM amazon.vero;");
		String tempBrand;
		VeroBand = VeroBand.trim();
		while(res.next())
		{
			tempBrand = res.getString("Brand").toLowerCase();
			tempBrand = tempBrand.trim();
			if (tempBrand.equals(VeroBand.replace("'", "").toLowerCase())) 
			{
				return true;
			}
		}
		System.out.println("Adding "+VeroBand.replace("'", "").toLowerCase());
		return false;
		} catch (SQLException e) {e.printStackTrace();return false;}
		
	
	}
	
}
