package ui;

import java.util.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PushbackReader;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import ui.smallstep.SmallStepGUI;
import ui.smallstep.SmallStepView;

import expressions.Expression;

public class Mainwindow extends JFrame {
  private JTabbedPane tabbedPane;
  private JTree treeView;
  private JMenuBar mainMenu;

  private static final String defaultExpression = "let rec f = lambda x. if x = 0 then 1 else x * f (x - 1) in f 3";
  private static final String SIMPLE = "let f = ref (lambda x.x) in let fact = lambda x.if x = 0 then 1 else x * (!f (x - 1)) in (f := fact, !f 3)";

  private LinkedList<SourceFile> fileList = new LinkedList<SourceFile>();
  private LinkedList<EditorWindow> editorList = new LinkedList<EditorWindow>();

  public Mainwindow() {
    super();
    
    setTitle("Projektgruppe v.01");
    initComponents();
    pack();
    setSize(640, 480);
    
 
  }
  
  private void initComponents() {
    tabbedPane = new JTabbedPane();

    JScrollPane scrollPane = new JScrollPane();
    treeView = new JTree();
    scrollPane.setViewportView(treeView);
  
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
        scrollPane, tabbedPane);

    getContentPane().setLayout(
        new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
    getContentPane().add(splitPane);

    ActionListener fileMenuListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "New")
          handleNew();
        else if (e.getActionCommand() == "Save")
          handleSave();
        else if (e.getActionCommand() == "Save as")
          handleSaveAs();
        else if (e.getActionCommand() == "Close")
          handleClose();
        else if (e.getActionCommand() == "Exit")
          handleExit();
      }
    };


    this.mainMenu = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    this.mainMenu.add(fileMenu);

    JMenuItem newItem = fileMenu.add("New");
    newItem.addActionListener(fileMenuListener);
    newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
        KeyEvent.CTRL_MASK));
    newItem.setMnemonic(KeyEvent.VK_N);

    fileMenu.addSeparator();

    JMenuItem saveItem = fileMenu.add("Save");
    saveItem.addActionListener(fileMenuListener);
    saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
        KeyEvent.CTRL_MASK));
    saveItem.setMnemonic(KeyEvent.VK_S);

    JMenuItem saveAsItem = fileMenu.add("Save as");
    saveAsItem.addActionListener(fileMenuListener);
    saveAsItem.setMnemonic(KeyEvent.VK_A);

    fileMenu.addSeparator();

    JMenuItem closeItem = fileMenu.add("Close");
    closeItem.addActionListener(fileMenuListener);
    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
        KeyEvent.CTRL_MASK));
    closeItem.setMnemonic(KeyEvent.VK_C);

    fileMenu.addSeparator();

    JMenuItem exitItem = fileMenu.add("Exit");
    exitItem.addActionListener(fileMenuListener);
    exitItem.setMnemonic(KeyEvent.VK_X);
    
    JMenu editMenu = new JMenu("Edit");
    editMenu.setMnemonic(KeyEvent.VK_E);
    this.mainMenu.add(editMenu);
    
    JMenuItem preferences = editMenu.add("Preferences");
    preferences.setMnemonic(KeyEvent.VK_P);
    preferences.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent event) {
    		SettingsGUI gui = new SettingsGUI(Mainwindow.this, false);
    		gui.setVisible(true);
    	}
    });
    

    JMenu actionsMenu = new JMenu("Actions");
    actionsMenu.setMnemonic(KeyEvent.VK_A);
    this.mainMenu.add(actionsMenu);

    JMenuItem smallStepItem = actionsMenu.add("SmallStep");
    smallStepItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
    smallStepItem.setMnemonic(KeyEvent.VK_S);
    smallStepItem.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent event) {
    		handleSmallStep();
    	}
    });
    
    

    
    JMenuItem typeCheckerItem = actionsMenu.add("TypeChecker");
    typeCheckerItem.setAccelerator(KeyStroke.getKeyStroke("F12"));
    typeCheckerItem.setMnemonic(KeyEvent.VK_T);
    typeCheckerItem.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent event) {
    		handleTypeChecker();
    	}
    });
    
    setJMenuBar(this.mainMenu);
    
    // open first tab
    handleNew();
  }

  private void handleExit() {
    dispose();
  }

  private void handleNew() {
    SourceFile newFile = new SourceFile();
    try {
//    	newFile.getDocument().insertString(0, "let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 5", null);
//    	newFile.getDocument().insertString(0, "let twice = lambda f.lambda x.f (f x) in twice", null);
//      newFile.getDocument().insertString(0, "snd (fst, snd (true, fst)) (#8_1, #10_2)", null);
        newFile.getDocument().insertString(0, SIMPLE, null);
    } catch (Exception e) { }
//    EditorWindow newEditor = new EditorWindow(newFile, this);
    EditorWindow newEditor = null;
    tabbedPane.add(newEditor);
    fileList.add(newFile);
    editorList.add(newEditor);
  }

  private void handleSave() {
    // JFileChooser chooser = new JFileChooser();
  }

  private void handleClose() {
    int index = tabbedPane.getSelectedIndex();
    // guiTest.closeFile (index);
    tabbedPane.remove(index);
  }

  private void handleSaveAs() {
    JFileChooser chooser = new JFileChooser();
    /*
     * // Note: source for ExampleFileFilter can be found in FileChooserDemo, //
     * under the demo/jfc directory in the JDK. ExampleFileFilter filter = new
     * ExampleFileFilter(); filter.addExtension("jpg");
     * filter.addExtension("gif"); filter.setDescription("JPG & GIF Images");
     * chooser.setFileFilter(filter);
     */
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
    }

  }
  
  private Expression getExpression() {
	    int index = tabbedPane.getSelectedIndex();
	    SourceFile sf = fileList.get(index);

	    Expression result = null;
	    try {
          /*
	      // Allocate the parser
	      Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(
	          sf.getDocument().getText(0, sf.getDocument().getLength())), 1024)));

	      // Parse the input
	      Start tree = parser.parse();

	      // translate the AST to a small step expression
	      Translator translator = new Translator();
	      tree.apply(translator);

	      result = translator.getExpression();
	      */
	    } catch (Exception e) {
	      JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return result;
  }
  
  private EditorWindow getEditor() {
	    int index = tabbedPane.getSelectedIndex();
	    return (editorList.get(index));
  }

  private void handleSmallStep() {
	  int index = tabbedPane.getSelectedIndex();
	  SourceFile sf = fileList.get(index);
	  try {

		  //SmallStepGUI gui = new SmallStepGUI (this, "SmallStep", true, sf.getDocument().getText(0, sf.getDocument().getLength()));
		  //gui.setVisible(true);
	  } catch (Exception e) {
	
		  e.printStackTrace();
	  }
	
	  /*
	  DebugView dbv = new DebugView (this, "narf", true, new SmallStepView ());
	  dbv.setSize(600, 800);
	  dbv.setVisible(true);
	  */
	  /*
	   * XXX handleSmallStep is degraded to a debug view
      Font f = new JComboBox().getFont();
      SmallStepModel model = new SmallStepModel(getExpression());
      model.setFont(f);
      
      // evaluate the resulting small step expression
      SmallStepGUI gui = new SmallStepGUI(this, "SmallStep", false, model);
      gui.setVisible(true);
      ((EditorWindow)getEditor()).handleSmallStep(gui);
      
      */
  }
  
  
  private void handleTypeChecker() {
//	  TypeCheckerGUI gui = new TypeCheckerGUI (this, "TypeChecker", true);
//	  gui.startTypeChecking (getExpression ());
//	  gui.setVisible(true);
  }
  
}
