package Item;

public class Item {

	private static double ebay_fees = 0.09;
	private static double paypal_fees = 0.039;
	private static double paypal_fixed = 0.3;
	private static double priceDiffrenceWithCompetitor = 0.01;
	
	public enum PriceChangeStatus
	{
		PriceIncrease,
		PriceReduce,
		NoChange
	}
	
	public enum QuantitiyChangeStatus
	{
		OutOfStock,
		Instock,
		NoChange
	}
	
	private QuantitiyChangeStatus quantitiyStatus;
	private PriceChangeStatus priceStatus;
	private String      supplierCode;
	private String		marketPlaceCode;
	private String		universalCode;
	private String		title;
	private double 		currentSupplierPrice;
	private double 		currentTax;
	private double 		currentMarketPlacePrice;
	private double 		minPriceToSale;
	private double 		updatedPrice;
	private boolean 	inStock;
	private int 		quantity;
	private double 		marketPlaceLowestPrice;
	private double 		marketPlaceSecondLowestPrice;
	private int 		marketPlaceResults;
	private int 		placeInLowestPrice;
	private short		placeInbestMatch;
	private int 		itemRank;
	private boolean		isPrime;
	private boolean   	isPreorder;
	private boolean		supplyerRequestSuccess;
	private double 		myProfitPercent;
	private String 		CodeType;
	private int			quantitySold;
	private int 		sellerSoldTheItem;
	private String		orderNumber;
	private String		orderId;
	private String		buyerUserID;
	private String		Carrier;
	private String		Tracking;
	private int 		amountSoldByTheSeller;
	private double		sellerPrice;
	private double		arbitraje;
	
	/* Constructor */
	
	public Item() {
		this.supplierCode 					= null;
		this.universalCode					= null;
		this.marketPlaceCode				= null;
		this.title							= null;
		this.CodeType						= null;
		this.orderNumber					= null;
		this.orderId						= null;
		this.buyerUserID					= null;
		this.Carrier						= null;
		this.Tracking 						= null;
		this.inStock 						= false;
		this.isPrime						= false;
		this.isPreorder						= false;
		this.currentSupplierPrice			= -1;
		this.currentMarketPlacePrice		= -1;
		this.currentTax						= -1;
		this.placeInLowestPrice 			= -1;
		this.placeInbestMatch 				= -1;
		this.marketPlaceLowestPrice 		= -1;
		this.marketPlaceSecondLowestPrice 	= -1;
		this.itemRank						= -1;
		this.marketPlaceResults				= -1;
		this.minPriceToSale					= -1;
		this.quantity						= -1;
		this.sellerPrice					= -1;
		this.quantitySold					= 0;
		this.sellerSoldTheItem				= 0;
		this.amountSoldByTheSeller			= 0;
		this.myProfitPercent				= 1.00;
		this.supplyerRequestSuccess 		= true;
		this.arbitraje						= -1;
		this.quantitiyStatus				= QuantitiyChangeStatus.NoChange;
		this.priceStatus					= PriceChangeStatus.NoChange;
	}

	public Item(String code) {
		this();
		this.supplierCode 					= code;
	}

	
	/* Public functions */
	
