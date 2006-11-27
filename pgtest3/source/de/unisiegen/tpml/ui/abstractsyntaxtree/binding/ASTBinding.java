package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;


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
    LinkedList < Expression > tmpList = new LinkedList < Expression > ( ) ;
    findBinding ( tmpList , new LinkedList < String > ( ) , pExpression , pId ) ;
    this.list.add ( tmpList ) ;
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
      LinkedList < Expression > tmpList = new LinkedList < Expression > ( ) ;
      findBinding ( tmpList , new LinkedList < String > ( ) , pExpression , id ) ;
      this.list.add ( tmpList ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pList
   * @return TODO
   */
  private LinkedList < String > copyList ( LinkedList < String > pList )
  {
    LinkedList < String > result = new LinkedList < String > ( ) ;
    for ( String s : pList )
    {
      result.add ( s ) ;
    }
    return result ;
  }


  /**
   * TODO
   * 
   * @param pResult
   * @param pBound
   * @param pExpression
   * @param pId
   */
  // TODO Test, test, test ...
  private void findBinding ( LinkedList < Expression > pResult ,
      LinkedList < String > pBound , Expression pExpression , String pId )
  {
    if ( ( pExpression instanceof Identifier )
        && ( ! pBound.contains ( ( ( Identifier ) pExpression ).getName ( ) ) && ( ( Identifier ) pExpression )
            .getName ( ).equals ( pId ) ) )
    {
      pResult.add ( pExpression ) ;
      return ;
    }
    else if ( ( pExpression instanceof CurriedLetRec )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( CurriedLetRec ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 ) )
    {
      CurriedLetRec curriedLetRec = ( CurriedLetRec ) pExpression ;
      if ( curriedLetRec.getIdentifiers ( 0 ).equals ( pId ) )
      {
        LinkedList < String > newBound1 = copyList ( pBound ) ;
        LinkedList < String > newBound2 = copyList ( pBound ) ;
        newBound1.add ( curriedLetRec.getIdentifiers ( 0 ) ) ;
        newBound2.add ( curriedLetRec.getIdentifiers ( 0 ) ) ;
        findBinding ( pResult , newBound1 , curriedLetRec.getE1 ( ) , pId ) ;
        findBinding ( pResult , newBound2 , curriedLetRec.getE2 ( ) , pId ) ;
        return ;
      }
      else if ( identifierIndex ( curriedLetRec.getIdentifiers ( ) , pId ) > 0 )
      {
        LinkedList < String > newBound = copyList ( pBound ) ;
        newBound.add ( curriedLetRec.getIdentifiers ( identifierIndex (
            curriedLetRec.getIdentifiers ( ) , pId ) ) ) ;
        findBinding ( pResult , newBound , curriedLetRec.getE1 ( ) , pId ) ;
        return ;
      }
    }
    else if ( ( pExpression instanceof CurriedLet )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex (
            ( ( CurriedLet ) pExpression ).getIdentifiers ( ) , pId ) >= 0 ) )
    {
      CurriedLet curriedLet = ( CurriedLet ) pExpression ;
      if ( curriedLet.getIdentifiers ( 0 ).equals ( pId ) )
      {
        LinkedList < String > newBound = copyList ( pBound ) ;
        newBound.add ( curriedLet.getIdentifiers ( 0 ) ) ;
        findBinding ( pResult , newBound , curriedLet.getE2 ( ) , pId ) ;
        return ;
      }
      else if ( identifierIndex ( curriedLet.getIdentifiers ( ) , pId ) > 0 )
      {
        LinkedList < String > newBound = copyList ( pBound ) ;
        newBound.add ( curriedLet.getIdentifiers ( identifierIndex ( curriedLet
            .getIdentifiers ( ) , pId ) ) ) ;
        findBinding ( pResult , newBound , curriedLet.getE1 ( ) , pId ) ;
        return ;
      }
    }
    // MultiLet
    else if ( ( pExpression instanceof MultiLet )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( MultiLet ) pExpression ).getIdentifiers ( ) ,
            pId ) >= 0 ) )
    {
      MultiLet multiLet = ( MultiLet ) pExpression ;
      LinkedList < String > newBound = copyList ( pBound ) ;
      findBinding ( pResult , newBound , multiLet.getE1 ( ) , pId ) ;
      return ;
    }
    else if ( ( pExpression instanceof MultiLambda )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( MultiLambda ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 ) )
    {
      return ;
    }
    else if ( ( pExpression instanceof Lambda )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Lambda ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      return ;
    }
    else if ( ( pExpression instanceof LetRec )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( LetRec ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      return ;
    }
    else if ( ( pExpression instanceof Let )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Let ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      Let let = ( Let ) pExpression ;
      LinkedList < String > newBound = copyList ( pBound ) ;
      findBinding ( pResult , newBound , let.getE1 ( ) , pId ) ;
      return ;
    }
    else if ( ( pExpression instanceof Recursion )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Recursion ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      return ;
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        Expression child = children.nextElement ( ) ;
        findBinding ( pResult , pBound , child , pId ) ;
      }
    }
  }


  /**
   * TODO
   * 
   * @param pExpression
   * @param pId
   * @return TODO
   */
  @ SuppressWarnings ( "unused" )
  private LinkedList < Expression > findBindingOld ( Expression pExpression ,
      String pId )
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
    /* && ( ! pExpression.equals ( this.holeExpression ) ) */)
    {
      int index = identifierIndex ( ( ( CurriedLetRec ) pExpression )
          .getIdentifiers ( ) , pId ) ;
      LinkedList < Expression > tmpList ;
      if ( index == 0 )
      {
        return result ;
      }
      tmpList = findBindingOld ( ( ( CurriedLetRec ) pExpression ).getE2 ( ) ,
          pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof CurriedLet )
        && ( identifierIndex (
            ( ( CurriedLet ) pExpression ).getIdentifiers ( ) , pId ) >= 0 )
    /* && ( ! pExpression.equals ( this.holeExpression ) ) */)
    {
      int index = identifierIndex ( ( ( CurriedLet ) pExpression )
          .getIdentifiers ( ) , pId ) ;
      LinkedList < Expression > tmpList ;
      if ( index == 0 )
      {
        tmpList = findBindingOld ( ( ( CurriedLet ) pExpression ).getE1 ( ) ,
            pId ) ;
      }
      else
      {
        tmpList = findBindingOld ( ( ( CurriedLet ) pExpression ).getE2 ( ) ,
            pId ) ;
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
      tmpList = findBindingOld ( ( ( MultiLet ) pExpression ).getE1 ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof LetRec )
        && ( ( ( LetRec ) pExpression ).getId ( ).equals ( pId ) )
    /* && ( ! pExpression.equals ( this.holeExpression ) ) */)
    {
      return result ;
    }
    else if ( ( pExpression instanceof Let )
        && ( ( ( Let ) pExpression ).getId ( ).equals ( pId ) )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = findBindingOld ( ( ( Let ) pExpression ).getE1 ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof MultiLambda )
        && ( identifierIndex ( ( ( MultiLambda ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = findBindingOld ( ( ( MultiLambda ) pExpression ).getE ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof Lambda )
        && ( ( ( Lambda ) pExpression ).getId ( ).equals ( pId ) )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = findBindingOld ( ( ( Lambda ) pExpression ).getE ( ) , pId ) ;
      for ( Expression tmpExpr : tmpList )
      {
        result.add ( tmpExpr ) ;
      }
      return result ;
    }
    else if ( ( pExpression instanceof Recursion )
        && ( ( ( Recursion ) pExpression ).getId ( ).equals ( pId ) )
        && ( ! pExpression.equals ( this.holeExpression ) ) )
    {
      LinkedList < Expression > tmpList ;
      tmpList = findBindingOld ( ( ( Recursion ) pExpression ).getE ( ) , pId ) ;
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
          tmpList = findBindingOld ( current , pId ) ;
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
