package Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;

import AmazonOrders.Tracking_number_amazon;
import Database.DatabaseOp;
import Ebay.ItemSpecifics;
import Ebay.OrdersInfo;
import Main.DatabaseMain;
import Main.Main;
import Main.Store;
import Main.Yaballe;
import PriceChanger.ItemsPosition;

public class OrdersGui {
	public  JComboBox comboBox_1 = new JComboBox();
	public static String StoreName = "All";
	private JFrame frmAbeCommerce;
	private JTable table;
	private JTextField textField;
	Object[][] values;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	public static JLabel PriceCount1 = new JLabel("0");
	public static JLabel PriceCount2 = new JLabel("0");
	public static JLabel PriceCount3 = new JLabel("0");
	public static JLabel Percent1 = new JLabel("0%");
	public static JLabel Percent2 = new JLabel("0%");
	public static JLabel Percent3 = new JLabel("0%");
	public static JLabel SaleThrough = new JLabel("0");
	public static JLabel lblAvgDayProfit = new JLabel("Avg day profit:");
	public static JLabel AvgDayProfit = new JLabel("0");
	public static  JLabel AvgDaySalesVal = new JLabel("0");
	public static JLabel SoldLast30Days = new JLabel("0");
	public static JLabel StoreNameLable = new JLabel("Null");
	public static  JLabel Store_name_Label = new JLabel("0");
	public static  JLabel  Monthly_profit= new JLabel("0");
	public static  JLabel  MonthlyOrders= new JLabel("0");
	public static  JLabel AvgItemProfit = new JLabel("0");
	public static  JLabel OnlineItemsAmount= new JLabel("0"); 
	
	
	
	
	/**
	 * Create the application.
	 */
	public OrdersGui() {
		initialize();
		StoreNameLable.setText(StoreName);
		Store_name_Label.setText(StoreName);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAbeCommerce = new JFrame();
		frmAbeCommerce.setTitle("ABE Commerce ");
		frmAbeCommerce.setBounds(100, 100, 450, 300);
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		frmAbeCommerce.setSize(d.width, d.height);
		frmAbeCommerce.setLocationByPlatform(true);
		frmAbeCommerce.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setToolTipText("Orders");
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JPanel Orders_Tab = new JPanel();
		tabbedPane.addTab("Orders", null, Orders_Tab, null);
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		ArrayList<Order> List = new ArrayList<Order>();
		DatabaseGui DatabaseGui = new DatabaseGui();
		DatabaseGui.GetOrders(List);
		ConvertForTable(List);
		table.setModel(new DefaultTableModel(ConvertForTable(List),new String[] {"Sotre name", "eBay username", "Order status", "Sale date", "Amazon price", "Tax", "Profit", "Ebay price", "Amazon order id", "Tracking number", "Carrier", "Feedback", "Checkout status", "Ebay item id", "Asin", "Edit", "Retry"}));
		table.getColumnModel().getColumn(1).setPreferredWidth(86);
		scrollPane.setViewportView(table);

		// here need to add the buttons //
		
		
		
		
		
		
		
		
		
		
		
		
		JPanel StoreStatusPanel = new JPanel();
		StoreStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		
		JLabel label_1 = new JLabel("Store status");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JPanel Item_Search_Panel = new JPanel();
		Item_Search_Panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		
		JPanel Store_Operations = new JPanel();
		Store_Operations.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		GroupLayout gl_Orders_Tab = new GroupLayout(Orders_Tab);
		gl_Orders_Tab.setHorizontalGroup(
			gl_Orders_Tab.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Orders_Tab.createSequentialGroup()
					.addGroup(gl_Orders_Tab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1979, Short.MAX_VALUE))
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addGap(22)
							.addComponent(Store_Operations, GroupLayout.PREFERRED_SIZE, 498, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Item_Search_Panel, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(StoreStatusPanel, GroupLayout.PREFERRED_SIZE, 661, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_Orders_Tab.setVerticalGroup(
			gl_Orders_Tab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Orders_Tab.createSequentialGroup()
					.addGroup(gl_Orders_Tab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addContainerGap()
							.addComponent(Item_Search_Panel, 0, 0, Short.MAX_VALUE))
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addContainerGap()
							.addComponent(Store_Operations, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addContainerGap()
							.addComponent(StoreStatusPanel, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
						.addGroup(gl_Orders_Tab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_Orders_Tab.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_5, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addGap(17)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 654, GroupLayout.PREFERRED_SIZE)
					.addGap(217))
		);
		panel_5.setLayout(null);
		
		JLabel lblStoreInfo = new JLabel("Store info");
		lblStoreInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStoreInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreInfo.setBounds(10, 11, 154, 23);
		panel_5.add(lblStoreInfo);
		
		JLabel lblStoreName_1 = new JLabel("Store name:");
		lblStoreName_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStoreName_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreName_1.setBounds(10, 55, 167, 14);
		panel_5.add(lblStoreName_1);
		

		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"All", "ConfigFile"}));
		comboBox_1.setBounds(21, 183, 143, 20);
		panel_5.add(comboBox_1);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				StoreName = comboBox_1.getSelectedItem().toString();
				System.out.println(StoreName);
				StoreNameLable.setText(StoreName);
			
			}
		});
		btnLoad.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLoad.setBounds(47, 221, 89, 23);
		panel_5.add(btnLoad);
		StoreNameLable.setHorizontalAlignment(SwingConstants.CENTER);
		

		StoreNameLable.setBounds(10, 80, 167, 23);
		panel_5.add(StoreNameLable);
		
		JLabel OnlineItemsLable = new JLabel("");
		OnlineItemsLable.setBounds(118, 80, 46, 14);
		panel_5.add(OnlineItemsLable);
		
		JLabel StoreLoadedName = new JLabel("");
		StoreLoadedName.setBounds(109, 55, 46, 14);
		panel_5.add(StoreLoadedName);
		
		JLabel lblItemsPriceDevition = new JLabel("Items price devition");
		lblItemsPriceDevition.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemsPriceDevition.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblNewLabel = new JLabel("0-30$");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblPriceBeetwen = new JLabel("30-70$");
		lblPriceBeetwen.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblPriceBeetwen_1 = new JLabel("70-300$");
		lblPriceBeetwen_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		PriceCount1.setFont(new Font("Tahoma", Font.BOLD, 14));
		PriceCount1.setHorizontalAlignment(SwingConstants.CENTER);
		
		PriceCount2.setFont(new Font("Tahoma", Font.BOLD, 14));
		PriceCount2.setHorizontalAlignment(SwingConstants.CENTER);
		
		PriceCount3.setFont(new Font("Tahoma", Font.BOLD, 14));
		PriceCount3.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnRefreshPrices = new JButton("Refresh Prices");
		
		
		
		btnRefreshPrices.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		Percent1.setFont(new Font("Tahoma", Font.BOLD, 14));
		Percent1.setHorizontalAlignment(SwingConstants.CENTER);
		
		Percent2.setFont(new Font("Tahoma", Font.BOLD, 14));
		Percent2.setHorizontalAlignment(SwingConstants.CENTER);
		
		Percent3.setFont(new Font("Tahoma", Font.BOLD, 14));
		Percent3.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnRefreshPrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (OrdersGui.StoreName.equals("All"))
				{
				for (Store ele:Main.ListOfStores)
				{
					CalculateItemsDevition(ele);
				}
				}else
				{
					CalculateItemsDevition(Main.GetStoreByName(OrdersGui.StoreName));
				}
				
			}
		});
		
		
		

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(22)
							.addComponent(lblItemsPriceDevition, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblPriceBeetwen_1)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(PriceCount3, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblPriceBeetwen)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(PriceCount2, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(PriceCount1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(Percent2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
										.addComponent(Percent1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(32)
									.addComponent(Percent3))))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnRefreshPrices, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblItemsPriceDevition)
					.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(PriceCount1)
						.addComponent(Percent1))
					.addGap(31)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPriceBeetwen)
						.addComponent(PriceCount2)
						.addComponent(Percent2))
					.addGap(38)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPriceBeetwen_1)
						.addComponent(PriceCount3)
						.addComponent(Percent3))
					.addGap(57)
					.addComponent(btnRefreshPrices, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(26))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Price Changer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Store operations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_Store_Operations = new GroupLayout(Store_Operations);
		gl_Store_Operations.setHorizontalGroup(
			gl_Store_Operations.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Store_Operations.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
					.addGap(29))
		);
		gl_Store_Operations.setVerticalGroup(
			gl_Store_Operations.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Store_Operations.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_Store_Operations.createParallelGroup(Alignment.BASELINE)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(41, Short.MAX_VALUE))
		);
		panel_4.setLayout(null);
		
		JButton btnUpdateItemSpecifics = new JButton("Update item specifics");
		btnUpdateItemSpecifics.setBounds(26, 23, 187, 25);
		panel_4.add(btnUpdateItemSpecifics);
		btnUpdateItemSpecifics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread Thread = new Thread(new ItemSpecifics());
				Thread.start();
				Thread = null;
				System.gc();
				}
		});
		btnUpdateItemSpecifics.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnUpdateOnlineItems = new JButton("Update online items");
		btnUpdateOnlineItems.setBounds(26, 59, 187, 25);
		panel_4.add(btnUpdateOnlineItems);
		btnUpdateOnlineItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread Thread = new Thread(new Yaballe());
				Thread.start();
				Thread = null;
				System.gc();
			}
		});
		
		btnUpdateOnlineItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnUpdateTrackingNumbers = new JButton("Update tracking numbers");
		btnUpdateTrackingNumbers.setBounds(26, 131, 187, 25);
		panel_4.add(btnUpdateTrackingNumbers);
		btnUpdateTrackingNumbers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Tracking_number_amazon());
				thread.start();
				thread = null;
				System.gc();
			}
		});
		btnUpdateTrackingNumbers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_1 = new JButton("Delete failed");
		btnNewButton_1.setBounds(26, 198, 187, 25);
		panel_4.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				DeleteFailedOrders();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_5 = new JButton("Check new sales");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GetOrders();
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_5.setBounds(26, 167, 187, 23);
		panel_4.add(btnNewButton_5);
		panel_3.setLayout(null);
		
		JButton btnNewButton_3 = new JButton("Price change");
		btnNewButton_3.setBounds(31, 198, 155, 25);
		panel_3.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnAmazonPriceCheck = new JButton("Amazon price check");
		btnAmazonPriceCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				AmazonPriceAndTaxCheck();
			}
		});
		btnAmazonPriceCheck.setBounds(31, 132, 155, 25);
		panel_3.add(btnAmazonPriceCheck);
		btnAmazonPriceCheck.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnCheckSales = new JButton("Check Sales");
		btnCheckSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				CheckSold();
			}
		});
		btnCheckSales.setBounds(31, 61, 155, 25);
		panel_3.add(btnCheckSales);
		btnCheckSales.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_2 = new JButton("Price changer all");
		btnNewButton_2.setBounds(31, 25, 155, 25);
		panel_3.add(btnNewButton_2);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_4 = new JButton("Check position");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ItemPostionCheck();
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_4.setBounds(31, 98, 155, 23);
		panel_3.add(btnNewButton_4);
		
		JButton btnNewButton = new JButton("Calculate data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				CalculateData();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(30, 168, 156, 25);
		panel_3.add(btnNewButton);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					Thread t = new Thread(new ItemsPosition());
					t.start();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		});
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				PriceChanger();
			}
			
		});
		Store_Operations.setLayout(gl_Store_Operations);
		
		JLabel lblCurrentMonth = new JLabel("Current month");
		lblCurrentMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentMonth.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel Profit_defenition = new JLabel("Monthly profit:");
		Profit_defenition.setFont(new Font("Tahoma", Font.BOLD, 14));
		

		Monthly_profit.setHorizontalAlignment(SwingConstants.CENTER);
		Monthly_profit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblAvgItemProfit = new JLabel("Avg item profit:");
		lblAvgItemProfit.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		AvgItemProfit.setHorizontalAlignment(SwingConstants.CENTER);
		AvgItemProfit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblMonthlyOrders = new JLabel("Monthly orders:");
		lblMonthlyOrders.setFont(new Font("Tahoma", Font.BOLD, 14));
		
	
		MonthlyOrders.setHorizontalAlignment(SwingConstants.CENTER);
		MonthlyOrders.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblOnlineItems = new JLabel("Online items:");
		lblOnlineItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel Store_name_defenition = new JLabel("Store name:");
		Store_name_defenition.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblGlobal = new JLabel("Global");
		lblGlobal.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		Store_name_Label.setHorizontalAlignment(SwingConstants.CENTER);
		Store_name_Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
	
		OnlineItemsAmount.setHorizontalAlignment(SwingConstants.CENTER);
		OnlineItemsAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblSaleThrough = new JLabel("Sale through:");
		lblSaleThrough.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblDays = new JLabel("30 Days");
		lblDays.setHorizontalAlignment(SwingConstants.CENTER);
		lblDays.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblSoldLast = new JLabel("Sold last 30 days:");
		lblSoldLast.setFont(new Font("Tahoma", Font.BOLD, 14));
		

		SoldLast30Days.setHorizontalAlignment(SwingConstants.CENTER);
		SoldLast30Days.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		lblAvgDayProfit.setFont(new Font("Tahoma", Font.BOLD, 14));
		JLabel AvgDaySales = new JLabel("Avg day sales:");
		AvgDaySales.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		AvgDayProfit.setHorizontalAlignment(SwingConstants.CENTER);
		AvgDayProfit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		AvgDaySalesVal.setHorizontalAlignment(SwingConstants.CENTER);
		AvgDaySalesVal.setFont(new Font("Tahoma", Font.PLAIN, 14));

		
		SaleThrough.setHorizontalAlignment(SwingConstants.CENTER);
		SaleThrough.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JButton RefreshButton = new JButton("Refresh");
		RefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				UpdateStoreStatus(Main.GetStoreByName(StoreName));
			}
		});
		
		JPanel Current_Month_Panel = new JPanel();
		Current_Month_Panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		
		JPanel Days30Panel = new JPanel();
		Days30Panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		
		JPanel Global_panel = new JPanel();
		Global_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		GroupLayout gl_StoreStatusPanel = new GroupLayout(StoreStatusPanel);
		gl_StoreStatusPanel.setHorizontalGroup(
			gl_StoreStatusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_StoreStatusPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_StoreStatusPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_StoreStatusPanel.createSequentialGroup()
							.addComponent(Current_Month_Panel, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(Days30Panel, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(Global_panel, GroupLayout.PREFERRED_SIZE, 197, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_StoreStatusPanel.createSequentialGroup()
							.addComponent(RefreshButton, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
							.addGap(235)))
					.addGap(39))
				.addGroup(gl_StoreStatusPanel.createSequentialGroup()
					.addGap(253)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(608, Short.MAX_VALUE))
		);
		gl_StoreStatusPanel.setVerticalGroup(
			gl_StoreStatusPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_StoreStatusPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
					.addGap(17)
					.addGroup(gl_StoreStatusPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(Days30Panel, 0, 0, Short.MAX_VALUE)
						.addGroup(gl_StoreStatusPanel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(Current_Month_Panel, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
							.addComponent(Global_panel, GroupLayout.PREFERRED_SIZE, 137, Short.MAX_VALUE)))
					.addGap(18)
					.addComponent(RefreshButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(31))
		);

		GroupLayout gl_Global_panel = new GroupLayout(Global_panel);
		gl_Global_panel.setHorizontalGroup(
			gl_Global_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Global_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_Global_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSaleThrough)
						.addComponent(lblOnlineItems)
						.addComponent(Store_name_defenition))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_Global_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(Store_name_Label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(OnlineItemsAmount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(SaleThrough, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(gl_Global_panel.createSequentialGroup()
					.addGap(71)
					.addComponent(lblGlobal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(239))
		);
		gl_Global_panel.setVerticalGroup(
			gl_Global_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Global_panel.createSequentialGroup()
					.addGap(8)
					.addComponent(lblGlobal)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_Global_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(Store_name_defenition, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(Store_name_Label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_Global_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSaleThrough)
						.addComponent(SaleThrough, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_Global_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOnlineItems)
						.addComponent(OnlineItemsAmount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(58))
		);
		Global_panel.setLayout(gl_Global_panel);
		

		GroupLayout gl_Days30Panel = new GroupLayout(Days30Panel);
		gl_Days30Panel.setHorizontalGroup(
			gl_Days30Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Days30Panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_Days30Panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_Days30Panel.createSequentialGroup()
							.addGroup(gl_Days30Panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAvgDayProfit, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSoldLast, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
								.addComponent(AvgDaySales, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_Days30Panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_Days30Panel.createSequentialGroup()
									.addGroup(gl_Days30Panel.createParallelGroup(Alignment.LEADING)
										.addComponent(AvgDaySalesVal, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
										.addComponent(AvgDayProfit, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_Days30Panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(SoldLast30Days, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))))
						.addComponent(lblDays, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
					.addGap(12))
		);
		gl_Days30Panel.setVerticalGroup(
			gl_Days30Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Days30Panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDays)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_Days30Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSoldLast, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(SoldLast30Days))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_Days30Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAvgDayProfit, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(AvgDayProfit))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_Days30Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(AvgDaySales, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(AvgDaySalesVal))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		Days30Panel.setLayout(gl_Days30Panel);
		

		GroupLayout gl_Current_Month_Panel = new GroupLayout(Current_Month_Panel);
		gl_Current_Month_Panel.setHorizontalGroup(
			gl_Current_Month_Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Current_Month_Panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_Current_Month_Panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_Current_Month_Panel.createSequentialGroup()
							.addComponent(lblMonthlyOrders, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(MonthlyOrders, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_Current_Month_Panel.createSequentialGroup()
							.addComponent(lblAvgItemProfit, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(AvgItemProfit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_Current_Month_Panel.createSequentialGroup()
							.addComponent(Profit_defenition, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Monthly_profit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(lblCurrentMonth, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
					.addGap(89))
		);
		gl_Current_Month_Panel.setVerticalGroup(
			gl_Current_Month_Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Current_Month_Panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCurrentMonth)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_Current_Month_Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(Profit_defenition, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(Monthly_profit))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_Current_Month_Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAvgItemProfit, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(AvgItemProfit))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_Current_Month_Panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMonthlyOrders, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(MonthlyOrders))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		Current_Month_Panel.setLayout(gl_Current_Month_Panel);
		StoreStatusPanel.setLayout(gl_StoreStatusPanel);
		
		JLabel lblItemSearch = new JLabel("Item search");
		lblItemSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		final JComboBox comboBox = new JComboBox();

		comboBox.setModel(new DefaultComboBoxModel(new String[] {"All", "Buyer username", "Order status",  "Amazon order id", "eBay id", "Asin"}));
		
		JButton Search_Button = new JButton("Search");
		Search_Button.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Search_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(comboBox.getItemAt(comboBox.getSelectedIndex()).toString());
				System.out.println(textField.getText());
				DatabaseOp Db = null;
				try {
					Db = new DatabaseOp();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				Object[][] Values = null;
				try {
					if (comboBox.getSelectedIndex()==0)
					{
					ArrayList<Order> List = new ArrayList<Order>();
					DatabaseGui DatabaseGui = new DatabaseGui();
					DatabaseGui.GetOrders(List);
					ConvertForTable(List);
					table.setModel(new DefaultTableModel(ConvertForTable(List),new String[] {"eBay order id", "eBay username", "Order status", "Sale date", "Amazon price", "Tax", "Profit", "Ebay price", "Amazon order id", "Tracking number", "Carrier", "Feedback", "Checkout status", "Ebay item id", "Asin", "Edit", "Retry"}));
					}else	
					{
					Values = Db.GetTable(comboBox.getItemAt(comboBox.getSelectedIndex()).toString(),textField.getText());
					table.setModel(new DefaultTableModel(Values,new String[] {"eBay order id", "eBay username", "Order status","Sale date", "Amazon price", "Tax", "Profit", "Ebay price", "Amazon order id", "Tracking number", "Carrier", "Feedback", "Checkout status", "Ebay item id", "Asin", "Edit", "Retry"}));  
					}
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
			}
		});
		
		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout gl_Item_Search_Panel = new GroupLayout(Item_Search_Panel);
		gl_Item_Search_Panel.setHorizontalGroup(
			gl_Item_Search_Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Item_Search_Panel.createSequentialGroup()
					.addGroup(gl_Item_Search_Panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_Item_Search_Panel.createSequentialGroup()
							.addGap(34)
							.addGroup(gl_Item_Search_Panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textField)
								.addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblItemSearch, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
						.addGroup(gl_Item_Search_Panel.createSequentialGroup()
							.addGap(73)
							.addComponent(Search_Button, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(49, Short.MAX_VALUE))
		);
		gl_Item_Search_Panel.setVerticalGroup(
			gl_Item_Search_Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Item_Search_Panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblItemSearch, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(45)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(Search_Button, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(36, Short.MAX_VALUE))
		);
		Item_Search_Panel.setLayout(gl_Item_Search_Panel);
		Orders_Tab.setLayout(gl_Orders_Tab);
		GroupLayout groupLayout = new GroupLayout(frmAbeCommerce.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Log", null, panel_2, null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 1861, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(128, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 975, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(185, Short.MAX_VALUE))
		);
		
		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		panel_2.setLayout(gl_panel_2);
		System.setOut(new PrintStream(new TextAreaOutputStreamd(textArea)));
		
		JPanel AccountPanel = new JPanel();
		tabbedPane.addTab("Account", null, AccountPanel, null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
		GroupLayout gl_AccountPanel = new GroupLayout(AccountPanel);
		gl_AccountPanel.setHorizontalGroup(
			gl_AccountPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AccountPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 744, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(1245, Short.MAX_VALUE))
		);
		gl_AccountPanel.setVerticalGroup(
			gl_AccountPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AccountPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(745, Short.MAX_VALUE))
		);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		textField_1 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_1, -73, SpringLayout.EAST, panel_1);
		textField_1.setColumns(10);
		panel_1.add(textField_1);
		
		JLabel lblStoreName = new JLabel("Store name");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblStoreName, 79, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblStoreName, 29, SpringLayout.WEST, panel_1);
		lblStoreName.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_1, -3, SpringLayout.NORTH, lblStoreName);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_1, 28, SpringLayout.EAST, lblStoreName);
		panel_1.add(lblStoreName);
		
		textField_3 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_3, -73, SpringLayout.EAST, panel_1);
		textField_3.setColumns(10);
		panel_1.add(textField_3);
		
		JLabel lblEbayToken = new JLabel("eBay token");
		sl_panel_1.putConstraint(SpringLayout.WEST, lblEbayToken, 29, SpringLayout.WEST, panel_1);
		lblEbayToken.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_3, -3, SpringLayout.NORTH, lblEbayToken);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_3, 29, SpringLayout.EAST, lblEbayToken);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblEbayToken, 27, SpringLayout.SOUTH, lblStoreName);
		panel_1.add(lblEbayToken);
		
		JLabel lblApiServerUrl = new JLabel("Server url");
		sl_panel_1.putConstraint(SpringLayout.WEST, lblApiServerUrl, 29, SpringLayout.WEST, panel_1);
		lblApiServerUrl.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblApiServerUrl, 29, SpringLayout.SOUTH, lblEbayToken);
		panel_1.add(lblApiServerUrl);
		
		textField_4 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_4, -3, SpringLayout.NORTH, lblApiServerUrl);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_4, 0, SpringLayout.WEST, textField_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_4, -73, SpringLayout.EAST, panel_1);
		textField_4.setColumns(10);
		panel_1.add(textField_4);
		
		JLabel lblNewLabel_1 = new JLabel("Application id");
		sl_panel_1.putConstraint(SpringLayout.WEST, lblNewLabel_1, 29, SpringLayout.WEST, panel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 26, SpringLayout.SOUTH, lblApiServerUrl);
		panel_1.add(lblNewLabel_1);
		
		textField_5 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_5, 18, SpringLayout.SOUTH, textField_4);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_5, 16, SpringLayout.EAST, lblNewLabel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_5, -73, SpringLayout.EAST, panel_1);
		panel_1.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Site");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblNewLabel_3, 19, SpringLayout.SOUTH, lblNewLabel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblNewLabel_3, 29, SpringLayout.WEST, panel_1);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblNewLabel_3);
		
		textField_6 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_6, 16, SpringLayout.SOUTH, lblNewLabel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_6, 82, SpringLayout.EAST, lblNewLabel_3);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_6, -73, SpringLayout.EAST, panel_1);
		panel_1.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel lblConnection = new JLabel("Connection");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblConnection, 19, SpringLayout.SOUTH, lblNewLabel_3);
		lblConnection.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.EAST, lblConnection, 0, SpringLayout.EAST, lblStoreName);
		panel_1.add(lblConnection);
		
		textField_7 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_7, 16, SpringLayout.SOUTH, textField_6);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_7, 28, SpringLayout.EAST, lblConnection);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_7, -73, SpringLayout.EAST, panel_1);
		panel_1.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Schame");
		sl_panel_1.putConstraint(SpringLayout.WEST, lblNewLabel_4, 29, SpringLayout.WEST, panel_1);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblNewLabel_4, 22, SpringLayout.SOUTH, lblConnection);
		panel_1.add(lblNewLabel_4);
		
		textField_8 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_8, 17, SpringLayout.SOUTH, textField_7);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_8, 54, SpringLayout.EAST, lblNewLabel_4);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_8, -73, SpringLayout.EAST, panel_1);
		panel_1.add(textField_8);
		textField_8.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnSave, -52, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSave, -417, SpringLayout.EAST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnSave, -29, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnSave, -284, SpringLayout.EAST, panel_1);
		panel_1.add(btnSave);
		
		JLabel lblNewLabel_2 = new JLabel("Keys");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblNewLabel_2, 31, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, lblNewLabel_2, -344, SpringLayout.EAST, panel_1);
		panel_1.add(lblNewLabel_2);
		AccountPanel.setLayout(gl_AccountPanel);
		frmAbeCommerce.getContentPane().setLayout(groupLayout);
		frmAbeCommerce.setVisible(true);
	}
	
	public Object [][] ConvertForTable(ArrayList<Order> ListGuiData)
	{
		Object [][] TableInfo = new String [ListGuiData.size()][17];
		for (int i=0;i<ListGuiData.size();i++)
		{
			TableInfo[i][0] = (String)ListGuiData.get(i).StoreName;
			TableInfo[i][1] = (String)ListGuiData.get(i).BuyerUserId;
			TableInfo[i][2] = (String)ListGuiData.get(i).OrderStatus;
			TableInfo[i][3] =  ListGuiData.get(i).SaleDate;
			TableInfo[i][4] =  String.valueOf(ListGuiData.get(i).AmazonPriceBeforTax);
			TableInfo[i][5] =  String.valueOf(ListGuiData.get(i).Tax);
			TableInfo[i][6] =  String.valueOf(ListGuiData.get(i).Profit);
			TableInfo[i][7] =  String.valueOf(ListGuiData.get(i).eBaySalePrice);
			TableInfo[i][8] = (String)ListGuiData.get(i).AmazonOrderId;
			TableInfo[i][9] = (String)ListGuiData.get(i).Tracking;
			TableInfo[i][10] = (String)ListGuiData.get(i).Carrier;
			TableInfo[i][11] = String.valueOf(ListGuiData.get(i).FeedBack);
			TableInfo[i][12] = (String)ListGuiData.get(i).CheckOutStatus;
			TableInfo[i][13] = (String)ListGuiData.get(i).EbayId;
			TableInfo[i][14] = (String)ListGuiData.get(i).Asin;
			TableInfo[i][15] = "Edit";
			TableInfo[i][16] = "Retry";

		}
		return TableInfo;
	}
	
	public void CheckSold() {

	    new Thread() {

			public void run() {
				ItemsPosition ItemPosition = null;
				try {
					ItemPosition = new ItemsPosition();
				} catch (InterruptedException e1) {
					
					e1.printStackTrace();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				
				try {
					
					if (OrdersGui.StoreName.equals("All"))
					{
					for (Store ele:Main.ListOfStores)
					{
					ItemPosition.CheckSoldItems(ele);
					}
					}else 
					{
						ItemPosition.CheckSoldItems(Main.GetStoreByName(OrdersGui.StoreName));
					}
					
					
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
	    		}.start();
	}
	
	public void ItemPostionCheck() {

	    new Thread() {

			public void run() {
				ItemsPosition ItemPosition = null;
				try {
					ItemPosition = new ItemsPosition();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				try {
					
					if (OrdersGui.StoreName.equals("All"))
					{
					for (Store ele:Main.ListOfStores)
					{
					ItemPosition.CheckItemsPosition(ele);
					}
					}else
					{
						ItemPosition.CheckItemsPosition(Main.GetStoreByName(OrdersGui.StoreName));
					}
					
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					
					e.printStackTrace();
				} catch (SAXException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			}
	    		}.start();
	}

	public void AmazonPriceAndTaxCheck() {

	    new Thread() {

			public void run() {
				ItemsPosition ItemPosition = null;
				try {
					ItemPosition = new ItemsPosition();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				try {
					
					if (OrdersGui.StoreName.equals("All"))
					{
					for (Store ele:Main.ListOfStores)
					{
					ItemPosition.CheckAmazonPriceAndTax(ele);
					}
					}else
					{
						ItemPosition.CheckAmazonPriceAndTax(Main.GetStoreByName(OrdersGui.StoreName));
					}
					
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			}
	    		}.start();
	}

	public void PriceChanger() {

	    new Thread() {

			public void run() {
				ItemsPosition ItemPosition = null;
				try {
					ItemPosition = new ItemsPosition();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				
				try {
					
					if (OrdersGui.StoreName.equals("All"))
					{
					for (Store ele:Main.ListOfStores)
					{
						ItemPosition.PriceChange(ele);
					}
					}else
					{
						ItemPosition.PriceChange(Main.GetStoreByName(OrdersGui.StoreName));
					}
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			}
	    		}.start();
	}

	public void CalculateData() {

	    new Thread() {

			public void run() {
				ItemsPosition ItemPosition = null;
				try {
					ItemPosition = new ItemsPosition();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				if (OrdersGui.StoreName.equals("All"))
				{
				for (Store ele:Main.ListOfStores)
				{
				ItemPosition.CalculateData(ele);
				}
				}
				else 
				{
					ItemPosition.CalculateData(Main.GetStoreByName(OrdersGui.StoreName));
				}
			}
	    		}.start();
	}

	public void GetOrders() {

	    new Thread() {

			public void run() {
				
				if (OrdersGui.StoreName.equals("All"))
				{
				for(Store ele:Main.ListOfStores)
				{
				OrdersInfo OrdersInfo = new OrdersInfo(ele);
				try {
				OrdersInfo.Get_Orders_Info();
				} 
				catch (ApiException e) {e.printStackTrace();}
				catch (SdkException e) {e.printStackTrace();} 
				catch (Exception e) {e.printStackTrace();}
				
				OrdersInfo = null;
				System.gc();
				}
				}else
				{
					
					OrdersInfo OrdersInfo = new OrdersInfo(Main.GetStoreByName(OrdersGui.StoreName));
					try {
						OrdersInfo.Get_Orders_Info();
					} catch (ApiException e) {
						
						e.printStackTrace();
					} catch (SdkException e) {
						
						e.printStackTrace();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}

				
			}
	    		}.start();
	}
	
	public void DeleteFailedOrders() {

	    new Thread() {

			public void run() {
				try {
					DatabaseMain Db = new DatabaseMain();
					try 
					{
						System.out.println("Retrying failed");
						System.out.println("Deleting order number = "+Db.SelecteOrderingOrOrderErrorAndReturnLast());
						Db.DeleteOrderById(Db.SelecteOrderingOrOrderErrorAndReturnLast());
					} catch (SQLException e) {e.printStackTrace();}
					Db = null;
					System.gc();
				} catch (Exception e) {e.printStackTrace();}

				
				
			}
	    		}.start();
	}
	
	
	public void CalculateItemsDevition(Store Store) {

	    new Thread() {

			public void run() {

				
				DatabaseOp Db = null;
				try {
					Db = new DatabaseOp();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				DecimalFormat df = new DecimalFormat("#.#");
				
		
				PriceCount1.setText(String.valueOf(Db.OnlineItemsAmount0to30()));
				PriceCount2.setText(String.valueOf(Db.OnlineItemsAmount30to70()));
				PriceCount3.setText(String.valueOf(Db.OnlineItemsAmount70to300()));
				Percent1.setText(String.valueOf(df.format(((double)Db.OnlineItemsAmount0to30()/(double)Db.OnlineItemsAmount(StoreName))*100))+" %");
				Percent2.setText(String.valueOf(df.format(((double)Db.OnlineItemsAmount30to70()/(double)Db.OnlineItemsAmount(StoreName))*100))+" %");
				Percent3.setText(String.valueOf(df.format(((double)Db.OnlineItemsAmount70to300()/(double)Db.OnlineItemsAmount(StoreName))*100))+" %");
				Db = null;
				System.gc();
			
				
				
				
				
				
			}
	    		}.start();
	}
	
	
	
	
	
	
	public void UpdateStoreStatus(Store Store) {

	    new Thread() {

			public void run() {
				DatabaseOp Db = null;
				try {
					Db = new DatabaseOp();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				Store_name_Label.setText(StoreName);
				Monthly_profit.setText(String.valueOf(Db.Get_Monthly_profit(StoreName)).substring(0, String.valueOf(Db.Get_Monthly_profit(StoreName)).indexOf(".")+2)+" $");
				MonthlyOrders.setText(String.valueOf(Db.MonthlyOrders(StoreName)));
				AvgItemProfit.setText(String.valueOf(Db.Get_Monthly_profit(StoreName)/Db.MonthlyOrders(StoreName)).substring(0, String.valueOf(Db.Get_Monthly_profit(StoreName)).indexOf(".")+3)+" $");
				OnlineItemsAmount.setText(String.valueOf(Db.OnlineItemsAmount(StoreName)));
				AvgDayProfit.setText(String.valueOf(Db.AvgDayProfit()).substring(0, String.valueOf(Db.AvgDayProfit()).indexOf(".")+2)+" $");
				AvgDaySalesVal.setText(String.valueOf(Db.AvgDaySalesVal()).substring(0, String.valueOf(Db.AvgDaySalesVal()).indexOf(".")+2));
				SoldLast30Days.setText(String.valueOf(Db.SoldLast30Days()));
				SaleThrough.setText(String.valueOf(Db.SaleThrough(StoreName)).substring(0, String.valueOf(Db.SaleThrough(StoreName)).indexOf(".")+2)+" %");
				Db = null;
				System.gc();

				}
	    		}.start();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
}
