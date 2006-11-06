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
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.RelationalOperator ;


public class AbstractSyntaxTree
{
  private JFrame jFrameTest ;


  private GridBagConstraints gridBagConstraints ;


  private JTree jTreeSyntax ;


  private DefaultMutableTreeNode root ;


  private DefaultTreeModel treeModel ;


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


  private DefaultMutableTreeNode syntaxTree ( Expression pExpression )
  {
    if ( pExpression instanceof Application )
    {
      return application ( ( Application ) pExpression ) ;
    }
    else if ( pExpression instanceof Let )
    {
      return let ( ( Let ) pExpression ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      return lambda ( ( Lambda ) pExpression ) ;
    }
    else if ( pExpression instanceof InfixOperation )
    {
      return infixOperation ( ( InfixOperation ) pExpression ) ;
    }
    else if ( pExpression instanceof Condition )
    {
      return condition ( ( Condition ) pExpression ) ;
    }
    else if ( pExpression instanceof Identifier )
    {
      return identifier ( ( Identifier ) pExpression ) ;
    }
    else if ( pExpression instanceof IntegerConstant )
    {
      return integerConstant ( ( IntegerConstant ) pExpression ) ;
    }
    return new DefaultMutableTreeNode ( "UNKNOWN" ) ;
  }


  private DefaultMutableTreeNode application ( Application pApplication )
  {
    Expression e1 = pApplication.getE1 ( ) ;
    Expression e2 = pApplication.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "Application { " + pApplication.toPrettyString ( ) + " }" ) ;
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


  private DefaultMutableTreeNode let ( Let pLet )
  {
    String id = pLet.getId ( ) ;
    Expression e1 = pLet.getE1 ( ) ;
    Expression e2 = pLet.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Let { "
        + pLet.toPrettyString ( ) + " }" ) ;
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


  private DefaultMutableTreeNode lambda ( Lambda pLambda )
  {
    /*
     * String id = pLambda.getId ( ) ; Expression e = pLambda.getE ( ) ; Object []
     * a = pLambda.free ( ).toArray ( ) ; Object [] b = e.free ( ).toArray ( ) ;
     * for ( int i = 0 ; i< a.length ; i++) { System.err.println ( "Free
     * Lambda:" + a[i] ) ; } for ( int i = 0 ; i< b.length ; i++) {
     * System.err.println ( "Free e:" + b[i] ) ; }
     */
    String id = pLambda.getId ( ) ;
    Expression e = pLambda.getE ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Lamdba { "
        + pLambda.toPrettyString ( ) + " }" ) ;
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


  private DefaultMutableTreeNode infixOperation ( InfixOperation pInfixOperation )
  {
    Expression e1 = pInfixOperation.getE1 ( ) ;
    Expression e2 = pInfixOperation.getE2 ( ) ;
    BinaryOperator b = pInfixOperation.getOp ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "InfixOperation { " + pInfixOperation.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( syntaxTree ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    String text ;
    text = "BinaryOperator { " + b.toPrettyString ( ) + " }" ;
    if ( b instanceof ArithmeticOperator )
    {
      text = "ArithmeticOperator { " + b.toPrettyString ( ) + " }" ;
    }
    else if ( b instanceof RelationalOperator )
    {
      text = "RelationalOperator { " + b.toPrettyString ( ) + " }" ;
    }
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( text ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( syntaxTree ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode condition ( Condition pCondition )
  {
    Expression e0 = pCondition.getE0 ( ) ;
    Expression e1 = pCondition.getE1 ( ) ;
    Expression e2 = pCondition.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Condition { "
        + pCondition.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e0 { "
        + e0.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( syntaxTree ( e0 ) , subchild1 , 0 ) ;
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


  private DefaultMutableTreeNode identifier ( Identifier pIdentifier )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Identifier { "
        + pIdentifier.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode integerConstant (
      IntegerConstant pIntegerConstant )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "IntegerConstant { " + pIntegerConstant.toPrettyString ( ) + " }" ) ;
    return child ;
  }
}
