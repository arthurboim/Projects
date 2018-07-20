package Supplier;

import java.util.ArrayList;

import Item.Item;

public interface SupplierInterface {

	/* Update the price and stock of the items */
	void GetItemsUpdate(ArrayList<Item> ListOfItems);
	
	/* Create lists for given items */
	//BuildItems(ArrayList<List> ListOfItems)
}
