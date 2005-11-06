package ui;

import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PushbackReader;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import l1.Translator;
import l1.lexer.Lexer;
import l1.node.Start;
import l1.parser.Parser;

public class Mainwindow extends JDialog {
  private JTabbedPane tabbedPane;
  private JTree treeView;
  private JMenuBar mainMenu;

  // Just for this this application
  //

  private LinkedList<SourceFile> fileList = new LinkedList<SourceFile>();

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

    ActionListener mlMenuListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "SmallStep")
          handleSmallStep();
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

    JMenu actionsMenu = new JMenu("Actions");
    actionsMenu.setMnemonic(KeyEvent.VK_A);
    this.mainMenu.add(actionsMenu);

    JMenuItem smallStepItem = actionsMenu.add("SmallStep");
    smallStepItem.addActionListener(mlMenuListener);
    smallStepItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
    smallStepItem.setMnemonic(KeyEvent.VK_S);

    setJMenuBar(this.mainMenu);
    
    // open first tab
    handleNew();
  }

  private void handleExit() {
    dispose();
  }

  private void handleNew() {
    SourceFile newFile = new SourceFile();
    tabbedPane.add(newFile.getComponent());
    fileList.add(newFile);
  }

  private void handleSave() {
    System.out.println("HandlerSave");
    // JFileChooser chooser = new JFileChooser();
  }

  private void handleClose() {
    int index = tabbedPane.getSelectedIndex();
    // guiTest.closeFile (index);
    tabbedPane.remove(index);
  }

  private void handleSaveAs() {
    System.out.println("handleSaveAs");
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
      System.out.println("You chose to open this file: "
          + chooser.getSelectedFile().getName());
    }

  }

  private void handleSmallStep() {
    int index = tabbedPane.getSelectedIndex();
    SourceFile sf = fileList.get(index);

    try {
      // Allocate the parser
      Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(
          sf.getDocument().getText(0, sf.getDocument().getLength())), 1024)));

      // Parse the input
      Start tree = parser.parse();

      // translate the AST to a small step expression
      Translator translator = new Translator();
      tree.apply(translator);

      // evaluate the resulting small step expression
      SmallStepGUI gui = new SmallStepGUI(null, "narf", true,
          new SmallStepModel(translator.getExpression()));
      gui.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // SmallStepGUI gui = new SmallStepGUI (this, "SmallStep Interpreter");
    // gui.setVisible (true);
  }
}
