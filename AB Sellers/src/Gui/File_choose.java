package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;

public class File_choose {

	public JFrame frame1;

	/**
	 * Launch the application.
	 */
	public  void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File_choose window = new File_choose();
					window.frame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public File_choose() {
		//initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public  String initialize() {
		frame1 = new JFrame();
		frame1.setBounds(100, 100, 724, 488);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fileChooser = new JFileChooser();
		frame1.getContentPane().add(fileChooser, BorderLayout.CENTER);
		//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			  System.out.println("getCurrentDirectory(): " + fileChooser.getCurrentDirectory());
			  System.out.println("getSelectedFile() : " + fileChooser.getSelectedFile());
			  fileChooser.setVisible(false);
			  return fileChooser.getSelectedFile().getPath();
			} else {
			  System.out.println("No Selection ");
			}
		return null;
	}

}
