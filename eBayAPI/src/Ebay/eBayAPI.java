package Ebay;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.sdk.call.EndItemCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetItemTransactionsCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.sdk.call.ReviseFixedPriceItemCall;
import com.ebay.sdk.call.ReviseItemCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.CommentTypeCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.EndReasonCodeType;
import com.ebay.soap.eBLBaseComponents.FeedbackInfoType;
import com.ebay.soap.eBLBaseComponents.GranularityLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.ReturnPolicyType;
import com.ebay.soap.eBLBaseComponents.ReviseFixedPriceItemRequestType;
import com.ebay.soap.eBLBaseComponents.ReviseItemRequestType;
import com.ebay.soap.eBLBaseComponents.ShipmentTrackingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShipmentType;
import com.ebay.soap.eBLBaseComponents.ShippingDetailsType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import Item.Item;
import Item.Item.APIRequetsStatus;
import Item.Item.SellerPrice;



public class eBayAPI
{
	
	/* Final variables */
	private final String				FILENAME		= "C:\\Keys\\ConfigFile-Keys.txt";
	private final int					pageSize		= 200;
	
	/* Account configurations */
	private ApiContext					apiContext;
	private ApiCredential				cred;
	private String						devId			= null;
	private String						appId			= null;
	private String						certId			= null;
	private String						eBay_token		= null;
	private String						Server_url		= null;
	private String						Application_id	= null;
	private String						storeName;
	private ApiAccount 					account;
	private SiteCodeType				SiteCode		= null;
	private String						Contry			= null;
	private PaginationType				pt;
	private PaginationInput				ptInput;
	private ApiLogging					apiLogging;
	private ClientConfig				config;
	private TimeFilter					tf;
	
	/* Update tracking and carrier */
	private CompleteSaleCall 			CompleteSaleCall;
	private FeedbackInfoType 			Feedback;
	private ShipmentTrackingDetailsType Shpmnt; 
	private ShipmentType 				ShipType;
	
	private GetSellerListCall			GetSellerListCall;
	private GetItemCall					GetItemCall;
	private GetItemTransactionsCall		GetItemTransactionsCall;
	
	/* Finding API */
	
	private FindingServicePortType		serviceClient;
	private FindItemsAdvancedRequest	FindItemsAdvancedRequest;
	private FindItemsAdvancedResponse	FindItemsAdvancedResponse;
	
	private FindItemsByKeywordsRequest	FindItemsbykeywordRequest;
	private FindItemsByKeywordsResponse	FindItemsbykeywordRespond;
	private ReviseFixedPriceItemCall 	ReviseFixedPriceItemCall;
	
	private ReviseItemCall				ReviseItemCall;
	private EndItemCall					endItemCall;
	/* Time and date */
	private Calendar					calFrom;
	private Calendar					calTo;
	
	/* Contractor */
	public eBayAPI()
	{
		
		/* Load configurations */
		InitFromFile();

		apiContext = new ApiContext();
        apiContext.getApiLogging().setLogSOAPMessages(true);
		cred = apiContext.getApiCredential();
		cred.seteBayToken(eBay_token);
		endItemCall = new EndItemCall(apiContext);
		ReviseItemCall = new ReviseItemCall(apiContext);
		
		/* eBay API Initialization */
		eBayAPIGeneralinitialization();
		InitializationSellerListCall();
		InitializationGetItemCall();
		InitializationFindAdvancedRequest();
		InitializationFindByKeyWordRequest();
		InitializationOfGetItemTransactions();
		InitializationCompleteSaleCall();
		InitializationReviseFixedPriceItemCall();
		
	}
	
