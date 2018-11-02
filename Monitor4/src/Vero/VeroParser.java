package Vero;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import DataBase.IDataBase;
import DataBase.SQLDataBase;


public class VeroParser {

	IDataBase 	  		 SQLDb;

	/* Contractor */
	
	public VeroParser() {
		SQLDb  = new SQLDataBase();
	}

	/* Public function */
	
	public void TMFilesParser(String pathToFolder) throws SQLException, IOException, InterruptedException
	{
		ArrayList<String> list = new ArrayList<String>();
		list = GetFilesNameFromFolder(pathToFolder);
		for(String ele:list)
		{
			ParseFile(ele);
		}
	}

	
	
	/* Private function */
	
	private ArrayList<String> GetFilesNameFromFolder(String path)
	{
		ArrayList<String> FilesList = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) 
	    {	
	      if (listOfFiles[i].isFile()) 
	      {
	    	FilesList.add("D:\\Downloads\\TM-XML\\"+listOfFiles[i].getName());
	      } 
	    }
	    
		return FilesList;
	}
	
	private void ParseFile(String PathToFile) 
	{
		try {
			String str;
			ArrayList<String> BrandsName = new ArrayList<String>();
			FileInputStream fileInput = new FileInputStream(PathToFile);
			DataInputStream dataInput = new DataInputStream(fileInput);
			InputStreamReader inputStr = new InputStreamReader(dataInput);
			BufferedReader bufRead = new BufferedReader(inputStr);

			while ((str = bufRead.readLine()) != null) {
				if (str.contains("mark-identification")) {
					str = str.substring(str.indexOf("mark-identification") + ("mark-identification").length() + 1);
					str = str.replace("</mark-identification>", "");
					BrandsName.add(str);
				}

				if (str.contains("mark-text")) {
					str = str.substring(str.indexOf("mark-text") + ("mark-text").length() + 1);
					str = str.replace("</mark-text>", "");
					BrandsName.add(str);
				}
			}

			RemoveDuplicatedBrands(BrandsName);
			UpdateBrandsInDatabase(BrandsName);
			
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void RemoveDuplicatedBrands(ArrayList<String> BrandsName) 
	{
		Set<String> hs = new HashSet<>();
		hs.addAll(BrandsName);
		BrandsName.clear();
		BrandsName.addAll(hs);
	}

	private void UpdateBrandsInDatabase(ArrayList<String> BrandsName)
	{
		for(String ele:BrandsName)
		{
			if (!SQLDb.IsExcist("SELECT * FROM amazon.vero where Brand = '"+ele.trim().toLowerCase()+"'"))
			{
				SQLDb.Write("INSERT INTO amazon.vero (Brand) VALUES ('"+ele.trim().toLowerCase()+"')");
			}
		}
	}
}
