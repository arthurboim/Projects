package Item;

public class Item {

	private String      supplierCode;
	private String		marketPlaceCode;
	private String		universalCode;
	private double 		currentPrice;
	private double 		minPriceToSale;
	private boolean 	inStock;
	private double 		marketPlaceLowestPrice;
	private int 		marketPlaceResults;
	private int 		placeInLowestPrice;
	private short		placeInbestMatch;
	private int 		itemRank;
	private boolean		isPrime;
	private boolean   	isPreorder;
	private boolean		supplyerRequestSuccess;
	
	
	/* Constructor */
	
	public Item() {
		this.supplierCode 			= null;
		this.universalCode			= null;
		this.marketPlaceCode		= null;
		this.inStock 				= false;
		this.isPrime				= false;
		this.isPreorder				= false;
		this.currentPrice			= -1;
		this.placeInLowestPrice 	= -1;
		this.placeInbestMatch 		= -1;
		this.marketPlaceLowestPrice = -1;
		this.itemRank				= -1;
		this.marketPlaceResults		= -1;
		this.minPriceToSale			= -1;
		this.supplyerRequestSuccess = true;
	}

	public Item(String code) {
	
		this.supplierCode 			= code;
		this.universalCode			= null;
		this.marketPlaceCode		= null;
		this.inStock 				= false;
		this.isPrime				= false;
		this.isPreorder				= false;
		this.currentPrice			= -1;
		this.placeInLowestPrice 	= -1;
		this.placeInbestMatch 		= -1;
		this.marketPlaceLowestPrice = -1;
		this.itemRank				= -1;
		this.marketPlaceResults		= -1;
		this.minPriceToSale			= -1;
		this.supplyerRequestSuccess = true;

	}

	
	/* Note: Calculation not take in account supplier taxes */
	private void SetMinPriceToSale()
	{
		double ebay_fees = 0.09;
		double paypal_fees = 0.039;
		double paypal_fixed = 0.3;
		double my_percent = 1.06;
		minPriceToSale = ((currentPrice * my_percent) + paypal_fixed) / (1 - ebay_fees - paypal_fees);
	}

	
	
	/* Getter and Setters */
	
	public double getPrice() {
		return currentPrice;
	}

	public void setPrice(double currentPrice) {
		this.currentPrice = currentPrice;
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

	
	@Override
	public String toString() {
//		return "Item [supplierCode=" + supplierCode + ", marketPlaceCode=" + marketPlaceCode + ", currentPrice=" + currentPrice
//				+ ", inStock=" + inStock + ", marketPlaceLowestPrice=" + marketPlaceLowestPrice
//				+ ", placeInLowestPrice=" + placeInLowestPrice + ", placeInbestMatch=" + placeInbestMatch
//				+ ", itemRank=" + itemRank + ", isPrime=" + isPrime + ", isPreorder=" + isPreorder + "]";
	
		return "supplierCode = " +supplierCode+ " isPrime=" + isPrime + " inStock=" + inStock + " isPreorder=" + isPreorder ;
	
	}

	
	
	public int getMarketPlaceResults() {
		return marketPlaceResults;
	}

	
	
	public void setMarketPlaceResults(int marketPlaceResults) {
		this.marketPlaceResults = marketPlaceResults;
	}

	
	public double getCurrentPrice() {
		return currentPrice;
	}

	
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
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

}
