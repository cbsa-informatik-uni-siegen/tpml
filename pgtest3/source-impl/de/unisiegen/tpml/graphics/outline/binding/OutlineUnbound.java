package de.unisiegen.tpml.graphics.outline.binding ;


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
 * Finds the unbound {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class OutlineUnbound
{
  /**
   * The list of unbound {@link Identifier}s in the {@link Expression}.
   */
  private ArrayList < Identifier > unboundList ;


  /**
   * Initilizes the lists and finds the unbound {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineUnbound ( Expression pExpression )
  {
    this.unboundList = new ArrayList < Identifier > ( ) ;
    ArrayList < String > bounded = new ArrayList < String > ( ) ;
    find ( this.unboundList , bounded , pExpression ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  private final void find ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Expression pExpression )
  {
    if ( ( pExpression instanceof Identifier )
        && ( ! pBounded.contains ( ( ( Identifier ) pExpression ).getName ( ) ) ) )
    {
      pResult.add ( ( Identifier ) pExpression ) ;
      return ;
    }
    if ( pExpression instanceof CurriedLetRec )
    {
      CurriedLetRec curriedLetRec = ( CurriedLetRec ) pExpression ;
      ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
      ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
      // New bindings in E1, Identifier 0 to n
      for ( int i = 0 ; i < curriedLetRec.getIdentifiers ( ).length ; i ++ )
      {
        unbound1.add ( curriedLetRec.getIdentifiers ( i ) ) ;
      }
      find ( pResult , unbound1 , curriedLetRec.getE1 ( ) ) ;
      // New bindings in E2, Identifier 0
      unbound2.add ( curriedLetRec.getIdentifiers ( 0 ) ) ;
      find ( pResult , unbound2 , curriedLetRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof CurriedLet )
    {
      CurriedLet curriedLet = ( CurriedLet ) pExpression ;
      ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
      ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
      // New bindings in E1, Identifier 1 to n
      for ( int i = 1 ; i < curriedLet.getIdentifiers ( ).length ; i ++ )
      {
        unbound1.add ( curriedLet.getIdentifiers ( i ) ) ;
      }
      find ( pResult , unbound1 , curriedLet.getE1 ( ) ) ;
      // New bindings in E2, Identifier 0
      unbound2.add ( curriedLet.getIdentifiers ( 0 ) ) ;
      find ( pResult , unbound2 , curriedLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLet )
    {
      MultiLet multiLet = ( MultiLet ) pExpression ;
      ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
      ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
      // No new binding in E1
      find ( pResult , unbound1 , multiLet.getE1 ( ) ) ;
      // New bindings in E2
      for ( int i = 0 ; i < multiLet.getIdentifiers ( ).length ; i ++ )
      {
        unbound2.add ( multiLet.getIdentifiers ( i ) ) ;
      }
      find ( pResult , unbound2 , multiLet.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof MultiLambda )
    {
      MultiLambda multiLambda = ( MultiLambda ) pExpression ;
      ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
      // New bindings in E
      for ( int i = 0 ; i < multiLambda.getIdentifiers ( ).length ; i ++ )
      {
        unbound.add ( multiLambda.getIdentifiers ( i ) ) ;
      }
      find ( pResult , unbound , multiLambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof LetRec )
    {
      LetRec letRec = ( LetRec ) pExpression ;
      ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
      ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
      // New binding in E1
      unbound1.add ( letRec.getId ( ) ) ;
      find ( pResult , unbound1 , letRec.getE1 ( ) ) ;
      // New binding in E2
      unbound2.add ( letRec.getId ( ) ) ;
      find ( pResult , unbound2 , letRec.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Let )
    {
      Let let = ( Let ) pExpression ;
      ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
      ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
      // No new binding in E1
      find ( pResult , unbound1 , let.getE1 ( ) ) ;
      // New binding in E2
      unbound2.add ( let.getId ( ) ) ;
      find ( pResult , unbound2 , let.getE2 ( ) ) ;
    }
    else if ( pExpression instanceof Lambda )
    {
      Lambda lambda = ( Lambda ) pExpression ;
      ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
      // New binding in E
      unbound.add ( lambda.getId ( ) ) ;
      find ( pResult , unbound , lambda.getE ( ) ) ;
    }
    else if ( pExpression instanceof Recursion )
    {
      Recursion recursion = ( Recursion ) pExpression ;
      ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
      // New binding in E
      unbound.add ( recursion.getId ( ) ) ;
      find ( pResult , unbound , recursion.getE ( ) ) ;
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        find ( pResult , pBounded , children.nextElement ( ) ) ;
      }
    }
  }


  /**
   * Returns the unbound {@link Identifier} in the {@link Expression}.
   * 
   * @param pIndex The index of the unbound {@link Identifier}.
   * @return The unbound {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( int pIndex )
  {
    return this.unboundList.get ( pIndex ) ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of unbound
   * {@link Identifier}s.
   * 
   * @return The number of unbound {@link Identifier}s.
   */
  public final int size ( )
  {
    return this.unboundList.size ( ) ;
  }
}
