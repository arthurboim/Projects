package Database;


import java.util.ArrayList;

public class Item{

		public String Title = null;
		public boolean ReadyToUpload = false;
		public String SupplierCode = null;
		public double AmazonPrice = -1;
		public double AmazonSalePrice = -1;
		public int Rank = -1;
		public ArrayList<String> Imeges = new ArrayList<String>();
		public ArrayList<String> Features  = new ArrayList<String>();
		public String EAN = null;
		public String Brand= null;
		public String Color= null;
		public String Format= null;
		public String Manufacturer= null;
		public String Model= null;
		public String MPN= null;
		public String NumberOfItems= null;
		public String PartNumber= null;
		public String UPC= null;
		public String PackageQuantity= null;
		public String Size= null;
		public String NumberOfPages= null;
		public String Language= null;
		public String ManufacturerMinimumAge = null;
		public String ManufacturerMaximumAge = null;	
		public boolean IsNew = false;
		public boolean prime = false;
		public boolean IsPreorder = false;
		public int MaximumDaysToShip = -1;
		public int MinimumDaysToShip = -1;
		public String Content = null;
		public String AvailabilityType= null;
		public String PublicationDate= null;
		public String OperatingSystem = null;
		public String Warranty = null;
		public String Publisher = null;
		public String PackageDimensions = null;
		public String PackageWeight = null;
		public String ItemDimensions = null;
		public String ItenWeight = null;
		public String Condition= null;
		public String PathFolder= null;
		public String SupplierCategory = null;
		public String[] PicturesString =  new String[1];
		public void ItemPrint() 
		{
			System.out.println();
			System.out.println("Item information:");
			if (this.Content!=null)
			System.out.println("Content = "+this.Content);
			if (this.SupplierCode!=null)
			System.out.println("Asin = "+this.SupplierCode);
			if (this.Rank!=-1)
			System.out.println("Rank = "+this.Rank);
			if (this.Title!=null)
			System.out.println("Title = "+this.Title);
			if (this.AmazonPrice!=-1)
			System.out.println("Amazon price = "+this.AmazonPrice);
			if (this.EAN!=null)
			System.out.println("EAN = "+this.EAN);
			if (this.Brand!=null)
			System.out.println("Brand = "+this.Brand);
			if (this.Color!=null)
			System.out.println("Color = "+this.Color);
			if (this.Format!=null)
			System.out.println("Format = "+this.Format);
			if (this.Manufacturer!=null)
			System.out.println("Manufacturer = "+this.Manufacturer);
			if (this.Model!=null)
			System.out.println("Model = "+this.Model);
			if (this.MPN!=null)
			System.out.println("MPN = "+this.MPN);
			if (this.NumberOfItems!=null)
			System.out.println("NumberOfItems = "+this.NumberOfItems);
			if (this.PartNumber!=null)
			System.out.println("PartNumber = "+this.PartNumber);
			if (this.UPC!=null)
			System.out.println("UPC = "+this.UPC);
			if (this.PackageQuantity!=null)
			System.out.println("PackageQuantity = "+this.PackageQuantity);
			if (this.Size!=null)
			System.out.println("Size = "+this.Size);
			if (this.NumberOfPages!=null)
			System.out.println("NumberOfPages = "+this.NumberOfPages);
			if (this.Language!=null)
			System.out.println("Language = "+this.Language);
			if (this.ManufacturerMinimumAge!=null)
			System.out.println("ManufacturerMinimumAge = "+this.ManufacturerMinimumAge);
			if (this.ManufacturerMaximumAge!=null)
			System.out.println("ManufacturerMaximumAge = "+this.ManufacturerMaximumAge);
			if (this.PublicationDate!=null)
			System.out.println("PublicationDate = "+this.PublicationDate);
			if (this.OperatingSystem!=null)
			System.out.println("OperatingSystem = "+this.OperatingSystem);
			if (this.Warranty!=null)
			System.out.println("Warranty = "+this.Warranty);
			if (this.Publisher!=null)
			System.out.println("Publisher = "+this.Publisher);
			if (this.PackageDimensions!=null)
			System.out.println("PackageDimensions = "+this.PackageDimensions);
			if (this.PackageWeight!=null)
			System.out.println("PackageWeight = "+this.PackageWeight);
			if (this.ItemDimensions!=null)
			System.out.println("ItemDimensions = "+this.ItemDimensions);
			if (this.ItenWeight!=null)
			System.out.println("ItemWeight = "+this.ItenWeight);
			if (this.AvailabilityType!=null)
			System.out.println("AvailabilityType = "+this.AvailabilityType);
			if (this.MinimumDaysToShip!=-1)
			System.out.println("MinimumDaysToShip = "+this.MinimumDaysToShip);
			if (this.MaximumDaysToShip!=-1)
			System.out.println("MaximumDaysToShip = "+this.MaximumDaysToShip);
			if (this.AmazonPrice!=-1)
			System.out.println("AmazonPrice = "+this.AmazonPrice);
			if (this.AmazonSalePrice!=-1)
			System.out.println("AmazonSalePrice = "+this.AmazonSalePrice);
			System.out.println("Is new? = "+this.IsNew);
			System.out.println("Is prime? = "+this.prime);
			System.out.println("Is preorder? = "+this.IsPreorder);
			for (String ele:this.Features)
			{
				System.out.println("* "+ele);
			}
			for (String ele:this.Imeges)
			{
				System.out.println("* "+ele);
			}
		}


}