	public void CalculateMinPriceToSale()
	{
		minPriceToSale = (currentSupplierPrice + currentTax + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}

	public void CalculateNewProfitPersentFromSpecificPrice(double price)
	{
		myProfitPercent = ((price - priceDiffrenceWithCompetitor)*(1 - ebay_fees - paypal_fees)) /(currentSupplierPrice + currentTax + paypal_fixed); 
	}
	
	public void CalculateCurrentProfitPersent()
	{
		myProfitPercent = (currentMarketPlacePrice * (1 - ebay_fees - paypal_fees)) / (currentSupplierPrice + currentTax + paypal_fixed);
	}
	
	public void CalculateNewMarketPlacePrice(double ProfitPercent)
	{
		updatedPrice = (currentSupplierPrice + currentTax + paypal_fixed)* myProfitPercent / (1 - ebay_fees - paypal_fees);
	}
	
	public boolean IsEnoughDemand()
	{
		if ((  quantitySold>3   &&  1<sellerSoldTheItem &&  sellerPrice<30 && sellerPrice>10) 	
			||(quantitySold>1   &&  0<sellerSoldTheItem  && sellerPrice>30 && sellerPrice<70)
			||(quantitySold>0   &&  0<sellerSoldTheItem  && sellerPrice>70 && sellerPrice<1000))
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	
	@Override
	public String toString() {
		return    "\n****************"
				+ " \n"
				+ " Current market place price "+currentMarketPlacePrice+" \n"
				+ " updated price = "+updatedPrice+" \n"
				+ " current price = "+ currentSupplierPrice +" \n"
				+ " current TAX = "+currentTax+" \n"
				+ " my profit percent = "+myProfitPercent+" \n"
				+ " minimum price to sale = "+minPriceToSale+" \n"
				+ " supplierCode = " + supplierCode + " \n"
				+ " universal Code = " + universalCode + " \n"
				+ " marketPlaceCode = " + marketPlaceCode + " \n"
				+ " inStock = " + inStock + " \n"
				+ " marketPlaceLowestPrice = " + marketPlaceLowestPrice+" \n"
				+ " placeInLowestPrice = " + placeInLowestPrice + " \n"
				+ " marketPlaceResults = " + marketPlaceResults + " \n"
				+ " placeInbestMatch = " + placeInbestMatch+" \n"
				+ " itemRank = " + itemRank + " \n"
				+ " isPrime = " + isPrime + " \n"
				+ " isPreorder = " + isPreorder
				+ "\n****************";
	
//		return "supplierCode = " +supplierCode+ " isPrime=" + isPrime + " inStock=" + inStock +" Updated price= "+currentSupplierPrice+ " Updated TAX = "+currentTax;
	
	}

	
	public void rest()
	{

		this.supplierCode 					= null;
		this.universalCode					= null;
		this.marketPlaceCode				= null;
		this.title							= null;
		this.CodeType						= null;
		this.inStock 						= false;
		this.isPrime						= false;
		this.isPreorder						= false;
		this.currentSupplierPrice			= -1;
		this.currentMarketPlacePrice		= -1;
		this.currentTax						= -1;
		this.placeInLowestPrice 			= -1;
		this.placeInbestMatch 				= -1;
		this.marketPlaceLowestPrice 		= -1;
		this.marketPlaceSecondLowestPrice 	= -1;
		this.itemRank						= -1;
		this.marketPlaceResults				= -1;
		this.minPriceToSale					= -1;
		this.quantity						= -1;
		this.sellerPrice					= -1;
		this.quantitySold					= 0;
		this.sellerSoldTheItem				= 0;
		this.amountSoldByTheSeller			= 0;
		this.myProfitPercent				= 1.00;
		this.supplyerRequestSuccess 		= true;
		this.arbitraje						= -1;
		this.quantitiyStatus				= QuantitiyChangeStatus.NoChange;
		this.priceStatus					= PriceChangeStatus.NoChange;
	
	}
	
	
	
	
	
	/* Getter and Setters */
	
	public double getPrice() {
		return currentSupplierPrice;
	}

	public void setPrice(double currentPrice) {
		this.currentSupplierPrice = currentPrice;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public double getMarketPlaceLowestPrice() {
		return marketPlaceLowestPrice;
	}

	public void setMarketPlaceLowestPrice(double marketPlaceLowestPrice) {
		this.marketPlaceLowestPrice = marketPlaceLowestPrice;
	}

	public int getPlaceInLowestPrice() {
		return placeInLowestPrice;
	}

	public void setPlaceInLowestPrice(int placeInLowestPrice) {
		this.placeInLowestPrice = placeInLowestPrice;
	}

	public short getPlaceInbestMatch() {
		return placeInbestMatch;
	}

	public void setPlaceInbestMatch(short placeInbestMatch) {
		this.placeInbestMatch = placeInbestMatch;
	}

	public int getItemRank() {
		return itemRank;
	}

	public void setItemRank(int itemRank) {
		this.itemRank = itemRank;
	}

	public boolean isPrime() {
		return isPrime;
	}

	public void setPrime(boolean isPrime) {
		this.isPrime = isPrime;
	}

	public boolean isPreorder() {
		return isPreorder;
	}

	public void setPreorder(boolean isPreorder) {
		this.isPreorder = isPreorder;
	}

	
	public String getSupplierCode() {
		return supplierCode;
	}

	
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	
	public String getMarketPlaceCode() {
		return marketPlaceCode;
	}

	
	public void setMarketPlaceCode(String marketPlaceCode) {
		this.marketPlaceCode = marketPlaceCode;
	}

	

	public String getUniversalCode() {
		return universalCode;
	}

	
	public void setUniversalCode(String universalCode) {
		this.universalCode = universalCode;
	}

	
	public int getMarketPlaceResults() {
		return marketPlaceResults;
	}

	
	public void setMarketPlaceResults(int marketPlaceResults) {
		this.marketPlaceResults = marketPlaceResults;
	}

	
	public double getCurrentPrice() {
		return currentSupplierPrice;
	}

	
	public void setCurrentPrice(double currentPrice) {
		this.currentSupplierPrice = currentPrice;
	}

	
	public double getMinPriceToSale() {
		return minPriceToSale;
	}

	
	public void setMinPriceToSale(double minPriceToSale) {
		this.minPriceToSale = minPriceToSale;
	}

	public boolean isSupplyerRequestSuccess() {
		return supplyerRequestSuccess;
	}

	public void setSupplyerRequestSuccess(boolean supplyerRequestSuccess) {
		this.supplyerRequestSuccess = supplyerRequestSuccess;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getCurrentTax() {
		return currentTax;
	}
	
	public void setCurrentTax(double currentTax) {
		this.currentTax = currentTax;
	}

	public double getUpdatedPrice() {
		return updatedPrice;
	}

	public void setUpdatedPrice(double updatedPrice) {
		this.updatedPrice = updatedPrice;
	}

	
	public String getTitle() {
		return title;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	public double getCurrentSupplierPrice() {
		return currentSupplierPrice;
	}

	public void setCurrentSupplierPrice(double currentSupplierPrice) {
		this.currentSupplierPrice = currentSupplierPrice;
	}

	public double getCurrentMarketPlacePrice() {
		return currentMarketPlacePrice;
	}

	public void setCurrentMarketPlacePrice(double currentMarketPlacePrice) {
		this.currentMarketPlacePrice = currentMarketPlacePrice;
	}

	
	public double getMarketPlaceSecondLowestPrice() {
		return marketPlaceSecondLowestPrice;
	}

	
	public void setMarketPlaceSecondLowestPrice(double marketPlaceSecondLowestPrice) {
		this.marketPlaceSecondLowestPrice = marketPlaceSecondLowestPrice;
	}



	public double getMyProfitPercent() {
		return myProfitPercent;
	}

	public void setMyProfitPercent(double myProfitPercent) {
		this.myProfitPercent = myProfitPercent;
	}

	public QuantitiyChangeStatus getQuantitiyStatus() {
		return quantitiyStatus;
	}

	public void setQuantitiyStatus(QuantitiyChangeStatus quantitiyStatus) {
		this.quantitiyStatus = quantitiyStatus;
	}

	public PriceChangeStatus getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(PriceChangeStatus priceStatus) {
		this.priceStatus = priceStatus;
	}

	public String getCodeType() {
		return CodeType;
	}

	public void setCodeType(String codeType) {
		CodeType = codeType;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public int getSellerSoldTheItem() {
		return sellerSoldTheItem;
	}

	public void setSellerSoldTheItem(int sellerSoldTheItem) {
		this.sellerSoldTheItem = sellerSoldTheItem;
	}

	public int getAmountSoldByTheSeller() {
		return amountSoldByTheSeller;
	}
			   
	public void setAmountSoldByTheSeller(int  amountSoldByTheSeller) {
		this.amountSoldByTheSeller = amountSoldByTheSeller;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getSellerPrice() {
		return sellerPrice;
	}
	
	public String getBuyerUserID() {
		return buyerUserID;
	}


	public void setSellerPrice(double sellerPrice) {
		this.sellerPrice = sellerPrice;
	}
	
	public void setBuyerUserID(String buyerUserID) {
		this.buyerUserID = buyerUserID;
	}
	
		public double getArbitraje() {
		return arbitraje;
	}


	public String getCarrier() {
		return Carrier;
	}
	
		public void setArbitraje(double arbitraje) {
		this.arbitraje = arbitraje;
	}


	public void setCarrier(String carrier) {
		Carrier = carrier;
	}

	public String getTracking() {
		return Tracking;
	}

	public void setTracking(String tracking) {
		Tracking = tracking;
	}

}
