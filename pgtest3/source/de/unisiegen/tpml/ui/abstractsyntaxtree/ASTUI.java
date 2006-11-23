package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.util.ResourceBundle ;
import javax.swing.BorderFactory ;
import javax.swing.JButton ;
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


public class ASTUI
{
  // private JFrame jFrameAbstractSyntaxTree ;
  private GridBagConstraints gridBagConstraints ;


  private GridBagLayout gridBagLayout ;


  private JTree jTreeAbstractSyntax ;


  private DefaultMutableTreeNode rootNode ;


  private DefaultTreeModel treeModel ;


  private DefaultTreeCellRenderer cellRenderer ;


  private JScrollPane jScrollPaneAbstractSyntax ;


  private JPanel jPanelPreferences ;


  private JPanel jPanelMain ;


  private JButton jButtonExpand ;


  private JButton jButtonCollapse ;


  private JButton jButtonExpandAll ;


  private JButton jButtonClose ;


  private JButton jButtonCloseAll ;


  private JButton jButtonCollapseAll ;


  private JCheckBox jCheckBoxReplace ;


  private JCheckBox jCheckBoxBinding ;


  private JCheckBox jCheckBoxSelection ;


  private ASTTreeSelectionListener aSTTreeSelectionListener ;


  private ASTActionListener aSTActionListener ;


  private ASTItemListener aSTItemListener ;


  private AbstractSyntaxTree abstractSyntaxTree ;


  private ASTMouseListener aSTMouseListener ;


  private ResourceBundle resourceBundle ;


  private JPopupMenu jPopupMenu ;


  private JMenuItem jMenuItemExpand ;


  private JMenuItem jMenuItemExpandAll ;


  private JMenuItem jMenuItemCollapse ;


  private JMenuItem jMenuItemCollapseAll ;


  private JMenuItem jMenuItemClose ;


  private JCheckBoxMenuItem jMenuItemBinding ;


  private JMenuItem jMenuItemCloseAll ;


  private JMenu jMenuItemPreferences ;


  private JCheckBoxMenuItem jMenuItemReplace ;


  private JCheckBoxMenuItem jMenuItemSelection ;


