package Control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import Database.Database;
import Files.FilesGraber;
import Parser.FileDecoder;

public class MainControl {

	
	public void TMFilesParser() throws SQLException, IOException, InterruptedException
	{
		int i;
		Database Db = new Database();
		ArrayList<String> list;
		FilesGraber Graber = new FilesGraber();
		Connection con = null;
		java.sql.Statement statement = null;
		
		list = Graber.GetFilesNameFromFolder("D:\\Downloads\\TM-XML");
		ArrayList<ArrayList<String>> BrandsList = new ArrayList<ArrayList<String>>();
		

		for(i=0;i<list.size();i++)
		{
			BrandsList.add(new ArrayList<String>());
		}
		
		for(i=0; i<list.size();i++)
		{
			Thread t = new Thread(new FileDecoder(list.get(i),BrandsList.get(i)));
			t.start();
			System.out.println("File number: "+i);
			t.join();
		}
		
		for(i=0; i<list.size();i++)
		{
			Db.AddBrandToDb(con, statement,BrandsList.get(i));
			BrandsList.removeAll(BrandsList.get(i));
		}
	}
}
