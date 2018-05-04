package Gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseGui {

	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static Connection con = null;
	public static java.sql.Statement statement = null;
	public static String Connection = null;
	public static String scham = null;
	
	public DatabaseGui() {


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
				//	System.out.println("Connection = "+Connection);
				}
				
				if (sCurrentLine.contains("Schame:"))
				{
					scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame:")+8);
				//	System.out.println("Schame = "+scham);
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

	public void GetOrders(ArrayList<Order> OrderList)
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders;");
			res.last();
			do
			{
				Order order = new  Order();
				order.AmazonPriceBeforTax = res.getDouble("Amazon_price_before_tax");
				order.Tax = res.getDouble("Fees");
				order.Profit = res.getDouble("Profit");
				order.eBaySalePrice = res.getDouble("Total_price");
				order.eBayOrderId = res.getString("OrderId");
				order.BuyerUserId = res.getString("Buyer_User_ID");
				order.OrderStatus = res.getString("OrderStatus");
				order.Quantity    = res.getInt("Quantity");
				order.SaleDate = res.getString("Sale_date");
				order.AmazonOrderId = res.getString("Amazon_OrderNumber");
				order.Tracking = res.getString("Tracking");
				order.Carrier = res.getString("Carrier");
				order.FeedBack = res.getInt("Feedback_left");
				order.CheckOutStatus = res.getString("CheckoutStatus");
				order.EbayId = res.getString("EbayId");
				order.Asin = res.getString("AmazonAsin");
				order.BuyerFullname = res.getString("Buyer_Full_Name");
				order.Street = res.getString("Street");
				order.Street2 = res.getString("Street2");
				order.City = res.getString("City");
				order.State = res.getString("State_Province");
				order.Zip = res.getString("Zip_Postal_Code");
				order.PhoneNumber = res.getString("Phone_Number");
				order.StoreName = res.getString("StoreName");
				OrderList.add(order);
				}
				while (res.previous());
				
		} catch (SQLException e) {e.printStackTrace();}
	}
		
}
