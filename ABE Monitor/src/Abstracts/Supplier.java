package Abstracts;



import Database.Item;

public abstract class Supplier 
{
	
	public abstract boolean StockCheck(Item item); 
	public abstract boolean PriceCheck(Item item); 
	public abstract boolean ShippingTimeCheck(Item item);
	public abstract Item    BuildItem(String code);


}
