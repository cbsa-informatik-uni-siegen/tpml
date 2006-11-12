package de.unisiegen.tpml.ui ;


import javax.swing.tree.DefaultMutableTreeNode ;
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
  private static AbstractSyntaxTree abstractSyntaxTree = null ;


  private AbstractSyntaxTreeUI abstractSyntaxTreeUI ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntaxTree == null )
    {
      abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
      return abstractSyntaxTree ;
    }
    else
    {
      return abstractSyntaxTree ;
    }
  }


  private AbstractSyntaxTree ( )
  {
    this.abstractSyntaxTreeUI = new AbstractSyntaxTreeUI ( ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      Expression pExpr )
  {
    return new DefaultMutableTreeNode ( new AbstractSyntaxTreeNode (
        pDescription , pExpr ) ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      String pName , int pStart , int pEnd )
  {
    return new DefaultMutableTreeNode ( new AbstractSyntaxTreeNode (
        pDescription , pName , pStart , pEnd ) ) ;
  }


  private DefaultMutableTreeNode exprAnd ( And pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "And" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprApplication ( Application pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "Application" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprArithmeticOperator (
      ArithmeticOperator pExpr )
  {
    return createNode ( "ArithmeticOperator" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprAssign ( Assign pExpr )
  {
    return createNode ( "Assign" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprBinaryCons ( BinaryCons pExpr )
  {
    return createNode ( "BinaryCons" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprBinaryOperator ( BinaryOperator pExpr )
  {
    return createNode ( "BinaryOperator" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprBooleanConstant ( BooleanConstant pExpr )
  {
    return createNode ( "BooleanConstant" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprCondition ( Condition pExpr )
  {
    Expression e0 = pExpr.getE0 ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = createNode ( "Condition" , pExpr ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild0 = createNode ( "e0" , e0 ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild0.add ( exprExpression ( e0 ) ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild0 ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCondition1 ( Condition1 pExpr )
  {
    Expression e0 = pExpr.getE0 ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    DefaultMutableTreeNode child = createNode ( "Condition1" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e0" , e0 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e1" , e1 ) ;
    subchild1.add ( exprExpression ( e0 ) ) ;
    subchild2.add ( exprExpression ( e1 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprConstant ( Constant pExpr )
  {
    return createNode ( "Constant" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprCurriedLet ( CurriedLet pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "CurriedLet" , pExpr ) ;
    int length = 0 ;
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" , id [ i ] ,
          length + 4 + i , length + 3 + id [ i ].length ( ) + i ) ;
      length += id [ i ].length ( ) ;
      child.add ( subchilds ) ;
    }
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprCurriedLetRec ( CurriedLetRec pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    // Child
    DefaultMutableTreeNode child = createNode ( "CurriedLetRec" , pExpr ) ;
    int length = 0 ;
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" , id [ i ] ,
          length + 8 + i , length + 7 + id [ i ].length ( ) + i ) ;
      length += id [ i ].length ( ) ;
      child.add ( subchilds ) ;
    }
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprDeref ( Deref pExpr )
  {
    return createNode ( "Deref" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprEmptyList ( EmptyList pExpr )
  {
    return createNode ( "EmptyList" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprExn ( Exn pExpr )
  {
    return createNode ( "Exn" , pExpr ) ;
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
    return exprUnknown ( pExpr ) ;
  }


  private DefaultMutableTreeNode exprFst ( Fst pExpr )
  {
    return createNode ( "Fst" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprHd ( Hd pExpr )
  {
    return createNode ( "Hd" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprIdentifier ( Identifier pExpr )
  {
    return createNode ( "Identifier" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprInfixOperation ( InfixOperation pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    BinaryOperator b = pExpr.getOp ( ) ;
    // Child
    DefaultMutableTreeNode child = createNode ( "InfixOperation" , pExpr ) ;
    // Subchild 1
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    child.add ( subchild1 ) ;
    DefaultMutableTreeNode subchild2 ;
    String name ;
    if ( b instanceof ArithmeticOperator )
    {
      name = "ArithmeticOperator" ;
    }
    else if ( b instanceof RelationalOperator )
    {
      name = "RelationalOperator" ;
    }
    else if ( b instanceof BinaryCons )
    {
      name = "BinaryCons" ;
    }
    else if ( b instanceof Assign )
    {
      name = "Assign" ;
    }
    else
    {
      name = "BinaryOperator" ;
    }
    subchild2 = createNode ( name , b.toString ( ) , e1.toPrettyString ( )
        .toString ( ).length ( ) , e1.toPrettyString ( ).toString ( ).length ( )
        + b.toString ( ).length ( ) ) ;
    child.add ( subchild2 ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild3 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprIntegerConstant ( IntegerConstant pExpr )
  {
    return createNode ( "IntegerConstant" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprIsEmpty ( IsEmpty pExpr )
  {
    return createNode ( "IsEmpty" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprLambda ( Lambda pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( "Lamdba" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 1 , id
        .length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLet ( Let pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "Let" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 4 ,
        3 + id.length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild2.add ( exprExpression ( e1 ) ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    child.add ( subchild3 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLetRec ( LetRec pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "LetRec" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 8 ,
        7 + id.length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild2.add ( exprExpression ( e1 ) ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    child.add ( subchild3 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprList ( List pExpr )
  {
    Expression [ ] e = pExpr.getExpressions ( ) ;
    DefaultMutableTreeNode child = createNode ( "List" , pExpr ) ;
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "e" + i , e [ i ] ) ;
      subchild.add ( exprExpression ( e [ i ] ) ) ;
      child.add ( subchild ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode exprLocation ( Location pExpr )
  {
    String name = pExpr.getName ( ) ;
    DefaultMutableTreeNode child = createNode ( "Location" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Name" , name , 0 , name
        .length ( ) - 1 ) ;
    child.add ( subchild1 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLultiLambda ( MultiLambda pExpr )
  {
    String id[] = pExpr.getIdentifiers ( ) ;
    Expression e = pExpr.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( "MultiLamdba" , pExpr ) ;
    int length = 0 ;
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" , id [ i ] ,
          length + 2 + ( i * 2 ) , length + 1 + id [ i ].length ( ) + ( i * 2 ) ) ;
      length += id [ i ].length ( ) ;
      child.add ( subchild ) ;
    }
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprMultiLet ( MultiLet pExpr )
  {
    String [ ] id = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "MultiLet" , pExpr ) ;
    int length = 0 ;
    for ( int i = 0 ; i < id.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" , id [ i ] ,
          length + 5 + ( i * 2 ) , length + 4 + id [ i ].length ( ) + ( i * 2 ) ) ;
      length += id [ i ].length ( ) ;
      this.abstractSyntaxTreeUI.appendNode ( subchild , child ) ;
    }
    DefaultMutableTreeNode subchild2 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild2.add ( exprExpression ( e1 ) ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild2 ) ;
    child.add ( subchild3 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprNot ( Not pExpr )
  {
    return createNode ( "Not" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprOr ( Or pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "Or" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprProjection ( Projection pExpr )
  {
    return createNode ( "Projection" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprRecursion ( Recursion pExpr )
  {
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( "Recursion" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 4 ,
        3 + id.length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprRef ( Ref pExpr )
  {
    return createNode ( "Ref" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprRelationalOperator (
      RelationalOperator pExpr )
  {
    return createNode ( "RelationalOperator" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprSequence ( Sequence pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "Sequence" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprSnd ( Snd pExpr )
  {
    return createNode ( "Snd" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprTl ( Tl pExpr )
  {
    return createNode ( "Tl" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprTuple ( Tuple pExpr )
  {
    Expression [ ] e = pExpr.getExpressions ( ) ;
    DefaultMutableTreeNode child = createNode ( "Tuple" , pExpr ) ;
    for ( int i = 0 ; i < e.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "e" + i , e [ i ] ) ;
      subchild.add ( exprExpression ( e [ i ] ) ) ;
      child.add ( subchild ) ;
    }
    return child ;
  }


  private DefaultMutableTreeNode exprUnaryCons ( UnaryCons pExpr )
  {
    return createNode ( "UnaryCons" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprUnaryListOperator ( UnaryListOperator pExpr )
  {
    return createNode ( "UnaryListOperator" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprUnaryOperator ( UnaryOperator pExpr )
  {
    return createNode ( "UnaryOperator" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprUnitConstant ( UnitConstant pExpr )
  {
    return createNode ( "UnitConstant" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprUnknown ( Expression pExpr )
  {
    return createNode (
        "<b><font color=\"#FF0000\">UNKNOWN EXPRESSION</font></b>" , pExpr
            .toPrettyString ( ).toString ( ) , 0 , pExpr.toPrettyString ( )
            .toString ( ).length ( ) - 1 ) ;
  }


  private DefaultMutableTreeNode exprValue ( Value pExpr )
  {
    return createNode ( "Value" , pExpr ) ;
  }


  private DefaultMutableTreeNode exprWhile ( While pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( "While" , pExpr ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild2.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  public void setExpression ( Expression pExpr )
  {
    DefaultMutableTreeNode rootNode = createNode ( "Expression" , pExpr ) ;
    rootNode.add ( exprExpression ( pExpr ) ) ;
    this.abstractSyntaxTreeUI.setRootNode ( rootNode ) ;
    this.abstractSyntaxTreeUI.expandRow ( 0 ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.abstractSyntaxTreeUI.setVisible ( pVisible ) ;
  }
}
