package Item;

public class Item {

	private String      supplierCode;
	private String		marketPlaceCode;
	private double 		price;
	private boolean 	inStock;
	private double 		marketPlaceLowestPrice;
	private short 		placeInLowestPrice;
	private short		placeInbestMatch;
	private int 		itemRank;
	private boolean		isPrime;
	private boolean   	isPreorder;
	/* Constructor */
	
	public Item() {
		this.supplierCode 				  	= null;
		this.inStock 				= false;
		this.isPrime				= false;
		this.isPreorder				= false;
		this.price				    = -1;
		this.placeInLowestPrice 	= -1;
		this.placeInbestMatch 		= -1;
		this.marketPlaceLowestPrice = -1;
		this.itemRank				= -1;
	}

	public Item(String code) {
	
		this.supplierCode = code;
		this.inStock 				= false;
		this.isPrime				= false;
		this.isPreorder				= false;
		this.price				    = -1;
		this.placeInLowestPrice 	= -1;
		this.placeInbestMatch 		= -1;
		this.marketPlaceLowestPrice = -1;
		this.itemRank				= -1;
	}

	
	

	
	
	
	
	/* Getter and Setters */
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public short getPlaceInLowestPrice() {
		return placeInLowestPrice;
	}

	public void setPlaceInLowestPrice(short placeInLowestPrice) {
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

	
	@Override
	public String toString() {
		return "Item [supplierCode=" + supplierCode + ", marketPlaceCode=" + marketPlaceCode + ", price=" + price
				+ ", inStock=" + inStock + ", marketPlaceLowestPrice=" + marketPlaceLowestPrice
				+ ", placeInLowestPrice=" + placeInLowestPrice + ", placeInbestMatch=" + placeInbestMatch
				+ ", itemRank=" + itemRank + ", isPrime=" + isPrime + ", isPreorder=" + isPreorder + "]";
	}


}