  public ASTUI ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    Insets insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    // Preferences
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ;
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    // Listener
    this.aSTItemListener = new ASTItemListener ( this ) ;
    this.aSTActionListener = new ASTActionListener ( this ) ;
    this.aSTTreeSelectionListener = new ASTTreeSelectionListener ( this ) ;
    this.aSTMouseListener = new ASTMouseListener ( this ) ;
    // PopupMenu
    this.jPopupMenu = new JPopupMenu ( ) ;
    this.jMenuItemExpand = new JMenuItem ( this.resourceBundle
        .getString ( "expand" ) ) ;
    this.jMenuItemExpand.setMnemonic ( this.resourceBundle.getString (
        "expandMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemExpand.setActionCommand ( "expand" ) ;
    this.jMenuItemExpand.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpand ) ;
    this.jMenuItemExpandAll = new JMenuItem ( this.resourceBundle
        .getString ( "expand_all" ) ) ;
    this.jMenuItemExpandAll.setMnemonic ( this.resourceBundle.getString (
        "expand_allMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemExpandAll.setActionCommand ( "expand_all" ) ;
    this.jMenuItemExpandAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemExpandAll ) ;
    this.jPopupMenu.addSeparator ( ) ;
    this.jMenuItemCollapse = new JMenuItem ( this.resourceBundle
        .getString ( "collapse" ) ) ;
    this.jMenuItemCollapse.setMnemonic ( this.resourceBundle.getString (
        "collapseMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCollapse.setActionCommand ( "collapse" ) ;
    this.jMenuItemCollapse.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapse ) ;
    this.jMenuItemCollapseAll = new JMenuItem ( this.resourceBundle
        .getString ( "collapse_all" ) ) ;
    this.jMenuItemCollapseAll.setMnemonic ( this.resourceBundle.getString (
        "collapse_allMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCollapseAll.setActionCommand ( "collapse_all" ) ;
    this.jMenuItemCollapseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCollapseAll ) ;
    this.jPopupMenu.addSeparator ( ) ;
    this.jMenuItemClose = new JMenuItem ( this.resourceBundle
        .getString ( "close" ) ) ;
    this.jMenuItemClose.setMnemonic ( this.resourceBundle.getString (
        "closeMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemClose.setActionCommand ( "close" ) ;
    this.jMenuItemClose.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemClose ) ;
    this.jMenuItemCloseAll = new JMenuItem ( this.resourceBundle
        .getString ( "close_all" ) ) ;
    this.jMenuItemCloseAll.setMnemonic ( this.resourceBundle.getString (
        "close_allMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemCloseAll.setActionCommand ( "close_all" ) ;
    this.jMenuItemCloseAll.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemCloseAll ) ;
    this.jPopupMenu.addSeparator ( ) ;
    this.jMenuItemPreferences = new JMenu ( this.resourceBundle
        .getString ( "preferences" ) ) ;
    this.jMenuItemPreferences.setMnemonic ( this.resourceBundle.getString (
        "preferencesMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemPreferences.setActionCommand ( "preferences" ) ;
    this.jMenuItemPreferences.addActionListener ( this.aSTActionListener ) ;
    this.jPopupMenu.add ( this.jMenuItemPreferences ) ;
    this.jMenuItemSelection = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "selection" ) ) ;
    this.jMenuItemSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemSelection.setActionCommand ( "selection" ) ;
    this.jMenuItemSelection.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemSelection.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isSelected ( ) ) ;
    this.jMenuItemPreferences.add ( this.jMenuItemSelection ) ;
    this.jMenuItemBinding = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "binding" ) ) ;
    this.jMenuItemBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemBinding.setActionCommand ( "binding" ) ;
    this.jMenuItemBinding.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemBinding.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isBindings ( ) ) ;
    this.jMenuItemPreferences.add ( this.jMenuItemBinding ) ;
    this.jMenuItemReplace = new JCheckBoxMenuItem ( this.resourceBundle
        .getString ( "replace" ) ) ;
    this.jMenuItemReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ;
    this.jMenuItemReplace.setActionCommand ( "replace" ) ;
    this.jMenuItemReplace.addActionListener ( this.aSTActionListener ) ;
    this.jMenuItemReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isReplace ( ) ) ;
    this.jMenuItemPreferences.add ( this.jMenuItemReplace ) ;
    // Layout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // Tree
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
    this.cellRenderer.setBorderSelectionColor ( Color.BLUE ) ;
    this.cellRenderer.setTextSelectionColor ( Color.BLACK ) ;
    this.cellRenderer.setTextNonSelectionColor ( Color.BLACK ) ;
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    this.jTreeAbstractSyntax = new JTree ( this.treeModel ) ;
    this.jTreeAbstractSyntax.setCellRenderer ( this.cellRenderer ) ;
    this.jTreeAbstractSyntax.getSelectionModel ( ).addTreeSelectionListener (
        this.aSTTreeSelectionListener ) ;
    this.jTreeAbstractSyntax.setRowHeight ( 22 ) ;
    this.jTreeAbstractSyntax.addMouseListener ( this.aSTMouseListener ) ;
    this.jScrollPaneAbstractSyntax = new JScrollPane ( this.jTreeAbstractSyntax ) ;
    /*
     * this.jScrollPaneAbstractSyntax.setBorder ( new TitledBorder (
     * BorderFactory .createLineBorder ( Color.black , 1 ) , "" ,
     * TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
     * "SansSerif" , Font.PLAIN , 12 ) ) ) ;
     */
    // Button Expand
    this.jButtonExpand = new JButton ( this.resourceBundle
        .getString ( "expand" ) ) ;
    this.jButtonExpand.setMnemonic ( this.resourceBundle.getString (
        "expandMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonExpand.setActionCommand ( "expand" ) ;
    this.jButtonExpand.setFocusable ( false ) ;
    this.jButtonExpand.addActionListener ( this.aSTActionListener ) ;
    // Button ExpandAll
    this.jButtonExpandAll = new JButton ( this.resourceBundle
        .getString ( "expand_all" ) ) ;
    this.jButtonExpandAll.setMnemonic ( this.resourceBundle.getString (
        "expand_allMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonExpandAll.setActionCommand ( "expand_all" ) ;
    this.jButtonExpandAll.setFocusable ( false ) ;
    this.jButtonExpandAll.addActionListener ( this.aSTActionListener ) ;
    // Button CollapseAll
    this.jButtonCollapseAll = new JButton ( this.resourceBundle
        .getString ( "collapse_all" ) ) ;
    this.jButtonCollapseAll.setMnemonic ( this.resourceBundle.getString (
        "collapse_allMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonCollapseAll.setActionCommand ( "collapse_all" ) ;
    this.jButtonCollapseAll.setFocusable ( false ) ;
    this.jButtonCollapseAll.addActionListener ( this.aSTActionListener ) ;
    // Button Collapse
    this.jButtonCollapse = new JButton ( this.resourceBundle
        .getString ( "collapse" ) ) ;
    this.jButtonCollapse.setMnemonic ( this.resourceBundle.getString (
        "collapseMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonCollapse.setActionCommand ( "collapse" ) ;
    this.jButtonCollapse.setFocusable ( false ) ;
    this.jButtonCollapse.addActionListener ( this.aSTActionListener ) ;
    // Button Close
    this.jButtonClose = new JButton ( this.resourceBundle.getString ( "close" ) ) ;
    this.jButtonClose.setMnemonic ( this.resourceBundle.getString (
        "closeMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonClose.setActionCommand ( "close" ) ;
    this.jButtonClose.setFocusable ( false ) ;
    this.jButtonClose.addActionListener ( this.aSTActionListener ) ;
    // Button Close All
    this.jButtonCloseAll = new JButton ( this.resourceBundle
        .getString ( "close_all" ) ) ;
    this.jButtonCloseAll.setMnemonic ( this.resourceBundle.getString (
        "close_allMnemonic" ).charAt ( 0 ) ) ;
    this.jButtonCloseAll.setActionCommand ( "close_all" ) ;
    this.jButtonCloseAll.setFocusable ( false ) ;
    this.jButtonCloseAll.addActionListener ( this.aSTActionListener ) ;
    // CheckBox Selected
    this.jCheckBoxSelection = new JCheckBox ( this.resourceBundle
        .getString ( "selection" ) ) ;
    this.jCheckBoxSelection.setMnemonic ( this.resourceBundle.getString (
        "selectionMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxSelection.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isSelected ( ) ) ;
    this.jCheckBoxSelection.setFocusable ( false ) ;
    this.jCheckBoxSelection.addItemListener ( this.aSTItemListener ) ;
    // CheckBox Bindings
    this.jCheckBoxBinding = new JCheckBox ( this.resourceBundle
        .getString ( "binding" ) ) ;
    this.jCheckBoxBinding.setMnemonic ( this.resourceBundle.getString (
        "bindingMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxBinding.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isBindings ( ) ) ;
    this.jCheckBoxBinding.setFocusable ( false ) ;
    this.jCheckBoxBinding.addItemListener ( this.aSTItemListener ) ;
    // CheckBox Replace
    this.jCheckBoxReplace = new JCheckBox ( this.resourceBundle
        .getString ( "replace" ) ) ;
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isReplace ( ) ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
    this.jCheckBoxReplace.addItemListener ( this.aSTItemListener ) ;
    // Panel Preferences
    this.jPanelPreferences = new JPanel ( ) ;
    this.jPanelPreferences.setLayout ( this.gridBagLayout ) ;
    this.jPanelPreferences.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
    // CheckBox Selected
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxSelection ,
        this.gridBagConstraints ) ;
    // CheckBox Bindings
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxBinding ,
        this.gridBagConstraints ) ;
    // CheckBox Replace
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jCheckBoxReplace ,
        this.gridBagConstraints ) ;
    // Button Expand
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jButtonExpand , this.gridBagConstraints ) ;
    // Button ExpandAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jButtonExpandAll ,
        this.gridBagConstraints ) ;
    // Button Collapse
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences
        .add ( this.jButtonCollapse , this.gridBagConstraints ) ;
    // Button CollapseAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jButtonCollapseAll ,
        this.gridBagConstraints ) ;
    // Button Close
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 4 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences.add ( this.jButtonClose , this.gridBagConstraints ) ;
    // Button CloseAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 4 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelPreferences
        .add ( this.jButtonCloseAll , this.gridBagConstraints ) ;
    // Panel Main
    this.jPanelMain = new JPanel ( ) ;
    this.jPanelMain.setLayout ( this.gridBagLayout ) ;
    // ScrollPane AbstractSyntax
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 0 , 0 , 1 , 0 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelMain.add ( this.jScrollPaneAbstractSyntax ,
        this.gridBagConstraints ) ;
    // Panel Preferences
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    insets.set ( 1 , 0 , 0 , 1 ) ;
    this.gridBagConstraints.insets = insets ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 0 ;
    this.jPanelMain.add ( this.jPanelPreferences , this.gridBagConstraints ) ;
  }


  public void appendNode ( DefaultMutableTreeNode pChild ,
      DefaultMutableTreeNode pParent )
  {
    this.treeModel
        .insertNodeInto ( pChild , pParent , pParent.getChildCount ( ) ) ;
  }


  public void expandRow ( int pIndex )
  {
    this.jTreeAbstractSyntax.expandRow ( pIndex ) ;
  }


  public AbstractSyntaxTree getAbstractSyntaxTree ( )
  {
    return this.abstractSyntaxTree ;
  }


  public ASTActionListener getASTActionListener ( )
  {
    return this.aSTActionListener ;
  }


  public ASTItemListener getASTItemListener ( )
  {
    return this.aSTItemListener ;
  }


  public ASTMouseListener getASTMouseListener ( )
  {
    return this.aSTMouseListener ;
  }


  public ASTTreeSelectionListener getASTTreeSelectionListener ( )
  {
    return this.aSTTreeSelectionListener ;
  }


  public JTree getJAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntax ;
  }


  public JButton getJButtonClose ( )
  {
    return this.jButtonClose ;
  }


  public JButton getJButtonCloseAll ( )
  {
    return this.jButtonCloseAll ;
  }


  public JButton getJButtonCollapse ( )
  {
    return this.jButtonCollapse ;
  }


  public JButton getJButtonCollapseAll ( )
  {
    return this.jButtonCollapseAll ;
  }


  public JButton getJButtonExpand ( )
  {
    return this.jButtonExpand ;
  }


  public JButton getJButtonExpandAll ( )
  {
    return this.jButtonExpandAll ;
  }


  public JCheckBox getJCheckBoxBinding ( )
  {
    return this.jCheckBoxBinding ;
  }


  public JCheckBox getJCheckBoxReplace ( )
  {
    return this.jCheckBoxReplace ;
  }


  public JCheckBox getJCheckBoxSelection ( )
  {
    return this.jCheckBoxSelection ;
  }


  /*
   * public JFrame getJFrameAbstractSyntaxTree ( ) { return
   * this.jFrameAbstractSyntaxTree ; }
   */
  public JCheckBoxMenuItem getJMenuItemBinding ( )
  {
    return this.jMenuItemBinding ;
  }


  public JMenuItem getJMenuItemClose ( )
  {
    return this.jMenuItemClose ;
  }


  public JMenuItem getJMenuItemCloseAll ( )
  {
    return this.jMenuItemCloseAll ;
  }


  public JMenuItem getJMenuItemCollapse ( )
  {
    return this.jMenuItemCollapse ;
  }


  public JMenuItem getJMenuItemCollapseAll ( )
  {
    return this.jMenuItemCollapseAll ;
  }


  public JMenuItem getJMenuItemExpand ( )
  {
    return this.jMenuItemExpand ;
  }


  public JMenuItem getJMenuItemExpandAll ( )
  {
    return this.jMenuItemExpandAll ;
  }


  public JCheckBoxMenuItem getJMenuItemReplace ( )
  {
    return this.jMenuItemReplace ;
  }


  public JCheckBoxMenuItem getJMenuItemSelection ( )
  {
    return this.jMenuItemSelection ;
  }


  public JPopupMenu getJPopupMenu ( )
  {
    return this.jPopupMenu ;
  }


  public DefaultTreeModel getTreeModel ( )
  {
    return this.treeModel ;
  }


  public void nodeChanged ( DefaultMutableTreeNode pNode )
  {
    this.treeModel.nodeChanged ( pNode ) ;
  }


  public void setRootNode ( DefaultMutableTreeNode pRootNode )
  {
    this.rootNode = pRootNode ;
    this.treeModel.setRoot ( this.rootNode ) ;
  }


  /*
   * public void setVisible ( boolean pVisible ) {
   * this.jFrameAbstractSyntaxTree.setVisible ( pVisible ) ; }
   */
  public JPanel getJPanelMain ( )
  {
    return this.jPanelMain ;
  }
}
