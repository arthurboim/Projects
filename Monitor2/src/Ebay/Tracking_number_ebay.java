package Ebay;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.soap.eBLBaseComponents.CommentTypeCodeType;
import com.ebay.soap.eBLBaseComponents.FeedbackInfoType;
import com.ebay.soap.eBLBaseComponents.ShipmentTrackingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShipmentType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import Main.Store;

public class Tracking_number_ebay {

	public static ApiContext apiContext = null;
	public static ApiCredential cred = null;
	public static  String FILENAME = "C:\\Keys\\ConfigFile-Keys.txt";
	public static  String eBay_token = null;
	public static  String Server_url = null;
	public static  String Application_id= null;
	public static  SiteCodeType SiteCode = null;
	public static  String StoreName = null;
	public static  String Connection = null;
	public static  String Contry = null;
	public static  String scham = null;	

	
	public Tracking_number_ebay(Store store) 
	{
 		StoreName = store.Store_name;
		Connection = store.Connection;
		scham = store.Schame;
		Connection =Connection+scham;
		eBay_token = store.token;
		Server_url = store.ServerUrl;
		Application_id = store.ApplicationId;
		Contry = store.Site;
		if (Contry.equals("US")) SiteCode =  SiteCodeType.US;
		if (Contry.equals("UK")) SiteCode =  SiteCodeType.UK; 
	}

	
	public  String  Update_tracking_and_feedback_in_ebay(String ItemID ,String TransactionID , String Tracking , String Carrier , String Ebay_user_id)
	{
        apiContext = new ApiContext();
        cred = apiContext.getApiCredential();
        cred.seteBayToken(eBay_token);
   
        apiContext.setApiServerUrl(Server_url);// Pointing to sandbox for testing.  
        
        apiContext.getApiLogging().setLogSOAPMessages(true);// This will log SOAP requests and responses

        apiContext.setSite(SiteCode); // Set site to UK
        CompleteSaleCall completeSaleApi = new CompleteSaleCall(apiContext);
        
        
        completeSaleApi.setItemID(ItemID);
        completeSaleApi.setTransactionID(TransactionID);
        completeSaleApi.setShipped(true);
        
        ShipmentType shipType = new ShipmentType();
        
        ShipmentTrackingDetailsType shpmnt = new ShipmentTrackingDetailsType();
        shpmnt.setShipmentTrackingNumber(Tracking);
        shpmnt.setShippingCarrierUsed(Carrier);
        
        shipType.setShipmentTrackingDetails(new ShipmentTrackingDetailsType[]{shpmnt});
        
        completeSaleApi.setShipment(shipType);
        
        FeedbackInfoType feedback = new FeedbackInfoType();
        feedback.setCommentText("Thank you for an easy, pleasant transaction. Excellent buyer. A++++++.");
        feedback.setTargetUser(Ebay_user_id);
        feedback.setCommentType(CommentTypeCodeType.POSITIVE);
        
        completeSaleApi.setFeedbackInfo(feedback);
        
        try {
             completeSaleApi.completeSale();
        } catch (ApiException e) { 
             e.printStackTrace();
             return ("Fail");
        } catch (SdkException e) { 
              e.printStackTrace();
              return ("Fail");
        } catch (Exception e) { 
             e.printStackTrace();
             return ("Fail");
        }
   
        System.out.println("Tracking and feedback left!!!");
        return ("Success");
	}

}
