package Main;

public class ProductOnline {

	String AmazonAsin;
	String EbayId;
	String Title;
	Double Price;
	Double AmazonPrice;
	public double ProfitPersent = -1;
	public double BreakEven = -1;
	int flag =-1;
	



	public String getAmazonAsin() {
		return AmazonAsin;
	}


	public void setAmazonAsin(String amazonAsin) {
		AmazonAsin = amazonAsin;
	}


	public String getEbayId() {
		return EbayId;
	}


	public void setEbayId(String ebayId) {
		EbayId = ebayId;
	}


	public String getTitle() {
		return Title;
	}


	public void setTitle(String title) {
		Title = title;
	}



	public Double getPrice() {
		return Price;
	}


	public void setPrice(Double price) {
		Price = price;
	}



	
}
