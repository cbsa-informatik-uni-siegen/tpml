package de.unisiegen.tpml.graphics.outline.binding ;


import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;


/**
 * Finds the unbound {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineUnbound
{
  /**
   * The <code>String</code> for the beginning of the find methods.
   */
  private static final String FIND = "find" ; //$NON-NLS-1$


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
    for ( Method method : this.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          FIND + pExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 3 ] ;
          argument [ 0 ] = pResult ;
          argument [ 1 ] = pBounded ;
          argument [ 2 ] = pExpression ;
          method.invoke ( this , argument ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        return ;
      }
    }
    Enumeration < Expression > children = pExpression.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      find ( pResult , pBounded , children.nextElement ( ) ) ;
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , CurriedLet pExpression )
  {
    ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
    ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
    // New bindings in E1, Identifier 1 to n
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      unbound1.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pResult , unbound1 , pExpression.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    unbound2.add ( pExpression.getIdentifiers ( 0 ) ) ;
    find ( pResult , unbound2 , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , CurriedLetRec pExpression )
  {
    ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
    ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
    // New bindings in E1, Identifier 0 to n
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      unbound1.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pResult , unbound1 , pExpression.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    unbound2.add ( pExpression.getIdentifiers ( 0 ) ) ;
    find ( pResult , unbound2 , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMeth ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , CurriedMeth pExpression )
  {
    ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
    // New bindings in E, Identifier 1 to n
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      unbound.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pResult , unbound , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Identifier pExpression )
  {
    if ( ! pBounded.contains ( pExpression.getName ( ) ) )
    {
      pResult.add ( pExpression ) ;
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Lambda pExpression )
  {
    ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
    // New binding in E
    unbound.add ( pExpression.getId ( ) ) ;
    find ( pResult , unbound , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Let pExpression )
  {
    ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
    ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
    // No new binding in E1
    find ( pResult , unbound1 , pExpression.getE1 ( ) ) ;
    // New binding in E2
    unbound2.add ( pExpression.getId ( ) ) ;
    find ( pResult , unbound2 , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , LetRec pExpression )
  {
    ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
    ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
    // New binding in E1
    unbound1.add ( pExpression.getId ( ) ) ;
    find ( pResult , unbound1 , pExpression.getE1 ( ) ) ;
    // New binding in E2
    unbound2.add ( pExpression.getId ( ) ) ;
    find ( pResult , unbound2 , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , MultiLambda pExpression )
  {
    ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
    // New bindings in E
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      unbound.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pResult , unbound , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , MultiLet pExpression )
  {
    ArrayList < String > unbound1 = new ArrayList < String > ( pBounded ) ;
    ArrayList < String > unbound2 = new ArrayList < String > ( pBounded ) ;
    // No new binding in E1
    find ( pResult , unbound1 , pExpression.getE1 ( ) ) ;
    // New bindings in E2
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      unbound2.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pResult , unbound2 , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Recursion pExpression )
  {
    ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
    // New binding in E
    unbound.add ( pExpression.getId ( ) ) ;
    find ( pResult , unbound , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the unbounded {@link Identifier}s.
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( ArrayList < Identifier > pResult ,
      ArrayList < String > pBounded , Row pExpression )
  {
    ArrayList < String > id = new ArrayList < String > ( ) ;
    for ( int i = 0 ; i < pExpression.getExpressions ( ).length ; i ++ )
    {
      if ( pExpression.getExpressions ( i ) instanceof Attr )
      {
        Attr expr = ( Attr ) pExpression.getExpressions ( i ) ;
        ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
        for ( int j = 0 ; j < i ; j ++ )
        {
          if ( id.get ( j ) != null )
          {
            unbound.add ( id.get ( j ) ) ;
          }
        }
        find ( pResult , unbound , expr ) ;
        id.add ( expr.getIdentifier ( ) ) ;
      }
      else if ( pExpression.getExpressions ( i ) instanceof Meth )
      {
        Meth expr = ( Meth ) pExpression.getExpressions ( i ) ;
        ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
        for ( int j = 0 ; j < i ; j ++ )
        {
          if ( id.get ( j ) != null )
          {
            unbound.add ( id.get ( j ) ) ;
          }
        }
        find ( pResult , unbound , expr ) ;
        id.add ( null ) ;
      }
      else if ( pExpression.getExpressions ( i ) instanceof CurriedMeth )
      {
        CurriedMeth expr = ( CurriedMeth ) pExpression.getExpressions ( i ) ;
        ArrayList < String > unbound = new ArrayList < String > ( pBounded ) ;
        for ( int j = 0 ; j < i ; j ++ )
        {
          if ( id.get ( j ) != null )
          {
            unbound.add ( id.get ( j ) ) ;
          }
        }
        find ( pResult , unbound , expr ) ;
        id.add ( null ) ;
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
