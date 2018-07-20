package MainPackage;

public class product {

	double price = 0;
	String ASIN = null;
	int prime=1;
	String Isbn=null;
	String link = null;
	int ebayResults = -1;
	double ebayLowestPrice = 0;
	double arbitraje = -100;
	int MAS_percent = -1;
	double Ebay_shipping = 0;
	int sold = -1;
	String bestresult = null;
	String UPC = null;
	int Amazon_Rank = -1;
	int Search = 0;
	double Sale_true = -1;
	int PlaceInBestMatch = -1;
	int PlaceInlowestprice = -1;
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
