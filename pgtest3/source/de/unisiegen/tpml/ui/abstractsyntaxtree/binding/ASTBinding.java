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
  private Expression holeExpression ;


  /**
   * TODO
   */
  private LinkedList < String > identifier ;


  /**
   * TODO
   * 
   * @param pHoleExpression
   */
  public ASTBinding ( Expression pHoleExpression )
  {
    this.list = new LinkedList < LinkedList < Expression >> ( ) ;
    this.identifier = new LinkedList < String > ( ) ;
    this.holeExpression = pHoleExpression ;
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
    findBinding ( tmpList , pExpression , pId ) ;
    int found = - 1 ;
    for ( int i = 0 ; i < this.identifier.size ( ) ; i ++ )
    {
      if ( this.identifier.get ( i ).equals ( pId ) )
      {
        found = i ;
        break ;
      }
    }
    if ( found != - 1 )
    {
      this.list.set ( found , new LinkedList < Expression > ( ) ) ;
      this.identifier.set ( found , "" ) ;
    }
    this.identifier.add ( pId ) ;
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
      add ( pExpression , id ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pResult
   * @param pExpression
   * @param pId
   */
  private void findBinding ( LinkedList < Expression > pResult ,
      Expression pExpression , String pId )
  {
    // Identifier
    if ( ( pExpression instanceof Identifier )
        && ( ( ( Identifier ) pExpression ).getName ( ).equals ( pId ) ) )
    {
      pResult.add ( pExpression ) ;
      return ;
    }
    // CurriedLetRec
    else if ( ( pExpression instanceof CurriedLetRec )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( CurriedLetRec ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 ) )
    {
      CurriedLetRec curriedLetRec = ( CurriedLetRec ) pExpression ;
      if ( curriedLetRec.getIdentifiers ( 0 ).equals ( pId ) )
      {
        /*
         * Search is finished, because all Identifiers in E1 and E2 are bounded
         * to the Identifier in this child expression.
         */
        return ;
      }
      /*
       * Search only in E2, because all Identifiers in E1 are bounded to the
       * Identifier in this child expression.
       */
      findBinding ( pResult , curriedLetRec.getE2 ( ) , pId ) ;
      return ;
    }
    // CurriedLet
    else if ( ( pExpression instanceof CurriedLet )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex (
            ( ( CurriedLet ) pExpression ).getIdentifiers ( ) , pId ) >= 0 ) )
    {
      CurriedLet curriedLet = ( CurriedLet ) pExpression ;
      if ( curriedLet.getIdentifiers ( 0 ).equals ( pId ) )
      {
        /*
         * Search only in E1, because all Identifiers in E2 are bounded to the
         * Identifier in this child expression.
         */
        findBinding ( pResult , curriedLet.getE1 ( ) , pId ) ;
        return ;
      }
      /*
       * Search only in E2, because all Identifiers in E1 are bounded to the
       * Identifier in this child expression.
       */
      findBinding ( pResult , curriedLet.getE2 ( ) , pId ) ;
      return ;
    }
    // MultiLet
    else if ( ( pExpression instanceof MultiLet )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( MultiLet ) pExpression ).getIdentifiers ( ) ,
            pId ) >= 0 ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findBinding ( pResult , ( ( MultiLet ) pExpression ).getE1 ( ) , pId ) ;
      return ;
    }
    // MultiLambda
    else if ( ( pExpression instanceof MultiLambda )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( identifierIndex ( ( ( MultiLambda ) pExpression )
            .getIdentifiers ( ) , pId ) >= 0 ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
      return ;
    }
    // Lambda
    else if ( ( pExpression instanceof Lambda )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Lambda ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
      return ;
    }
    // LetRec
    else if ( ( pExpression instanceof LetRec )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( LetRec ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E1 and E2 are bounded to
       * the Identifier in this child expression.
       */
      return ;
    }
    // Let
    else if ( ( pExpression instanceof Let )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Let ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findBinding ( pResult , ( ( Let ) pExpression ).getE1 ( ) , pId ) ;
      return ;
    }
    // Recursion
    else if ( ( pExpression instanceof Recursion )
        && ( ! pExpression.equals ( this.holeExpression ) )
        && ( ( ( Recursion ) pExpression ).getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
      return ;
    }
    // Other
    else
    {
      /*
       * Search in all children.
       */
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        findBinding ( pResult , children.nextElement ( ) , pId ) ;
      }
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
