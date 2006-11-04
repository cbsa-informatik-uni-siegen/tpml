package de.unisiegen.tpml.ui ;


import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import javax.swing.JFrame ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.MutableTreeNode ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.Value ;


public class AbstractSyntaxTree
{
  private JFrame jFrameTest ;


  private GridBagConstraints gridBagConstraints ;


  private JTree jTreeSyntax ;


  private DefaultMutableTreeNode root ;


  private DefaultTreeModel treeModel ;


  private DefaultMutableTreeNode syntaxTree ( Expression pExpression )
  {
    if ( pExpression instanceof Application )
    {
      Application a = ( Application ) pExpression ;
      Expression e1 = a.getE1 ( ) ;
      Expression e2 = a.getE2 ( ) ;
      // Child
      DefaultMutableTreeNode child = new DefaultMutableTreeNode (
          "Application { " + pExpression.toPrettyString ( ) + " }" ) ;
      // Subchild 1
      DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
          + e1.toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
      treeModel.insertNodeInto ( syntaxTree ( e1 ) , subchild1 , 0 ) ;
      // Subchild 2
      DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
          + e2.toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
      treeModel.insertNodeInto ( syntaxTree ( e2 ) , subchild2 , 0 ) ;
      return child ;
    }
    else if ( pExpression instanceof Let )
    {
      Let l = ( Let ) pExpression ;
      String id = l.getId ( ) ;
      Expression e1 = l.getE1 ( ) ;
      Expression e2 = l.getE2 ( ) ;
      // Child
      DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Let { "
          + l.toPrettyString ( ) + " }" ) ;
      // Subchild 1
      DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
          "Identifier { " + id + " }" ) ;
      treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
      // Subchild 2
      DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
          + e1.toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
      treeModel.insertNodeInto ( syntaxTree ( e1 ) , subchild2 , 0 ) ;
      // Subchild 3
      DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
          + e2.toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
      treeModel.insertNodeInto ( syntaxTree ( e2 ) , subchild3 , 0 ) ;
      return child ;
    }
    else if ( pExpression instanceof Lambda )
    {
      Lambda l = ( Lambda ) pExpression ;
      String id = l.getId ( ) ;
      Expression e = l.getE ( ) ;
      // Child
      DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Lamdba { "
          + pExpression.toPrettyString ( ) + " }" ) ;
      // Subchild 1
      DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
          "Identifier { " + id + " }" ) ;
      treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
      // Subchild 2
      DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e { "
          + e.toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
      treeModel.insertNodeInto ( syntaxTree ( e ) , subchild2 , 0 ) ;
      return child ;
    }
    else if ( pExpression instanceof Identifier )
    {
      Identifier i = ( Identifier ) pExpression ;
      // Child
      String text = "Identifier { " + i.toPrettyString ( ) + " }" ;
      DefaultMutableTreeNode child = new DefaultMutableTreeNode ( text ) ;
      return child ;
    }
    else if ( pExpression instanceof Value )
    {
      Value v = ( Value ) pExpression ;
      // Child
      String text = "Value { " + v.toPrettyString ( ) + " }" ;
      DefaultMutableTreeNode child = new DefaultMutableTreeNode ( text ) ;
      return child ;
    }
    return new DefaultMutableTreeNode ( "UNKNOWN" ) ;
  }


  private static AbstractSyntaxTree abstractSyntax = null ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntax == null )
    {
      abstractSyntax = new AbstractSyntaxTree ( ) ;
      return abstractSyntax ;
    }
    else
    {
      return abstractSyntax ;
    }
  }


  public void setExpression ( Expression pExpression )
  {
    treeModel.setRoot ( new DefaultMutableTreeNode ( pExpression
        .toPrettyString ( ) ) ) ;
    treeModel.insertNodeInto ( syntaxTree ( pExpression ) ,
        ( MutableTreeNode ) treeModel.getRoot ( ) , 0 ) ;
    this.jTreeSyntax.expandRow ( 0 ) ;
  }


  private AbstractSyntaxTree ( )
  {
    this.root = new DefaultMutableTreeNode ( "root" ) ;
    this.treeModel = new DefaultTreeModel ( this.root ) ;
    this.jTreeSyntax = new JTree ( this.treeModel ) ;
    this.gridBagConstraints = new GridBagConstraints ( ) ;
    this.gridBagConstraints.insets = new Insets ( 2 , 2 , 2 , 2 ) ;
    this.gridBagConstraints.fill = GridBagConstraints.BOTH ;
    this.jFrameTest = new JFrame ( ) ;
    this.jFrameTest.setLayout ( new GridBagLayout ( ) ) ;
    this.jFrameTest.setTitle ( "AbstractSyntaxTree" ) ;
    this.jFrameTest.setSize ( 600 , 450 ) ;
    this.jFrameTest.setLocation ( 50 , 50 ) ;
    this.jFrameTest.setDefaultCloseOperation ( JFrame.HIDE_ON_CLOSE ) ;
    this.gridBagConstraints.gridx = 0 ;
    this.gridBagConstraints.gridy = 0 ;
    this.gridBagConstraints.weightx = 10 ;
    this.gridBagConstraints.weighty = 10 ;
    this.jFrameTest.getContentPane ( ).add ( this.jTreeSyntax ,
        this.gridBagConstraints ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.jFrameTest.setVisible ( pVisible ) ;
  }
}