	/* Initialization */
	private void InitFromFile()
	{
		
		BufferedReader br = null;
		FileReader fr = null;
		try
		{
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null)
			{
				
				
				
				if (sCurrentLine.contains("Server url:"))
				{
					Server_url = sCurrentLine
							.substring(sCurrentLine.indexOf("Server url:") + "Server url:".length() + 1);
				}
				
				if (sCurrentLine.contains("Store name: "))
				{
					storeName = sCurrentLine
							.substring(sCurrentLine.indexOf("Store name: ") + "Store name: ".length());
				}
				
				if (sCurrentLine.contains("eBay token:"))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf("eBay token:") + 12);
				}
				
				if (sCurrentLine.contains("Application id:"))
				{
					Application_id = sCurrentLine.substring(sCurrentLine.indexOf("Application id:") + 16);
				}
				
				if (sCurrentLine.contains("Site:") && sCurrentLine.contains("US"))
				{
					SiteCode = SiteCodeType.US;
					Contry = "US";
				}
				
				if (sCurrentLine.contains("Site:") && sCurrentLine.contains("UK"))
				{
					SiteCode = SiteCodeType.UK;
					Contry = "UK";
				}
				
				if (sCurrentLine.contains("devId:") )
				{
					devId = sCurrentLine.substring(sCurrentLine.indexOf("devId: ") + "devId: ".length());
				}
				
				if (sCurrentLine.contains("appId:") )
				{
					appId = sCurrentLine.substring(sCurrentLine.indexOf("appId: ") + "appId: ".length());
				}
				
