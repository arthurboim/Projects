package DataBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLDataBase implements IDataBase{

	private  Connection con ;
	private  java.sql.Statement statement;
	private  String Scham;
	private  String Connection;
	private  String User;
	private  String Pass;
	private  String Port;
	private  ResultSet res;

	
	/* Contractor */
	
	public SQLDataBase() {
		try{
		ReadDataBaseConfigurations(Config.KeysFilePath);
		}catch(Exception e)
		{
			System.out.println("Exception in SQLDataBase: "+e.getMessage() );
		};
		
		OpenConnection();
	}
	
	public SQLDataBase(String KeysFilePath) {
		try{
			ReadDataBaseConfigurations(KeysFilePath);
		}catch(Exception e)
		{
			System.out.println("Exception in SQLDataBase: "+e.getMessage() );
		};
		OpenConnection();
	}
	
	private void ReadDataBaseConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(KeysFilePath);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("Connection: "))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+"Connection: ".length());
				}
				
				if (sCurrentLine.contains("Schame: "))
				{
					Scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame: ")+"Schame: ".length());
				}
				
				if (sCurrentLine.contains("DataBaseUser: "))
				{
					User = sCurrentLine.substring(sCurrentLine.indexOf("DataBaseUser: ")+"DataBaseUser: ".length());
				}
				
				if (sCurrentLine.contains("DataBasePass: "))
				{
					Pass = sCurrentLine.substring(sCurrentLine.indexOf("DataBasePass: ")+"DataBasePass: ".length());
				}
				
				if (sCurrentLine.contains("Port: "))
				{
					Port = sCurrentLine.substring(sCurrentLine.indexOf("Port: ")+"Port: ".length());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			
			try {
				
				if (br != null)
					br.close();
				
				if (fr != null)
					fr.close();
				
			} catch (IOException ex) {
				
				ex.printStackTrace();
				throw ex;
			}
		}

		Connection+=Port;
		Connection+="/";
	
	}
	
	
	
	/* Database operations */
	
	public void OpenConnection()  
	{
		try {
			con = DriverManager.getConnection(Connection,User,Pass);
			statement = con.createStatement();//
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CloseConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Write(String statment)
	{
		try {
			statement.executeUpdate(statment);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}
	
	public ResultSet Read(String statment)
	{
		try {
			res = statement.executeQuery(statment);
		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}
		return res;
	}
	
		@Override
	public int GetResultsAmount(String statment) {
		int ResultsAmount = 0;
		
		try {
			res = Read(statment);
			res.beforeFirst();
			res.last();  
			ResultsAmount = res.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
		return ResultsAmount;
	}
	
	@Override
	public Boolean IsExcist(String statment) 
	{
		int size = 0;
		Boolean isExcist = false;
		size = GetResultsAmount(statment);
		if (size > 0)
		{
			isExcist = true;
		}
		
		return isExcist;
	}

	
	/* Getters and Setters */
	
	public ResultSet getRes() {
		return res;
	}


	
	/* Destractor override */
	
	@Override
	protected void finalize() throws Throwable {
		CloseConnection();
	}




	
}
