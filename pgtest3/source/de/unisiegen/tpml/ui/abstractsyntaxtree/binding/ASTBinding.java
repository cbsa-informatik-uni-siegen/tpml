package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLet ;


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
    }
    if ( ( pExpression instanceof CurriedLetRec )
        && ( identifierIndex ( ( ( CurriedLetRec ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      int index = identifierIndex ( ( ( CurriedLetRec ) pExpression )
          .getIdentifiers ( ) , pId ) ;
      LinkedList < Expression > tmpList ;
      if ( index == 0 )
      {
        return result ;
      }
      tmpList = free ( ( ( CurriedLetRec ) pExpression ).getE2 ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof CurriedLet )
        && ( identifierIndex (
            ( ( CurriedLet ) pExpression ).getIdentifiers ( ) , pId ) >= 0 )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      int index = identifierIndex ( ( ( CurriedLet ) pExpression )
          .getIdentifiers ( ) , pId ) ;
      LinkedList < Expression > tmpList ;
      if ( index == 0 )
      {
        tmpList = free ( ( ( CurriedLet ) pExpression ).getE1 ( ) , pId ) ;
      }
      else
      {
        tmpList = free ( ( ( CurriedLet ) pExpression ).getE2 ( ) , pId ) ;
      }
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof MultiLet )
        && ( identifierIndex ( ( ( MultiLet ) pExpression ).getIdentifiers ( ) ,
            pId ) >= 0 ) && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = free ( ( ( MultiLet ) pExpression ).getE1 ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof LetRec )
        && ( ( ( LetRec ) pExpression ).getId ( ).equals ( pId ) )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      return result ;
    }
    else if ( ( pExpression instanceof Let )
        && ( ( ( Let ) pExpression ).getId ( ).equals ( pId ) )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = free ( ( ( Let ) pExpression ).getE1 ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        Expression current = children.nextElement ( ) ;
        if ( ! current.free ( ).isEmpty ( ) )
        {
          LinkedList < Expression > tmpList ;
          tmpList = free ( current , pId ) ;
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
   * @return TODO
   */
  public LinkedList < Expression > getNotFree ( )
  {
    return this.notFree ;
  }


  /**
   * TODO
   * 
   * @param pIdentifiers
   * @param pId
   * @return TODO
   */
  private int identifierIndex ( String [ ] pIdentifiers , String pId )
  {
    for ( int i = 0 ; i < pIdentifiers.length ; i ++ )
    {
      if ( pIdentifiers [ i ].equals ( pId ) )
      {
        return i ;
      }
    }
    return - 1 ;
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
