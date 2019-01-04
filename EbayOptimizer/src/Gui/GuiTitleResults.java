package Gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import Database.GetSetDB;
import Database.Item;
import Ebay.EbayCalls;
import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GuiTitleResults {

	private JFrame frame;
	private JLabel lblTitle;
	private JTextField MyTitle;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_3;
	private JLabel label_4;
	private JTextField Title2;
	private JTextField Title3;
	private JLabel lblLength;
	private JButton btnUpdate;
	private JLabel MyItempicture6;
	private JLabel MyItempicture5;
	private JLabel MyItempicture2;
	private JLabel FirstCompetitorPicture1;
	private JLabel FirstCompetitorPicture2;
	private JLabel FirstCompetitorPicture3;
	private JLabel SecondCompetitorPicture1;
	private JLabel SecondCompetitorPicture2;
	private JLabel SecondCompetitorPicture3;
	private JLabel ThirdCompetitorPicture1;
	private JLabel ThirdCompetitorPicture2;
	private JLabel ThirdCompetitorPicture3;
	private JTextField Title1;
	private JLabel MyItempicture4;
	private JLabel MyItempicture1;
	private JLabel MyItempicture3;
	private JLabel FirstCompetitorPicture4;
	private JLabel FirstCompetitorPicture5;
	private JLabel FirstCompetitorPicture6;
	private JLabel SecondCompetitorPicture4;
	private JLabel SecondCompetitorPicture5;
	private JLabel SecondCompetitorPicture6;
	private JLabel ThirdCompetitorPicture4;
	private JLabel ThirdCompetitorPicture5;
	private JLabel ThirdCompetitorPicture6;
	private JButton CollageMaker;
	private JButton Copy1;
	private JButton Copy2;
	private JButton Copy3;
	private JLabel Sold1;
	private JLabel Competitor2;
	private JLabel Competitor3;
	private static String ImageDirectory = "C:\\Users\\Noname\\workspace\\EbayOptimizer\\Itemspictures";
	private JButton btnCancel;
	private JLabel MySold;
	private JButton EbayIdLink1;
	private JButton MyEbayIdLink2;
	private JButton MyEbayIdLink3;
	private JButton MyEbayIdLink;
	
	
	/**
	 * Launch the application.
	 */
	public  void mainGuiTitleResults(ArrayList<Item> results, Item MyItem,GuiData Update) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					GuiTitleResults window = new GuiTitleResults(results,MyItem,Update);
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public GuiTitleResults(ArrayList<Item> results, Item MyItem,GuiData Update) throws IOException {
		initialize(results,MyItem,Update);
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize(ArrayList<Item> results, Item MyItem ,GuiData GuiData) throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1123, 785);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		
		lblTitle = new JLabel("Title");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		MyTitle = new JTextField();
		MyTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				label.setText(String.valueOf(MyTitle.getText().length()));
			}
		});
		MyTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				label.setText(String.valueOf(MyTitle.getText().length()));
				
			}
		});

		
		MyTitle.setColumns(10);
		MyTitle.setText(MyItem.Title);
		
		label = new JLabel("0");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		label_1 = new JLabel("1)");
		
		label_3 = new JLabel("2)");
		
		label_4 = new JLabel("3)");
		
		Title2 = new JTextField();
		Title2.setColumns(10);
		Title2.setText(results.get(1).Title);
		
		Title3 = new JTextField();
		Title3.setColumns(10);
		Title3.setText(results.get(2).Title);
		
		lblLength = new JLabel("Length");
		lblLength.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnUpdate = new JButton("Update");

		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(MyTitle.getText());
				EbayCalls Call = new EbayCalls();
				try {
					String Results = null;
					Results = Call.ChangeTitle(MyItem.EbayId, MyTitle.getText());
					System.out.println("Ack = "+Results);
					GuiData.Update = Results;
					GetSetDB Db = new GetSetDB(); 
					Db.SetOptimizedItems(MyItem);
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SdkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		



		ThirdCompetitorPicture1 = new JLabel();
		ThirdCompetitorPicture1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+0).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		
		ThirdCompetitorPicture2 = new JLabel();
		ThirdCompetitorPicture2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+1).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));		
		
		ThirdCompetitorPicture3 = new JLabel();
		ThirdCompetitorPicture3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+2).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		ThirdCompetitorPicture4 = new JLabel();
		ThirdCompetitorPicture4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+3).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		
		ThirdCompetitorPicture5 = new JLabel();
		ThirdCompetitorPicture5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+4).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		ThirdCompetitorPicture6 = new JLabel();
		ThirdCompetitorPicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+5).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		Title1 = new JTextField();
		Title1.setColumns(10);
		Title1.setText(results.get(0).Title);
		
		
		MyItempicture1 = new JLabel();
		MyItempicture1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+0).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
	
		MyItempicture2 = new JLabel();
		MyItempicture2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+1).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		MyItempicture3 = new JLabel();
		MyItempicture3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+2).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		MyItempicture4 = new JLabel();
		MyItempicture4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+3).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		MyItempicture5 = new JLabel();
		MyItempicture5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+4).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		MyItempicture6 = new JLabel();
		MyItempicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+5).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		
		FirstCompetitorPicture1 = new JLabel();
		FirstCompetitorPicture1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+0).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		FirstCompetitorPicture2 = new JLabel();
		FirstCompetitorPicture2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+1).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		FirstCompetitorPicture3 = new JLabel();
		FirstCompetitorPicture3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+2).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		FirstCompetitorPicture4 = new JLabel();
		FirstCompetitorPicture4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+3).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		FirstCompetitorPicture5 = new JLabel();
		FirstCompetitorPicture5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+4).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		FirstCompetitorPicture6 = new JLabel();
		FirstCompetitorPicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+5).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		SecondCompetitorPicture1 = new JLabel();
		SecondCompetitorPicture1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+0).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		
		SecondCompetitorPicture2 = new JLabel();
		SecondCompetitorPicture2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+1).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		//SecondCompetitorPicture2.setIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+1));
		
		SecondCompetitorPicture3 = new JLabel();
		SecondCompetitorPicture3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+2).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));
		
		SecondCompetitorPicture4 = new JLabel();
		//SecondCompetitorPicture4.setIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+3));
		SecondCompetitorPicture4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+3).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		SecondCompetitorPicture5 = new JLabel();
		SecondCompetitorPicture5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+4).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		//SecondCompetitorPicture5.setIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+4));
		
		SecondCompetitorPicture6 = new JLabel();
		SecondCompetitorPicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+5).getImage().getScaledInstance(90,84, Image.SCALE_DEFAULT)));

		//SecondCompetitorPicture6.setIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+5));
		

		CollageMaker = new JButton("Collage Maker");
		
		Copy1 = new JButton("Copy");
		Copy1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				MyTitle.setText(Title1.getText());
				label.setText(String.valueOf(MyTitle.getText().length()));
			}
		});
		
		Copy2 = new JButton("Copy");
		Copy2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MyTitle.setText(Title2.getText());
				label.setText(String.valueOf(MyTitle.getText().length()));
			}
		});
		
		Copy3 = new JButton("Copy");
		Copy3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MyTitle.setText(Title3.getText());
				label.setText(String.valueOf(MyTitle.getText().length()));
			}
		});
		
		Sold1 = new JLabel("Sold = "+results.get(0).QuantitySold);
		
		Competitor2 = new JLabel("Sold = "+results.get(1).QuantitySold);
		
		Competitor3 = new JLabel("Sold = "+results.get(2).QuantitySold);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		
		MySold = new JLabel("Sold = "+MyItem.QuantitySold);
		
		EbayIdLink1 = new JButton(results.get(0).EbayId);
		EbayIdLink1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					open("http://www.ebay.com/itm/"+results.get(0).EbayId);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		MyEbayIdLink2 = new JButton(results.get(1).EbayId);
		MyEbayIdLink2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					open("http://www.ebay.com/itm/"+results.get(1).EbayId);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		MyEbayIdLink3 = new JButton(results.get(2).EbayId);
		MyEbayIdLink3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					open("http://www.ebay.com/itm/"+results.get(2).EbayId);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		
		MyEbayIdLink = new JButton("New button");
		MyEbayIdLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					open("http://www.ebay.com/itm/"+MyItem.EbayId);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		MyEbayIdLink.setText(MyItem.EbayId);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
									.addGap(178)
									.addComponent(lblLength, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
									.addGap(165))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(label_4)
											.addGap(18)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(ThirdCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(ThirdCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(ThirdCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(SecondCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(SecondCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(SecondCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(SecondCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(SecondCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
												.addComponent(Title3, GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(label_1)
											.addGap(18)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
												.addGroup(groupLayout.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(FirstCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(FirstCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(FirstCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(FirstCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(FirstCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(FirstCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(MyItempicture1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(18)
													.addComponent(MyItempicture2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(MyItempicture3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(MyItempicture4, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(MyItempicture5, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(MyItempicture6, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
												.addComponent(MyTitle, GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
												.addComponent(Title1)
												.addGroup(groupLayout.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(Title2)))))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(MySold, GroupLayout.DEFAULT_SIZE, 32530, Short.MAX_VALUE)
										.addComponent(MyEbayIdLink, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
										.addComponent(CollageMaker, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(EbayIdLink1)
											.addGap(32443))
										.addComponent(SecondCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
										.addComponent(Competitor2)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(MyEbayIdLink3)
												.addComponent(Competitor3))
											.addGap(4)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(ThirdCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED, 31987, Short.MAX_VALUE)
													.addComponent(ThirdCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(6)
													.addComponent(ThirdCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
													.addGap(176))
												.addGroup(groupLayout.createSequentialGroup()
													.addGap(52)
													.addComponent(btnCancel))))
										.addComponent(Copy3)
										.addComponent(MyEbayIdLink2)
										.addComponent(Copy2)
										.addComponent(Copy1)
										.addComponent(Sold1))))
							.addGap(76))
						.addComponent(label_3)))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(527)
					.addComponent(btnUpdate)
					.addContainerGap(32995, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLength)
						.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(MyTitle, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(CollageMaker, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(MyItempicture1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addComponent(MyItempicture2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addComponent(MyItempicture3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addComponent(MyItempicture4, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addComponent(MyItempicture5, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addComponent(MyItempicture6, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(MyEbayIdLink)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(MySold)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(Title1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1)
						.addComponent(Copy1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(EbayIdLink1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Sold1))
						.addComponent(FirstCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(FirstCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(FirstCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(FirstCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(FirstCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(FirstCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3)
						.addComponent(Title2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(Copy2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(MyEbayIdLink2)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(SecondCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(SecondCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(SecondCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(SecondCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(SecondCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(26)
									.addComponent(SecondCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(Competitor2)))
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(Title3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_4)
						.addComponent(Copy3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(ThirdCompetitorPicture1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(ThirdCompetitorPicture2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(ThirdCompetitorPicture3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(ThirdCompetitorPicture4, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(ThirdCompetitorPicture5, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(ThirdCompetitorPicture6, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(MyEbayIdLink3)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(Competitor3)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnUpdate)
					.addContainerGap(56, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
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
	
	  public static void open(String uri) throws URISyntaxException {
		
		    if (Desktop.isDesktopSupported()) {
		      try {
		        Desktop.getDesktop().browse(new URI(uri));
		      } catch (IOException e) { /* TODO: error handling */ }
		    } else { /* TODO: error handling */ }
		  }
}
