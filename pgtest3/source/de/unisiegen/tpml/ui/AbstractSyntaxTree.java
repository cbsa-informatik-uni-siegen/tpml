package de.unisiegen.tpml.ui ;


import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import javax.swing.JFrame ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.MutableTreeNode ;
import de.unisiegen.tpml.core.expressions.And ;
import de.unisiegen.tpml.core.expressions.Application ;
import de.unisiegen.tpml.core.expressions.ArithmeticOperator ;
import de.unisiegen.tpml.core.expressions.Assign ;
import de.unisiegen.tpml.core.expressions.BinaryCons ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.BooleanConstant ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.Condition1 ;
import de.unisiegen.tpml.core.expressions.Constant ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Deref ;
import de.unisiegen.tpml.core.expressions.EmptyList ;
import de.unisiegen.tpml.core.expressions.Exn ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Fst ;
import de.unisiegen.tpml.core.expressions.Hd ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.IntegerConstant ;
import de.unisiegen.tpml.core.expressions.IsEmpty ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.List ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Not ;
import de.unisiegen.tpml.core.expressions.Or ;
import de.unisiegen.tpml.core.expressions.Projection ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.expressions.RelationalOperator ;
import de.unisiegen.tpml.core.expressions.Sequence ;
import de.unisiegen.tpml.core.expressions.Snd ;
import de.unisiegen.tpml.core.expressions.Tl ;
import de.unisiegen.tpml.core.expressions.Tuple ;
import de.unisiegen.tpml.core.expressions.UnaryCons ;
import de.unisiegen.tpml.core.expressions.UnaryListOperator ;
import de.unisiegen.tpml.core.expressions.UnaryOperator ;
import de.unisiegen.tpml.core.expressions.UnitConstant ;
import de.unisiegen.tpml.core.expressions.Value ;
import de.unisiegen.tpml.core.expressions.While ;


public class AbstractSyntaxTree
{
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


  private JFrame jFrameTest ;


  private GridBagConstraints gridBagConstraints ;


  private JTree jTreeSyntax ;


  private DefaultMutableTreeNode root ;


  private DefaultTreeModel treeModel ;


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


