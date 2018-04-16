package Gui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserAbFinder {

	private JFrame frame;
	public String Path = null;
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileChooserAbFinder window = new FileChooserAbFinder();
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
	public FileChooserAbFinder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 621, 457);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 582, 399);
		frame.getContentPane().add(fileChooser);
		//frame.setVisible(true);
		//System.out.println(fileChooser.getSelectedFile().getName()); 
		
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			 // System.out.println("getCurrentDirectory(): " + fileChooser.getCurrentDirectory());
			  //System.out.println("getSelectedFile() : " + fileChooser.getSelectedFile());
			  Path = fileChooser.getSelectedFile().getPath();
			  System.out.println(Path);
		} else 
		{
			  System.out.println("No Selection ");
		}
	}

}
