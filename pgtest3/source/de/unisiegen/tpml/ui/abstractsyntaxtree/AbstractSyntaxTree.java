package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.Enumeration ;
import java.util.LinkedList ;
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


  private DefaultMutableTreeNode exprChilds ( Expression pExpr )
  {
    LinkedList < Expression > listFree = null ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    if ( pExpr instanceof Recursion )
    {
      Recursion r = ( Recursion ) pExpr ;
      listFree = AbstractSyntaxTreeFree.free ( r , r.getId ( ) ) ;
      tmp.add ( listFree ) ;
    }
    else if ( pExpr instanceof CurriedLetRec )
    {
      CurriedLetRec c = ( CurriedLetRec ) pExpr ;
      String idList[] = c.getIdentifiers ( ) ;
      for ( int i = 0 ; i < idList.length ; i ++ )
      {
        listFree = AbstractSyntaxTreeFree.free ( c , idList [ i ] ) ;
        tmp.add ( listFree ) ;
      }
    }
    else if ( pExpr instanceof CurriedLet )
    {
      CurriedLet c = ( CurriedLet ) pExpr ;
      String idList[] = c.getIdentifiers ( ) ;
      for ( int i = 0 ; i < idList.length ; i ++ )
      {
        listFree = AbstractSyntaxTreeFree.free ( c.getE2 ( ) , idList [ i ] ) ;
        tmp.add ( listFree ) ;
      }
    }
    else if ( pExpr instanceof MultiLambda )
    {
      MultiLambda m = ( MultiLambda ) pExpr ;
      String idList[] = m.getIdentifiers ( ) ;
      for ( int i = 0 ; i < idList.length ; i ++ )
      {
        listFree = AbstractSyntaxTreeFree.free ( m.getE ( ) , idList [ i ] ) ;
        tmp.add ( listFree ) ;
      }
    }
    else if ( pExpr instanceof MultiLet )
    {
      MultiLet l = ( MultiLet ) pExpr ;
      String idList[] = l.getIdentifiers ( ) ;
      for ( int i = 0 ; i < idList.length ; i ++ )
      {
        listFree = AbstractSyntaxTreeFree.free ( l.getE2 ( ) , idList [ i ] ) ;
        tmp.add ( listFree ) ;
      }
    }
    else if ( pExpr instanceof Lambda )
    {
      Lambda l = ( Lambda ) pExpr ;
      listFree = AbstractSyntaxTreeFree.free ( l.getE ( ) , l.getId ( ) ) ;
      tmp.add ( listFree ) ;
    }
    else if ( pExpr instanceof LetRec )
    {
      LetRec l = ( LetRec ) pExpr ;
      listFree = AbstractSyntaxTreeFree.free ( l , l.getId ( ) ) ;
      tmp.add ( listFree ) ;
    }
    else if ( pExpr instanceof Let )
    {
      Let l = ( Let ) pExpr ;
      listFree = AbstractSyntaxTreeFree.free ( l.getE2 ( ) , l.getId ( ) ) ;
      tmp.add ( listFree ) ;
    }
    DefaultMutableTreeNode node = createNode ( pExpr.getClass ( )
        .getSimpleName ( ) , pExpr , tmp ) ;
    Enumeration < Expression > list = pExpr.children ( ) ;
    int i = 0 ;
    while ( list.hasMoreElements ( ) )
    {
      Expression e = list.nextElement ( ) ;
      /*
       * listFree = null ; tmp = new AbstractSyntaxTreeFree ( ) ; if ( e
       * instanceof Lambda ) { Lambda l = ( Lambda ) e ; listFree =
       * AbstractSyntaxTreeFree.free ( l.getE ( ) , l .getId ( ) ) ; tmp.add (
       * listFree ) ; } else if ( e instanceof MultiLambda ) { MultiLambda m
       * =(MultiLambda) e ; String idList[] = m.getIdentifiers ( ) ; for ( int j =
       * 0 ; j < idList.length ; j ++ ) { listFree = AbstractSyntaxTreeFree.free (
       * m.getE ( ) , idList [ j ] ) ; tmp.add ( listFree ) ; } } else if ( e
       * instanceof LetRec ) { LetRec l = ( LetRec ) e ; listFree =
       * AbstractSyntaxTreeFree.free ( l , l.getId ( ) ) ; tmp.add ( listFree ) ; }
       * else if ( e instanceof Let ) { Let l = ( Let ) e ; listFree =
       * AbstractSyntaxTreeFree.free ( l.getE2 ( ) , l.getId ( ) ) ; tmp.add (
       * listFree ) ; }
       */
      DefaultMutableTreeNode child = createNode ( "e" + i ++ , e ) ;
      child.add ( exprExpression ( e ) ) ;
      node.add ( child ) ;
    }
    return node ;
  }


  private DefaultMutableTreeNode exprCurriedLet ( CurriedLet pExpr )
  {
    String [ ] idList = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    LinkedList < Expression > listFree ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      listFree = AbstractSyntaxTreeFree.free ( pExpr.getE2 ( ) , idList [ i ] ) ;
      tmp.add ( listFree ) ;
    }
    DefaultMutableTreeNode child = createNode ( "CurriedLet" , pExpr , tmp ) ;
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


  private DefaultMutableTreeNode exprCurriedLetRec ( CurriedLetRec pExpr )
  {
    String [ ] idList = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    LinkedList < Expression > listFree ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      listFree = AbstractSyntaxTreeFree.free ( pExpr , idList [ i ] ) ;
      tmp.add ( listFree ) ;
    }
    DefaultMutableTreeNode child = createNode ( "CurriedLetRec" , pExpr , tmp ) ;
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


  private DefaultMutableTreeNode exprExpression ( Expression pExpr )
  {
    // Value - MultiLambda
    if ( pExpr instanceof MultiLambda )
    {
      return exprLultiLambda ( ( MultiLambda ) pExpr ) ;
    }
    // Value - Lambda
    else if ( pExpr instanceof Lambda )
    {
      return exprLambda ( ( Lambda ) pExpr ) ;
    }
    // Value - Location
    else if ( pExpr instanceof Location )
    {
      return exprLocation ( ( Location ) pExpr ) ;
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
    return exprChilds ( pExpr ) ;
  }


  private DefaultMutableTreeNode exprInfixOperation ( InfixOperation pExpr )
  {
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    BinaryOperator b = pExpr.getOp ( ) ;
    DefaultMutableTreeNode node = createNode ( "InfixOperation" , pExpr ) ;
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


  private DefaultMutableTreeNode exprLambda ( Lambda pExpr )
  {
    LinkedList < Expression > listFree = AbstractSyntaxTreeFree.free ( pExpr
        .getE ( ) , pExpr.getId ( ) ) ;
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    tmp.add ( listFree ) ;
    DefaultMutableTreeNode child = createNode ( pExpr.getClass ( )
        .getSimpleName ( ) , pExpr , tmp ) ;
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
    LinkedList < Expression > listFree = AbstractSyntaxTreeFree.free ( pExpr
        .getE2 ( ) , pExpr.getId ( ) ) ;
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    tmp.add ( listFree ) ;
    DefaultMutableTreeNode child = createNode ( pExpr.getClass ( )
        .getSimpleName ( ) , pExpr , tmp ) ;
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
    LinkedList < Expression > listFree = AbstractSyntaxTreeFree.free ( pExpr ,
        pExpr.getId ( ) ) ;
    String id = pExpr.getId ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    tmp.add ( listFree ) ;
    DefaultMutableTreeNode child = createNode ( pExpr.getClass ( )
        .getSimpleName ( ) , pExpr , tmp ) ;
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
    String idList[] = pExpr.getIdentifiers ( ) ;
    Expression e = pExpr.getE ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    LinkedList < Expression > listFree ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      listFree = AbstractSyntaxTreeFree.free ( pExpr , idList [ i ] ) ;
      tmp.add ( listFree ) ;
    }
    DefaultMutableTreeNode child = createNode ( "MultiLamdba" , pExpr , tmp ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 2 + ( i * 2 ) , length + 1
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
      child.add ( subchild ) ;
    }
    DefaultMutableTreeNode subchild2 = createNode ( "e" , e ) ;
    subchild2.add ( exprExpression ( e ) ) ;
    child.add ( subchild2 ) ;
    return child ;
  }


  private DefaultMutableTreeNode exprMultiLet ( MultiLet pExpr )
  {
    String [ ] idList = pExpr.getIdentifiers ( ) ;
    Expression e1 = pExpr.getE1 ( ) ;
    Expression e2 = pExpr.getE2 ( ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    LinkedList < Expression > listFree ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      listFree = AbstractSyntaxTreeFree.free ( pExpr.getE2 ( ) , idList [ i ] ) ;
      tmp.add ( listFree ) ;
    }
    DefaultMutableTreeNode child = createNode ( "MultiLet" , pExpr , tmp ) ;
    int length = 0 ;
    for ( int i = 0 ; i < idList.length ; i ++ )
    {
      DefaultMutableTreeNode subchild = createNode ( "Identifier" ,
          idList [ i ] , length + 5 + ( i * 2 ) , length + 4
              + idList [ i ].length ( ) + ( i * 2 ) ) ;
      length += idList [ i ].length ( ) ;
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


  private DefaultMutableTreeNode exprRecursion ( Recursion pExpr )
  {
    LinkedList < Expression > listFree = AbstractSyntaxTreeFree.free ( pExpr ,
        pExpr.getId ( ) ) ;
    AbstractSyntaxTreeFree tmp = new AbstractSyntaxTreeFree ( ) ;
    tmp.add ( listFree ) ;
    String id = pExpr.getId ( ) ;
    Expression e = pExpr.getE ( ) ;
    DefaultMutableTreeNode child = createNode ( "Recursion" , pExpr , tmp ) ;
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
