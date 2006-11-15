package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.Enumeration ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;


public class AbstractSyntaxTree
{
  private static AbstractSyntaxTree abstractSyntaxTree = null ;


  public static AbstractSyntaxTree getInstance ( )
  {
    if ( abstractSyntaxTree == null )
    {
      abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
      return abstractSyntaxTree ;
    }
    return abstractSyntaxTree ;
  }


  private AbstractSyntaxTreeUI abstractSyntaxTreeUI ;


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
      Expression pExpr , AbstractSyntaxTreeFree pRelations )
  {
    return new DefaultMutableTreeNode ( new AbstractSyntaxTreeNode (
        pDescription , pExpr , pRelations ) ) ;
  }


  private DefaultMutableTreeNode createNode ( String pDescription ,
      String pName , int pStart , int pEnd )
  {
    return new DefaultMutableTreeNode ( new AbstractSyntaxTreeNode (
        pDescription , pName , pStart , pEnd ) ) ;
  }


  private DefaultMutableTreeNode exprCurriedLet ( CurriedLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    AbstractSyntaxTreeFree free = new AbstractSyntaxTreeFree ( ) ;
    free.add ( pExpression.getE2 ( ) , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      free.add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , free ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" ,
          idList [ i ] , length + 4 + i , length + 3 + idList [ i ].length ( )
              + i ) ;
      length += idList [ i ].length ( ) ;
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


  private DefaultMutableTreeNode exprCurriedLetRec ( CurriedLetRec pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    AbstractSyntaxTreeFree free = new AbstractSyntaxTreeFree ( ) ;
    free.add ( pExpression , pExpression.getIdentifiers ( 0 ) ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      free.add ( pExpression.getE1 ( ) , pExpression.getIdentifiers ( i ) ) ;
    }
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , free ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchilds = createNode ( "Identifier" ,
          idList [ i ] , length + 8 + i , length + 7 + idList [ i ].length ( )
              + i ) ;
      length += idList [ i ].length ( ) ;
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


  private DefaultMutableTreeNode exprExpression ( Expression pExpression )
  {
    if ( pExpression instanceof MultiLambda )
    {
      return exprLultiLambda ( ( MultiLambda ) pExpression ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      return exprLambda ( ( Lambda ) pExpression ) ;
    }
    else if ( pExpression instanceof Location )
    {
      return exprLocation ( ( Location ) pExpression ) ;
    }
    else if ( pExpression instanceof LetRec )
    {
      return exprLetRec ( ( LetRec ) pExpression ) ;
    }
    else if ( pExpression instanceof Let )
    {
      return exprLet ( ( Let ) pExpression ) ;
    }
    else if ( pExpression instanceof CurriedLetRec )
    {
      return exprCurriedLetRec ( ( CurriedLetRec ) pExpression ) ;
    }
    else if ( pExpression instanceof CurriedLet )
    {
      return exprCurriedLet ( ( CurriedLet ) pExpression ) ;
    }
    else if ( pExpression instanceof MultiLet )
    {
      return exprMultiLet ( ( MultiLet ) pExpression ) ;
    }
    else if ( pExpression instanceof Recursion )
    {
      return exprRecursion ( ( Recursion ) pExpression ) ;
    }
    else if ( pExpression instanceof InfixOperation )
    {
      return exprInfixOperation ( ( InfixOperation ) pExpression ) ;
    }
    return exprNormal ( pExpression ) ;
  }


  private DefaultMutableTreeNode exprInfixOperation ( InfixOperation pExpression )
  {
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    BinaryOperator b = pExpression.getOp ( ) ;
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild2 = createNode ( b.getClass ( )
        .getSimpleName ( ) , b.toString ( ) , e1.toPrettyString ( ).toString ( )
        .length ( ) , e1.toPrettyString ( ).toString ( ).length ( )
        + b.toString ( ).length ( ) ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild1.add ( exprExpression ( e1 ) ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    node.add ( subchild1 ) ;
    node.add ( subchild2 ) ;
    node.add ( subchild3 ) ;
    return node ;
  }


  private DefaultMutableTreeNode exprLambda ( Lambda pExpression )
  {
    String id = pExpression.getId ( ) ;
    Expression e = pExpression.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression.getE ( ) , pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 1 , id
        .length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLet ( Let pExpression )
  {
    String id = "" ;
    for ( Method m : pExpression.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( m.getName ( ).equals ( "getId" ) )
      {
        try
        {
          id = ( String ) m.invoke ( pExpression , new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          System.err.println ( "IllegalArgumentException" ) ;
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "IllegalAccessException" ) ;
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "InvocationTargetException" ) ;
        }
      }
    }
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression.getE2 ( ) , pExpression.getId ( ) ) ) ;
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


  private DefaultMutableTreeNode exprLetRec ( LetRec pExpression )
  {
    String id = pExpression.getId ( ) ;
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression , pExpression.getId ( ) ) ) ;
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


  private DefaultMutableTreeNode exprLocation ( Location pExpression )
  {
    String name = pExpression.getName ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Name" , name , 0 , name
        .length ( ) - 1 ) ;
    child.add ( subchild1 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprLultiLambda ( MultiLambda pExpression )
  {
    String idList[] = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression , pExpression.getIdentifiers ( ) ) ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 2 + ( i * 2 ) , length + 1
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
      child.add ( subchild ) ;
    }
    Expression e = pExpression.getE ( ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprMultiLet ( MultiLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression.getE2 ( ) , pExpression.getIdentifiers ( ) ) ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 5 + ( i * 2 ) , length + 4
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
      child.add ( subchild ) ;
    }
    DefaultMutableTreeNode subchild2 = createNode ( "e1" , e1 ) ;
    DefaultMutableTreeNode subchild3 = createNode ( "e2" , e2 ) ;
    subchild2.add ( exprExpression ( e1 ) ) ;
    subchild3.add ( exprExpression ( e2 ) ) ;
    child.add ( subchild2 ) ;
    child.add ( subchild3 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprNormal ( Expression pExpression )
  {
    DefaultMutableTreeNode node = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression ) ;
    Enumeration < Expression > children = pExpression.children ( ) ;
    int i = 0 ;
    while ( children.hasMoreElements ( ) )
    {
      Expression child = children.nextElement ( ) ;
      DefaultMutableTreeNode childNode = createNode ( "e" + i ++ , child ) ;
      childNode.add ( exprExpression ( child ) ) ;
      node.add ( childNode ) ;
    }
    return node ;
  }


  private DefaultMutableTreeNode exprRecursion ( Recursion pExpression )
  {
    String id = pExpression.getId ( ) ;
    Expression e = pExpression.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( pExpression.getClass ( )
        .getSimpleName ( ) , pExpression , new AbstractSyntaxTreeFree (
        pExpression , pExpression.getId ( ) ) ) ;
    DefaultMutableTreeNode subchild1 = createNode ( "Identifier" , id , 4 ,
        3 + id.length ( ) ) ;
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild1 ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  public void setExpression ( Expression pExpression )
  {
    DefaultMutableTreeNode rootNode = createNode ( "Expression" , pExpression ) ;
    rootNode.add ( exprExpression ( pExpression ) ) ;
    this.abstractSyntaxTreeUI.setRootNode ( rootNode ) ;
    this.abstractSyntaxTreeUI.expandRow ( 0 ) ;
  }


  public void setVisible ( boolean pVisible )
  {
    this.abstractSyntaxTreeUI.setVisible ( pVisible ) ;
  }
}
