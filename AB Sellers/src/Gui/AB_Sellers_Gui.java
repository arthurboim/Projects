package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import Amazon.AmazonCalls;
import Ebay.eBayCalls;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;


public class AB_Sellers_Gui {

	
	//public static String FILENAMEIPS = "C:\\Users\\Noname\\Desktop\\ips.txt";
	public static String FILENAMEIPS = null;

	private JFrame frmProductFromSellers;
	private JTextField Ip;
	private JTextField user;
	private JTextField password;
	private JTextField port;
	private JTextField scham;
	private JTextField Pathfiled;
	public static String Path = null;
	private static int ebayThreadNumber = 0;
	private int AmazonThreadNumber = 0;
	private JTextField Ip_input;
	private JTextField Port_input;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AB_Sellers_Gui window = new AB_Sellers_Gui();
					window.frmProductFromSellers.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AB_Sellers_Gui() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProductFromSellers = new JFrame();
		frmProductFromSellers.setTitle("AB sellers");
		frmProductFromSellers.setBounds(500, 500, 450, 300);
		frmProductFromSellers.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProductFromSellers.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(frmProductFromSellers.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1888, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
					.addContainerGap())
		);

		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Main", null, panel, null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Test connection", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panel_4.setForeground(Color.BLUE);
		
		final JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Items", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panel_5.setForeground(Color.BLUE);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panel_6.setForeground(Color.BLUE);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sellers", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		

		

		
	
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1863, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))
								.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_7.setLayout(null);
		
		JButton btnNewButton_6 = new JButton("Get sellers");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_6.setBounds(10, 50, 120, 33);
		panel_7.add(btnNewButton_6);
		panel_6.setLayout(null);
		JButton btnNewButton = new JButton("Ebay thread");
		btnNewButton.setBounds(10, 23, 132, 35);
		panel_6.add(btnNewButton);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		final JLabel lblThreadNumber = new JLabel("Thread count = ");
		lblThreadNumber.setBounds(152, 35, 103, 14);
		panel_6.add(lblThreadNumber);
		
		JButton btnNewButton_1 = new JButton("Amazon thread");
		btnNewButton_1.setBounds(10, 59, 132, 35);
		panel_6.add(btnNewButton_1);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		final JLabel lblAmazonThreadCount = new JLabel("Thread count = ");
		lblAmazonThreadCount.setBounds(152, 71, 117, 14);
		panel_6.add(lblAmazonThreadCount);
		JButton btnNewButton_2 = new JButton("Tax Check"); 
		btnNewButton_2.setBounds(265, 23, 117, 35);
		panel_6.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JButton btnNewButton_3 = new JButton("Get images");
		btnNewButton_3.setBounds(265, 59, 117, 35);
		panel_6.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				AmazonThreadNumber++;
				lblAmazonThreadCount.setText(lblAmazonThreadCount.getText().substring(0, lblAmazonThreadCount.getText().indexOf("=")+1)+" "+AmazonThreadNumber);
				Thread Tempthread2 = new Thread(new AmazonCalls());
				Tempthread2.start();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ebayThreadNumber++;
				lblThreadNumber.setText(lblThreadNumber.getText().substring(0, lblThreadNumber.getText().indexOf("=")+1)+" "+ebayThreadNumber);
				eBayThread();

			}
		});
		JButton btnGetItemsFrom = new JButton("AmazonAvailable = 0");
		btnGetItemsFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
		  
			}
		});
		btnGetItemsFrom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_5 = new JButton("AmazonAvailable = 1");
		btnNewButton_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_5, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnGetItemsFrom, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnGetItemsFrom, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(btnNewButton_5, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(39, Short.MAX_VALUE))
		);
		panel_5.setLayout(gl_panel_5);
		
		Ip_input = new JTextField();
		Ip_input.setColumns(10);
		
		Port_input = new JTextField();
		Port_input.setColumns(10);
		
		JLabel lblIp = new JLabel("IP");
		
		JLabel lblPort_1 = new JLabel("Port");
		
		JButton btnNewButton_4 = new JButton("Get items from cloud");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		

		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPort_1)
						.addComponent(lblIp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false)
						.addComponent(Port_input)
						.addComponent(Ip_input, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton_4, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(13)
					.addComponent(btnNewButton_4, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(Ip_input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblIp))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(Port_input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort_1))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		panel.setLayout(gl_panel);
		System.setOut(new PrintStream(new TextAreaOutputStreamd(textArea)));
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Connection", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Keys", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 625, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(955, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 373, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(583, Short.MAX_VALUE))
		);
		panel_8.setLayout(null);
		
		Pathfiled = new JTextField();
		Pathfiled.setBounds(55, 45, 222, 20);
		panel_8.add(Pathfiled);
		Pathfiled.setColumns(10);
		
		JLabel lblPath = new JLabel("Path:");
		lblPath.setBounds(10, 45, 33, 17);
		panel_8.add(lblPath);
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JButton btnChooseFile = new JButton("Choose file");
		btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnChooseFile.setBounds(98, 11, 106, 25);
		panel_8.add(btnChooseFile);
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				File_choose File_choose_window = new File_choose();
				Path = File_choose_window.initialize();
				Path = Path.replace("\\","\\\\");
				System.out.println(Path);


				BufferedReader br = null;
				FileReader fr = null;
				try {
					fr = new FileReader(Path);
					br = new BufferedReader(fr);
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) 
					{
						if (sCurrentLine.contains("FILENAMEIPS:"))
						{
							FILENAMEIPS = sCurrentLine.substring(sCurrentLine.indexOf("FILENAMEIPS:")+13);
						
						}

					}
				} catch (IOException e) {} 
				finally {

					try {

						if (br != null)
							br.close();

						if (fr != null)
							fr.close();

					} catch (IOException ex) {}
					}
			
				System.out.println(FILENAMEIPS);
				Pathfiled.setText(Path);
				frmProductFromSellers.repaint();
			}
		});
		
		JLabel lblManualConnection = new JLabel("Manual connection check");
		lblManualConnection.setFont(new Font("Tahoma", Font.BOLD, 16));
		

		
		JLabel lblConnectionIp = new JLabel("Connection ip:");
		lblConnectionIp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		Ip = new JTextField();
		Ip.setColumns(10);
		Ip.setText("localhost");
		
		user = new JTextField();
		user.setColumns(10);
		user.setText("root");
		
		password = new JTextField();
		password.setColumns(10);
		password.setText("root");
		
		port = new JTextField();
		port.setColumns(10);
		port.setText("4444");
		scham = new JTextField();
		scham.setColumns(10);
		scham.setText("amazon");
		final JLabel status = new JLabel("Not connected");
		status.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{}
		});
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblScham = new JLabel("Scham:");
		lblScham.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));

		


		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(39)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(lblConnectionIp)
						.addComponent(lblUser)
						.addComponent(lblPassword)
						.addComponent(lblPort)
						.addComponent(lblScham))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
						.addComponent(scham)
						.addComponent(port)
						.addComponent(password)
						.addComponent(user)
						.addComponent(Ip, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
					.addComponent(status)
					.addGap(26))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(221)
					.addComponent(btnConnect)
					.addContainerGap(313, Short.MAX_VALUE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(161)
					.addComponent(lblManualConnection)
					.addContainerGap(261, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(39)
					.addComponent(lblManualConnection)
					.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblConnectionIp)
						.addComponent(Ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(status)
						.addComponent(lblStatus))
					.addGap(18)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUser)
						.addComponent(user, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(scham, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblScham))
					.addGap(38)
					.addComponent(btnConnect)
					.addGap(33))
		);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		frmProductFromSellers.getContentPane().setLayout(groupLayout);

		frmProductFromSellers.setVisible(true);
	}
	public void eBayThread() {

	    new Thread() {

			public void run() {
				eBayCalls eBay = new eBayCalls();
				eBay.run();
	        }
	    	}.start();
	}


}
