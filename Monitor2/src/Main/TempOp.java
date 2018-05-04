package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TempOp {

	public String ebayId = null; 
	public Date startdate = null; 
	public String SaleDate = null; 
	public long diffrence = 1000;
	
	public void GetDaysForFIrstSale() throws SQLException, ParseException
	{
		//getEbayId
		ArrayList<TempOp> list = new ArrayList<TempOp>();
		GetOnline(list);
		for(TempOp ele:list)
		{
			GetOrders(ele);
		}
		
		long counter = 0;
		long sum = 0;
		for(TempOp ele:list)
		{
			if (ele.diffrence<1000 &&ele.diffrence>0)
			{
			System.out.println("ebayId = "+ele.ebayId);	
			System.out.println("TempOp.startdate = "+ele.startdate);
			System.out.println("TempOp.SaleDate  = "+ele.SaleDate);
			System.out.println("diffrence = "+ele.diffrence);
			System.out.println("----------------------------");	
			sum += ele.diffrence;
			counter++;
			}
		}
		
		System.out.println("Avg days for sale = "+sum/counter);
		
	}
	
	public void GetOnline(List<TempOp> List) throws SQLException
	{
		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("select * from amazon.online");
			while (res.next())
			{
				TempOp temp = new TempOp();
				temp.ebayId = res.getString("EbayId");
				temp.startdate = res.getDate(7);
				List.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void GetOrders(TempOp TempOp) throws SQLException, ParseException
	{
		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4444/amazon","root","root");
			java.sql.Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("select * from amazon.orders where EbayId = '"+TempOp.ebayId+"';");

			res.last();
			
			if (res.getRow()>0)
			{
			while (res.previous())
			{
				TempOp.SaleDate = res.getString("Sale_date");
				if (differenceDays(TempOp.startdate,TempOp.SaleDate)<TempOp.diffrence)
				TempOp.diffrence = 	differenceDays(TempOp.startdate,TempOp.SaleDate);
			}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public long differenceDays(Date StartDate , String FirstSaleDate) throws ParseException
	{
		if (StartDate !=null && FirstSaleDate !=null)
		{
		DateFormat format = new SimpleDateFormat("EEE MMM dd hh:kk:ss z yyyy", Locale.ENGLISH);
		//Date date  = format2.parse(StartDate);
		Date date2 = format.parse(FirstSaleDate);
		
		long difference = date2.getTime() - StartDate.getTime();
		long differenceDays = difference / (1000 * 60 * 60 * 24);
		//System.out.println(differenceDays);
		return differenceDays;
		}return -1;
		
	}
	
	public void GetOrders()
	{}

}
