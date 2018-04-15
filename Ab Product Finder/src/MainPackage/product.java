package MainPackage;

public class product {

	
	String title = null;
	double price = 0;
	double priceEbay = 0;
	String ASIN = null;
	String Date;
	int addon=0;
	int prime=1;
	int request = 1;
	int reviews=-1;
	String Isbn=null;
	String ebayid = null;
	String Ean=null;
	String link = null;
	int ebayResults = -1;
	double ebayLowestPrice = 0;
	double arbitraje = -100;
	int MAS_percent = -1;
	double Ebay_shipping = 0;
	int sold = -1;
	String bestresult = null;
	String UPC = null;
	String Model_Number = null;
	int Amazon_Rank = -1;
	int Search = 0;

	int amazon_search_inside = 0;
	String brand = null;
	String category = null;
	double MedianPrice = -1;
	double Sale_true = -1;
	int PlaceInBestMatch = -1;
	int PlaceInlowestprice = -1;
	double LowestSold = -1;
	double MedianPriceSold = -1;
	int PlaceInlowestpriceSold = -1;
	int time_to_send = -1;
	String AvailabilityType = null;
	double Breakevenlowestprice = -1;
	
	public String getBestresult() {
		return bestresult;
	}
	public void setBestresult(String bestresult) {
		this.bestresult = bestresult;
	}

	
}
