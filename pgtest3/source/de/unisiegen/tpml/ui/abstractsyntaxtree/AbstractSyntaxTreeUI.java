package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import javax.swing.JButton ;
import javax.swing.JCheckBox ;
import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.WindowConstants ;
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


  private JPanel jPanelSouth ;


  private JButton jButtonExpand ;


  private JButton jButtonClose ;


  private JButton jButtonHide ;


  private JCheckBox jCheckBoxReplace ;


  private AbstractSyntaxTreeListener abstractSyntaxTreeListener ;


  private BorderLayout borderLayout ;


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
    this.abstractSyntaxTreeListener = new AbstractSyntaxTreeListener ( this ) ;
    this.jTreeAbstractSyntax.getSelectionModel ( ).addTreeSelectionListener (
        this.abstractSyntaxTreeListener ) ;
    // Tree - ScrollPane
    this.jScrollPaneAbstractSyntax = new JScrollPane ( this.jTreeAbstractSyntax ) ;
    // GridBagLayout
    this.gridBagLayout = new GridBagLayout ( ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    // BorderLayout
    this.borderLayout = new BorderLayout ( ) ;
    // Buttons
    this.jButtonClose = new JButton ( "close all" ) ;
    this.jButtonClose.addActionListener ( new ActionListener ( )
    {
      public void actionPerformed ( ActionEvent e )
      {
        close ( ) ;
      }
    } ) ;
    this.jButtonHide = new JButton ( "hide all" ) ;
    this.jButtonHide.addActionListener ( new ActionListener ( )
    {
      public void actionPerformed ( ActionEvent e )
      {
        hide ( ) ;
      }
    } ) ;
    this.jButtonExpand = new JButton ( "expand" ) ;
    this.jButtonExpand.addActionListener ( new ActionListener ( )
    {
      public void actionPerformed ( ActionEvent e )
      {
        expand ( ) ;
      }
    } ) ;
    // CheckBox
    this.jCheckBoxReplace = new JCheckBox ( "Replace Expression" , false ) ;
    this.jCheckBoxReplace.addItemListener ( new ItemListener ( )
    {
      public void itemStateChanged ( ItemEvent e )
      {
        AbstractSyntaxTreeNode
            .setReplaceGeneral ( e.getStateChange ( ) == ItemEvent.SELECTED ) ;
        abstractSyntaxTreeListener
            .update ( AbstractSyntaxTreeUI.this.jTreeAbstractSyntax
                .getSelectionPath ( ) ) ;
      }
    } ) ;
    // Panel
    this.jPanelSouth = new JPanel ( ) ;
    this.jPanelSouth.add ( this.jCheckBoxReplace ) ;
    this.jPanelSouth.add ( this.jButtonClose ) ;
    this.jPanelSouth.add ( this.jButtonHide ) ;
    this.jPanelSouth.add ( this.jButtonExpand ) ;
    // Frame
    this.jFrameAbstractSyntaxTree = new JFrame ( ) ;
    // this.jFrameAbstractSyntaxTree.setLayout ( this.gridBagLayout ) ;
    this.jFrameAbstractSyntaxTree.setLayout ( this.borderLayout ) ;
    this.jFrameAbstractSyntaxTree.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameAbstractSyntaxTree.setSize ( 600 , 450 ) ;
    this.jFrameAbstractSyntaxTree.setLocation ( 100 , 0 ) ;
    this.jFrameAbstractSyntaxTree
        .setDefaultCloseOperation ( WindowConstants.HIDE_ON_CLOSE ) ;
    // Build
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jPanelSouth.add ( this.jScrollPaneAbstractSyntax ) ;
    // this.jFrameAbstractSyntaxTree.getContentPane ( ).add
    // (this.jScrollPaneAbstractSyntax , this.gridBagConstraints ) ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add (
        this.jScrollPaneAbstractSyntax , BorderLayout.CENTER ) ;
    this.jFrameAbstractSyntaxTree.getContentPane ( ).add ( this.jPanelSouth ,
        BorderLayout.SOUTH ) ;
  }


  public void appendNode ( DefaultMutableTreeNode pChild ,
      DefaultMutableTreeNode pParent )
  {
    this.treeModel
        .insertNodeInto ( pChild , pParent , pParent.getChildCount ( ) ) ;
  }


  public void close ( )
  {
    for ( int i = this.jTreeAbstractSyntax.getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.jTreeAbstractSyntax.collapseRow ( i ) ;
    }
  }


  public void expand ( )
  {
    // TODO dummerweise klappt er alles auf, was unter der Selection ist, nicht
    // nur den Unterbaum :(
    int start = 0 ;
    try
    {
      start = this.jTreeAbstractSyntax.getSelectionRows ( ) [ 0 ] ;
    }
    catch ( Exception e )
    {
      start = 0 ;
    }
    // this.jTreeAbstractSyntax.expa
    while ( start < this.jTreeAbstractSyntax.getRowCount ( ) )
    {
      this.jTreeAbstractSyntax.expandRow ( start ) ;
      start ++ ;
    }
    // TreePath path = this.jTreeAbstractSyntax.getSelectionPath();
    // int pathCount = path.get();
    // for (int i = 0 ; i < path.getPathCount(); i++)
    // {
    // path.(i);
    // }
    // this.jTreeAbstractSyntax.expandPath(path);
    // int [] selection = this.jTreeAbstractSyntax.getSelectionRows();
    // for (int i = 0 ; i < selection.length ; i++)
    // {
    // System.out.println("Row: "+i);
    // this.jTreeAbstractSyntax.expandRow(i);
    // }
  }


  public void expandRow ( int pIndex )
  {
    this.jTreeAbstractSyntax.expandRow ( pIndex ) ;
  }


  public JTree getJAbstractSyntaxTree ( )
  {
    return this.jTreeAbstractSyntax ;
  }


  public void hide ( )
  {
    this.jTreeAbstractSyntax.collapseRow ( 0 ) ;
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
