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
import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.WindowConstants ;
import javax.swing.border.TitledBorder ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import javax.swing.tree.DefaultTreeModel ;


public class ASTUI
{
  private JFrame jFrameAbstractSyntaxTree ;


  private GridBagConstraints gridBagConstraints ;


  private GridBagLayout gridBagLayout ;


  private JTree jTreeAbstractSyntax ;


  private DefaultMutableTreeNode rootNode ;


  private DefaultTreeModel treeModel ;


  private DefaultTreeCellRenderer cellRenderer ;


  private JScrollPane jScrollPaneAbstractSyntax ;


  private JPanel jPanelSouth ;


  private JButton jButtonExpand ;


  private JButton jButtonCollapse ;


  private JButton jButtonExpandAll ;


  private JButton jButtonClose ;


  private JButton jButtonCloseAll ;


  private JButton jButtonCollapseAll ;


  private JCheckBox jCheckBoxReplace ;


  private JCheckBox jCheckBoxBindings ;


  private JCheckBox jCheckBoxSelected ;


  private ASTTreeSelectionListener aSTTreeSelectionListener ;


  private ASTActionListener aSTActionListener ;


  private ASTItemListener aSTItemListener ;


  private AbstractSyntaxTree abstractSyntaxTree ;


  private ResourceBundle resourceBundle ;


  public ASTUI ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.resourceBundle = ResourceBundle
        .getBundle ( "de/unisiegen/tpml/ui/abstractsyntaxtree/ast" ) ;
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    // Listener
    this.aSTItemListener = new ASTItemListener ( this ) ;
    this.aSTActionListener = new ASTActionListener ( this ) ;
    this.aSTTreeSelectionListener = new ASTTreeSelectionListener ( this ) ;
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
    this.jScrollPaneAbstractSyntax = new JScrollPane ( this.jTreeAbstractSyntax ) ;
    this.jScrollPaneAbstractSyntax.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
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
    this.jCheckBoxSelected = new JCheckBox ( this.resourceBundle
        .getString ( "selected" ) ) ;
    this.jCheckBoxSelected.setMnemonic ( this.resourceBundle.getString (
        "selectedMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxSelected.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isCheckedSelected ( ) ) ;
    this.jCheckBoxSelected.setFocusable ( false ) ;
    this.jCheckBoxSelected.addItemListener ( this.aSTItemListener ) ;
    // CheckBox Bindings
    this.jCheckBoxBindings = new JCheckBox ( this.resourceBundle
        .getString ( "bindings" ) ) ;
    this.jCheckBoxBindings.setMnemonic ( this.resourceBundle.getString (
        "bindingsMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxBindings.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isCheckedBindings ( ) ) ;
    this.jCheckBoxBindings.setFocusable ( false ) ;
    this.jCheckBoxBindings.addItemListener ( this.aSTItemListener ) ;
    // CheckBox Replace
    this.jCheckBoxReplace = new JCheckBox ( this.resourceBundle
        .getString ( "replace" ) ) ;
    this.jCheckBoxReplace.setMnemonic ( this.resourceBundle.getString (
        "replaceMnemonic" ).charAt ( 0 ) ) ;
    this.jCheckBoxReplace.setSelected ( this.abstractSyntaxTree
        .getASTPreferences ( ).isCheckedReplace ( ) ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
    this.jCheckBoxReplace.addItemListener ( this.aSTItemListener ) ;
    // ScrollPane AbstractSyntax
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    // Panel South
    this.jPanelSouth = new JPanel ( ) ;
    this.jPanelSouth.setLayout ( this.gridBagLayout ) ;
    this.jPanelSouth.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
    // CheckBox Selected
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jCheckBoxSelected , this.gridBagConstraints ) ;
    // CheckBox Bindings
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jCheckBoxBindings , this.gridBagConstraints ) ;
    // CheckBox Replace
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 2 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jCheckBoxReplace , this.gridBagConstraints ) ;
    // Button Expand
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonExpand , this.gridBagConstraints ) ;
    // Button ExpandAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonExpandAll , this.gridBagConstraints ) ;
    // Button Collapse
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCollapse , this.gridBagConstraints ) ;
    // Button CollapseAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCollapseAll , this.gridBagConstraints ) ;
    // Button Close
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 2 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonClose , this.gridBagConstraints ) ;
    // Button CloseAll
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 2 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCloseAll , this.gridBagConstraints ) ;
    // Frame
    this.jFrameAbstractSyntaxTree = new JFrame ( ) ;
    this.jFrameAbstractSyntaxTree.setLayout ( this.gridBagLayout ) ;
    this.jFrameAbstractSyntaxTree.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameAbstractSyntaxTree.setSize ( 600 , 450 ) ;
    this.jFrameAbstractSyntaxTree.setLocation ( 100 , 0 ) ;
    this.jFrameAbstractSyntaxTree.setIconImage ( null ) ;
    this.jFrameAbstractSyntaxTree
        .setDefaultCloseOperation ( WindowConstants.HIDE_ON_CLOSE ) ;
    // ScrollPane AbstractSyntax
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add (
        this.jScrollPaneAbstractSyntax , this.gridBagConstraints ) ;
    // Panel South
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 0 ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add ( this.jPanelSouth ,
        this.gridBagConstraints ) ;
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


  public ASTTreeSelectionListener getASTTreeSelectionListener ( )
  {
    return this.aSTTreeSelectionListener ;
  }


  public JTree getJAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntax ;
  }


  public JCheckBox getJCheckBoxBindings ( )
  {
    return this.jCheckBoxBindings ;
  }


  public JCheckBox getJCheckBoxReplace ( )
  {
    return this.jCheckBoxReplace ;
  }


  public JCheckBox getJCheckBoxSelected ( )
  {
    return this.jCheckBoxSelected ;
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


  public void setVisible ( boolean pVisible )
  {
    this.jFrameAbstractSyntaxTree.setVisible ( pVisible ) ;
  }
}
