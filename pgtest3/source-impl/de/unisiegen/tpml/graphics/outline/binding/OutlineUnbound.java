package de.unisiegen.tpml.graphics.outline.binding ;


import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
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
  private ArrayList < Identifier > list ;


  /**
   * Initilizes the list and finds the unbound {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public OutlineUnbound ( Expression pExpression )
  {
    this.list = new ArrayList < Identifier > ( ) ;
    find ( new ArrayList < String > ( ) , pExpression ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   */
  private final void find ( ArrayList < String > pBounded ,
      Expression pExpression )
  {
    for ( Method method : this.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          FIND + pExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 2 ] ;
          argument [ 0 ] = pBounded ;
          argument [ 1 ] = pExpression ;
          method.invoke ( this , argument ) ;
        }
        catch ( IllegalArgumentException e )
        {
          System.err.println ( "IllegalArgumentException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + FIND //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "IllegalAccessException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + FIND //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "InvocationTargetException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + FIND //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        return ;
      }
    }
    Enumeration < Expression > children = pExpression.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      find ( pBounded , children.nextElement ( ) ) ;
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link CurriedLet}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pCurriedLet The input {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( ArrayList < String > pBounded ,
      CurriedLet pCurriedLet )
  {
    // New bindings in E1, Identifier 1 to n
    ArrayList < String > bounded1 = new ArrayList < String > ( pBounded ) ;
    for ( int i = 1 ; i < pCurriedLet.getIdentifiers ( ).length ; i ++ )
    {
      bounded1.add ( pCurriedLet.getIdentifiers ( i ) ) ;
    }
    find ( bounded1 , pCurriedLet.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    ArrayList < String > bounded2 = new ArrayList < String > ( pBounded ) ;
    bounded2.add ( pCurriedLet.getIdentifiers ( 0 ) ) ;
    find ( bounded2 , pCurriedLet.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link CurriedLetRec}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pCurriedLetRec The input {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( ArrayList < String > pBounded ,
      CurriedLetRec pCurriedLetRec )
  {
    // New bindings in E1, Identifier 0 to n
    ArrayList < String > bounded1 = new ArrayList < String > ( pBounded ) ;
    for ( int i = 0 ; i < pCurriedLetRec.getIdentifiers ( ).length ; i ++ )
    {
      bounded1.add ( pCurriedLetRec.getIdentifiers ( i ) ) ;
    }
    find ( bounded1 , pCurriedLetRec.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    ArrayList < String > bounded2 = new ArrayList < String > ( pBounded ) ;
    bounded2.add ( pCurriedLetRec.getIdentifiers ( 0 ) ) ;
    find ( bounded2 , pCurriedLetRec.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link CurriedMethod}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pCurriedMethod The input {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMethod ( ArrayList < String > pBounded ,
      CurriedMethod pCurriedMethod )
  {
    // New bindings in E, Identifier 1 to n
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( int i = 1 ; i < pCurriedMethod.getIdentifiers ( ).length ; i ++ )
    {
      bounded.add ( pCurriedMethod.getIdentifiers ( i ) ) ;
    }
    find ( bounded , pCurriedMethod.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Identifier}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pIdentifier The input {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( ArrayList < String > pBounded ,
      Identifier pIdentifier )
  {
    if ( ! pBounded.contains ( pIdentifier.getName ( ) ) )
    {
      this.list.add ( pIdentifier ) ;
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Lambda}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pLambda The input {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( ArrayList < String > pBounded , Lambda pLambda )
  {
    // New binding in E
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    bounded.add ( pLambda.getId ( ) ) ;
    find ( bounded , pLambda.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Let}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pLet The input {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( ArrayList < String > pBounded , Let pLet )
  {
    // No new binding in E1
    ArrayList < String > bounded1 = new ArrayList < String > ( pBounded ) ;
    find ( bounded1 , pLet.getE1 ( ) ) ;
    // New binding in E2
    ArrayList < String > bounded2 = new ArrayList < String > ( pBounded ) ;
    bounded2.add ( pLet.getId ( ) ) ;
    find ( bounded2 , pLet.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link LetRec}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pLetRec The input {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( ArrayList < String > pBounded , LetRec pLetRec )
  {
    // New binding in E1
    ArrayList < String > bounded1 = new ArrayList < String > ( pBounded ) ;
    bounded1.add ( pLetRec.getId ( ) ) ;
    find ( bounded1 , pLetRec.getE1 ( ) ) ;
    // New binding in E2
    ArrayList < String > bounded2 = new ArrayList < String > ( pBounded ) ;
    bounded2.add ( pLetRec.getId ( ) ) ;
    find ( bounded2 , pLetRec.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link MultiLambda}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pMultiLambda The input {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( ArrayList < String > pBounded ,
      MultiLambda pMultiLambda )
  {
    // New bindings in E
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( int i = 0 ; i < pMultiLambda.getIdentifiers ( ).length ; i ++ )
    {
      bounded.add ( pMultiLambda.getIdentifiers ( i ) ) ;
    }
    find ( bounded , pMultiLambda.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link MultiLet}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pMultiLet The input {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( ArrayList < String > pBounded ,
      MultiLet pMultiLet )
  {
    // No new binding in E1
    ArrayList < String > bounded1 = new ArrayList < String > ( pBounded ) ;
    find ( bounded1 , pMultiLet.getE1 ( ) ) ;
    // New bindings in E2
    ArrayList < String > bounded2 = new ArrayList < String > ( pBounded ) ;
    for ( int i = 0 ; i < pMultiLet.getIdentifiers ( ).length ; i ++ )
    {
      bounded2.add ( pMultiLet.getIdentifiers ( i ) ) ;
    }
    find ( bounded2 , pMultiLet.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link ObjectExpr}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pObjectExpr The input {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findObjectExpr ( ArrayList < String > pBounded ,
      ObjectExpr pObjectExpr )
  {
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    bounded.add ( pObjectExpr.getId ( ) ) ;
    find ( new ArrayList < String > ( bounded ) , pObjectExpr.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Recursion}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pRecursion The input {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( ArrayList < String > pBounded ,
      Recursion pRecursion )
  {
    // New binding in E
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    bounded.add ( pRecursion.getId ( ) ) ;
    find ( bounded , pRecursion.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Row}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pRow The input {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( ArrayList < String > pBounded , Row pRow )
  {
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( Expression expr : pRow.getExpressions ( ) )
    {
      find ( new ArrayList < String > ( bounded ) , expr ) ;
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        // New binding in the rest of the row
        bounded.add ( attribute.getId ( ) ) ;
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
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of unbound
   * {@link Identifier}s.
   * 
   * @return The number of unbound {@link Identifier}s.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
