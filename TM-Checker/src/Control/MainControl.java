package Control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
		/*FileDecoder Decoder = new FileDecoder();*/
		Connection con = null;
		java.sql.Statement statement = null;
		ArrayList<String> temp;
		
		list = Graber.GetFilesNameFromFolder("D:\\Downloads\\TM-XML");
		ArrayList<ArrayList<String>> BrandsList = new ArrayList<ArrayList<String>>();
		

		for(i=0;i<list.size();i++)
		{
			BrandsList.add(new ArrayList<String>());
		}
		
		
		for(i=0; i<list.size();i++)
		{
			
			/*Decoder.ParseFile(list.get(i),BrandsList);
			BrandsList.set(i, new ArrayList<String>());*/
			Thread t = new Thread(new FileDecoder(list.get(i),BrandsList.get(i)));
			t.start();
			//t.join();
			/*Decoder.ParseFileWithScanner(list.get(i),BrandsList);*/
			while(FileDecoder.numofthreads>10);
			System.out.println("Num of threads: "+FileDecoder.numofthreads);

			System.out.println("File number: "+i);
			Db.AddBrandToDb(con, statement,BrandsList.get(i));
			BrandsList.removeAll(BrandsList.get(i));
		}
		
		//Db.AddBrandToDb(con, statement,BrandsList);
	}
}
