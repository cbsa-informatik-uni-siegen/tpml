package de.unisiegen.tpml.ui ;


import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import javax.swing.JFrame ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import javax.swing.tree.DefaultTreeModel ;


public class AbstractSyntaxTreeUI
{
  private JFrame jFrameAbstractSyntaxTree ;


  private GridBagConstraints gridBagConstraints ;


  private GridBagLayout gridBagLayout ;


  private JTree jTreeAbstractSyntax ;


  private DefaultMutableTreeNode rootNode ;


  private DefaultTreeModel treeModel ;


  private DefaultTreeCellRenderer cellRenderer ;


  private JScrollPane jScrollPaneAbstractSyntax ;


  public AbstractSyntaxTreeUI ( )
  {
    // Tree - Renderer
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
    // Tree - TreeModel
    this.treeModel = new DefaultTreeModel ( this.rootNode ) ;
    // Tree
    this.jTreeAbstractSyntax = new JTree ( this.treeModel ) ;
    this.jTreeAbstractSyntax.setCellRenderer ( this.cellRenderer ) ;
    this.jTreeAbstractSyntax.getSelectionModel ( ).addTreeSelectionListener (
        new AbstractSyntaxTreeListener ( this ) ) ;
    // Tree - ScrollPane
    this.jScrollPaneAbstractSyntax = new JScrollPane ( this.jTreeAbstractSyntax ) ;
    // GridBagLayout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // Frame
    this.jFrameAbstractSyntaxTree = new JFrame ( ) ;
    this.jFrameAbstractSyntaxTree.setLayout ( this.gridBagLayout ) ;
    this.jFrameAbstractSyntaxTree.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameAbstractSyntaxTree.setSize ( 600 , 450 ) ;
    this.jFrameAbstractSyntaxTree.setLocation ( 100 , 0 ) ;
    this.jFrameAbstractSyntaxTree
        .setDefaultCloseOperation ( JFrame.HIDE_ON_CLOSE ) ;
    // Build
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add (
        this.jScrollPaneAbstractSyntax , this.gridBagConstraints ) ;
  }


  public JTree getJAbstractSyntaxTree ( )
  {
    return jTreeAbstractSyntax ;
  }


  public void nodeChanged ( DefaultMutableTreeNode pNode )
  {
    this.treeModel.nodeChanged ( pNode ) ;
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
