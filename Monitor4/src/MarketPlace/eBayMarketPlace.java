package MarketPlace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.OutputSelectorType;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import Config.Config;
import Item.Item;

public class eBayMarketPlace implements MarketPlaceInterface
{	
	/* Variables */
	
	private ApiAccount ApiAccount;
	private String eBay_token;
	private String Server_url;
	private SiteCodeType SiteCode; 
	private String storeName;
	private String devId; 
	private String appId;
	private String certId;
	private ApiCredential credential;
	private ApiContext context;
	private ApiCall call; 
	private GetItemRequestType Request;
	private GetItemResponseType Response;
	private FindingServicePortType serviceClient;
	private ClientConfig config;
	private FindItemsAdvancedRequest findItemsRequest;
	private ArrayList<ItemFilter> ListOfFilters;
	private FindItemsAdvancedResponse findItemsResponse;
	
	/* Constructor */
	
	public eBayMarketPlace() 
	{
		try {
			ReadFileConfigurations(Config.KeysFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ApiAccount = new ApiAccount();
		ApiAccount.setDeveloper(devId);
		ApiAccount.setApplication(appId);
		ApiAccount.setCertificate(certId);
		
		credential = new ApiCredential();
		credential.setApiAccount(ApiAccount);
		credential.seteBayToken(eBay_token);
		
		ApiContext context = new ApiContext();
		context.setApiCredential(credential);
		context.setApiServerUrl(Server_url); 
		context.setTimeout(1000);
		
		call = new ApiCall(context);
		Request = new GetItemRequestType();
		Request.setIncludeItemSpecifics(true);
		Request.setIncludeCrossPromotion(true);
		Request.setIncludeItemCompatibilityList(true);
		InitFindItemRequest();
		
	}

	private void ReadFileConfigurations(String KeysFilePath) throws IOException
	{
		BufferedReader br = null;
		FileReader fr = null;
		String Temp;
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
				Temp = "eBay token:" ;
				if (sCurrentLine.contains(Temp))
				{
					eBay_token = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				Temp = "Server url:";
				if (sCurrentLine.contains(Temp))
				{
					Server_url = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				Temp = "Store name:";
				if (sCurrentLine.contains(Temp))
				{
					storeName = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				
				Temp = "devId:";
				if (sCurrentLine.contains(Temp))
				{
					devId = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				Temp = "appId:";
				if (sCurrentLine.contains(Temp))
				{
					appId = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				Temp = "certId:";
				if (sCurrentLine.contains(Temp))
				{
					certId = sCurrentLine.substring(sCurrentLine.indexOf(Temp)+Temp.length()+1);
				}
				
				
				if (sCurrentLine.contains("Site:") && sCurrentLine.contains("US"))
				{
					SiteCode =  SiteCodeType.US;
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

	private void InitFindItemRequest()
	{
    	config = new ClientConfig();
    	config.setApplicationId(appId);
		serviceClient = FindingServiceClientFactory.getServiceClient(config);
		findItemsRequest = new FindItemsAdvancedRequest();
		ListOfFilters = new ArrayList<ItemFilter>();
		
		InitFiltersList();
		AddFiltersToFindItemsRequest();
		
		findItemsRequest.setSortOrder(SortOrderType.PRICE_PLUS_SHIPPING_LOWEST);
		
		List<OutputSelectorType> outputSelector = findItemsRequest.getOutputSelector();
		outputSelector.add(OutputSelectorType.SELLER_INFO);
		
		outputSelector = findItemsRequest.getOutputSelector();
		outputSelector.add(OutputSelectorType.STORE_INFO);
	}
	
	private void InitFiltersList()
	{
		ItemFilter filter = new ItemFilter();
		filter.setName(ItemFilterType.CONDITION);
		filter.getValue().add("1000");
		ListOfFilters.add(filter);
		
		ItemFilter filter1 = new ItemFilter();
		filter1.setName(ItemFilterType.LISTING_TYPE);
		filter1.getValue().add("FixedPrice");
		ListOfFilters.add(filter1);
		
		ItemFilter filter2 = new ItemFilter();
		filter2.setName(ItemFilterType.AVAILABLE_TO);
		filter2.getValue().add("US");
		ListOfFilters.add(filter2);
		
		ItemFilter filter3 = new ItemFilter();
		filter3.setName(ItemFilterType.LOCATED_IN);
		filter3.getValue().add("US");
		ListOfFilters.add(filter3);
	}
	
	private void AddFiltersToFindItemsRequest()
	{
		for(ItemFilter ele:ListOfFilters)
		{
			findItemsRequest.getItemFilter().add(ele);
		}
	}
	
	
	/* Interface implementation */
	
	@Override
	public void GetItemsUpdate(ArrayList<Item> ListOfItems) 
	{
		
		
	}
	
	
	/* Operational functions */
	
 	void GetItemInformation(Item item)
	{
		try{
			
			Request.setItemID(item.getMarketPlaceCode());
			Response = (GetItemResponseType) call.executeByApiName("GetItem", Request);
			NameValueListType[] list  = Response.getItem().getItemSpecifics().getNameValueList();
	
			for (NameValueListType ele:list)
			{
				if (ele.getName().contains("UPC") || 
					ele.getName().contains("ISBN"))
				{
					item.setUniversalCode(ele.getValue(0));
					break;
				}
				
				if (ele.getName().contains("EAN"))
				{
					item.setUniversalCode(ele.getValue(0));
				}
			}
			
		}catch(Exception e){}
	}
	
	void GetItemPosition(Item item)
	{
		/* Local variables */
		List<SearchItem> foundItems;
		
		/* Request */
    	findItemsRequest.setKeywords(item.getUniversalCode());
    	findItemsResponse = serviceClient.findItemsAdvanced(findItemsRequest);
    	
    	/* Respond */
    	item.setMarketPlaceResults(findItemsResponse.getSearchResult().getCount());
    	foundItems = findItemsResponse.getSearchResult().getItem();
        item.setMarketPlaceLowestPrice((foundItems.get(0).getSellingStatus().getCurrentPrice().getValue()+foundItems.get(0).getShippingInfo().getShippingServiceCost().getValue()));
        
        /* Find the place in search of lowest price */
        findPlaceInSearch(item,foundItems);
        
    	return;
	}
	
	
	
	/* Not implemented yet*/
	
	void DeleteItem(Item item)
	{
		
	}
	
	void UpdateItem(Item item)
	{
		
	}
	
	void SetNotInStock(Item item)
	{
		
	}
	
	void ChangePrice(Item item)
	{
		
	}
	
	void ChangeQuantity(Item item)
	{
		
	}
	

	
	/* Inner functions */
	
	private void findPlaceInSearch(Item item , List<SearchItem> items )
	{
        if (item.getMarketPlaceResults() < 2)
        {
        	if (item.getMarketPlaceResults() != 1)
        	{
            	item.setPlaceInLowestPrice(-1);        	
        	}else
        	{
        		item.setPlaceInLowestPrice(1); 
        	}
        }else
        {
	        int placeInLowestPrice = 1;
	        for(SearchItem ele : items) 
	        {
	        	try{
	        	if (ele.getStoreInfo().getStoreName().equals(storeName))
	        	{
	        		item.setPlaceInLowestPrice(placeInLowestPrice);
	        		break;
	        	}else
	        	{
	        		placeInLowestPrice++;
	        	}
	        	}catch(Exception e){}
	        }
        }
	}
	
	
	

	/* Getter and Setters*/
	
	public String geteBay_token() {
		return eBay_token;
	}

	public void seteBay_token(String eBay_token) {
		this.eBay_token = eBay_token;
	}

	public String getServer_url() {
		return Server_url;
	}

	public void setServer_url(String server_url) {
		Server_url = server_url;
	}

	public SiteCodeType getSiteCode() {
		return SiteCode;
	}

	public void setSiteCode(SiteCodeType siteCode) {
		SiteCode = siteCode;
	}

	public ApiAccount getApiAccount() {
		return ApiAccount;
	}

	public void setApiAccount(ApiAccount apiAccount) {
		ApiAccount = apiAccount;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public ApiCredential getCredential() {
		return credential;
	}

	public void setCredential(ApiCredential credential) {
		this.credential = credential;
	}

	public ApiContext getContext() {
		return context;
	}

	public void setContext(ApiContext context) {
		this.context = context;
	}

	public ApiCall getCall() {
		return call;
	}

	public void setCall(ApiCall call) {
		this.call = call;
	}

	public GetItemRequestType getRequest() {
		return Request;
	}

	public void setRequest(GetItemRequestType request) {
		Request = request;
	}

	public GetItemResponseType getResponse() {
		return Response;
	}

	public void setResponse(GetItemResponseType response) {
		Response = response;
	}

	
	


	
	
	
	
	
	
}
