package Main;

public class Order {

	public String OrderId;
	public String Buyer_User_ID;
	public String Buyer_Email;
	public String Buyer_Full_Name;
	public String Street;
	public String Street2;
	public String City;
	public String State_Province;
	public String Zip_Postal_Code;
	public String Phone_Number;
	public double Total_price;
	public String Sale_date;
	public int Feedback_left=0;
	public String EbayId;
	public String AmazonAsin;
	public String OrderStatus;
	public String CheckoutStatus;
	public String Amazon_OrderNumber;
	public String Tracking;
	public String Carrier;
	
	public String getAmazon_OrderNumber() {
		return Amazon_OrderNumber;
	}
	public void setAmazon_OrderNumber(String amazon_OrderNumber) {
		Amazon_OrderNumber = amazon_OrderNumber;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	
	public String getBuyer_User_ID() {
		return Buyer_User_ID;
	}
	public void setBuyer_User_ID(String buyer_User_ID) {
		Buyer_User_ID = buyer_User_ID;
	}
	public String getBuyer_Email() {
		return Buyer_Email;
	}
	public void setBuyer_Email(String buyer_Email) {
		Buyer_Email = buyer_Email;
	}
	public String getBuyer_Full_Name() {
		return Buyer_Full_Name;
	}
	public void setBuyer_Full_Name(String buyer_Full_Name) {
		Buyer_Full_Name = buyer_Full_Name;
	}
	public String getStreet() {
		return Street;
	}
	public void setStreet(String street) {
		Street = street;
	}
	public String getStreet2() {
		return Street2;
	}
	public void setStreet2(String street2) {
		Street2 = street2;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState_Province() {
		return State_Province;
	}
	public void setState_Province(String state_Province) {
		State_Province = state_Province;
	}
	public String getZip_Postal_Code() {
		return Zip_Postal_Code;
	}
	public void setZip_Postal_Code(String zip_Postal_Code) {
		Zip_Postal_Code = zip_Postal_Code;
	}
	public String getPhone_Number() {
		return Phone_Number;
	}
	public void setPhone_Number(String phone_Number) {
		Phone_Number = phone_Number;
	}
	public double getTotal_price() {
		return Total_price;
	}
	public void setTotal_price(double total_price) {
		Total_price = total_price;
	}
	public String getSale_date() {
		return Sale_date;
	}
	public void setSale_date(String sale_date) {
		Sale_date = sale_date;
	}
	public int getFeedback_left() {
		return Feedback_left;
	}
	public void setFeedback_left(int feedback_left) {
		Feedback_left = feedback_left;
	}
	public String getEbayId() {
		return EbayId;
	}
	public void setEbayId(String ebayId) {
		EbayId = ebayId;
	}
	public String getAmazonAsin() {
		return AmazonAsin;
	}
	public void setAmazonAsin(String amazonAsin) {
		AmazonAsin = amazonAsin;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getCheckoutStatus() {
		return CheckoutStatus;
	}
	public void setCheckoutStatus(String checkoutStatus) {
		CheckoutStatus = checkoutStatus;
	}


	
}
