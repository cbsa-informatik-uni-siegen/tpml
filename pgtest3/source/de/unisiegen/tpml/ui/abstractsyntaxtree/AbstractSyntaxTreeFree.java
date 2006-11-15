package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


public class AbstractSyntaxTreeFree
{
  private LinkedList < LinkedList < Expression >> list ;


  public AbstractSyntaxTreeFree ( )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
  }


  public AbstractSyntaxTreeFree ( Expression pExpr , String pId )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    add ( pExpr , pId ) ;
  }


  public AbstractSyntaxTreeFree ( Expression pExpr , String pIdentifiers[] )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    add ( pExpr , pIdentifiers ) ;
  }


  public void add ( Expression pExpr , String pId )
  {
    this.list.add ( free ( pExpr , pId ) ) ;
  }


  public void add ( Expression pExpr , String pIdentifiers[] )
  {
    for ( int i = 0 ; i < pIdentifiers.length ; i ++ )
    {
      this.list.add ( free ( pExpr , pIdentifiers [ i ] ) ) ;
    }
  }


  private LinkedList < Expression > free ( Expression pExpr , String pId )
  {
    LinkedList < Expression > tmp = new LinkedList < Expression > ( ) ;
    if ( pExpr instanceof Identifier )
    {
      if ( ( ( Identifier ) pExpr ).getName ( ).equals ( pId ) )
      {
        tmp.add ( pExpr ) ;
        return tmp ;
      }
    }
    Enumeration < Expression > children = pExpr.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression current = children.nextElement ( ) ;
      if ( ! current.free ( ).isEmpty ( ) )
      {
        LinkedList < Expression > tmpList = free ( current , pId ) ;
        for ( Expression tmpExpr : tmpList )
        {
          tmp.add ( tmpExpr ) ;
        }
      }
    }
    return tmp ;
  }


  public Expression get ( int pList , int pIndex )
  {
    return this.list.get ( pList ).get ( pIndex ) ;
  }


  public int size ( )
  {
    return this.list.size ( ) ;
  }


  public int size ( int pList )
  {
    return this.list.get ( pList ).size ( ) ;
  }
}
