package eBayOptimizer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel {

	public  int Placeinlowestprice = -1;
	public  int Placeinbestmatch = -1;
	public  int Feedbackamount = -1;
	static final String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	static Connection con = null;
	static java.sql.Statement statement = null;
	static String Connection = null;
	static String scham = null;
	
	public Excel() {

		System.out.println("Constractor of Database");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				//System.out.println(sCurrentLine);
				if (sCurrentLine.contains("Connection:"))
				{
					Connection = sCurrentLine.substring(sCurrentLine.indexOf("Connection:")+12);
					//System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
					//System.out.println("Schame = "+scham);
					Connection =Connection+scham;
					//System.out.println("Connection = "+Connection);
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

				ex.printStackTrace();

			}
			}
	
	}

	public void ExcelMain()
	{
		ArrayList<Excel> List = new ArrayList<Excel>();
		GetInfo(List);
		{
	        try {
	            String filename = "D:/New.xls" ;
	            HSSFWorkbook workbook = new HSSFWorkbook();
	            HSSFSheet sheet = workbook.createSheet("FirstSheet");  

	            
	            HSSFRow rowhead = sheet.createRow((short)0);
	            rowhead.createCell(0).setCellValue("No.");
	            rowhead.createCell(1).setCellValue("Placeinbestmatch");
	            rowhead.createCell(2).setCellValue("Placeinlowestprice");
	            rowhead.createCell(3).setCellValue("Fedbackamount");
	            int counterplaceinbestmatch = 0;
	            int counterplaceinlowestprice = 0;
	            int Fedbackamount = 0;
	            for(int counter =1; counter<=50;counter++)
	            {
	            	for (Excel ele1:List)
	            	{
		            	if (counter==ele1.Placeinbestmatch) counterplaceinbestmatch++;
		            	if (counter==ele1.Placeinlowestprice) counterplaceinlowestprice++;
		            	if (counter==ele1.Feedbackamount) Fedbackamount++;
	            	}
	            HSSFRow row = sheet.createRow((short)counter);
	            row.createCell(0).setCellValue(counter);
	            row.createCell(1).setCellValue(counterplaceinbestmatch);
	            row.createCell(2).setCellValue(counterplaceinlowestprice);
	            row.createCell(3).setCellValue(Fedbackamount);
	            counterplaceinbestmatch=0;
	            counterplaceinlowestprice=0;
	            Fedbackamount = 0;
	            }
	            
	            FileOutputStream fileOut = new FileOutputStream(filename);
	            workbook.write(fileOut);
	            fileOut.close();
	            System.out.println("Your excel file has been generated!");
	            workbook.close();
	        } catch ( Exception ex ) {
	            System.out.println(ex);
	        }
	    
	}

	}
	
	public void GetInfo(ArrayList<Excel> List)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".salesrecord;"); 
			while (res.next())
			{
				Excel temp = new Excel();
				temp.Placeinbestmatch = res.getInt("Placeinbestmatch");
				temp.Placeinlowestprice = res.getInt("Placeinlowestprice");
				temp.Feedbackamount =  res.getInt("Placeinfeedbackamount");
				List.add(temp);
			}
			res = null;
			System.gc();
		} catch (SQLException e) {e.printStackTrace();}
	}
		
}
