package Gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Control.Control;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ABEMonitorGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ABEMonitorGUI window = new ABEMonitorGUI();
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
	public ABEMonitorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 596, 390);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ABEStart();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 560, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(205)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE))
		);
		
		frame.getContentPane().setLayout(groupLayout);
		JTextArea textArea = new JTextArea();
		
		scrollPane.setViewportView(textArea);
		System.setOut(new PrintStream(new TextAreaOutputStreamd(textArea)));
	
		
		
		
	}
	
	
	public void ABEStart() {

	    new Thread() {
	       
			public void run() {

				Control Control = new Control();
				while(true)
				{
					try {
						Control.WebDriverCheckItemBeforeUpload();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000*60*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			
				
				
				
				
			}
	    		}.start();
	}
	
	
	
	
}