				if (sCurrentLine.contains("certId:") )
				{
					certId = sCurrentLine.substring(sCurrentLine.indexOf("certId: ") + "certId: ".length());
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			
			try
			{
				
				if (br != null)
					br.close();
				
				if (fr != null)
					fr.close();
				
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	
	private void eBayAPIGeneralinitialization()
	{
		/* eBay API calls general initialization */
		apiContext = new ApiContext();
		account = new ApiAccount();
		
		account.setDeveloper(devId);
		account.setApplication(appId);
		account.setCertificate(certId);
		
		cred = apiContext.getApiCredential();
		
		cred.seteBayToken(eBay_token);
		cred.setApiAccount(account);
		apiContext.setApiServerUrl(Server_url);
		apiContext.setSite(SiteCode);
		pt = new PaginationType();
		pt.setEntriesPerPage(pageSize);
		apiLogging = new ApiLogging();
		apiContext.setApiLogging(apiLogging);
		config = new ClientConfig();
		config.setApplicationId(Application_id);
	}
	
	private void InitializationSellerListCall()
	{
		/* initialization Seller list call */
		GetSellerListCall = new GetSellerListCall(apiContext);
		GetSellerListCall.setGranularityLevel(GranularityLevelCodeType.COARSE);
		calFrom = Calendar.getInstance();
		calFrom.add(Calendar.DAY_OF_MONTH, 0);
		calTo = Calendar.getInstance();
		calTo.add(Calendar.DAY_OF_MONTH, 30);
		TimeFilter tf = new TimeFilter(calFrom, calTo);
		GetSellerListCall.setEndTimeFilter(tf);
		GetSellerListCall.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
	}
	
	private void InitializationGetItemCall()
	{
		/* initialization Get Item call */
		GetItemCall = new GetItemCall(apiContext);
		GetItemCall.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
	}
	
	private void InitializationFindAdvancedRequest()
	{
		/* initialization of find item advanced request */
		serviceClient = FindingServiceClientFactory.getServiceClient(config);
		FindItemsAdvancedRequest  = new FindItemsAdvancedRequest();
		FindItemsAdvancedResponse = new FindItemsAdvancedResponse();
		ItemFilter filter2 = new ItemFilter();
		filter2.setName(ItemFilterType.CONDITION);
		filter2.getValue().add("1000");
		FindItemsAdvancedRequest.getItemFilter().add(filter2);
		
		ItemFilter filter = new ItemFilter();
		filter.setName(ItemFilterType.LISTING_TYPE);
		filter.getValue().add("FixedPrice");
		FindItemsAdvancedRequest.getItemFilter().add(filter);
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add(Contry);
		FindItemsAdvancedRequest.getItemFilter().add(filter6);
		
		List<OutputSelectorType> outputSelector = FindItemsAdvancedRequest.getOutputSelector();
		OutputSelectorType outputSelectorType = OutputSelectorType.SELLER_INFO;
		outputSelector.add(outputSelectorType);
		ptInput = new PaginationInput();
		ptInput.setEntriesPerPage(8);
		ptInput.setPageNumber(1);
		FindItemsAdvancedRequest.setPaginationInput(ptInput);
		FindItemsAdvancedRequest.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
	}
	
	private void InitializationOfGetItemTransactions()
	{
		/* initialization of get item transactions */
		GetItemTransactionsCall = new GetItemTransactionsCall(apiContext);
		tf = new TimeFilter(calFrom, calTo);
		GetItemTransactionsCall.setModifiedTimeFilter(tf);
	}
	
	private void InitializationFindByKeyWordRequest()
	{
		
		/* initialization of find item advanced request */
		serviceClient = FindingServiceClientFactory.getServiceClient(config);
		FindItemsbykeywordRequest = new FindItemsByKeywordsRequest();
		ItemFilter filter2 = new ItemFilter();
		filter2.setName(ItemFilterType.CONDITION);
		filter2.getValue().add("1000");
		FindItemsAdvancedRequest.getItemFilter().add(filter2);
		
		ItemFilter filter = new ItemFilter();
		filter.setName(ItemFilterType.LISTING_TYPE);
		filter.getValue().add("FixedPrice");
		FindItemsbykeywordRequest.getItemFilter().add(filter);
		
		ItemFilter filter6 = new ItemFilter();
		filter6.setName(ItemFilterType.LOCATED_IN);
		filter6.getValue().add(Contry);
		FindItemsbykeywordRequest.getItemFilter().add(filter6);
		
		List<OutputSelectorType> outputSelector = FindItemsbykeywordRequest.getOutputSelector();
		OutputSelectorType outputSelectorType = OutputSelectorType.SELLER_INFO;
		outputSelector.add(outputSelectorType);
		ptInput = new PaginationInput();
		ptInput.setEntriesPerPage(100);
		ptInput.setPageNumber(1);
		FindItemsbykeywordRequest.setPaginationInput(ptInput);
		FindItemsbykeywordRequest.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
		
	}
	
	private void InitializationCompleteSaleCall()
	{
		CompleteSaleCall = new CompleteSaleCall(apiContext);
		Feedback = new FeedbackInfoType();
		Shpmnt = new ShipmentTrackingDetailsType();
		ShipType = new ShipmentType();
	}
	
	private void InitializationReviseFixedPriceItemCall(){

		ReviseFixedPriceItemCall = new ReviseFixedPriceItemCall(apiContext);
	}
	
	/* Public functions */
	
	public void GetItemsUpdate(List<Item> ListOfItems)
	{
		for (Item ele : ListOfItems)
		{
			GetPositionInLowestPriceSearch(ele);				
		}
	}
	
	public void ChangePrice(Item item) 
	{
		try
		{
			ItemType newItem = new ItemType();
			DecimalFormat df = new DecimalFormat("0.##");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setRoundingMode(RoundingMode.HALF_DOWN);
			AmountType newPrice = new AmountType();
			
			
			//Set values
			newPrice.setValue(Double.valueOf(df.format(item.getUpdatedPrice()))); // Must be set
			newPrice.setCurrencyID(CurrencyCodeType.USD); // Must be set
			newItem.setStartPrice(newPrice); // Must be set
			newItem.setItemID(item.getMarketPlaceCode()); // Must be set
			ReviseFixedPriceItemCall.setItemToBeRevised(newItem); // Must be set
			
			// Execute
			ReviseFixedPriceItemCall.reviseFixedPriceItem();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public boolean DeleteItem(Item item)
	{
		Boolean status = false;
		try
		{
			Calendar endTime = null;
			endItemCall.setItemID(item.getMarketPlaceCode());
			endItemCall.setEndingReason(EndReasonCodeType.NOT_AVAILABLE);
			endTime = endItemCall.endItem();
			if (null != endTime)
			{
				status = true;
			}
			else
			{
				System.out.println("Fail");
			}
		} catch (Exception e)
		{
		}
		
		return status;
	}
	
	public void ChangeQuantity(Item item, int newQuantity)
	{
		try
		{
			ItemType localItem = new ItemType();
			localItem.setQuantity(newQuantity);
			localItem.setItemID(item.getMarketPlaceCode());
			ReviseItemCall.setItemToBeRevised(localItem);
			ReviseItemCall.reviseItem();
			item.setQuantity(newQuantity);
			
		} catch (ApiException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SdkException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public  void GetPositionInLowestPriceSearch(Item itemToCheck)
	{
		itemToCheck.setAPIRequestStatus(APIRequetsStatus.Success);
		if (null == itemToCheck.getUniversalCode())
		{
			GetItemUniversalCode(itemToCheck);		
		}
		
		if (null != itemToCheck.getUniversalCode())
		{
			GetItemPosition(itemToCheck);			
		}else
		{
			itemToCheck.setAPIRequestStatus(APIRequetsStatus.Fail);
			System.out.println("itemToCheck: API FAIL"+itemToCheck.getMarketPlaceCode());
		}
	}
	
	public synchronized void GetItemUniversalCode(Item newItem)
	{
		try
		{
			GetItemCall.setItemID(newItem.getMarketPlaceCode());
			if (null == GetItemCall.getItem())
			{
				newItem.setAPIRequestStatus(APIRequetsStatus.Fail);
				return;
			}
			
			
			if (GetItemCall.getItem().getProductListingDetails().getISBN() != null
					&& !GetItemCall.getItem().getProductListingDetails().getISBN().toLowerCase().contains("apply")
					&& !GetItemCall.getItem().getProductListingDetails().getISBN().toLowerCase().contains("applicable")
					&& !GetItemCall.getItem().getProductListingDetails().getISBN().toLowerCase().contains("Does not apply")
					)
			{
				newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getISBN());
				newItem.setCodeType("ISBN");
			}
			else if (GetItemCall.getItem().getProductListingDetails().getUPC() != null
					&& !GetItemCall.getItem().getProductListingDetails().getUPC().toLowerCase().contains("apply")
					&& !GetItemCall.getItem().getProductListingDetails().getUPC().toLowerCase().contains("applicable")
					&& !GetItemCall.getItem().getProductListingDetails().getUPC().toLowerCase().contains("Does not apply")
					)
			{
				newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getUPC());
				newItem.setCodeType("UPC");
			}
			else if (GetItemCall.getItem().getProductListingDetails().getEAN() != null
					&& !GetItemCall.getItem().getProductListingDetails().getEAN().toLowerCase().contains("apply")
					&& !GetItemCall.getItem().getProductListingDetails().getEAN().toLowerCase().contains("applicable")
					&& !GetItemCall.getItem().getProductListingDetails().getEAN().toLowerCase().contains("Does not apply")
					)
			{
				newItem.setUniversalCode(GetItemCall.getItem().getProductListingDetails().getEAN());
				newItem.setCodeType("EAN");
			}
			
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public void UpDateTrackingNumber(ArrayList<Item> ListOfItems)
	{
		for(Item ele:ListOfItems)
		{
			if (null != ele.getTracking() && null != ele.getCarrier())
			{
				UpdateTrackingAndFeedback(ele.getOrderId(),ele.getOrderId(),ele.getTracking(),ele.getCarrier(),ele.getBuyerUserID());
			}
		}
	}
	

	
	/* Protected functions */
	
	protected  void GetItemPosition(Item item)
	{
		try
		{

			
			/* Request */
			if (null != item.getUniversalCode())
			{
				/* Local variables */
				List<SearchItem> foundItems;
				FindItemsbykeywordRequest.setKeywords(item.getUniversalCode());
				FindItemsbykeywordRespond = serviceClient.findItemsByKeywords(FindItemsbykeywordRequest);
				
				/* Respond */
				item.setMarketPlaceResults(FindItemsbykeywordRespond.getSearchResult().getCount());
				foundItems = FindItemsbykeywordRespond.getSearchResult().getItem();
				
				/* Find the place in search of lowest price */
				findPlaceInSearch(item, foundItems);
			}
			
		} catch (Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		
		return;
	}
	
	/* Private functions */
	
	protected void findPlaceInSearch(Item item, List<SearchItem> items)
	{
		if (item.getMarketPlaceResults() < 2)
		{
			if (item.getMarketPlaceResults() != 1)
			{
				item.setPlaceInLowestPrice(-1);
			}
			else
			{
				SellerPrice newSellerrice = new SellerPrice();
				item.setPlaceInLowestPrice(1);
				newSellerrice.setName(storeName);
				newSellerrice.setPrice((items.get(0).getSellingStatus().getCurrentPrice().getValue()
										+ items.get(0).getShippingInfo().getShippingServiceCost().getValue()));
				item.setCurrentMarketPlacePrice((items.get(0).getSellingStatus().getCurrentPrice().getValue()
										+ items.get(0).getShippingInfo().getShippingServiceCost().getValue()));
				item.getPricesList().add(newSellerrice);
			}		 
		}
		else
		{
			int placeInLowestPrice = 1;
			for (SearchItem ele : items)
			{
				try{
					SellerPrice newSellerPrice = new SellerPrice();
					if (ele.getSellerInfo().getSellerUserName().equals(storeName))
					{
						item.setPlaceInLowestPrice(placeInLowestPrice);
						item.setCurrentMarketPlacePrice(ele.getSellingStatus().getCurrentPrice().getValue()
							+ ele.getShippingInfo().getShippingServiceCost().getValue());
					}
					else
					{
						placeInLowestPrice++;
					}
					newSellerPrice.setName(ele.getSellerInfo().getSellerUserName());
					newSellerPrice.setPrice(ele.getSellingStatus().getCurrentPrice().getValue()
							+ ele.getShippingInfo().getShippingServiceCost().getValue());
					item.getPricesList().add(newSellerPrice);
				}catch(Exception e)
				{
					
				}
			}
		}
	}

	
	private void UpdateTrackingAndFeedback(String ItemID ,String TransactionID , String Tracking , String Carrier , String Ebay_user_id)
	{
		ItemID = ItemID.substring(0,ItemID.indexOf("-"));
		CompleteSaleCall.setItemID(ItemID);
		TransactionID = TransactionID.substring(TransactionID.indexOf("-")+1);
		CompleteSaleCall.setTransactionID(TransactionID);
		CompleteSaleCall.setShipped(true);
        Shpmnt.setShipmentTrackingNumber(Tracking);
        Shpmnt.setShippingCarrierUsed(Carrier);
        ShipType.setShipmentTrackingDetails(new ShipmentTrackingDetailsType[]{Shpmnt});
        CompleteSaleCall.setShipment(ShipType);
        Feedback.setCommentText("Thank you for an easy, pleasant transaction. Excellent buyer. A++++++.");
        Feedback.setTargetUser(Ebay_user_id);
        Feedback.setCommentType(CommentTypeCodeType.POSITIVE);
        CompleteSaleCall.setFeedbackInfo(Feedback);
        
		try
		{
			CompleteSaleCall.completeSale();
			System.out.println("Tracking and carrier added!!!");
		} catch (ApiException e)
		{
			e.printStackTrace();
		} catch (SdkException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{}
	}
	
	
}
