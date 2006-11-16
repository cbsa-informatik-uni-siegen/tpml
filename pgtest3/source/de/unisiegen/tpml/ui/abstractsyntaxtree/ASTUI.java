package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
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


  private JPanel jPanelCenter ;


  private JPanel jPanelSouth ;


  private JButton jButtonExpand ;


  private JButton jButtonCollapse ;


  private JButton jButtonExpandAll ;


  private JButton jButtonClose ;


  private JButton jButtonCloseAll ;


  private JButton jButtonCollapseAll ;


  private JCheckBox jCheckBoxReplace ;


  private JCheckBox jCheckBoxBindings ;


  private ASTTreeSelectionListener aSTTreeSelectionListener ;


  private ASTActionListener aSTActionListener ;


  private ASTItemListener aSTItemListener ;


  public ASTUI ( )
  {
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
    // Button
    this.jButtonClose = new JButton ( "close" ) ;
    this.jButtonClose.setActionCommand ( "close" ) ;
    this.jButtonClose.setFocusable ( false ) ;
    this.jButtonClose.addActionListener ( this.aSTActionListener ) ;
    this.jButtonCloseAll = new JButton ( "close all" ) ;
    this.jButtonCloseAll.setActionCommand ( "close_all" ) ;
    this.jButtonCloseAll.setFocusable ( false ) ;
    this.jButtonCloseAll.addActionListener ( this.aSTActionListener ) ;
    this.jButtonCollapseAll = new JButton ( "collapse all" ) ;
    this.jButtonCollapseAll.setActionCommand ( "collapse_all" ) ;
    this.jButtonCollapseAll.setFocusable ( false ) ;
    this.jButtonCollapseAll.addActionListener ( this.aSTActionListener ) ;
    this.jButtonCollapse = new JButton ( "collapse" ) ;
    this.jButtonCollapse.setActionCommand ( "collapse" ) ;
    this.jButtonCollapse.setFocusable ( false ) ;
    this.jButtonCollapse.addActionListener ( this.aSTActionListener ) ;
    this.jButtonExpand = new JButton ( "expand" ) ;
    this.jButtonExpand.setActionCommand ( "expand" ) ;
    this.jButtonExpand.setFocusable ( false ) ;
    this.jButtonExpand.addActionListener ( this.aSTActionListener ) ;
    this.jButtonExpandAll = new JButton ( "expand all" ) ;
    this.jButtonExpandAll.setActionCommand ( "expand_all" ) ;
    this.jButtonExpandAll.setFocusable ( false ) ;
    this.jButtonExpandAll.addActionListener ( this.aSTActionListener ) ;
    // CheckBox
    this.jCheckBoxReplace = new JCheckBox ( "replace expressions" ) ;
    this.jCheckBoxReplace.setSelected ( true ) ;
    this.jCheckBoxReplace.setFocusable ( false ) ;
    this.jCheckBoxReplace.addItemListener ( this.aSTItemListener ) ;
    this.jCheckBoxBindings = new JCheckBox ( "show bindings" ) ;
    this.jCheckBoxBindings.setSelected ( true ) ;
    this.jCheckBoxBindings.setFocusable ( false ) ;
    this.jCheckBoxBindings.addItemListener ( this.aSTItemListener ) ;
    // Panel
    this.jPanelCenter = new JPanel ( ) ;
    this.jPanelCenter.setLayout ( this.gridBagLayout ) ;
    this.jPanelCenter.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelCenter.add ( this.jScrollPaneAbstractSyntax ,
        this.gridBagConstraints ) ;
    this.jPanelSouth = new JPanel ( ) ;
    this.jPanelSouth.setLayout ( this.gridBagLayout ) ;
    this.jPanelSouth.setBorder ( new TitledBorder ( BorderFactory
        .createLineBorder ( Color.black , 1 ) , "" ,
        TitledBorder.DEFAULT_JUSTIFICATION , TitledBorder.TOP , new Font (
            "SansSerif" , Font.PLAIN , 12 ) ) ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jCheckBoxBindings , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jCheckBoxReplace , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonClose , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 1 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCloseAll , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCollapse , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 2 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonCollapseAll , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonExpand , this.gridBagConstraints ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 3 ;
    this.gridBagConstraints.gridy = 1 ;
    this.gridBagConstraints.weightx = 0 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jButtonExpandAll , this.gridBagConstraints ) ;
    // Frame
    this.jFrameAbstractSyntaxTree = new JFrame ( ) ;
    this.jFrameAbstractSyntaxTree.setLayout ( this.gridBagLayout ) ;
    this.jFrameAbstractSyntaxTree.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameAbstractSyntaxTree.setSize ( 600 , 450 ) ;
    this.jFrameAbstractSyntaxTree.setLocation ( 100 , 0 ) ;
    this.jFrameAbstractSyntaxTree
        .setDefaultCloseOperation ( WindowConstants.HIDE_ON_CLOSE ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.insets = new Insets ( 4 , 4 , 4 , 4 ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add ( this.jPanelCenter ,
        this.gridBagConstraints ) ;
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
