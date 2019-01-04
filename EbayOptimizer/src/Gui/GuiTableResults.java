package Gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuiTableResults {

	private JFrame frame;
	public JTable table;
	private JScrollPane scrollPane;
	private JButton btnRefresh;
	public static String ImageDirectory = "C:\\Users\\Noname\\workspace\\EbayOptimizer\\Itemspictures";
	/**
	 * Launch the application.
	 */
	public void TableMain(ArrayList<GuiData> ListGuiData) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiTableResults window = new GuiTableResults(ListGuiData);
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
	public GuiTableResults(ArrayList<GuiData> ListGuiData) {
		initialize(ListGuiData);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ArrayList<GuiData> ListGuiData) {
		frame = new JFrame();
		frame.setBounds(100, 100, 761, 713);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();

		scrollPane.setBounds(10, 11, 725, 552);
		frame.getContentPane().add(scrollPane);
		
		 table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(ConvertForTable(ListGuiData),new String[] {"Id", "EBayId", "Fetching", "Update", "Edit"}) 
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, JButton.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table.setModel(new DefaultTableModel(ConvertForTable(ListGuiData),new String[] {"Id", "EBayId", "Fetching", "Update", "Edit"}) 
				{
					Class[] columnTypes = new Class[] {
						String.class, String.class, String.class, String.class, JButton.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				table.getColumnModel().getColumn(0).setPreferredWidth(15);
				table.getColumnModel().getColumn(1).setMinWidth(5);
			    table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
			    table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(),ListGuiData));
				frame.repaint();
			}
		});
		btnRefresh.setBounds(335, 603, 89, 23);
		frame.getContentPane().add(btnRefresh);
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(1).setMinWidth(5);
	    table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
	    table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(),ListGuiData));

	}
	
	public Object [][] ConvertForTable(ArrayList<GuiData> ListGuiData)
	{
		Object [][] TableInfo = new String [ListGuiData.size()][5];
		for (int i=0;i<ListGuiData.size();i++)
		{
			TableInfo[i][0] = (String)String.valueOf(i+1);
			TableInfo[i][1] = (String)ListGuiData.get(i).Item.EbayId;
			TableInfo[i][2] = (String)ListGuiData.get(i).Fetching;
			TableInfo[i][3] = (String)ListGuiData.get(i).Update;
			TableInfo[i][4] = "Edit";
		}
		return TableInfo;
	}
	
	
	public class ButtonRenderer extends JButton implements TableCellRenderer {
		  
		  public ButtonRenderer() {
		    setOpaque(true);
		  }
		   
		  public Component getTableCellRendererComponent(JTable table, Object value,
		                   boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      setForeground(table.getSelectionForeground());
		      setBackground(table.getSelectionBackground());
		    } else{
		      setForeground(table.getForeground());
		      setBackground(UIManager.getColor("Button.background"));
		    }
		    setText( (value ==null) ? "" : value.toString() );
		    return this;
		  }
		}
	
  public class ButtonEditor extends DefaultCellEditor {
  protected JButton button;
  private String    label;
  private boolean   isPushed;
  public int TableRow = 0;
  public ButtonEditor(JCheckBox checkBox, ArrayList<GuiData> listGuiData) {
    super(checkBox);
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  System.out.println(listGuiData.get(TableRow).Item.EbayId);
			GuiTitleResult GuiResults = null;
			GuiResults = new GuiTitleResult(listGuiData.get(TableRow).Competitors , listGuiData.get(TableRow).Item,listGuiData.get(TableRow));
			GuiResults.main(listGuiData.get(TableRow).Competitors , listGuiData.get(TableRow).Item,listGuiData.get(TableRow));
      }
    });
  }
  
  public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) 
  {
	  if (isSelected) {
      button.setForeground(table.getSelectionForeground());
      button.setBackground(table.getSelectionBackground());
    } else{
      button.setForeground(table.getForeground());
      button.setBackground(table.getBackground());
    }
    label = (value ==null) ? "" : value.toString();
    button.setText( label );
    TableRow = row;
    isPushed = true;
    return button;
  }
  
  public Object getCellEditorValue() {
    if (isPushed)  {
      //
      //
      //JOptionPane.showMessageDialog(button ,label + ": Ouch!");
      // System.out.println(label + ": Ouch!");
    }
    isPushed = false;
    return new String( label ) ;
  }
    
  public boolean stopCellEditing() {
    isPushed = false;
    return super.stopCellEditing();
  }
  
  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }
}
		  
}