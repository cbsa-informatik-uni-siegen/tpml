package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.ArrayList ;
import java.util.Enumeration ;
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
 * Finds the bounded Identifiers in a given Expression.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTBinding
{
  /**
   * The list of lists of Identifiers, which are bounded by the given Identifier
   * in the hole Expression.
   */
  private ArrayList < ArrayList < Identifier >> list ;


  /**
   * The hole Expression.
   */
  private Expression holeExpression ;


  /**
   * The list of Identifiers.
   */
  private ArrayList < String > identifierList ;


  /**
   * The list of Expressions.
   */
  private ArrayList < Expression > expressionList ;


  /**
   * Initilizes the lists and sets the hole Expression.
   * 
   * @param pHoleExpression
   */
  public ASTBinding ( Expression pHoleExpression )
  {
    this.list = new ArrayList < ArrayList < Identifier >> ( ) ;
    this.identifierList = new ArrayList < String > ( ) ;
    this.expressionList = new ArrayList < Expression > ( ) ;
    this.holeExpression = pHoleExpression ;
  }


  /**
   * Adds a Expression and the name of the Identifier to their list. After
   * adding all Expressions, the search prozess can be started by calling the
   * find method.
   * 
   * @param pExpression The Expression.
   * @param pId The Identifier.
   */
  public void add ( Expression pExpression , String pId )
  {
    this.expressionList.add ( pExpression ) ;
    this.identifierList.add ( pId ) ;
  }


  /**
   * Finds the bounded Identifiers in the given Expression.
   */
  public void find ( )
  {
    // MultiLet || MultiLambda
    if ( ( this.holeExpression instanceof MultiLet )
        || ( this.holeExpression instanceof MultiLambda ) )
    {
      /*
       * Search for an Identifier with the same name as the current, starting
       * with the first Identifier. If someone is found, the current Expression
       * and the current Identifier is set to null, because he has no bindings.
       */
      for ( int i = 0 ; i < this.identifierList.size ( ) - 1 ; i ++ )
      {
        for ( int j = i + 1 ; j < this.identifierList.size ( ) ; j ++ )
        {
          if ( this.identifierList.get ( i ).equals (
              this.identifierList.get ( j ) ) )
          {
            this.identifierList.set ( i , null ) ;
            this.expressionList.set ( i , null ) ;
            break ;
          }
        }
      }
    }
    // CurriedLetRec
    else if ( this.holeExpression instanceof CurriedLetRec )
    {
      /*
       * Search for an Identifier with the same name as the first Identifier. If
       * someone is found, the first Identifier binds only in E2.
       */
      for ( int i = 1 ; i < this.identifierList.size ( ) ; i ++ )
      {
        if ( this.identifierList.get ( 0 ).equals (
            this.identifierList.get ( i ) ) )
        {
          this.expressionList.set ( 0 ,
              ( ( CurriedLetRec ) this.holeExpression ).getE2 ( ) ) ;
          break ;
        }
      }
      /*
       * Search for an Identifier with the same name as the current, starting
       * with the second Identifier. If someone is found, the current Expression
       * and the current Identifier is set to null, because he has no bindings.
       */
      for ( int i = 1 ; i < this.identifierList.size ( ) - 1 ; i ++ )
      {
        for ( int j = i + 1 ; j < this.identifierList.size ( ) ; j ++ )
        {
          if ( this.identifierList.get ( i ).equals (
              this.identifierList.get ( j ) ) )
          {
            this.identifierList.set ( i , null ) ;
            this.expressionList.set ( i , null ) ;
            break ;
          }
        }
      }
    }
    // CurriedLet
    else if ( this.holeExpression instanceof CurriedLet )
    {
      /*
       * Search for an Identifier with the same name as the current, starting
       * with the second Identifier. If someone is found, the current Expression
       * and the current Identifier is set to null, because he has no bindings.
       */
      for ( int i = 1 ; i < this.identifierList.size ( ) - 1 ; i ++ )
      {
        for ( int j = i + 1 ; j < this.identifierList.size ( ) ; j ++ )
        {
          if ( this.identifierList.get ( i ).equals (
              this.identifierList.get ( j ) ) )
          {
            this.identifierList.set ( i , null ) ;
            this.expressionList.set ( i , null ) ;
            break ;
          }
        }
      }
    }
    ArrayList < Identifier > tmpList ;
    for ( int i = 0 ; i < this.identifierList.size ( ) ; i ++ )
    {
      tmpList = new ArrayList < Identifier > ( ) ;
      /*
       * If the current Identifier is null, he has no bindings, so it is not
       * necessary to search for them.
       */
      if ( this.identifierList.get ( i ) != null )
      {
        find ( tmpList , this.expressionList.get ( i ) , this.identifierList
            .get ( i ) ) ;
      }
      this.list.add ( tmpList ) ;
    }
  }


  /**
   * Finds the bounded Identifiers in the given Expression.
   * 
   * @param pResult The list of the bounded Identifiers.
   * @param pExpression The input Expression.
   * @param pId The name of the Identifier.
   */
  private void find ( ArrayList < Identifier > pResult ,
      Expression pExpression , String pId )
  {
    // Identifier
    if ( ( pExpression instanceof Identifier )
        && ( ( ( Identifier ) pExpression ).getName ( ).equals ( pId ) ) )
    {
      pResult.add ( ( Identifier ) pExpression ) ;
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
      find ( pResult , curriedLetRec.getE2 ( ) , pId ) ;
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
        find ( pResult , curriedLet.getE1 ( ) , pId ) ;
        return ;
      }
      /*
       * Search only in E2, because all Identifiers in E1 are bounded to the
       * Identifier in this child expression.
       */
      find ( pResult , curriedLet.getE2 ( ) , pId ) ;
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
      find ( pResult , ( ( MultiLet ) pExpression ).getE1 ( ) , pId ) ;
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
      find ( pResult , ( ( Let ) pExpression ).getE1 ( ) , pId ) ;
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
        find ( pResult , children.nextElement ( ) , pId ) ;
      }
    }
  }


  /**
   * Returns the bounded Identifier in the Expression.
   * 
   * @param pIdentifierIndex The index of the Identifier.
   * @param pBindingIndex The index of the binding.
   * @return The bounded Identifier in the Expression.
   */
  public Identifier get ( int pIdentifierIndex , int pBindingIndex )
  {
    return this.list.get ( pIdentifierIndex ).get ( pBindingIndex ) ;
  }


  /**
   * Returns the index of the given Identifier in the given Identifiers Array.
   * If the Identifier is not in the array it returns -1.
   * 
   * @param pIdentifiers
   * @param pId
   * @return The index of the given Identifier in the given Identifiers Array.
   *         If the Identifier is not in the array it returns -1.
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
   * Returns the size of the list. The size is equal to the number of
   * Identifiers.
   * 
   * @return The number of Identifiers.
   */
  public int size ( )
  {
    return this.list.size ( ) ;
  }


  /**
   * Returns the number of bindings from a given Identifier.
   * 
   * @param pIdentifierIndex The index of the Identifier.
   * @return The number of bindings from a given Identifier.
   */
  public int size ( int pIdentifierIndex )
  {
    return this.list.get ( pIdentifierIndex ).size ( ) ;
  }
}
