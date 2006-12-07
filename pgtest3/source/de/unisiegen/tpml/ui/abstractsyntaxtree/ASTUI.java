package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.InputEvent ;
import java.awt.event.KeyEvent ;
import java.util.ResourceBundle ;
import javax.swing.BorderFactory ;
import javax.swing.ImageIcon ;
import javax.swing.JCheckBox ;
import javax.swing.JCheckBoxMenuItem ;
import javax.swing.JMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JPanel ;
import javax.swing.JPopupMenu ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.KeyStroke ;
import javax.swing.border.TitledBorder ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTActionListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTItemListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTKeyListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTMouseListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTTreeSelectionListener ;


/**
 * This class creates the GUI of the AbstractSyntaxTree.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTUI
{
  /**
   * The GridBagConstraints.
   */
  private GridBagConstraints gridBagConstraints ;


  /**
   * The GridBagLayout.
   */
  private GridBagLayout gridBagLayout ;


  /**
   * The tree.
   * 
   * @see #getJTreeAbstractSyntaxTree()
   */
  private JTree jTreeAbstractSyntaxTree ;


  /**
   * The root node.
   * 
   * @see #setRootNode(DefaultMutableTreeNode)
   */
  private DefaultMutableTreeNode rootNode ;


  /**
   * The tree model.
   * 
   * @see #getTreeModel()
   */
  private DefaultTreeModel treeModel ;


  /**
   * The scroll pane.
   */
  private JScrollPane jScrollPaneAbstractSyntaxTree ;


  /**
   * The preferences panel.
   */
  private JPanel jPanelPreferences ;


  /**
   * The main panel.
   * 
   * @see #getJPanelMain()
   */
  private JPanel jPanelMain ;


  /**
   * The check box replace.
   * 
   * @see #getJCheckBoxReplace()
   */
  private JCheckBox jCheckBoxReplace ;


  /**
   * The check box binding. *
   * 
   * @see #getJCheckBoxBinding()
   */
  private JCheckBox jCheckBoxBinding ;


  /**
   * The check box unbound. *
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBox jCheckBoxUnbound ;


  /**
   * The check box selection.
   * 
   * @see #getJCheckBoxSelection()
   */
  private JCheckBox jCheckBoxSelection ;


  /**
   * The check box auto update.
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBox jCheckBoxAutoUpdate ;


  /**
   * The tree selection listener.
   * 
   * @see #getASTTreeSelectionListener()
   */
  private ASTTreeSelectionListener aSTTreeSelectionListener ;


  /**
   * The action listener.
   * 
   * @see #getASTActionListener()
   */
  private ASTActionListener aSTActionListener ;


  /**
   * The item listener.
   * 
   * @see #getASTItemListener()
   */
  private ASTItemListener aSTItemListener ;


  /**
   * The AbstractSyntaxTree.
   * 
   * @see #getAbstractSyntaxTree()
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * The mouse listener.
   * 
   * @see #getASTMouseListener()
   */
  private ASTMouseListener aSTMouseListener ;


  /**
   * The ResourceBundle.
   */
  private ResourceBundle resourceBundle ;


  /**
   * The popup menu.
   * 
   * @see #getJPopupMenu()
   */
  private JPopupMenu jPopupMenu ;


  /**
   * The menu item expand.
   * 
   * @see #getJMenuItemExpand()
   */
  private JMenuItem jMenuItemExpand ;


  /**
   * The menu item expand all.
   * 
   * @see #getJMenuItemExpandAll()
   */
  private JMenuItem jMenuItemExpandAll ;


  /**
   * The menu item collapse.
   * 
   * @see #getJMenuItemCollapse()
   */
  private JMenuItem jMenuItemCollapse ;


  /**
   * The menu item collapse all.
   * 
   * @see #getJMenuItemCollapseAll()
   */
  private JMenuItem jMenuItemCollapseAll ;


  /**
   * The menu item close.
   * 
   * @see #getJMenuItemClose()
   */
  private JMenuItem jMenuItemClose ;


  /**
   * The menu item close all.
   * 
   * @see #getJMenuItemCloseAll()
   */
  private JMenuItem jMenuItemCloseAll ;


  /**
   * The menu item copy.
   * 
   * @see #getJMenuItemCopy()
   */
  private JMenuItem jMenuItemCopy ;


  /**
   * The menu item binding.
   * 
   * @see #getJCheckBoxBinding()
   */
  private JCheckBoxMenuItem jMenuItemBinding ;


  /**
   * The menu item unbound.
   * 
   * @see #getJCheckBoxUnbound()
   */
  private JCheckBoxMenuItem jMenuItemUnbound ;


  /**
   * The menu item auto update.
   * 
   * @see #getJCheckBoxAutoUpdate()
   */
  private JCheckBoxMenuItem jMenuItemAutoUpdate ;


  /**
   * The menu preferences.
   */
  private JMenu jMenuPreferences ;


  /**
   * The menu item replace.
   * 
   * @see #getJCheckBoxReplace()
   */
  private JCheckBoxMenuItem jMenuItemReplace ;


  /**
   * The menu item selection.
   * 
   * @see #getJCheckBoxSelection()
   */
  private JCheckBoxMenuItem jMenuItemSelection ;


  /**
   * The insets.
   */
  private Insets insets ;


  /**
   * This constructor creates the GUI of the AbstractSyntaxTree.
   * 
   * @param pAbstractSyntaxTree The AbstractSyntaxTree.
   */
  public ASTUI ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    // Insets
    this.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    // Preferences
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ; //$NON-NLS-1$
    // Listener
    this.aSTItemListener = new ASTItemListener ( this ) ;
    this.aSTActionListener = new ASTActionListener ( this ) ;
    this.aSTTreeSelectionListener = new ASTTreeSelectionListener ( this ) ;
    this.aSTMouseListener = new ASTMouseListener ( this ) ;
    // PopupMenu
    createPopupMenu ( ) ;
    // Layout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // Panel Preferences
    this.jPanelPreferences = new JPanel ( ) ;
    this.jPanelPreferences.setLayout ( this.gridBagLayout ) ;
    this.jPanelPreferences.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" , //$NON-NLS-1$
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ; //$NON-NLS-1$
    // CheckBox Selection
    this.jCheckBoxSelection = new JCheckBox ( this.resourceBundle
        .getString ( "selection" ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxSelection.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isSelection ( ) ) ;
    this.jCheckBoxSelection.setFocusable ( false ) ;
    this.jCheckBoxSelection.addItemListener ( this.aSTItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxSelection ,
        this.gridBagConstraints ) ;
    // CheckBox Binding
    this.jCheckBoxBinding = new JCheckBox ( this.resourceBundle
        .getString ( "binding" ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxBinding.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isBinding ( ) ) ;
    this.jCheckBoxBinding.setFocusable ( false ) ;
    this.jCheckBoxBinding.addItemListener ( this.aSTItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxBinding ,
        this.gridBagConstraints ) ;
    // CheckBox Unbound
    this.jCheckBoxUnbound = new JCheckBox ( this.resourceBundle
        .getString ( "unbound" ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxUnbound.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isUnbound ( ) ) ;
    this.jCheckBoxUnbound.setFocusable ( false ) ;
    this.jCheckBoxUnbound.addItemListener ( this.aSTItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxUnbound ,
        this.gridBagConstraints ) ;
    // CheckBox Replace
    this.jCheckBoxReplace = new JCheckBox ( this.resourceBundle
        .getString ( "replace" ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isReplace ( ) ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
    this.jCheckBoxReplace.addItemListener ( this.aSTItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxReplace ,
        this.gridBagConstraints ) ;
    // CheckBox AutoUpdate
    this.jCheckBoxAutoUpdate = new JCheckBox ( this.resourceBundle
        .getString ( "autoUpdate" ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ; //$NON-NLS-1$
    this.jCheckBoxAutoUpdate.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isAutoUpdate ( ) ) ;
    this.jCheckBoxAutoUpdate.setFocusable ( false ) ;
    this.jCheckBoxAutoUpdate.addItemListener ( this.aSTItemListener ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 4 , 0 , 4 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 4 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxAutoUpdate ,
        this.gridBagConstraints ) ;
    // Panel Main
    this.jPanelMain = new JPanel ( ) ;
    this.jPanelMain.setLayout ( this.gridBagLayout ) ;
    // TreeModel
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    // Tree AbstractSyntaxTree
    this.jTreeAbstractSyntaxTree = new JTree ( this.treeModel ) ;
    /*
     * ToolTipManager.sharedInstance ( ).registerComponent (
     * this.jTreeAbstractSyntaxTree ) ;
     */
    this.jTreeAbstractSyntaxTree.setCellRenderer ( new ASTCellRenderer ( ) ) ;
    this.jTreeAbstractSyntaxTree.getSelectionModel ( )
        .addTreeSelectionListener ( this.aSTTreeSelectionListener ) ;
    this.jTreeAbstractSyntaxTree.setRowHeight ( 20 ) ;
    this.jTreeAbstractSyntaxTree.addMouseListener ( this.aSTMouseListener ) ;
    this.jTreeAbstractSyntaxTree.addKeyListener ( new ASTKeyListener ( this ) ) ;
    // ScrollPane AbstractSyntax
    this.jScrollPaneAbstractSyntaxTree = new JScrollPane (
        this.jTreeAbstractSyntaxTree ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 0 , 0 , 1 , 0 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelMain.add ( this.jScrollPaneAbstractSyntaxTree ,
        this.gridBagConstraints ) ;
    // Panel Preferences
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.insets.set ( 1 , 0 , 0 , 1 ) ;
    this.gridBagConstraints.insets = this.insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 0 ;
    this.jPanelMain.add ( this.jPanelPreferences , this.gridBagConstraints ) ;
  }


  /**
   * Creates the popup menu of the AbstractSyntaxTree.
   */
  private void createPopupMenu ( )
  {
    // PopupMenu
    this.jPopupMenu = new JPopupMenu ( ) ;
    // MenuItem Expand
    this.jMenuItemExpand = new JMenuItem ( this.resourceBundle
        .getString ( "expand" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setMnemonic ( this.resourceBundle.getString (
        "expandMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setToolTipText ( this.resourceBundle
        .getString ( "expandToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemExpand.setActionCommand ( "expand" ) ; //$NON-NLS-1$
    this.jMenuItemExpand.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpand ) ;
    // MenuItem ExpandAll
    this.jMenuItemExpandAll = new JMenuItem ( this.resourceBundle
        .getString ( "expandAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setMnemonic ( this.resourceBundle.getString (
        "expandAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setToolTipText ( this.resourceBundle
        .getString ( "expandAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.setActionCommand ( "expandAll" ) ; //$NON-NLS-1$
    this.jMenuItemExpandAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpandAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Collapse
    this.jMenuItemCollapse = new JMenuItem ( this.resourceBundle
        .getString ( "collapse" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setMnemonic ( this.resourceBundle.getString (
        "collapseMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setToolTipText ( this.resourceBundle
        .getString ( "collapseToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.setActionCommand ( "collapse" ) ; //$NON-NLS-1$
    this.jMenuItemCollapse.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapse ) ;
    // MenuItem CollapseAll
    this.jMenuItemCollapseAll = new JMenuItem ( this.resourceBundle
        .getString ( "collapseAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setMnemonic ( this.resourceBundle.getString (
        "collapseAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setToolTipText ( this.resourceBundle
        .getString ( "collapseAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setIcon ( new ImageIcon ( getClass ( )
        .getResource ( "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.setActionCommand ( "collapseAll" ) ; //$NON-NLS-1$
    this.jMenuItemCollapseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Close
    this.jMenuItemClose = new JMenuItem ( this.resourceBundle
        .getString ( "close" ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setMnemonic ( this.resourceBundle.getString (
        "closeMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setToolTipText ( this.resourceBundle
        .getString ( "closeToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemClose.setActionCommand ( "close" ) ; //$NON-NLS-1$
    this.jMenuItemClose.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemClose ) ;
    // MenuItem CloseAll
    this.jMenuItemCloseAll = new JMenuItem ( this.resourceBundle
        .getString ( "closeAll" ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setMnemonic ( this.resourceBundle.getString (
        "closeAllMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setToolTipText ( this.resourceBundle
        .getString ( "closeAllToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.setActionCommand ( "closeAll" ) ; //$NON-NLS-1$
    this.jMenuItemCloseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCloseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Copy
    this.jMenuItemCopy = new JMenuItem ( this.resourceBundle
        .getString ( "copy" ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setMnemonic ( this.resourceBundle.getString (
        "copyMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setToolTipText ( this.resourceBundle
        .getString ( "copyToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/copy16.gif" ) ) ) ; //$NON-NLS-1$
    this.jMenuItemCopy.setActionCommand ( "copy" ) ; //$NON-NLS-1$
    this.jMenuItemCopy.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemCopy.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_C ,
        InputEvent.CTRL_MASK ) ) ;
    this.jPopupMenu.add ( this.jMenuItemCopy ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Preferences
    this.jMenuPreferences = new JMenu ( this.resourceBundle
        .getString ( "preferences" ) ) ; //$NON-NLS-1$
    this.jMenuPreferences.setMnemonic ( this.resourceBundle.getString (
        "preferencesMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuPreferences.setIcon ( new ImageIcon ( getClass ( ).getResource (
        "/de/unisiegen/tpml/ui/icons/empty16.gif" ) ) ) ; //$NON-NLS-1$  
    this.jMenuPreferences.setActionCommand ( "preferences" ) ; //$NON-NLS-1$
    this.jMenuPreferences.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuPreferences ) ;
    // MenuItem Selection
    this.jMenuItemSelection = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "selection" ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemSelection.setActionCommand ( "selection" ) ; //$NON-NLS-1$
    this.jMenuItemSelection.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemSelection.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isSelection ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemSelection ) ;
    // MenuItem Binding
    this.jMenuItemBinding = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "binding" ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemBinding.setActionCommand ( "binding" ) ; //$NON-NLS-1$
    this.jMenuItemBinding.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemBinding.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isBinding ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemBinding ) ;
    // MenuItem Unbound
    this.jMenuItemUnbound = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "unbound" ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.setActionCommand ( "unbound" ) ; //$NON-NLS-1$
    this.jMenuItemUnbound.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemUnbound.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isUnbound ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemUnbound ) ;
    // MenuItem Replace
    this.jMenuItemReplace = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "replace" ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemReplace.setActionCommand ( "replace" ) ; //$NON-NLS-1$
    this.jMenuItemReplace.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isReplace ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemReplace ) ;
    // MenuItem AutoUpdate
    this.jMenuItemAutoUpdate = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "autoUpdate" ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.setActionCommand ( "autoUpdate" ) ; //$NON-NLS-1$
    this.jMenuItemAutoUpdate.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemAutoUpdate.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isAutoUpdate ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemAutoUpdate ) ;
  }


  /**
   * Returns the abstractSyntaxTree.
   * 
   * @return The abstractSyntaxTree.
   * @see #abstractSyntaxTree
   */
  public AbstractSyntaxTree getAbstractSyntaxTree ( )
  {
    return this.abstractSyntaxTree ;
  }


  /**
   * Returns the aSTActionListener.
   * 
   * @return The aSTActionListener.
   * @see #aSTActionListener
   */
  public ASTActionListener getASTActionListener ( )
  {
    return this.aSTActionListener ;
  }


  /**
   * Returns the aSTItemListener.
   * 
   * @return The aSTItemListener.
   * @see #aSTItemListener
   */
  public ASTItemListener getASTItemListener ( )
  {
    return this.aSTItemListener ;
  }


  /**
   * Returns the aSTMouseListener.
   * 
   * @return The aSTMouseListener.
   * @see #aSTMouseListener
   */
  public ASTMouseListener getASTMouseListener ( )
  {
    return this.aSTMouseListener ;
  }


  /**
   * Returns the aSTTreeSelectionListener.
   * 
   * @return The aSTTreeSelectionListener.
   * @see #aSTTreeSelectionListener
   */
  public ASTTreeSelectionListener getASTTreeSelectionListener ( )
  {
    return this.aSTTreeSelectionListener ;
  }


  /**
   * Returns the jCheckBoxAutoUpdate.
   * 
   * @return The jCheckBoxAutoUpdate.
   * @see #jCheckBoxAutoUpdate
   */
  public JCheckBox getJCheckBoxAutoUpdate ( )
  {
    return this.jCheckBoxAutoUpdate ;
  }


  /**
   * Returns the jCheckBoxBinding.
   * 
   * @return The jCheckBoxBinding.
   * @see #jCheckBoxBinding
   */
  public JCheckBox getJCheckBoxBinding ( )
  {
    return this.jCheckBoxBinding ;
  }


  /**
   * Returns the jCheckBoxReplace.
   * 
   * @return The jCheckBoxReplace.
   * @see #jCheckBoxReplace
   */
  public JCheckBox getJCheckBoxReplace ( )
  {
    return this.jCheckBoxReplace ;
  }


  /**
   * Returns the jCheckBoxSelection.
   * 
   * @return The jCheckBoxSelection.
   * @see #jCheckBoxSelection
   */
  public JCheckBox getJCheckBoxSelection ( )
  {
    return this.jCheckBoxSelection ;
  }


  /**
   * Returns the jCheckBoxUnbound.
   * 
   * @return The jCheckBoxUnbound.
   * @see #jCheckBoxUnbound
   */
  public JCheckBox getJCheckBoxUnbound ( )
  {
    return this.jCheckBoxUnbound ;
  }


  /**
   * Returns the jMenuItemAutoUpdate.
   * 
   * @return The jMenuItemAutoUpdate.
   * @see #jMenuItemAutoUpdate
   */
  public JCheckBoxMenuItem getJMenuItemAutoUpdate ( )
  {
    return this.jMenuItemAutoUpdate ;
  }


  /**
   * Returns the jMenuItemBinding.
   * 
   * @return The jMenuItemBinding.
   * @see #jMenuItemBinding
   */
  public JCheckBoxMenuItem getJMenuItemBinding ( )
  {
    return this.jMenuItemBinding ;
  }


  /**
   * Returns the jMenuItemClose.
   * 
   * @return The jMenuItemClose.
   * @see #jMenuItemClose
   */
  public JMenuItem getJMenuItemClose ( )
  {
    return this.jMenuItemClose ;
  }


  /**
   * Returns the jMenuItemCloseAll.
   * 
   * @return The jMenuItemCloseAll.
   * @see #jMenuItemCloseAll
   */
  public JMenuItem getJMenuItemCloseAll ( )
  {
    return this.jMenuItemCloseAll ;
  }


  /**
   * Returns the jMenuItemCollapse.
   * 
   * @return The jMenuItemCollapse.
   * @see #jMenuItemCollapse
   */
  public JMenuItem getJMenuItemCollapse ( )
  {
    return this.jMenuItemCollapse ;
  }


  /**
   * Returns the jMenuItemCollapseAll.
   * 
   * @return The jMenuItemCollapseAll.
   * @see #jMenuItemCollapseAll
   */
  public JMenuItem getJMenuItemCollapseAll ( )
  {
    return this.jMenuItemCollapseAll ;
  }


  /**
   * Returns the jMenuItemCopy.
   * 
   * @return The jMenuItemCopy.
   * @see #jMenuItemCopy
   */
  public JMenuItem getJMenuItemCopy ( )
  {
    return this.jMenuItemCopy ;
  }


  /**
   * Returns the jMenuItemExpand.
   * 
   * @return The jMenuItemExpand.
   * @see #jMenuItemExpand
   */
  public JMenuItem getJMenuItemExpand ( )
  {
    return this.jMenuItemExpand ;
  }


  /**
   * Returns the jMenuItemExpandAll.
   * 
   * @return The jMenuItemExpandAll.
   * @see #jMenuItemExpandAll
   */
  public JMenuItem getJMenuItemExpandAll ( )
  {
    return this.jMenuItemExpandAll ;
  }


  /**
   * Returns the jMenuItemReplace.
   * 
   * @return The jMenuItemReplace.
   * @see #jMenuItemReplace
   */
  public JCheckBoxMenuItem getJMenuItemReplace ( )
  {
    return this.jMenuItemReplace ;
  }


  /**
   * Returns the jMenuItemSelection.
   * 
   * @return The jMenuItemSelection.
   * @see #jMenuItemSelection
   */
  public JCheckBoxMenuItem getJMenuItemSelection ( )
  {
    return this.jMenuItemSelection ;
  }


  /**
   * Returns the jMenuItemUnbound.
   * 
   * @return The jMenuItemUnbound.
   * @see #jMenuItemUnbound
   */
  public JCheckBoxMenuItem getJMenuItemUnbound ( )
  {
    return this.jMenuItemUnbound ;
  }


  /**
   * Returns the jPanelMain.
   * 
   * @return The jPanelMain.
   * @see #jPanelMain
   */
  public JPanel getJPanelMain ( )
  {
    return this.jPanelMain ;
  }


  /**
   * Returns the jPopupMenu.
   * 
   * @return The jPopupMenu.
   * @see #jPopupMenu
   */
  public JPopupMenu getJPopupMenu ( )
  {
    return this.jPopupMenu ;
  }


  /**
   * Returns the jTreeAbstractSyntaxTree.
   * 
   * @return The jTreeAbstractSyntaxTree.
   * @see #jTreeAbstractSyntaxTree
   */
  public JTree getJTreeAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntaxTree ;
  }


  /**
   * Returns the treeModel.
   * 
   * @return The treeModel.
   * @see #treeModel
   */
  public DefaultTreeModel getTreeModel ( )
  {
    return this.treeModel ;
  }


  /**
   * Sets the new root node.
   * 
   * @param pRootNode The new root node.
   */
  public void setRootNode ( DefaultMutableTreeNode pRootNode )
  {
    this.rootNode = pRootNode ;
    this.treeModel.setRoot ( this.rootNode ) ;
  }
}
