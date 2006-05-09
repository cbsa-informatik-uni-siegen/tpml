package ui.newgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import ui.ThemeManager;

public class MainWindow extends JFrame{
	private static String defaultExpression = "let rec f = lambda x. if x = 0 then 1 else x * f (x - 1) in f 3";
	
	public MainWindow() {
		    super();
		    setTitle("Projektgruppe v.01");
		    initComponents();
		    pack();
		    setSize(640, 480);
		  }
	
	  private void initComponents() {
		  initMainMenu();
		  

		  
	  }
	  
	  public void initMainMenu(){
		  MenuListener menulistener = new MenuListener (this);
		    JMenuBar mainMenu = new JMenuBar();
		    JMenu fileMenu = new JMenu("File");
		    fileMenu.setMnemonic(KeyEvent.VK_F);
		    mainMenu.add(fileMenu);

		    JMenuItem newItem = fileMenu.add("New");
		    //newItem.addActionListener(fileMenuListener);
		    newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
		        KeyEvent.CTRL_MASK));
		    newItem.setMnemonic(KeyEvent.VK_N);

		    fileMenu.addSeparator();

		    JMenuItem saveItem = fileMenu.add("Save");
		    //saveItem.addActionListener(fileMenuListener);
		    saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		        KeyEvent.CTRL_MASK));
		    saveItem.setMnemonic(KeyEvent.VK_S);

		    JMenuItem saveAsItem = fileMenu.add("Save as");
		    //saveAsItem.addActionListener(fileMenuListener);
		    saveAsItem.setMnemonic(KeyEvent.VK_A);

		    fileMenu.addSeparator();

		    JMenuItem closeItem = fileMenu.add("Close");
		    //closeItem.addActionListener(fileMenuListener);
		    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
		        KeyEvent.CTRL_MASK));
		    closeItem.setMnemonic(KeyEvent.VK_C);

		    fileMenu.addSeparator();

		    JMenuItem exitItem = fileMenu.add("Exit");
		    exitItem.addActionListener(menulistener);
		    exitItem.setMnemonic(KeyEvent.VK_X);
		    
		    JMenu editMenu = new JMenu("Edit");
		    editMenu.setMnemonic(KeyEvent.VK_E);
		    mainMenu.add(editMenu);
		    
		    JMenuItem preferences = editMenu.add("Preferences");
		    preferences.setMnemonic(KeyEvent.VK_P);
		    
		    JMenu actionsMenu = new JMenu("Actions");
		    actionsMenu.setMnemonic(KeyEvent.VK_A);
		    mainMenu.add(actionsMenu);

		    JMenuItem smallStepItem = actionsMenu.add("SmallStep");
		    smallStepItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
		    smallStepItem.setMnemonic(KeyEvent.VK_S);
		    smallStepItem.addActionListener(menulistener);
		    
		    setJMenuBar(mainMenu);
		 
	  }
	  public void handleNew(){}
      public void handleSave(){}
      public void handleSaveAs(){}
      public void handleClose(){}
      public void handleExit(){}
      public void handleSmallStep(){
    	 System.out.println("SmallStep");
      }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ThemeManager.get();
		MainWindow mw = new MainWindow();
		mw.addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) { System.exit(0); };
		});
		mw.setVisible(true);

	}

}