  private DefaultMutableTreeNode exprAnd ( And pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "And { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprApplication ( Application pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "Application { " + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprArithmeticOperator (
      ArithmeticOperator pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "ArithmeticOperator { " + pExpr.toString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprAssign ( Assign pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Assign { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprBinaryCons ( BinaryCons pExpr )
  {
    return new DefaultMutableTreeNode ( "BinaryCons { "
        + pExpr.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode exprBinaryOperator ( BinaryOperator pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "BinaryOperator { " + pExpr.toString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprBooleanConstant ( BooleanConstant pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "BooleanConstant { " + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCondition ( Condition pExpr )
  {
    Expression e0 = pExpr.getE0 ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Condition { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e0 { "
        + e0.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e0 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCondition1 ( Condition1 pExpr )
  {
    Expression e0 = pExpr.getE0 ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Condition1 { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e0 { "
        + e0.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e0 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprConstant ( Constant pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Constant { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCurriedLet ( CurriedLet pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "CurriedLet { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode (
          "Identifier { " + id [ i ] + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
    }
    // Subchild
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , id.length ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , id.length + 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCurriedLetRec ( CurriedLetRec pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "CurriedLetRec { " + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode (
          "Identifier { " + id [ i ] + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
    }
    // Subchild
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , id.length ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , id.length + 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprDeref ( Deref pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Deref { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprEmptyList ( EmptyList pExpr )
  {
    return new DefaultMutableTreeNode ( "EmptyList { "
        + pExpr.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode exprExn ( Exn pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Exn { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprExpression ( Expression pExpr )
  {
    // Value - Constant - BinaryOperator - ArithmeticOperator
    if ( pExpr instanceof ArithmeticOperator )
    {
      return exprArithmeticOperator ( ( ArithmeticOperator ) pExpr ) ;
    }
    // Value - Constant - BinaryOperator - Assign
    else if ( pExpr instanceof Assign )
    {
      return exprAssign ( ( Assign ) pExpr ) ;
    }
    // Value - Constant - BinaryOperator - BinaryCons
    else if ( pExpr instanceof BinaryCons )
    {
      return exprBinaryCons ( ( BinaryCons ) pExpr ) ;
    }
    // Value - Constant - BinaryOperator - RelationalOperator
    else if ( pExpr instanceof RelationalOperator )
    {
      return exprRelationalOperator ( ( RelationalOperator ) pExpr ) ;
    }
    // Value - Constant - BinaryOperator
    else if ( pExpr instanceof BinaryOperator )
    {
      return exprBinaryOperator ( ( BinaryOperator ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - UnaryListOperator - Hd
    else if ( pExpr instanceof Hd )
    {
      return exprHd ( ( Hd ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - UnaryListOperator - Tl
    else if ( pExpr instanceof Tl )
    {
      return exprTl ( ( Tl ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - UnaryListOperator - IsEmpty
    else if ( pExpr instanceof IsEmpty )
    {
      return exprIsEmpty ( ( IsEmpty ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - UnaryListOperator
    else if ( pExpr instanceof UnaryListOperator )
    {
      return exprUnaryListOperator ( ( UnaryListOperator ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Projection - Fst
    else if ( pExpr instanceof Fst )
    {
      return exprFst ( ( Fst ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Projection - Snd
    else if ( pExpr instanceof Snd )
    {
      return exprSnd ( ( Snd ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Projection
    else if ( pExpr instanceof Projection )
    {
      return exprProjection ( ( Projection ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Ref
    else if ( pExpr instanceof Ref )
    {
      return exprRef ( ( Ref ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Deref
    else if ( pExpr instanceof Deref )
    {
      return exprDeref ( ( Deref ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator - Not
    else if ( pExpr instanceof Not )
    {
      return exprNot ( ( Not ) pExpr ) ;
    }
    // Value - Constant - UnaryOperator
    else if ( pExpr instanceof UnaryOperator )
    {
      return exprUnaryOperator ( ( UnaryOperator ) pExpr ) ;
    }
    // Value - Constant - UnaryCons
    else if ( pExpr instanceof UnaryCons )
    {
      return exprUnaryCons ( ( UnaryCons ) pExpr ) ;
    }
    // Value - Constant - IntegerConstant
    else if ( pExpr instanceof IntegerConstant )
    {
      return exprIntegerConstant ( ( IntegerConstant ) pExpr ) ;
    }
    // Value - Constant - BooleanConstant
    else if ( pExpr instanceof BooleanConstant )
    {
      return exprBooleanConstant ( ( BooleanConstant ) pExpr ) ;
    }
    // Value - Constant - UnitConstant
    else if ( pExpr instanceof UnitConstant )
    {
      return exprUnitConstant ( ( UnitConstant ) pExpr ) ;
    }
    // Value - Constant - EmptyList
    else if ( pExpr instanceof EmptyList )
    {
      return exprEmptyList ( ( EmptyList ) pExpr ) ;
    }
    // Value - Constant
    else if ( pExpr instanceof Constant )
    {
      return exprConstant ( ( Constant ) pExpr ) ;
    }
    // Value - Identifier
    else if ( pExpr instanceof Identifier )
    {
      return exprIdentifier ( ( Identifier ) pExpr ) ;
    }
    // Value - MultiLambda
    else if ( pExpr instanceof MultiLambda )
    {
      return exprLultiLambda ( ( MultiLambda ) pExpr ) ;
    }
    // Value - Lambda
    else if ( pExpr instanceof Lambda )
    {
      return exprLambda ( ( Lambda ) pExpr ) ;
    }
    // Value - Sequence
    else if ( pExpr instanceof Sequence )
    {
      return exprSequence ( ( Sequence ) pExpr ) ;
    }
    // Value - Location
    else if ( pExpr instanceof Location )
    {
      return exprLocation ( ( Location ) pExpr ) ;
    }
    // Value
    else if ( pExpr instanceof Value )
    {
      return exprValue ( ( Value ) pExpr ) ;
    }
    // Let - LetRec
    else if ( pExpr instanceof LetRec )
    {
      return exprLetRec ( ( LetRec ) pExpr ) ;
    }
    // Let
    else if ( pExpr instanceof Let )
    {
      return exprLet ( ( Let ) pExpr ) ;
    }
    // CurriedLetRec
    else if ( pExpr instanceof CurriedLetRec )
    {
      return exprCurriedLetRec ( ( CurriedLetRec ) pExpr ) ;
    }
    // CurriedLet
    else if ( pExpr instanceof CurriedLet )
    {
      return exprCurriedLet ( ( CurriedLet ) pExpr ) ;
    }
    // Application
    else if ( pExpr instanceof Application )
    {
      return exprApplication ( ( Application ) pExpr ) ;
    }
    // MultiLet
    else if ( pExpr instanceof MultiLet )
    {
      return exprMultiLet ( ( MultiLet ) pExpr ) ;
    }
    // Recursion
    else if ( pExpr instanceof Recursion )
    {
      return exprRecursion ( ( Recursion ) pExpr ) ;
    }
    // InfixOperation
    else if ( pExpr instanceof InfixOperation )
    {
      return exprInfixOperation ( ( InfixOperation ) pExpr ) ;
    }
    // Condition
    else if ( pExpr instanceof Condition )
    {
      return exprCondition ( ( Condition ) pExpr ) ;
    }
    // Condition1
    else if ( pExpr instanceof Condition1 )
    {
      return exprCondition1 ( ( Condition1 ) pExpr ) ;
    }
    // And
    else if ( pExpr instanceof And )
    {
      return exprAnd ( ( And ) pExpr ) ;
    }
    // Or
    else if ( pExpr instanceof Or )
    {
      return exprOr ( ( Or ) pExpr ) ;
    }
    // Tuple
    else if ( pExpr instanceof Tuple )
    {
      return exprTuple ( ( Tuple ) pExpr ) ;
    }
    // List
    else if ( pExpr instanceof List )
    {
      return exprList ( ( List ) pExpr ) ;
    }
    // While
    else if ( pExpr instanceof While )
    {
      return exprWhile ( ( While ) pExpr ) ;
    }
    // Exn
    else if ( pExpr instanceof Exn )
    {
      return exprExn ( ( Exn ) pExpr ) ;
    }
    return new DefaultMutableTreeNode ( "UNKNOWN EXPRESSION" ) ;
  }


  private DefaultMutableTreeNode exprFst ( Fst pExpr )
  {
    return new DefaultMutableTreeNode ( "Fst { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprHd ( Hd pExpr )
  {
    return new DefaultMutableTreeNode ( "Hd { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprIdentifier ( Identifier pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Identifier { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprInfixOperation ( InfixOperation pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    BinaryOperator b = pExpr.getOp ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "InfixOperation { " + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    String text ;
    text = "BinaryOperator { " + b.toString ( ) + " }" ;
    if ( b instanceof ArithmeticOperator )
    {
      text = "ArithmeticOperator { " + b.toString ( ) + " }" ;
    }
    else if ( b instanceof RelationalOperator )
    {
      text = "RelationalOperator { " + b.toString ( ) + " }" ;
    }
    else if ( b instanceof BinaryCons )
    {
      text = "BinaryCons { " + b.toString ( ) + " }" ;
    }
    else if ( b instanceof Assign )
    {
      text = "Assign { " + b.toString ( ) + " }" ;
    }
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( text ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprIntegerConstant ( IntegerConstant pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "IntegerConstant { " + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprIsEmpty ( IsEmpty pExpr )
  {
    return new DefaultMutableTreeNode ( "IsEmpty { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprLambda ( Lambda pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Lamdba { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e { "
        + e.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLet ( Let pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Let { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLetRec ( LetRec pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "LetRec { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild 3
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , 2 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprList ( List pExpr )
  {
    Expression [ ] e = pExpr.getExpressions ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "List { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode ( "e" + i
          + " { " + e [ i ].toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
      treeModel.insertNodeInto ( exprExpression ( e [ i ] ) , subchild , 0 ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode exprLocation ( Location pExpr )
  {
    String name = pExpr.getName ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Location { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "Name { "
        + name + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLultiLambda ( MultiLambda pExpr )
  {
    String id[] = pExpr.getIdentifiers ( ) ;
    Expression e = pExpr.getE ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "MultiLamdba { " + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
          "Identifier { " + id [ i ] + " }" ) ;
      treeModel.insertNodeInto ( subchild1 , child , i ) ;
    }
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e { "
        + e.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , id.length ) ;
    treeModel.insertNodeInto ( exprExpression ( e ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprMultiLet ( MultiLet pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "MultiLet { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode (
          "Identifier { " + id [ i ] + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
    }
    // Subchild
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , id.length ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild2 , 0 ) ;
    // Subchild
    DefaultMutableTreeNode subchild3 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild3 , child , id.length + 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild3 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprNot ( Not pExpr )
  {
    return new DefaultMutableTreeNode ( "Not { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprOr ( Or pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Or { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprProjection ( Projection pProjection )
  {
    return new DefaultMutableTreeNode ( "Projection { "
        + pProjection.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode exprRecursion ( Recursion pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Recursion { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode (
        "Identifier { " + id + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e { "
        + e.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprRef ( Ref pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Ref { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprRelationalOperator (
      RelationalOperator pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "RelationalOperator { " + pExpr.toString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprSequence ( Sequence pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Sequence { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprSnd ( Snd pExpr )
  {
    return new DefaultMutableTreeNode ( "Snd { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprTl ( Tl pExpr )
  {
    return new DefaultMutableTreeNode ( "Tl { " + pExpr.toPrettyString ( )
        + " }" ) ;
  }


  private DefaultMutableTreeNode exprTuple ( Tuple pExpr )
  {
    Expression [ ] e = pExpr.getExpressions ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Tuple { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchilds
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = new DefaultMutableTreeNode ( "e" + i
          + " { " + e [ i ].toPrettyString ( ) + " }" ) ;
      treeModel.insertNodeInto ( subchild , child , i ) ;
      treeModel.insertNodeInto ( exprExpression ( e [ i ] ) , subchild , 0 ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode exprUnaryCons ( UnaryCons pExpr )
  {
    return new DefaultMutableTreeNode ( "UnaryCons { "
        + pExpr.toPrettyString ( ) + " }" ) ;
  }


  private DefaultMutableTreeNode exprUnaryListOperator ( UnaryListOperator pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "UnaryListOperator { " + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprUnaryOperator ( UnaryOperator pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "UnaryOperator { " + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprUnitConstant ( UnitConstant pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode (
        "UnitConstant { " + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprValue ( Value pExpr )
  {
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "Value { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprWhile ( While pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = new DefaultMutableTreeNode ( "While { "
        + pExpr.toPrettyString ( ) + " }" ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = new DefaultMutableTreeNode ( "e1 { "
        + e1.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild1 , child , 0 ) ;
    treeModel.insertNodeInto ( exprExpression ( e1 ) , subchild1 , 0 ) ;
    // Subchild 2
    DefaultMutableTreeNode subchild2 = new DefaultMutableTreeNode ( "e2 { "
        + e2.toPrettyString ( ) + " }" ) ;
    treeModel.insertNodeInto ( subchild2 , child , 1 ) ;
    treeModel.insertNodeInto ( exprExpression ( e2 ) , subchild2 , 0 ) ;
    return child ;
  }


  public JTree getJTreeSyntax ( )
  {
    return jTreeSyntax ;
  }


  public void setExpression ( Expression pExpr )
  {
    treeModel
        .setRoot ( new DefaultMutableTreeNode ( pExpr.toPrettyString ( ) ) ) ;
    treeModel.insertNodeInto ( exprExpression ( pExpr ) ,
        ( MutableTreeNode ) treeModel.getRoot ( ) , 0 ) ;
    this.jTreeSyntax.expandRow ( 0 ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.jFrameTest.setVisible ( pVisible ) ;
  }
}
