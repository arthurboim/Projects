package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ebay.soap.eBLBaseComponents.OrderType;

public class DatabaseMain {

	public static double ebay_fees = 0.09;
	public static double paypal_fees = 0.039;
	public static double paypal_fixed = 0.3;
	static final String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	static Connection con = null;
	static java.sql.Statement statement = null;
	static String Connection = null;
	static String scham = null;
	public static  ResultSet res = null;

	public DatabaseMain() {
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

	public int SelecteOrderingOrOrderErrorAndReturnLast() throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				try{
					String WriteToData;
					WriteToData = "select * from "+scham+".orders where orderstatus = 'Ordering' or orderstatus = 'Order_Error';";
					ResultSet res = statement.executeQuery(WriteToData);
					res.last();
					return res.getInt("id");
					}catch(SQLException e){e.printStackTrace();}		
				con.close();return -1;
	}
	
	public void DeleteOrderById(int id) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				try{
					String WriteToData;
					WriteToData = "delete from "+scham+".orders where id = "+id+";";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e){e.printStackTrace();}	
				con.close();
	}
	
	void SetDatabase(List<ProductOnline> List) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		for (ProductOnline ele:List)
		{
			if (GetProductInfo(ele.getAmazonAsin())==null)
			{
				try{
					String WriteToData;
					WriteToData = "INSERT INTO "+scham+".online (AmazonAsin,EbayId,Title,Price)"+
					"VALUES ('"+ele.getAmazonAsin()+"','"+ele.getEbayId()+"','"+ele.getTitle()+"',"+ele.getPrice()+");";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						System.out.println("set error in product "+ele.getAmazonAsin());
					}			
			}else {
				System.out.println("The product "+ele.getAmazonAsin()+" is already in database");
			}

		}
		con.close();
		
	
	}
	
	public String GetProductInfo(String Asin) //not working //
	{
		ProductOnline Product = new ProductOnline();//
		int size=0;
		try {
			Connection con = DriverManager.getConnection(Connection,"root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("select * from "+scham+".online where AmazonAsin = '"+Asin+"'");
			while (res.next())
			{
				size++;
				Product.setAmazonAsin(res.getString("AmazonAsin"));
				Product.setEbayId(res.getString("EbayId"));
				Product.setPrice(res.getDouble("Price"));
				Product.setTitle(res.getString("Title"));
			}
			con.close();
			if (size ==0) return null;
		} catch (SQLException e) {
			
			e.printStackTrace();
			Product=null;
			System.out.println("Error in reading product info");
			return "error from catch";
		}
		return Product.getAmazonAsin();
	}

	void GetDatabase(List<ProductOnline> List) throws SQLException
	{
		try {
			
			 con = DriverManager.getConnection(Connection,"root","root");
			 statement = con.createStatement();//
			ResultSet res = statement.executeQuery("select * from "+scham+".online");
			
			while (res.next())
			{
				ProductOnline temp = new ProductOnline();
				temp.AmazonAsin = res.getString("AmazonAsin");
				temp.EbayId = res.getString("EbayId");
				temp.Price = res.getDouble("Price");
				//temp.ProfitPersent = res.getInt("ProfitPersent");
				List.add(temp);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public String GetAsinFromOnlineProducts(String EbayId)
	{
		ProductOnline Product = new ProductOnline();//
		int size=0;
		String id = EbayId;
		try{
		id = id.substring(0,id.indexOf("-"));
		}catch(Exception e){}

		try {
			Connection con = DriverManager.getConnection(Connection,"root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("select * from "+scham+".online where EbayId = '"+id+"'");

			while (res.next())
			{
				size++;
				Product.setAmazonAsin(res.getString("AmazonAsin"));
				Product.setEbayId(res.getString("EbayId"));
				//Product.setPrice(res.getDouble("Price"));
			
			}
			con.close();
			if (size ==0) return null;
		} catch (SQLException e) {
			
			e.printStackTrace();
			Product=null;
			System.out.println("Error in reading product info");
			return "error from catch";
		}
		return Product.getAmazonAsin();
	}
	
	public boolean Removed_list_check(String Asin)
	{

		ProductOnline Product = new ProductOnline();//
		int size=0;

		//productId = productId.substring(0,productId.indexOf("-"));
		try {
			Connection con = DriverManager.getConnection(Connection,"root","root");
			java.sql.Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("select * from "+scham+".online where EbayId = '"+Asin+"'");

			while (res.next())
			{
				size++;
				Product.setAmazonAsin(res.getString("AmazonAsin"));
				Product.setEbayId(res.getString("EbayId"));
				Product.setPrice(res.getDouble("Price"));
				Product.setTitle(res.getString("Title"));
			}
			con.close();
			if (size ==0) return true;
		} catch (SQLException e) {
			
			
			e.printStackTrace();
			System.out.println("***Error in Removed_list_check***");
		}
		return false;
	
	}
	
	public void Delete_list(String Asin)
	{
		try {
			Connection con = DriverManager.getConnection(Connection,"root","root");
			java.sql.Statement statement = con.createStatement();
			statement.executeUpdate("DELETE FROM "+scham+".online WHERE AmazonAsin = '"+Asin+"';");
			con.close();
		}catch(Exception e)
		{
			
				System.out.println(e.getMessage());
				System.out.println("Error in removing list in database");
		}
	}
	
	public String Check_status_in_database(String OrderId , String StoreName)
	{
		int size=0;
		try {
			Connection con = DriverManager.getConnection(Connection,"root","root");
			java.sql.Statement statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".orders Where OrderId = '"+OrderId+"' and StoreName = '"+StoreName+"';");
			String Status = null;
			while (res.next())
			{
				size++;
				Status = res.getString("OrderId");
			}
			con.close();
			if (size ==0) return "No_order";
			else if (size>1) return "Error";
			else return Status;
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error in reading product info");
		}
		
		return null;

	}
	
	public void UpdateOrderStatus(OrderType order,String Status) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement_update = con.createStatement();//

				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET OrderStatus='"+Status+"' WHERE OrderId='"+order.getOrderID()+"';";
					statement_update.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}	
				con.close();
	}
	
	public void Update_profit(String Orderid,double Ebay_sale_price, double Amazon_price ,double Amazon_fees) throws SQLException
	{
		
		double profit;
		
		profit = (Ebay_sale_price*(1-ebay_fees-paypal_fees))-paypal_fixed-Amazon_price-Amazon_fees;
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//

				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET Profit = "+profit+" WHERE OrderId = '"+Orderid+"' ;";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}			
				con.close();
		
	}
	
	void CheckFields(OrderType order)
	{
		if (order.getBuyerUserID().contains("'")) order.setBuyerUserID(order.getBuyerUserID().replace("'", ""));
		if (order.getShippingAddress().getName().contains("'")) order.getShippingAddress().setName(order.getShippingAddress().getName().replace("'", ""));
		if (order.getShippingAddress().getStreet1().contains("'")) order.getShippingAddress().setStreet1(order.getShippingAddress().getStreet1().replace("'", ""));
		if (order.getShippingAddress().getStreet2().contains("'")) order.getShippingAddress().setStreet2(order.getShippingAddress().getStreet2().replace("'", ""));
		if (order.getShippingAddress().getCityName().contains("'")) order.getShippingAddress().setCityName(order.getShippingAddress().getCityName().replace("'", ""));
		if (order.getShippingAddress().getStateOrProvince().contains("'")) order.getShippingAddress().setStateOrProvince(order.getShippingAddress().getStateOrProvince().replace("'", ""));

	}
	
	
	public Store GetStoreInfoByName(String StoreName) throws SQLException
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.stores where Store_name = '"+StoreName+"';");
			res.next();	
			Store NewStore = new Store();
				NewStore.Store_name = res.getString("Store_name");
				NewStore.token  = res.getString("token");
				NewStore.DevID = res.getString("DevID");
				NewStore.CertID = res.getString("CertID");
				NewStore.ApplicationId = res.getString("ApplicationId");
				NewStore.ServerUrl = res.getString("ServerUrl");
				NewStore.Site = res.getString("Site");
				NewStore.Connection = res.getString("Connection");
				NewStore.Schame = res.getString("Schame");
				NewStore.Port = res.getInt("Port");
				NewStore.User = res.getString("User");
				NewStore.Pass = res.getString("Pass");
				NewStore.AWS_ACCESS_KEY_ID = res.getString("AWS_ACCESS_KEY_ID");
				NewStore.AWS_SECRET_KEY = res.getString("AWS_SECRET_KEY");
				NewStore.ENDPOINT = res.getString("ENDPOINT");
				NewStore.ChromeDriverPath = res.getString("ChromeDriverPath");
				con.close();
				statement = null;
				res = null;
				System.gc();
				return NewStore;
				
		} catch (SQLException e) {e.printStackTrace();}
	return null;
		
	}
	
	public void GetAllStores(ArrayList<Store> ListOfStores) throws SQLException
	{
		try {
			con = DriverManager.getConnection(Connection,"root","root");
			statement = con.createStatement();//
			ResultSet res = statement.executeQuery("SELECT * FROM amazon.stores;");
			while (res.next())
			{
				Store NewStore = new Store();
				NewStore.Store_name = res.getString("Store_name");
				NewStore.token  = res.getString("token");
				NewStore.DevID = res.getString("DevID");
				NewStore.CertID = res.getString("CertID");
				NewStore.ApplicationId = res.getString("ApplicationId");
				NewStore.ServerUrl = res.getString("ServerUrl");
				NewStore.Site = res.getString("Site");
				NewStore.Connection = res.getString("Connection");
				NewStore.Schame = res.getString("Schame");
				NewStore.Port = res.getInt("Port");
				NewStore.User = res.getString("User");
				NewStore.Pass = res.getString("Pass");
				NewStore.AWS_ACCESS_KEY_ID = res.getString("AWS_ACCESS_KEY_ID");
				NewStore.AWS_SECRET_KEY = res.getString("AWS_SECRET_KEY");
				NewStore.ENDPOINT = res.getString("ENDPOINT");
				NewStore.ChromeDriverPath = res.getString("ChromeDriverPath");
				NewStore.MonitorUser = res.getString("MonitorUser");
				NewStore.MonitorPassword = res.getString("MonitorPass");
				NewStore.AmazonUser = res.getString("AmazonUser");
				NewStore.AmazonPassword = res.getString("AmazonPass");
				ListOfStores.add(NewStore);
			}
			con.close();
			statement = null;
			res = null;
			System.gc();
		} catch (SQLException e) {e.printStackTrace();}
	}
		
	public String Set_new_order(String Store_name,OrderType order,String Status,String Asin_for_order) throws SQLException
	{
		Check_Ascii(order);
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement_update = con.createStatement();
		Calendar cal = Calendar.getInstance();
        String EbayId = order.getOrderID();
    	String WriteToData;
        try{
        EbayId = EbayId.substring(0, EbayId.indexOf("-"));
        CheckFields(order);
        }catch(Exception e){}
		try{
			
			WriteToData = "INSERT INTO "+scham+".orders (StoreName,OrderId,Buyer_User_ID,Buyer_Full_Name,Street,Street2,City,State_Province,Zip_Postal_Code,Phone_Number,Total_price,Sale_date,Feedback_left,EbayId,AmazonAsin,OrderStatus,CheckoutStatus,quantity)"+
			"VALUES ('"+Store_name+"','"+order.getOrderID()+"','"+order.getBuyerUserID()+"','"+order.getShippingAddress().getName()+"','"+order.getShippingAddress().getStreet1()+"','"+order.getShippingAddress().getStreet2()+"','"+order.getShippingAddress().getCityName()+"','"+order.getShippingAddress().getStateOrProvince()+"','"+order.getShippingAddress().getPostalCode()+"','"+order.getShippingAddress().getPhone()+"',"+order.getAmountPaid().getValue()+",'"+cal.getTime().toString()+"',"+0+",'"+EbayId+"','"+Asin_for_order+"','"+Status+"','"+order.getCheckoutStatus().getStatus().value()+"',"+order.getTransactionArray().getTransaction(0).getQuantityPurchased()+")";
			System.out.println(WriteToData);
			statement_update.executeUpdate(WriteToData);
			}catch(SQLException e)
			{
			WriteToData = "INSERT INTO amazon.orders (OrderId,OrderStatus)"+
						"VALUES ('"+order.getOrderID()+"','Database insert error')";
			statement_update.executeUpdate(WriteToData);
			System.out.println(e);
			con.close();
			return "Error";	
			}
		con.close();
		return "Success";			
	
	}

	public void Check_Ascii(OrderType order)
	{
		String temp = null;
		temp = order.getBuyerUserID();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.setBuyerUserID(temp);//
		
		temp = order.getShippingAddress().getName();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setName(temp);
		
		temp = order.getShippingAddress().getStreet1();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setStreet1(temp);
		
		temp = order.getShippingAddress().getStreet2();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setStreet2(temp);
		
		temp = order.getShippingAddress().getStateOrProvince();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setStateOrProvince(temp);
		
		temp = order.getShippingAddress().getPostalCode();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setPostalCode(temp);
		
		temp = order.getShippingAddress().getPhone();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setPhone(temp);
		
		temp = order.getShippingAddress().getPhone();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setPhone(temp);
		
		temp = order.getShippingAddress().getPhone();
		if (temp.contains("'"))
			temp = temp.replace("'", " ");
		order.getShippingAddress().setPhone(temp);
		
		
	}
	
	public  void Get_Orders_without_tracking(List<Order> List) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();
		ResultSet res = statement.executeQuery("select * from "+scham+".orders WHERE OrderStatus = 'Orderd' or OrderStatus = 'TrackingUpdateFail';");
		
		while (res.next())
		{
			Order order = new Order();
			order.Amazon_OrderNumber = res.getString("Amazon_OrderNumber");
			order.OrderId = res.getString("OrderId");
			order.Buyer_User_ID = res.getString("Buyer_User_ID");
			List.add(order);
		}
		con.close();
		
	}
	
	public  void Set_tracking_to_database(String Tracking ,String Carrier,String Amazon_OrderNumber ) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//

				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET OrderStatus='Tracking added',Tracking='"+Tracking+"',Carrier='"+Carrier+"' WHERE Amazon_OrderNumber='"+Amazon_OrderNumber+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}		
				con.close();
		
	}
	
	public  void Set_OrderStatus_Complete_to_database(String Amazon_OrderNumber ) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				 
				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET OrderStatus='Complete', Feedback_left = 1 WHERE Amazon_OrderNumber='"+Amazon_OrderNumber+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}		
				con.close();
		
	}
	
	public  void Set_OrderStatus_Tracking_Update_Fail(String Amazon_OrderNumber ) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				 
				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET OrderStatus='TrackingUpdateFail', Feedback_left = 1 WHERE Amazon_OrderNumber='"+Amazon_OrderNumber+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}		
				con.close();
		
	}

	public  void Set_OrderStatus_Cancelled_to_database(String Amazon_OrderNumber ) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				 
				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".orders SET OrderStatus='Cancelled' WHERE Amazon_OrderNumber='"+Amazon_OrderNumber+"';";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}	
				con.close();
	}
	
	public void GeTOnlineItems(ArrayList<Item> Items) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online");
		while (res.next())
		{
			try{
			Item item = new Item();
			item.Asin = res.getString("AmazonAsin");
			item.EbayId = res.getString("EbayId");
			item.BestResults = res.getString("Bestresults");
			Items.add(item);
			}catch(Exception e)
			{
				System.out.println("Can't get item from Online table");
				System.out.println(e);
			}
		}
		con.close();
	}

	public int GetOnlineItem(String Asin) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		ResultSet res = statement.executeQuery("SELECT * FROM "+scham+".online where AmazonAsin = '"+Asin+"'");
		try{
			res.next();
			int profit  = res.getInt("ProfitPercent");
			return profit;
			}catch(Exception e)
			{
				System.out.println("Can't get item from Online table");
				System.out.println(e);
			
			}
		con.close();
		return -1;
	}
	
	public void UpdateOnlineItem(String Asin,int ProfitPersent) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
				try{
					String WriteToData;
					WriteToData = "UPDATE "+scham+".online SET ProfitPercent = "+ProfitPersent+" WHERE AmazonAsin = '"+Asin+"' ;";
					statement.executeUpdate(WriteToData);
					}catch(SQLException e)
					{
						e.printStackTrace();
					}			
	
				con.close();
	
	}
	
	public void SetBestResultsOnline(ArrayList<Item> Items) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		for (Item ele:Items)
		{
		try{
		if (ele.BestResults!=null)
		{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET bestresults = '"+ele.BestResults+"' WHERE AmazonAsin = '"+ele.Asin+"';";
		statement.executeUpdate(WriteToData);
		}else ele.BestResults = "*";
		}catch(SQLException e)
		{
		e.printStackTrace();
		}		

		}
		con.close();
		
	}

	public void FindBestResults (ArrayList<Item> Items) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		for (Item ele:Items)
		{
		try{
		if (ele.BestResults==null)
		{
		String message = "SELECT * FROM "+scham+".items where ASIN = '"+ele.Asin+"';";
		ResultSet res = statement.executeQuery(message);
		if(res.next()){

		}
			ele.BestResults = res.getString("bestresult");
			System.out.println("Best Results = "+ele.BestResults);
		}
		}catch(Exception e)
		{
		}
		}
		con.close();
		System.out.println("Finish FindBestResults");
	}

	public void SetPosition(Item item) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET BestMatchPosition = "+item.BestMatchPosition+",LowestPricePosition = "+item.LowestPricePosition+" WHERE AmazonAsin = '"+item.Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e)
		{
		e.printStackTrace();
		}		

		
		con.close();
		
	
		
	}

	public void Set_Quantity_Sold(Item item) throws SQLException
	{
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//
		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET Quantity_Sold = "+item.QuantitySold+" WHERE AmazonAsin = '"+item.Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e)
		{
		e.printStackTrace();
		}		
		con.close();
	}
	
	public void Set_UPC(Item item) throws SQLException
	{

		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET Bestresults = "+item.UPC+" WHERE AmazonAsin = '"+item.Asin+"';";
		statement.executeUpdate(WriteToData);
		}catch(SQLException e)
		{
		e.printStackTrace();
		}		
		con.close();
	}

	public void Set_Date(Item item) throws SQLException
	{

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		Connection con = DriverManager.getConnection(Connection,"root","root");
		java.sql.Statement statement = con.createStatement();//

		try{
		String WriteToData;
		WriteToData = "UPDATE "+scham+".online SET StartDate = '"+dateFormat.format(item.StartDate)+"' WHERE AmazonAsin = '"+item.Asin+"';";
		statement.executeUpdate(WriteToData);
		
		}catch(SQLException e)
		{
		e.printStackTrace();
		}
		con.close();

	}

}
