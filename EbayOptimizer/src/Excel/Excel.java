package Excel;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Database.Item;

public class Excel {

	public  int Placeinlowestprice = -1;
	public  int Placeinbestmatch = -1;
	public  int Feedbackamount = -1;
	
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
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.salesrecord;"); 
			while (res.next())
			{
				Excel temp = new Excel();
				temp.Placeinbestmatch = res.getInt("Placeinbestmatch");
				temp.Placeinlowestprice = res.getInt("Placeinlowestprice");
				temp.Feedbackamount =  res.getInt("Placeinfeedbackamount");
				List.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	
	
}
