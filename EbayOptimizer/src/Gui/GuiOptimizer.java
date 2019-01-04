package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import Database.GetSetDB;
import Database.Item;
import Ebay.EbayCalls;

import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiOptimizer {

	private JFrame frame;
	private JTextField DateFromJtextField;
	private JTextField DateToJtextField;
	private JTextField EbayIdJtextField;
	public static int ThreadsNumber = 10;
	public static String ImageDirectory = "C:\\Users\\Noname\\workspace\\EbayOptimizer\\Itemspictures";
	/**
	 * Launch the application.
	 */
	public void MainGuiOptimizer() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiOptimizer window = new GuiOptimizer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiOptimizer() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 408, 265);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DateFromJtextField = new JTextField();
		DateFromJtextField.setText("yyyy-mm-dd");
		DateFromJtextField.setColumns(10);
		
		JRadioButton ByDateRadioButton = new JRadioButton("By Date");
		
		JLabel lblFrom = new JLabel("From");
		
		DateToJtextField = new JTextField();
		DateToJtextField.setText("yyyy-mm-dd");
		DateToJtextField.setColumns(10);
		
		JLabel lblTo = new JLabel("To");
		
		JRadioButton ByCodeRadioButton = new JRadioButton("By Ebay Id");
		
		EbayIdJtextField = new JTextField();
		EbayIdJtextField.setColumns(10);
		
		JLabel EbayId = new JLabel("Ebay Id");
		
		JLabel lblTitleOptimizer = new JLabel("Title optimizer");
		lblTitleOptimizer.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JButton StartButton = new JButton("Start");
		StartButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String quary = null;
				if (ByDateRadioButton.isSelected()) 
				{
					quary = "SELECT * FROM amazon.online where StartDate >= '"+DateFromJtextField.getText()+"' and StartDate <= '"+DateToJtextField.getText()+"' and optimized = 0;"; 
					System.out.println(quary);
					frame.setVisible(false);

				}else
				if (ByCodeRadioButton.isSelected()) 
				{
					quary = "SELECT * FROM amazon.online where ebayid = '"+EbayIdJtextField.getText()+"';"; 
					System.out.println(quary);
					frame.setVisible(false);
				}
				else System.out.println("Choose date or code");
				
				GetSetDB db = new GetSetDB();
				ArrayList<Item> ItemsList = new ArrayList<Item>();
				db.GetNotOptimizedItemsGui(ItemsList, quary);
				EbayCalls EbayCalls = new EbayCalls();
				
				ArrayList<GuiData> ListGuiData = new ArrayList<GuiData>();
				System.out.println("Items amount  = "+ItemsList.size());
				ExecutorService pool = Executors.newFixedThreadPool(3);
				int pivot = 0;
				int i=0;
				int max = 0;

				for (i=1;i<ItemsList.size();i++)
				{
					if (ItemsList.size()/i>max && ItemsList.size()%i<ItemsList.size()/i&& max<i)
						max = i;
				}
				ThreadsNumber = max+1;
				System.out.println("Threads amount -> "+ThreadsNumber);
				System.out.println("Divided -> "+ItemsList.size()/ThreadsNumber);
				System.out.println("Left -> "+ItemsList.size()%ThreadsNumber);
				for (i=0;i<ThreadsNumber;i++) 
				{ 
					if (i==ThreadsNumber-1) pool.submit(new OneShotTask(ItemsList,ListGuiData,pivot,ItemsList.size()));
					else{
				    pool.submit(new OneShotTask(ItemsList,ListGuiData,pivot,pivot+ItemsList.size()/ThreadsNumber));
				    pivot+=ItemsList.size()/ThreadsNumber;
					}
				}
				
				/*
				for (i=0;i<ItemsList.size();i++)
				{
					try {
					ArrayList<Item> ItemsList2 = new ArrayList<Item>();
					EbayCalls.Get_Item_Info(ItemsList.get(i));
					ItemsList2 = EbayCalls.Find_Items_Info_Gui(ItemsList.get(i));
					GuiData GuiData = new GuiData();
					GuiData.Competitors = ItemsList2;
					GuiData.Item = ItemsList.get(i);
					GuiData.Fetching = "Done";
					GuiData.Update = "Not Updated";
					ListGuiData.add(GuiData);					
					} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
					
				}
				*/
			    LinkedHashSet<GuiData> lhs = new LinkedHashSet<GuiData>();
			    
			     /* Adding ArrayList elements to the LinkedHashSet
			      * in order to remove the duplicate elements and 
			      * to preserve the insertion order.
			      */
			     lhs.addAll(ListGuiData);
			  
			     // Removing ArrayList elements
			     ListGuiData.clear();
			 
			     // Adding LinkedHashSet elements to the ArrayList
			     ListGuiData.addAll(lhs);
				GuiTableResults GuiTableResults = new GuiTableResults(ListGuiData);
				GuiTableResults.TableMain(ListGuiData);
				/*
				GuiTitleResults GuiResults = null;
				try {
					GuiResults = new GuiTitleResults(ListGuiData.get(0).Competitors , ItemsList.get(0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				GuiResults.mainGuiTitleResults(ListGuiData.get(0).Competitors , ItemsList.get(0));
				 */
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(ByDateRadioButton)
						.addComponent(ByCodeRadioButton))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(EbayId)
						.addComponent(lblFrom))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblTitleOptimizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addComponent(DateFromJtextField, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(DateToJtextField, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
							.addGap(50))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(EbayIdJtextField, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(229, Short.MAX_VALUE)
					.addComponent(StartButton)
					.addGap(150))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addComponent(lblTitleOptimizer)
					.addGap(42)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ByDateRadioButton)
						.addComponent(lblFrom)
						.addComponent(DateFromJtextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(DateToJtextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTo))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ByCodeRadioButton)
						.addComponent(EbayId)
						.addComponent(EbayIdJtextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(StartButton)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	class OneShotTask implements Runnable 
	    {

	        ArrayList<Item> ItemsList = new ArrayList<Item>();
	        ArrayList<GuiData> ListGuiData = new ArrayList<GuiData>();
	        int i = 0;
	        int j = 0;
	        OneShotTask(ArrayList<Item> ItemsList,ArrayList<GuiData> ListGuiData,int i,int j) 
	        { 
	        	this.ItemsList = ItemsList;
	        	this.i = i;
	        	this.j = j;
	        	this.ListGuiData = ListGuiData;
	
	        }
	        
	        
	        public void run() {
	        	System.out.println("Thread id  = "+Thread.currentThread().getId());
	        	System.out.println("We check part "+i+" to "+j);
	            someFunc(ItemsList,ListGuiData,i,j);
	        }
	        
	        
	        void someFunc(ArrayList<Item> ItemsList,ArrayList<GuiData> ListGuiData,int k,int j)
	        {

	        	
	        	for(int i=k;i<j;i++)
	        	{
				try {
					EbayCalls EbayCalls = new EbayCalls();
					ArrayList<Item> ItemsList2 = new ArrayList<Item>();
					EbayCalls.Get_Item_Info(ItemsList.get(i));
					ItemsList2 = EbayCalls.Find_Items_Info_Gui(ItemsList.get(i));
					GuiData GuiData = new GuiData();
					GuiData.Competitors = ItemsList2;
					GuiData.Item = ItemsList.get(i);
					GuiData.Fetching = "Done";
					GuiData.Update = "Not Updated";
					ListGuiData.add(GuiData);	
					System.out.println("Thread id  = "+Thread.currentThread().getId());
					
					for(int j2=0;j2<GuiData.Item.PicturesString.length;j2++)
					{
						saveImage(GuiData.Item.PicturesString[j2],ImageDirectory,GuiData.Item.EbayId,j2);
					}
					int comp = 0;
					if (GuiData.Competitors.size()<3) 
						{
						 comp = GuiData.Competitors.size();
						}else 
						{
							 comp  = 3;
						}
					
					for (int i1=0;i1<comp;i1++)
					{
						for(int j1=0;j1<GuiData.Competitors.get(i1).PicturesString.length;j1++)
						{
							saveImage(GuiData.Competitors.get(i1).PicturesString[j1],ImageDirectory,GuiData.Competitors.get(i1).EbayId,j1);
						}

					}
					
					} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
	        	}
	        }

	    }


	public static void saveImage(String imageUrl, String destinationFile,String EbayId,int j) throws IOException {
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile+"\\"+EbayId+"-"+j);

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) {
	        os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}

}

