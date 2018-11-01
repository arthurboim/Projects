package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import MainPackage.*;
import Database.DataBaseOp;
import MainPackage.FileOp;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class AbFinderGui {

	private JFrame frame;
	 public JLabel lblNewLabel_1 = new JLabel("NULL");
	 public JLabel lblNewLabel = new JLabel("0");
	 public JLabel lblOfInf = new JLabel("0 of Inf");
	 public  JLabel label = new JLabel("0");
	 public String Path = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbFinderGui window = new AbFinderGui();
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
	public AbFinderGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 14));
		frame.setBounds(100, 100, 959, 571);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 0, 455, 164);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel EbayResults0 = new JLabel("Items eBay results =0:");
		EbayResults0.setFont(new Font("Tahoma", Font.PLAIN, 14));
		EbayResults0.setBounds(10, 32, 138, 14);
		panel.add(EbayResults0);
		
		JLabel lblItemsEbayResults = new JLabel("Items eBay results >0 and place in lowest price =1:");
		lblItemsEbayResults.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblItemsEbayResults.setBounds(10, 62, 311, 14);
		panel.add(lblItemsEbayResults);
		
		
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(332, 32, 132, 16);
		panel.add(lblNewLabel);
		
		
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(332, 62, 132, 16);
		panel.add(label);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				DataBaseOp Op = new DataBaseOp();
				FileOp Op2 = new FileOp();
				lblNewLabel.setText(""+Op.Get_eBay_Results_0());
				label.setText(""+Op.Get_eBay_Results_More_Than_0_Results_And_Place_In_lowest_Price_1());
				
				try {
					lblOfInf.setText(Op2.GetCurrentLineInFile(Path)+" of "+Op2.TotalAmountOfLinesInFile(Path));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Op = null;
				System.gc();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(168, 118, 119, 41);
		panel.add(btnNewButton);
		
		JLabel Links_checked = new JLabel("Links checked");
		Links_checked.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Links_checked.setBounds(10, 93, 247, 14);
		panel.add(Links_checked);
		

		lblOfInf.setHorizontalAlignment(SwingConstants.CENTER);
		lblOfInf.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOfInf.setBounds(332, 89, 132, 22);
		panel.add(lblOfInf);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(475, 11, 454, 99);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblLoadFile = new JLabel("File path:");
		lblLoadFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLoadFile.setBounds(10, 20, 55, 22);
		panel_1.add(lblLoadFile);
		

		lblNewLabel_1.setBounds(75, 26, 389, 14);
		panel_1.add(lblNewLabel_1);
		
		JButton btnNewButton_1 = new JButton("Load file");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				FileChooserAbFinder FileChooser = new FileChooserAbFinder();
				Path = FileChooser.Path.replace("\\", "\\\\");
				System.out.println(Path);
				lblNewLabel_1.setText(Path);
			}
			
		});
		
		
		
		
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.setBounds(164, 50, 116, 38);
		panel_1.add(btnNewButton_1);
		
		JButton Start = new JButton("Start");
		Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				execute1();
			}
		});
		Start.setFont(new Font("Tahoma", Font.BOLD, 14));
		Start.setBounds(416, 236, 116, 38);
		frame.getContentPane().add(Start);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Update database", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(11, 171, 454, 54);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnNewButton_2 = new JButton("Update");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				DataBaseOp Op = new DataBaseOp();
				Op.UpdateUploadedSet1();
				Op = null;
				System.gc();	
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.setBounds(310, 12, 117, 34);
		panel_2.add(btnNewButton_2);
		
		JLabel lblUpdateUploadedItems = new JLabel("Update uploaded items set to 1");
		lblUpdateUploadedItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUpdateUploadedItems.setHorizontalAlignment(SwingConstants.LEFT);
		lblUpdateUploadedItems.setBounds(10, 22, 454, 14);
		panel_2.add(lblUpdateUploadedItems);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 298, 919, 212);
		frame.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		scrollPane.setViewportView(textArea);
		//panel.setLayout(gl_panel);
		System.setOut(new PrintStream(new TextAreaOutputStreamd(textArea)));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Get items", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(475, 121, 455, 105);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblGetItemsResult = new JLabel("Get Items");
		lblGetItemsResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGetItemsResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGetItemsResult.setBounds(152, 11, 139, 22);
		panel_3.add(lblGetItemsResult);
		
		JButton btnGetItemsResult = new JButton("Get");
		btnGetItemsResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				execute2();
			}
		});
		btnGetItemsResult.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGetItemsResult.setBounds(139, 44, 172, 44);
		panel_3.add(btnGetItemsResult);
	}
	
	
	public void execute1() {

	    new Thread() {
	       
			public void run() {
	        	
				ScrapInfo info = null;
				try {
					info = new ScrapInfo();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		List<product> ItemsList = new ArrayList<product>();
	    		try {
					info.Items_info(ItemsList,Path);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    		}.start();
	}
	
	public void execute2() {

	    new Thread() {
	       
			public void run() {
				DataBaseOp Db = new DataBaseOp();
				try {
					while(true)
					{
						Db.GetItemsFromAbFinder();	
						Thread.sleep(1000*60*5);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Db = null;
				System.gc();
				}
	    		}.start();
	}
	
	

}
