package Abstracts;

import Database.Item;

public abstract class MarketPlace 
{
	
	public abstract boolean RemoveItem(Item item);
	public abstract boolean UpdatePrice(Item item);
	public abstract boolean UpdateItemQuantity(Item item , int Quantity);
	public abstract boolean IsVeroBrand(Item item);
	public abstract boolean ForbiddenWordsCheck(Item item);
	public abstract boolean ListItem(Item item);
	public abstract boolean IsAlreadyExcist(Item item);
	
}
