package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


public class ASTBindings
{
  private LinkedList < LinkedList < Expression >> list ;


  private LinkedList < Expression > nothingFree ;


  private LinkedList < Expression > notBounded ;


  public ASTBindings ( )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
  }


  public ASTBindings ( Expression pExpr , String pId )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
    add ( pExpr , pId ) ;
  }


  public ASTBindings ( Expression pExpr , String pIdentifiers[] )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
    add ( pExpr , pIdentifiers ) ;
  }


  public void add ( Expression pExpression , String pId )
  {
    this.list.add ( free ( pExpression , pId ) ) ;
  }


  public void add ( Expression pExpression , String pIdentifiers[] )
  {
    for ( int i = 0 ; i < pIdentifiers.length ; i ++ )
    {
      this.list.add ( free ( pExpression , pIdentifiers [ i ] ) ) ;
    }
  }


  private LinkedList < Expression > free ( Expression pExpression , String pId )
  {
    LinkedList < Expression > tmp = new LinkedList < Expression > ( ) ;
    if ( pExpression instanceof Identifier )
    {
      if ( ( ( Identifier ) pExpression ).getName ( ).equals ( pId ) )
      {
        tmp.add ( pExpression ) ;
        return tmp ;
      }
      this.notBounded.add ( pExpression ) ;
    }
    Enumeration < Expression > children = pExpression.children ( ) ;
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
      else
      {
        this.nothingFree.add ( current ) ;
      }
    }
    return tmp ;
  }


  public Expression get ( int pListIndex , int pIndex )
  {
    return this.list.get ( pListIndex ).get ( pIndex ) ;
  }


  public int size ( )
  {
    return this.list.size ( ) ;
  }


  public int size ( int pListIndex )
  {
    return this.list.get ( pListIndex ).size ( ) ;
  }


  public LinkedList < Expression > getNothingFree ( )
  {
    return this.nothingFree ;
  }


  public LinkedList < Expression > getNotBounded ( )
  {
    return this.notBounded ;
  }
}
