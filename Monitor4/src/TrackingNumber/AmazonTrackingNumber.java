package TrackingNumber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Config.Config;
import DataBase.IDataBase;
import DataBase.SQLDataBase;
import Item.Item;
import WebDriver.SelenumWebDriver;

public class AmazonTrackingNumber implements SuppliyerTrackingNumber{

	SelenumWebDriver 	Driver;
	IDataBase 		 	DB;
	String 			 	AmazonUser;
	String				AmazonPass;
	ArrayList<Item> 	ItemsToUpdate;
	String				Scham;
	
	public AmazonTrackingNumber() 
	{
		DB 	   		  	= new SQLDataBase();
		ItemsToUpdate 	= new ArrayList<Item>();
		
		try {
			ReadFileConfigurations(Config.KeysFilePath);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	/* Public API */

	@Override
	public void UpDateTrackingNumber() 
	{
		Driver 			= new SelenumWebDriver();
		GetItemsForUpdateTrackingNumber();
		Login();
		UpdateItemsTrackingNumber();
		ItemsToUpdate.clear();
		Driver.CloseWebDriver();

	}

	
	
	
	/* Private API */
	
	private void UpdateItemsTrackingNumber()
	{
		for(Item ele:ItemsToUpdate)
		{
			GetNextItemTrackingAndCarrier(ele);
		}
		
		for(Item ele:ItemsToUpdate)
		{
			UpdateTrackingAndCarrierOnMarketPlace(ele);
		}
	}
	
	private void UpdateTrackingAndCarrierOnMarketPlace(Item itemToUpdate)
	{
		
	}
	
	private void GetNextItemTrackingAndCarrier(Item itemToUpdate)
	{
		String Text;
		Driver.OpenLink("https://www.amazon.com/gp/your-account/order-history/ref=oh_aui_search?opt=ab&search="+itemToUpdate.getOrderNumber());
		Driver.wait(2);
		Text= Driver.GetText(null, "ordersContainer");
		if (Text.contains("No results found. Please try another search."))
		{
			// Go next
		}else
		{
			Driver.wait(2);
			Driver.Click("//*[@id='a-autoid-8-announce']", "a-autoid-8-announce");
			ParseText(itemToUpdate);
		}
	}
	
	private void ParseText(Item itemToUpdate)
	{
		String Text;
		String Carrier;
		String Tracking;
		
		Driver.wait(2);
		Text = Driver.GetText("//*[@id='carrierRelatedInfo-container']", "carrierRelatedInfo-container");
		if (Text.contains("Shipped with"))
		{
			Text 	 = Text.replace("\n", " ");
			Carrier  = Text.substring(Text.indexOf("Shipped with")+"Shipped with".length()+1,Text.indexOf("Tracking ID"));
			Tracking = Text.substring(Text.indexOf("Tracking ID")+"Tracking ID".length()+1);
			Tracking = Tracking.replace(" ", "");
			itemToUpdate.setCarrier(Carrier);
			itemToUpdate.setTracking(Tracking);
			System.out.println("Carrier	 : "+itemToUpdate.getCarrier());
			System.out.println("Tracking : "+itemToUpdate.getTracking());
		}
	}
	
	private void GetItemsForUpdateTrackingNumber()
	{
		ResultSet res = DB.Read("select * from "+Scham+".orders WHERE OrderStatus = 'Orderd' or OrderStatus = 'TrackingUpdateFail';");
		try {
			while (res.next())
			{
				Item newItem = new Item();
				newItem.setOrderNumber(res.getString("Amazon_OrderNumber"));
				newItem.setOrderId(res.getString("OrderId"));
				newItem.setBuyerUserID("Buyer_User_ID");
				ItemsToUpdate.add(newItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		try {
			
			if(null == KeysFilePath)
			{
				fr = new FileReader(Config.KeysFilePath);
			}else
			{
				fr = new FileReader(KeysFilePath);
			}
			
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if (sCurrentLine.contains("Schame: "))
				{
					Scham = sCurrentLine.substring(sCurrentLine.indexOf("Schame: ")+"Schame: ".length());
				}
				if (sCurrentLine.contains("Acid: ")) 
				{
					AmazonUser = sCurrentLine.substring(sCurrentLine.indexOf("Acid: ")+ "Acid: ".length());
				}
				
				if (sCurrentLine.contains("APass: ")) 
				{
					AmazonPass = sCurrentLine.substring(sCurrentLine.indexOf("APass: ")+ "APass: ".length());
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
	}

	private void Login()
	{
		Driver.OpenLink("https://www.amazon.com/");
		Driver.Click(null, "nav-link-accountList");
		Driver.Click(null, "//*[@id='ap_email']");
		Driver.GetDriver().findElementByXPath("//*[@id='ap_email']").clear();
		Driver.SetText("//*[@id='ap_email']", null, AmazonUser);
		Driver.Click("//*[@id='continue']", null);
		Driver.Click("//*[@id='ap_password']", null);
		Driver.SetText("//*[@id='ap_password']", null, AmazonPass);
		Driver.Click("//*[@id='signInSubmit']", null);
	}
	
}
