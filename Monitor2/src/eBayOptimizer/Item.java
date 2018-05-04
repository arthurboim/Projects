package eBayOptimizer;


public class Item {
		public int id = -1;
		public String Asin = null;
		public String EbayId = null;
		public String BestResults = null;
		public int LowestPricePosition = -1;
		public int BestMatchPosition = -1;
		public int EbayResults = -1;
		public int QuantitySold = -1;
		public String UPC = "";
		public int ProfitPersent = -1;
		public java.util.Date StartDate= null;
		public String MainImage = null;
		public String Title = "";
		public int Sold = -1;
		public int optimized = 0;
		public double sale_ture = 0;
		public int TransactionsLastWeek = -1;
		public int TransactionsLastMonth = -1;
		public String LastSaleDate = null;
		public double Tax = -1;
		public String[] PicturesString = null;
		public String Category = null;
		public int getQuantitySold() {
			return QuantitySold;
		}
		public void setQuantitySold(int quantitySold) {
			QuantitySold = quantitySold;
		}
		

}
