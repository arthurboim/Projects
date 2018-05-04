package Ebay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import Main.Item;

public class GetItemEbay {

	public static ApiContext apiContext = new ApiContext();
	public static ApiCredential cred = apiContext.getApiCredential(); //
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String Contry = null;

	public GetItemEbay() {

		System.out.println("Constractor of eBayCalls");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				//System.out.println(sCurrentLine);
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:")+12);
					//System.out.println("eBay_token = "+eBay_token);
				}
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf("Server url:")+12);
					//System.out.println("Server_url = "+Server_url);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:")+16);
					//System.out.println("Application_id = "+Application_id);
				}
				//
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("US"))
				{
					//System.out.println("Site code --> US");
					SiteCode =  SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:")&&sCurrentLine.contains("UK"))
				{
					//System.out.println("Site code --> UK");
					SiteCode =  SiteCodeType.UK;
					Contry = "UK";
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

	public void Get_Item_Info(Item item) throws ApiException, SdkException, Exception
	{

	    cred.seteBayToken(eBay_token);
	    apiContext.setApiServerUrl(Server_url);
	    apiContext.setSite(SiteCode);
	    
		GetItemCall Call = new GetItemCall (apiContext);
		Call.setItemID(item.EbayId);
		Call.setIncludeItemSpecifics(true);
	
		ItemType Item = Call.getItem();
		System.out.println("Quantity Sold = "+(Item.getQuantity()-1));
		item.QuantitySold = Item.getQuantity()-1;

		for(int i=0;i<Item.getItemSpecifics().getNameValueListLength();i++)
		{
		if (Item.getItemSpecifics().getNameValueList(i).getName().equals("UPC")&&!Item.getItemSpecifics().getNameValueList(i).getValue(0).equals("NA"))
		{
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getName());
		System.out.println(Item.getItemSpecifics().getNameValueList(i).getValue(0));
		item.UPC = Item.getItemSpecifics().getNameValueList(i).getValue(0);
		}
		}
		
        java.util.Date input = Item.getListingDetails().getStartTime().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(input);
        System.out.println(formattedDate);
        item.StartDate = input;
	}

}
