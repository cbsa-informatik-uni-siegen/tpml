package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTBinding
{
  /**
   * TODO
   */
  private LinkedList < LinkedList < Expression >> list ;


  /**
   * TODO
   */
  private LinkedList < Expression > notFree ;


  /**
   * TODO
   */
  private LinkedList < Expression > noBinding ;


  /**
   * TODO
   */
  private Expression holeExpression ;


  /**
   * TODO
   * 
   * @param pHoleExpression
   */
  public ASTBinding ( Expression pHoleExpression )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.notFree = new LinkedList < Expression > ( ) ;
    this.noBinding = new LinkedList < Expression > ( ) ;
    this.holeExpression = pHoleExpression ;
  }


  /**
   * TODO
   * 
   * @param pHoleExpression
   * @param pExpression
   * @param pId
   */
  public ASTBinding ( Expression pHoleExpression , Expression pExpression ,
      String pId )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.notFree = new LinkedList < Expression > ( ) ;
    this.noBinding = new LinkedList < Expression > ( ) ;
    this.holeExpression = pHoleExpression ;
    add ( pExpression , pId ) ;
  }


  /**
   * TODO
   * 
   * @param pHoleExpression
   * @param pExpression
   * @param pIdentifiers
   */
  public ASTBinding ( Expression pHoleExpression , Expression pExpression ,
      String pIdentifiers[] )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.notFree = new LinkedList < Expression > ( ) ;
    this.noBinding = new LinkedList < Expression > ( ) ;
    this.holeExpression = pHoleExpression ;
    add ( pExpression , pIdentifiers ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pId
   */
  public void add ( Expression pExpression , String pId )
  {
    this.list.add ( free ( pExpression , pId ) ) ;
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pIdentifiers
   */
  public void add ( Expression pExpression , String pIdentifiers[] )
  {
    for ( String id : pIdentifiers )
    {
      this.list.add ( free ( pExpression , id ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pId
   * @return TODO
   */
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
      this.noBinding.add ( pExpression ) ;
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
          result.add ( tmpExpr ) ;
        }
      }
      else
      {
        this.notFree.add ( current ) ;
      }
    }
    return result ;
  }


  /**
   * TODO
   * 
   * @param pListIndex
   * @param pExpressionIndex
   * @return TODO
   */
  public Expression get ( int pListIndex , int pExpressionIndex )
  {
    return this.list.get ( pListIndex ).get ( pExpressionIndex ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Expression getHoleExpression ( )
  {
    return this.holeExpression ;
  }


  /**
   * TODO
   * 
   * @param pIndex
   * @return TODO
   */
  public Expression getNoBinding ( int pIndex )
  {
    return this.noBinding.get ( pIndex ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getNoBindingSize ( )
  {
    return this.noBinding.size ( ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public LinkedList < Expression > getNotFree ( )
  {
    return this.notFree ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int size ( )
  {
    return this.list.size ( ) ;
  }


  /**
   * TODO
   * 
   * @param pListIndex
   * @return TODO
   */
  public int size ( int pListIndex )
  {
    return this.list.get ( pListIndex ).size ( ) ;
  }
}
