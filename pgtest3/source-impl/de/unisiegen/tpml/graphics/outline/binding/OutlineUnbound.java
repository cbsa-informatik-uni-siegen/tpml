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
   * @param pExpression The input {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( ArrayList < String > pBounded ,
      CurriedLet pExpression )
  {
    // New bindings in E1, Identifier 1 to n
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      bounded.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( bounded , pExpression.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    pBounded.add ( pExpression.getIdentifiers ( 0 ) ) ;
    find ( pBounded , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link CurriedLetRec}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( ArrayList < String > pBounded ,
      CurriedLetRec pExpression )
  {
    // New bindings in E1, Identifier 0 to n
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      bounded.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( bounded , pExpression.getE1 ( ) ) ;
    // New bindings in E2, Identifier 0
    pBounded.add ( pExpression.getIdentifiers ( 0 ) ) ;
    find ( pBounded , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link CurriedMethod}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMethod ( ArrayList < String > pBounded ,
      CurriedMethod pExpression )
  {
    // New bindings in E, Identifier 1 to n
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      pBounded.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pBounded , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Identifier}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( ArrayList < String > pBounded ,
      Identifier pExpression )
  {
    if ( ! pBounded.contains ( pExpression.getName ( ) ) )
    {
      this.list.add ( pExpression ) ;
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Lambda}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( ArrayList < String > pBounded ,
      Lambda pExpression )
  {
    // New binding in E
    pBounded.add ( pExpression.getId ( ) ) ;
    find ( pBounded , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Let}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( ArrayList < String > pBounded , Let pExpression )
  {
    // No new binding in E1
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    find ( bounded , pExpression.getE1 ( ) ) ;
    // New binding in E2
    pBounded.add ( pExpression.getId ( ) ) ;
    find ( pBounded , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link LetRec}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( ArrayList < String > pBounded ,
      LetRec pExpression )
  {
    // New binding in E1
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    bounded.add ( pExpression.getId ( ) ) ;
    find ( bounded , pExpression.getE1 ( ) ) ;
    // New binding in E2
    pBounded.add ( pExpression.getId ( ) ) ;
    find ( pBounded , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link MultiLambda}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( ArrayList < String > pBounded ,
      MultiLambda pExpression )
  {
    // New bindings in E
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      pBounded.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pBounded , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link MultiLet}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( ArrayList < String > pBounded ,
      MultiLet pExpression )
  {
    // No new binding in E1
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    find ( bounded , pExpression.getE1 ( ) ) ;
    // New bindings in E2
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      pBounded.add ( pExpression.getIdentifiers ( i ) ) ;
    }
    find ( pBounded , pExpression.getE2 ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link ObjectExpr}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findObjectExpr ( ArrayList < String > pBounded ,
      ObjectExpr pExpression )
  {
    Row row = pExpression.getE ( ) ;
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    bounded.add ( pExpression.getId ( ) ) ;
    for ( Expression expr : row.getExpressions ( ) )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        /*
         * Search in the old list, because the Identifiers of Attributes are not
         * bound in other Attributes.
         */
        ArrayList < String > boundedAttr = new ArrayList < String > ( pBounded ) ;
        find ( boundedAttr , attribute ) ;
        // New binding in the rest of the row
        bounded.add ( attribute.getId ( ) ) ;
      }
      else
      {
        find ( bounded , expr ) ;
      }
    }
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Recursion}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( ArrayList < String > pBounded ,
      Recursion pExpression )
  {
    // New binding in E
    pBounded.add ( pExpression.getId ( ) ) ;
    find ( pBounded , pExpression.getE ( ) ) ;
  }


  /**
   * Finds the unbounded {@link Identifier}s in the given {@link Row}.
   * 
   * @param pBounded The list of bounded {@link Identifier}s.
   * @param pExpression The input {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( ArrayList < String > pBounded , Row pExpression )
  {
    ArrayList < String > bounded = new ArrayList < String > ( pBounded ) ;
    for ( Expression expr : pExpression.getExpressions ( ) )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        /*
         * Search in the old list, because the Identifiers of Attributes are not
         * bound in other Attributes.
         */
        ArrayList < String > boundedAttr = new ArrayList < String > ( pBounded ) ;
        find ( boundedAttr , attribute ) ;
        // New binding in the rest of the row
        bounded.add ( attribute.getId ( ) ) ;
      }
      else
      {
        find ( bounded , expr ) ;
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
