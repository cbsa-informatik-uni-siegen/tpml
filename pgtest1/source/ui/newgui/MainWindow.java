package ui.newgui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.EditorWindow;
import ui.Mainwindow;
import ui.SettingsGUI;
import ui.SourceFile;
import ui.ThemeManager;

public class MainWindow extends JFrame{
//	private static String defaultExpression = "let rec f = lambda x. if x = 0 then 1 else x * f (x - 1) in f 3";
	private static String defaultExpression = "let f = ref (lambda x.x) in let fact = lambda x.if x = 0 then 1 else x * (!f (x - 1)) in (f := fact, !f 3)";
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu runMenu;
	private ActionListener menulistener;
	private JToolBar mainToolbar;
	private JTabbedPane tabbedPane;
	private EditorWindow selectedEditor;
	private PropertyChangeListener editorlistener;
	
	public MainWindow() {
		    super();
		    setTitle("Projektgruppe v.01");
		    editorlistener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					try {
						if (evt.getPropertyName().equals("name")){
						tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), (String)evt.getNewValue());
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
		    };
		    initComponents();
		    pack();
		    setSize(640, 480);
		  }
	
	  private void initComponents() {
		  initMainMenu();		
		  initToolbar();
		  initTabbedPane();
		  initDefaultFile();
	  }
	  
	  private void initMainMenu(){
		    menulistener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          if (e.getActionCommand() == "New")
		        	  	handleNew();
		          else if (e.getActionCommand() == "Open")
			            handleOpen();
		          else if (e.getActionCommand() == "Close")
			            handleClose();
		          else if (e.getActionCommand() == "Preferences")
			            handlePreferences();
		          else if (e.getActionCommand() == "Exit")
		            handleExit();
		        }
		      };
		    JMenuBar mainMenu = new JMenuBar();
		    fileMenu = new JMenu("File");
		    fileMenu.setMnemonic(KeyEvent.VK_F);
		    mainMenu.add(fileMenu);
   		    
		    editMenu = new JMenu("Edit");
		    editMenu.setMnemonic(KeyEvent.VK_E);
		    mainMenu.add(editMenu);
		    
		    runMenu = new JMenu("Run");
		    runMenu.setMnemonic(KeyEvent.VK_A);
		    mainMenu.add(runMenu);
		    
		    setJMenuBar(mainMenu);
		    this.setLayout(new BorderLayout()); 		 
	  }
	  private void initToolbar(){
		  mainToolbar = new JToolBar("MainMenu");

		  add (mainToolbar, BorderLayout.NORTH);
	  }
	  private void initTabbedPane(){
		  tabbedPane = new JTabbedPane();
		  add (tabbedPane, BorderLayout.CENTER);
		  tabbedPane.addChangeListener(
				  new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							selectedEditor = (EditorWindow)tabbedPane.getSelectedComponent();
							updateActions();
						}
				    });
	  }
	  private void initDefaultFile(){
				SourceFile newFile = new SourceFile();
				try {
				newFile.getDocument().insertString(0, defaultExpression, null);
				} catch (Exception e){}
				EditorWindow tmp = new EditorWindow(newFile, this);
				tmp.addPropertyChangeListener(editorlistener);
				tabbedPane.addTab(tmp.getTitle(), tmp);
	  }
	  
	  private void initDefaultActions () {
		    JMenuItem newItem = fileMenu.add("New");
		    newItem.addActionListener(menulistener);
		    newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
		        KeyEvent.CTRL_MASK));
		    newItem.setMnemonic(KeyEvent.VK_N);
		    
		    JMenuItem openItem = fileMenu.add("Open");
		    openItem.addActionListener(menulistener);
		    //newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
		    //   KeyEvent.CTRL_MASK));
		    //newItem.setMnemonic(KeyEvent.VK_N);
		    
		    JMenuItem closeItem = fileMenu.add("Close");
		    closeItem.addActionListener(menulistener);

		    fileMenu.addSeparator();
		    
		    JMenuItem preferences = editMenu.add("Preferences");
		    preferences.addActionListener(menulistener);
		    preferences.setMnemonic(KeyEvent.VK_P);
		    
			  JButton open = new JButton (new ImageIcon(EditorWindow.class.getResource("icons/open.png")));
			  JButton newb = new JButton (new ImageIcon(EditorWindow.class.getResource("icons/new.png")));
			  open.addActionListener(menulistener);
			  newb.addActionListener(menulistener);
			  open.setActionCommand("Open");
			  newb.setActionCommand("New");
			  mainToolbar.add(newb);
			  mainToolbar.add(open);
			  
	  }
	  
	  private void handleNew(){
			SourceFile newFile = new SourceFile();
			try {
			newFile.getDocument().insertString(0, defaultExpression, null);
			} catch (Exception e){}
			EditorWindow newItem = new EditorWindow(newFile, this);
			newItem.addPropertyChangeListener(editorlistener);
			tabbedPane.addTab(newItem.getTitle(), newItem);
			tabbedPane.setSelectedComponent(newItem);
	  }
	  private void handleOpen(){
				    		JFileChooser chooser = new JFileChooser();
				    		chooser.showOpenDialog(getMe());
				    		File infile = chooser.getSelectedFile();
				    		StringBuffer buffer = new StringBuffer();
				    		try{
				    		FileInputStream in = new FileInputStream (infile);
				            int onechar;
				            
				            while ((onechar=in.read()) != -1)
				               buffer.append((char)onechar);
				            
				            SourceFile newFile = new SourceFile();
				            newFile.setName(infile.getName());
							newFile.getDocument().insertString(0, buffer.toString(), null);
							EditorWindow newItem = new EditorWindow(newFile, getMe());
							newItem.addPropertyChangeListener(editorlistener);
							tabbedPane.addTab(newItem.getTitle(), newItem);
							tabbedPane.setSelectedComponent(newItem);
							newItem.setSystemfile(infile);
							
				    		} catch (Exception e){ //TODO; Add Handling!!
				    			System.out.println("Error while Loading File!");
				    		}
	  }
	  
	  private void handleClose(){
		  	((EditorWindow)tabbedPane.getSelectedComponent()).handleSave();
		  	tabbedPane.remove(tabbedPane.getSelectedIndex());
		  	this.repaint();
	  }
	  
	  private void updateActions(){
		  fileMenu.removeAll();
		  editMenu.removeAll();
		  runMenu.removeAll();
		  mainToolbar.removeAll();
		  initDefaultActions();
		  if(selectedEditor != null){
		  addActions(selectedEditor.getFileActions(), fileMenu);
		  addActions(selectedEditor.getToolbarActions(), mainToolbar);
		  addActions(selectedEditor.getRunActions(), runMenu);
		  addActions(selectedEditor.getEditActions(), editMenu);
		  }
	  }
      private void handleExit(){
    	  System.exit(0);
      }
      private void handlePreferences(){
  		SettingsGUI gui = new SettingsGUI(this, false);
		gui.setVisible(true);
      }
      
      private void addActions(List<EditorAction> list, JComponent component){
    	  List<EditorAction> actionlist = list;
		  for(int i = 0 ; i < actionlist.size() ; i++){
			 final EditorAction action = actionlist.get(i);
			 if (component instanceof JMenu){
				 final JMenuItem tmpmenu = new JMenuItem (action.getTitle());
				 tmpmenu.addActionListener(action.getActionListener());
                 tmpmenu.setEnabled(action.isEnabled());
				 component.add( tmpmenu);
                 action.addPropertyChangeListener("enabled", new PropertyChangeListener() {
                   public void propertyChange(PropertyChangeEvent evt) {
                     tmpmenu.setEnabled(action.isEnabled());
                   }
                 });
			 }
			 else{
				 //ImageIcon icon = new ImageIcon(action.getIcon());
				 JButton tmptoolbar;
				 if (action.getIcon() == null){
					  tmptoolbar = new JButton (action.getTitle());
				 }
				 else{
					  tmptoolbar = new JButton (action.getIcon());
				 }
			 component.add( tmptoolbar);
			 tmptoolbar.addActionListener(action.getActionListener());
			 }
			}
		  if (component.equals(fileMenu)){
		  fileMenu.addSeparator();
		   JMenuItem exitItem = fileMenu.add("Exit");
		   exitItem.addActionListener(menulistener);
		   exitItem.setMnemonic(KeyEvent.VK_X);
		   }
      }
    private MainWindow getMe(){
    	return this;
    	};

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
