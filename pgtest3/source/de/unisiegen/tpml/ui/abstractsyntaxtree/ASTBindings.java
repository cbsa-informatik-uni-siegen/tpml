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


  private Expression expression ;


  public ASTBindings ( )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
    this.expression = null ;
  }


  public ASTBindings ( Expression pExpression , String pId )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
    this.expression = pExpression ;
    add ( pExpression , pId ) ;
  }


  public ASTBindings ( Expression pExpression , String pIdentifiers[] )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.nothingFree = new LinkedList < Expression > ( ) ;
    this.notBounded = new LinkedList < Expression > ( ) ;
    this.expression = pExpression ;
    add ( pExpression , pIdentifiers ) ;
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
    LinkedList < Expression > result = new LinkedList < Expression > ( ) ;
    if ( pExpression instanceof Identifier )
    {
      if ( ( ( Identifier ) pExpression ).getName ( ).equals ( pId ) )
      {
        result.add ( pExpression ) ;
        return result ;
      }
      this.notBounded.add ( pExpression ) ;
    }
    Enumeration < Expression > children = pExpression.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression current = children.nextElement ( ) ;
      Enumeration < Expression > currentChildren = current.children ( ) ;
      if ( ! current.free ( ).isEmpty ( ) )
      {
        LinkedList < Expression > tmpList = free ( current , pId ) ;
        for ( Expression tmpExpr : tmpList )
        {
          result.add ( tmpExpr ) ;
        }
      }
      else if ( currentChildren.hasMoreElements ( ) )
      {
        this.nothingFree.add ( current ) ;
      }
    }
    return result ;
  }


  public Expression get ( int pListIndex , int pExpressionIndex )
  {
    return this.list.get ( pListIndex ).get ( pExpressionIndex ) ;
  }


  public Expression getExpression ( )
  {
    return this.expression ;
  }


  public LinkedList < Expression > getNotBounded ( )
  {
    return this.notBounded ;
  }


  public LinkedList < Expression > getNothingFree ( )
  {
    return this.nothingFree ;
  }


  public int size ( )
  {
    return this.list.size ( ) ;
  }


  public int size ( int pListIndex )
  {
    return this.list.get ( pListIndex ).size ( ) ;
  }
}
