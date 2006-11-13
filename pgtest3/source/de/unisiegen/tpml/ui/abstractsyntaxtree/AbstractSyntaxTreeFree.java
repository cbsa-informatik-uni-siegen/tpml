package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


public class AbstractSyntaxTreeFree
{
  public static LinkedList < Expression > free ( Expression pExpr , String id )
  {
    LinkedList < Expression > list = new LinkedList < Expression > ( ) ;
    if ( pExpr instanceof Identifier )
    {
      if ( ( ( Identifier ) pExpr ).getName ( ).equals ( id ) )
      {
        list.add ( pExpr ) ;
        return list ;
      }
    }
    Enumeration < Expression > children = pExpr.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      Expression current = children.nextElement ( ) ;
      if ( ! current.free ( ).isEmpty ( ) )
      {
        LinkedList < Expression > tmpList = free ( current , id ) ;
        for ( Expression tmpExpr : tmpList )
        {
          list.add ( tmpExpr ) ;
        }
      }
    }
    return list ;
  }


  private LinkedList < LinkedList < Expression >> list ;


  public AbstractSyntaxTreeFree ( )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
  }


  public void add ( LinkedList < Expression > pChild )
  {
    this.list.add ( pChild ) ;
  }


  public LinkedList get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  public int size ( )
  {
    return this.list.size ( ) ;
  }
}
