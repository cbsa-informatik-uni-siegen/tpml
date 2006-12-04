package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.util.ResourceBundle ;
import javax.swing.BorderFactory ;
import javax.swing.JCheckBox ;
import javax.swing.JCheckBoxMenuItem ;
import javax.swing.JMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JPanel ;
import javax.swing.JPopupMenu ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.border.TitledBorder ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import javax.swing.tree.DefaultTreeModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTActionListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTItemListener ;
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
   * The cell renderer.
   */
  private DefaultTreeCellRenderer cellRenderer ;


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
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ;
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
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
    // CheckBox Selection
    this.jCheckBoxSelection = new JCheckBox ( this.resourceBundle
        .getString ( "selection" ) ) ;
    this.jCheckBoxSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ;
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
        .getString ( "binding" ) ) ;
    this.jCheckBoxBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ;
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
        .getString ( "unbound" ) ) ;
    this.jCheckBoxUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ;
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
        .getString ( "replace" ) ) ;
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ;
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
        .getString ( "autoUpdate" ) ) ;
    this.jCheckBoxAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ;
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
    // CellRenderer
    this.cellRenderer = new DefaultTreeCellRenderer ( ) ;
    this.cellRenderer.setIcon ( null ) ;
    this.cellRenderer.setLeafIcon ( null ) ;
    this.cellRenderer.setOpenIcon ( null ) ;
    this.cellRenderer.setClosedIcon ( null ) ;
    this.cellRenderer.setDisabledIcon ( null ) ;
    this.cellRenderer.setFont ( new Font ( "SansSerif" , Font.PLAIN , 16 ) ) ;
    this.cellRenderer.setBackground ( Color.WHITE ) ;
    this.cellRenderer.setBackgroundNonSelectionColor ( Color.WHITE ) ;
    this.cellRenderer
        .setBackgroundSelectionColor ( new Color ( 225 , 225 , 255 ) ) ;
    this.cellRenderer.setFont ( new Font ( "SansSerif" , Font.PLAIN , 14 ) ) ;
    this.cellRenderer.setBorderSelectionColor ( Color.BLUE ) ;
    this.cellRenderer.setTextSelectionColor ( Color.BLACK ) ;
    this.cellRenderer.setTextNonSelectionColor ( Color.BLACK ) ;
    // TreeModel
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    // Tree AbstractSyntaxTree
    this.jTreeAbstractSyntaxTree = new JTree ( this.treeModel ) ;
    this.jTreeAbstractSyntaxTree.setCellRenderer ( this.cellRenderer ) ;
    this.jTreeAbstractSyntaxTree.getSelectionModel ( )
        .addTreeSelectionListener ( this.aSTTreeSelectionListener ) ;
    this.jTreeAbstractSyntaxTree.setRowHeight ( 20 ) ;
    this.jTreeAbstractSyntaxTree.addMouseListener ( this.aSTMouseListener ) ;
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
        .getString ( "expand" ) ) ;
    this.jMenuItemExpand.setMnemonic ( this.resourceBundle.getString (
        "expandMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemExpand.setToolTipText ( this.resourceBundle
        .getString ( "expandToolTip" ) ) ;
    this.jMenuItemExpand.setActionCommand ( "expand" ) ;
    this.jMenuItemExpand.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpand ) ;
    // MenuItem ExpandAll
    this.jMenuItemExpandAll = new JMenuItem ( this.resourceBundle
        .getString ( "expandAll" ) ) ;
    this.jMenuItemExpandAll.setMnemonic ( this.resourceBundle.getString (
        "expandAllMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemExpandAll.setToolTipText ( this.resourceBundle
        .getString ( "expandAllToolTip" ) ) ;
    this.jMenuItemExpandAll.setActionCommand ( "expandAll" ) ;
    this.jMenuItemExpandAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpandAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Collapse
    this.jMenuItemCollapse = new JMenuItem ( this.resourceBundle
        .getString ( "collapse" ) ) ;
    this.jMenuItemCollapse.setMnemonic ( this.resourceBundle.getString (
        "collapseMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCollapse.setToolTipText ( this.resourceBundle
        .getString ( "collapseToolTip" ) ) ;
    this.jMenuItemCollapse.setActionCommand ( "collapse" ) ;
    this.jMenuItemCollapse.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapse ) ;
    // MenuItem CollapseAll
    this.jMenuItemCollapseAll = new JMenuItem ( this.resourceBundle
        .getString ( "collapseAll" ) ) ;
    this.jMenuItemCollapseAll.setMnemonic ( this.resourceBundle.getString (
        "collapseAllMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCollapseAll.setToolTipText ( this.resourceBundle
        .getString ( "collapseAllToolTip" ) ) ;
    this.jMenuItemCollapseAll.setActionCommand ( "collapseAll" ) ;
    this.jMenuItemCollapseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Close
    this.jMenuItemClose = new JMenuItem ( this.resourceBundle
        .getString ( "close" ) ) ;
    this.jMenuItemClose.setMnemonic ( this.resourceBundle.getString (
        "closeMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemClose.setToolTipText ( this.resourceBundle
        .getString ( "closeToolTip" ) ) ;
    this.jMenuItemClose.setActionCommand ( "close" ) ;
    this.jMenuItemClose.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemClose ) ;
    // MenuItem CloseAll
    this.jMenuItemCloseAll = new JMenuItem ( this.resourceBundle
        .getString ( "closeAll" ) ) ;
    this.jMenuItemCloseAll.setMnemonic ( this.resourceBundle.getString (
        "closeAllMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCloseAll.setToolTipText ( this.resourceBundle
        .getString ( "closeAllToolTip" ) ) ;
    this.jMenuItemCloseAll.setActionCommand ( "closeAll" ) ;
    this.jMenuItemCloseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCloseAll ) ;
    // Separator
    this.jPopupMenu.addSeparator ( ) ;
    // MenuItem Preferences
    this.jMenuPreferences = new JMenu ( this.resourceBundle
        .getString ( "preferences" ) ) ;
    this.jMenuPreferences.setMnemonic ( this.resourceBundle.getString (
        "preferencesMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuPreferences.setActionCommand ( "preferences" ) ;
    this.jMenuPreferences.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuPreferences ) ;
    // MenuItem Selection
    this.jMenuItemSelection = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "selection" ) ) ;
    this.jMenuItemSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemSelection.setToolTipText ( this.resourceBundle
        .getString ( "selectionToolTip" ) ) ;
    this.jMenuItemSelection.setActionCommand ( "selection" ) ;
    this.jMenuItemSelection.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemSelection.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isSelection ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemSelection ) ;
    // MenuItem Binding
    this.jMenuItemBinding = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "binding" ) ) ;
    this.jMenuItemBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemBinding.setToolTipText ( this.resourceBundle
        .getString ( "bindingToolTip" ) ) ;
    this.jMenuItemBinding.setActionCommand ( "binding" ) ;
    this.jMenuItemBinding.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemBinding.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isBinding ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemBinding ) ;
    // MenuItem Unbound
    this.jMenuItemUnbound = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "unbound" ) ) ;
    this.jMenuItemUnbound.setMnemonic ( this.resourceBundle.getString (
        "unboundMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemUnbound.setToolTipText ( this.resourceBundle
        .getString ( "unboundToolTip" ) ) ;
    this.jMenuItemUnbound.setActionCommand ( "unbound" ) ;
    this.jMenuItemUnbound.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemUnbound.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isUnbound ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemUnbound ) ;
    // MenuItem Replace
    this.jMenuItemReplace = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "replace" ) ) ;
    this.jMenuItemReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemReplace.setToolTipText ( this.resourceBundle
        .getString ( "replaceToolTip" ) ) ;
    this.jMenuItemReplace.setActionCommand ( "replace" ) ;
    this.jMenuItemReplace.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isReplace ( ) ) ;
    this.jMenuPreferences.add ( this.jMenuItemReplace ) ;
    // MenuItem AutoUpdate
    this.jMenuItemAutoUpdate = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "autoUpdate" ) ) ;
    this.jMenuItemAutoUpdate.setMnemonic ( this.resourceBundle.getString (
        "autoUpdateMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemAutoUpdate.setToolTipText ( this.resourceBundle
        .getString ( "autoUpdateToolTip" ) ) ;
    this.jMenuItemAutoUpdate.setActionCommand ( "autoUpdate" ) ;
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
