package TrackingNumber;


import java.util.ArrayList;
import AmazonAPI.AmazonAPI;
import DataBase.SQLDataBase;
import Ebay.eBayAPI;
import Item.Item;


public class AmazonTrackingNumber implements SuppliyerTrackingNumber
{
	ArrayList<Item>		ItemsToUpdate;
	SQLDataBase			DB;
	AmazonAPI			Amazon;
	eBayAPI				eBay;
	
	/* Constractor */ 
	
	public AmazonTrackingNumber()
	{
		DB = new SQLDataBase();
		ItemsToUpdate = new ArrayList<Item>();
		Amazon = new AmazonAPI();
		eBay = new eBayAPI();
	}
	
	/* Public API */
	
	@Override
	public void UpDateTrackingNumber()
	{
		DB.GetItemsForUpdateTrackingNumber(ItemsToUpdate);
		Amazon.UpDateTrackingNumber(ItemsToUpdate);
		eBay.UpDateTrackingNumber(ItemsToUpdate);
		DB.UpDateTrackingNumber(ItemsToUpdate);
		ItemsToUpdate.clear();
	}
	
}
