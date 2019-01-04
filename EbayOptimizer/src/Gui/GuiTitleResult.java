package Gui;

import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import Database.GetSetDB;
import Database.Item;
import Ebay.EbayCalls;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuiTitleResult {

	private JFrame frame;
	private JTextField MyTitle;
	private JTextField Competitor1Title;
	private JTextField Competitor2Title;
	private JTextField Competitor3Title;
	private JLabel Competitor3Pictures1;
	private JLabel Competitor3Pictures2;
	private JLabel Competitor3Pictures4;
	private JLabel Competitor3Pictures3;
	private JLabel Competitor3Pictures7;
	private JLabel Competitor3Pictures6;
	private JLabel Competitor3Pictures5;
	private JLabel Competitor2Pictures7;
	private JLabel Competitor2Pictures6;
	private JLabel Competitor2Pictures5;
	private JLabel Competitor2Pictures4;
	private JLabel Competitor2Pictures3;
	private JLabel Competitor2Pictures2;
	private JLabel Competitor2Pictures1;
	private JLabel CompetitorPictures1;
	private JLabel CompetitorPictures2;
	private JLabel CompetitorPictures3;
	private JLabel CompetitorPictures4;
	private JLabel CompetitorPictures5;
	private JLabel CompetitorPictures6;
	private JLabel CompetitorPictures7;
	private JLabel MyPicture1;
	private JLabel MyPicture2;
	private JLabel MyPicture3;
	private JLabel MyPicture4;
	private JLabel MyPicture5;
	private JLabel MyPicture6;
	private JLabel MyPicture7;
	private JLabel label_28;
	private JLabel label_29;
	private JLabel label_30;
	public static String ImageDirectory = "C:\\Users\\Noname\\workspace\\EbayOptimizer\\Itemspictures";
	/**
	 * Launch the application.
	 */
	public  void main(ArrayList<Item> results, Item MyItem ,GuiData GuiData) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiTitleResult window = new GuiTitleResult(results,  MyItem , GuiData);
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
	public GuiTitleResult(ArrayList<Item> results, Item MyItem ,GuiData GuiData) {
		initialize(results,  MyItem , GuiData);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ArrayList<Item> results, Item MyItem ,GuiData GuiData) {
		frame = new JFrame();
		frame.setBounds(100, 100, 1477, 1041);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		MyTitle = new JTextField();

		
		MyTitle.setBounds(34, 47, 1028, 20);
		MyTitle.setText(MyItem.Title);
		frame.getContentPane().add(MyTitle);
		MyTitle.setColumns(10);
		
		Competitor1Title = new JTextField();
		Competitor1Title.setBounds(34, 255, 1028, 20);
		if (results.get(0).Title!=null)
		Competitor1Title.setText(results.get(0).Title);
		frame.getContentPane().add(Competitor1Title);
		Competitor1Title.setColumns(10);
		
		Competitor2Title = new JTextField();
		Competitor2Title.setColumns(10);
		Competitor2Title.setBounds(34, 473, 1028, 20);
		if (results.get(1).Title!=null)
		Competitor2Title.setText(results.get(1).Title);
		frame.getContentPane().add(Competitor2Title);
		
		Competitor3Title = new JTextField();
		Competitor3Title.setColumns(10);
		Competitor3Title.setBounds(34, 687, 1028, 20);
		if (results.get(2).Title!=null)
		Competitor3Title.setText(results.get(2).Title);
		frame.getContentPane().add(Competitor3Title);
		
		Competitor3Pictures1 = new JLabel("");
		Competitor3Pictures1.setBounds(34, 715, 191, 176);
		Competitor3Pictures1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+0).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures1);
		
		JLabel TitleLangth = new JLabel("");
		TitleLangth.setBounds(1072, 50, 46, 14);
		MyTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});
		MyTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});

		frame.getContentPane().add(TitleLangth);

		
		JLabel SoldCompetitor1 = new JLabel(""+results.get(0).QuantitySold);
		SoldCompetitor1.setBounds(1072, 258, 46, 14);
		frame.getContentPane().add(SoldCompetitor1);
		
		JLabel SoldCompetitor2 = new JLabel(""+results.get(1).QuantitySold);
		SoldCompetitor2.setBounds(1072, 476, 46, 14);
		frame.getContentPane().add(SoldCompetitor2);
		
		JLabel SoldCompetitor3 = new JLabel(""+results.get(2).QuantitySold);
		SoldCompetitor3.setBounds(1072, 690, 46, 14);
		frame.getContentPane().add(SoldCompetitor3);
		
		JButton MyLink = new JButton("");
		MyLink.setBounds(1189, 44, 89, 23);
		frame.getContentPane().add(MyLink);
		
		JButton Copy_button_1 = new JButton("Copy");
		Copy_button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyTitle.setText(Competitor1Title.getText());
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});
		Copy_button_1.setBounds(1189, 250, 89, 23);
		frame.getContentPane().add(Copy_button_1);
		
		JButton Copy_button_2 = new JButton("Copy");
		Copy_button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyTitle.setText(Competitor2Title.getText());
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});
		Copy_button_2.setBounds(1189, 472, 89, 23);
		frame.getContentPane().add(Copy_button_2);
		
		JButton Copy_button_3 = new JButton("Copy");
		Copy_button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyTitle.setText(Competitor3Title.getText());
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});
		Copy_button_3.setBounds(1189, 684, 89, 23);
		frame.getContentPane().add(Copy_button_3);
		
		JButton Link1 = new JButton(results.get(0).EbayId);
		Link1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					open("http://www.ebay.com/itm/"+results.get(0).EbayId);
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
			
				
			}
		});
		Link1.setBounds(1288, 250, 89, 23);
		frame.getContentPane().add(Link1);
		
		JButton Link2 = new JButton(results.get(1).EbayId);
		Link2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					open("http://www.ebay.com/itm/"+results.get(1).EbayId);
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
			
			}
		});
		Link2.setBounds(1288, 472, 89, 23);
		frame.getContentPane().add(Link2);
		
		JButton Link3 = new JButton(results.get(2).EbayId);
		Link3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					open("http://www.ebay.com/itm/"+results.get(2).EbayId);
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
			
			}
		});
		Link3.setBounds(1288, 684, 89, 23);
		frame.getContentPane().add(Link3);
		
		JButton Update = new JButton("Update");
		Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println(MyTitle.getText());
				//UpdateAction t = new UpdateAction(MyItem, GuiData);
				Thread thread = new Thread(new UpdateAction(MyItem, GuiData));
				//t.UpdateAction(MyItem, GuiData);	
				thread.start();
			}
		});
		Update.setBounds(466, 902, 166, 66);
		frame.getContentPane().add(Update);
		
		JButton Cancel = new JButton("Cancel");
		Cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
			}
		});
		Cancel.setBounds(744, 902, 166, 66);
		frame.getContentPane().add(Cancel);
		
		JButton CollageMaker = new JButton(MyItem.EbayId);
		CollageMaker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					open("http://www.ebay.com/itm/"+MyItem.EbayId);
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		CollageMaker.setBounds(1288, 44, 89, 23);
		frame.getContentPane().add(CollageMaker);
		
		Competitor3Pictures2 = new JLabel("");
		Competitor3Pictures2.setBounds(235, 715, 191, 176);
		Competitor3Pictures2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+1).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures2);
		
		Competitor3Pictures4 = new JLabel("");
		Competitor3Pictures4.setBounds(637, 715, 191, 176);
		Competitor3Pictures4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+3).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures4);
		
		Competitor3Pictures3 = new JLabel("");
		Competitor3Pictures3.setBounds(436, 715, 191, 176);
		Competitor3Pictures3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+2).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures3);
		
		Competitor3Pictures7 = new JLabel("");
		Competitor3Pictures7.setBounds(1227, 715, 191, 176);
		Competitor3Pictures7.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+6).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures7);
		
		Competitor3Pictures6 = new JLabel("");
		Competitor3Pictures6.setBounds(1026, 715, 191, 176);
		Competitor3Pictures6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+5).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures6);
		
		Competitor3Pictures5 = new JLabel("");
		Competitor3Pictures5.setBounds(825, 715, 191, 176);
		Competitor3Pictures5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(2).EbayId+"-"+4).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor3Pictures5);
		
		Competitor2Pictures7 = new JLabel("");
		Competitor2Pictures7.setBounds(1227, 501, 191, 176);
		Competitor2Pictures7.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+6).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures7);
		
		Competitor2Pictures6 = new JLabel("");
		Competitor2Pictures6.setBounds(1026, 501, 191, 176);
		Competitor2Pictures6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+5).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures6);
		
		Competitor2Pictures5 = new JLabel("");
		Competitor2Pictures5.setBounds(825, 501, 191, 176);
		Competitor2Pictures5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+4).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures5);
		
		Competitor2Pictures4 = new JLabel("");
		Competitor2Pictures4.setBounds(637, 501, 191, 176);
		Competitor2Pictures4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+3).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures4);
		
		Competitor2Pictures3 = new JLabel("");
		Competitor2Pictures3.setBounds(436, 501, 191, 176);
		Competitor2Pictures3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+2).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(Competitor2Pictures3);
		
		Competitor2Pictures2 = new JLabel("");
		Competitor2Pictures2.setBounds(235, 501, 191, 176);
		Competitor2Pictures2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+1).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures2);
		
		Competitor2Pictures1 = new JLabel("");
		Competitor2Pictures1.setBounds(34, 501, 191, 176);
		Competitor2Pictures1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(1).EbayId+"-"+0).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(Competitor2Pictures1);
		
		CompetitorPictures1 = new JLabel("");
		CompetitorPictures1.setBounds(34, 286, 191, 176);
		CompetitorPictures1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+0).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(CompetitorPictures1);
		
		CompetitorPictures2 = new JLabel("");
		CompetitorPictures2.setBounds(235, 283, 191, 176);
		CompetitorPictures2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+1).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures2);
		
		CompetitorPictures3 = new JLabel("");
		CompetitorPictures3.setBounds(436, 283, 191, 176);
		CompetitorPictures3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+2).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures3);
		
		CompetitorPictures4 = new JLabel("");
		CompetitorPictures4.setBounds(637, 283, 191, 176);
		CompetitorPictures4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+3).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures4);
		
		CompetitorPictures5 = new JLabel("");
		CompetitorPictures5.setBounds(825, 283, 191, 176);
		CompetitorPictures5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+4).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures5);
		
		CompetitorPictures6 = new JLabel("");
		CompetitorPictures6.setBounds(1026, 283, 191, 176);
		CompetitorPictures6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+5).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures6);
		
		CompetitorPictures7 = new JLabel("");
		CompetitorPictures7.setBounds(1227, 283, 191, 176);
		CompetitorPictures7.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+results.get(0).EbayId+"-"+6).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(CompetitorPictures7);
		
		MyPicture1 = new JLabel("");
		MyPicture1.setBounds(34, 68, 191, 176);
		MyPicture1.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+0).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(MyPicture1);
		
		MyPicture2 = new JLabel("");
		MyPicture2.setBounds(235, 68, 191, 176);
		MyPicture2.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+1).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(MyPicture2);
		
		MyPicture3 = new JLabel("");
		MyPicture3.setBounds(436, 68, 191, 176);
		MyPicture3.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+2).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(MyPicture3);
		
		MyPicture4 = new JLabel("");
		MyPicture4.setBounds(637, 68, 191, 176);
		MyPicture4.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+3).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(MyPicture4);
		
		MyPicture5 = new JLabel("");
		MyPicture5.setBounds(825, 68, 191, 176);
		MyPicture5.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+4).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(MyPicture5);
		
		MyPicture6 = new JLabel("");
		MyPicture6.setBounds(1026, 68, 191, 176);
		MyPicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+5).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));

		frame.getContentPane().add(MyPicture6);
		
		MyPicture7 = new JLabel("");
		MyPicture7.setBounds(1227, 68, 191, 176);
		MyPicture7.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+6).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(MyPicture7);
		
		label_28 = new JLabel("1)");
		label_28.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_28.setBounds(10, 255, 19, 21);
		frame.getContentPane().add(label_28);
		
		label_29 = new JLabel("2)");
		label_29.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_29.setBounds(10, 472, 19, 21);
		frame.getContentPane().add(label_29);
		
		label_30 = new JLabel("3)");
		label_30.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_30.setBounds(10, 686, 19, 21);
		frame.getContentPane().add(label_30);
		
		JButton btnNewadddddd = new JButton("New");
		btnNewadddddd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Temp = MyTitle.getText();
				Temp = Temp+" New";
				MyTitle.setText(Temp);
				TitleLangth.setText(String.valueOf(MyTitle.getText().length()));
				frame.repaint();
			}
		});
		btnNewadddddd.setBounds(34, 13, 89, 23);
		frame.getContentPane().add(btnNewadddddd);

		
		frame.getContentPane().add(MyPicture5);
		
		MyPicture6 = new JLabel("");
		MyPicture6.setBounds(1026, 68, 191, 176);
		try{
		MyPicture6.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+5).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		}catch(Exception e)
		{
			
		}
		frame.getContentPane().add(MyPicture6);
		
		MyPicture7 = new JLabel("");
		MyPicture7.setBounds(1227, 68, 191, 176);
		try{
		MyPicture7.setIcon(new ImageIcon(new ImageIcon(ImageDirectory+"\\"+MyItem.EbayId+"-"+6).getImage().getScaledInstance(191,176, Image.SCALE_DEFAULT)));
		}catch(Exception e)
		{
			
		}
		frame.getContentPane().add(MyPicture7);
		
		label_28 = new JLabel("1)");
		label_28.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_28.setBounds(10, 255, 19, 21);
		frame.getContentPane().add(label_28);
		
		label_29 = new JLabel("2)");
		label_29.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_29.setBounds(10, 472, 19, 21);
		frame.getContentPane().add(label_29);
		
		label_30 = new JLabel("3)");
		label_30.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_30.setBounds(10, 686, 19, 21);
		frame.getContentPane().add(label_30);
	}
	  public static void open(String uri) throws URISyntaxException {
			
		    if (Desktop.isDesktopSupported()) {
		      try {
		        Desktop.getDesktop().browse(new URI(uri));
		      } catch (IOException e) { }
		    } else {  }
		  }
	  

	  
	  public class UpdateAction implements Runnable {

		 public  Item MyItem = new Item();
		 public GuiData GuiData = new GuiData();
		  public UpdateAction(Item myItem2, Gui.GuiData guiData2) {
			  
			  this.MyItem = myItem2;
			  this.GuiData = guiData2;
			 // run();
		}

		    public void run() {


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
					
					e.printStackTrace();
				} catch (SdkException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			
			
		  }
	  }
	  
}
