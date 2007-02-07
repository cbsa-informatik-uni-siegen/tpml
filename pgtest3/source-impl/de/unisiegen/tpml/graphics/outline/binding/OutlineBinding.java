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
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;


/**
 * Finds the bounded {@link Identifier}s in a given {@link Expression}.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineBinding
{
  /**
   * The <code>String</code> for the beginning of the find methods.
   */
  private static final String FIND = "find" ; //$NON-NLS-1$


  /**
   * The list of {@link Identifier}s, which are bounded by the given
   * {@link Identifier} in the given {@link Expression}.
   */
  private ArrayList < Identifier > list ;


  /**
   * The {@link Identifier} which binds.
   */
  private String identifier ;


  /**
   * The {@link Expression} in which should be searched.
   */
  private Expression expression ;


  /**
   * The {@link Expression} in which the {@link Identifier} is bounded.
   */
  private Expression boundedExpression ;


  /**
   * The start index of the {@link Identifier} in the boundedExpression.
   */
  private int boundedStart ;


  /**
   * The end index of the {@link Identifier} in the boundedExpression.
   */
  private int boundedEnd ;


  /**
   * The index of the {@link Identifier} in the boundedExpression.
   */
  private int boundedIdentifierIndex ;


  /**
   * Initilizes the list.
   */
  public OutlineBinding ( )
  {
    this.list = new ArrayList < Identifier > ( ) ;
    this.boundedExpression = null ;
    this.boundedStart = OutlineNode.NO_BINDING ;
    this.boundedEnd = OutlineNode.NO_BINDING ;
    this.boundedIdentifierIndex = OutlineNode.NO_BINDING ;
  }


  /**
   * Initilizes the list and sets the bounded values.
   * 
   * @param pBoundedExpression The {@link Expression} in which this
   *          {@link Identifier} is bounded.
   * @param pBoundedStart The start index of this {@link Identifier} in the
   *          boundedExpression.
   * @param pBoundedEnd The end index of this {@link Identifier} in the
   *          boundedExpression.
   * @param pBoundedIdentifierIndex The index of this {@link Identifier} in the
   *          boundedExpression.
   */
  public OutlineBinding ( Expression pBoundedExpression , int pBoundedStart ,
      int pBoundedEnd , int pBoundedIdentifierIndex )
  {
    this.list = new ArrayList < Identifier > ( ) ;
    this.boundedExpression = pBoundedExpression ;
    this.boundedStart = pBoundedStart ;
    this.boundedEnd = pBoundedEnd ;
    this.boundedIdentifierIndex = pBoundedIdentifierIndex ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The {@link Expression}.
   * @param pId The {@link Identifier}.
   */
  public final void find ( Expression pExpression , String pId )
  {
    this.expression = pExpression ;
    this.identifier = pId ;
    findExpression ( this.expression ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Attr}.
   * 
   * @param pExpression The input {@link Attr}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findAttr ( Attr pExpression )
  {
    findExpression ( pExpression.getE ( ) ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedLet}.
   * 
   * @param pExpression The input {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( CurriedLet pExpression )
  {
    if ( isInArray ( pExpression.getIdentifiers ( ) , this.identifier ) )
    {
      if ( pExpression.getIdentifiers ( 0 ).equals ( this.identifier ) )
      {
        /*
         * Search is finished, if the searched Identifier is also equal to
         * another bounded Identifier in the CurriedLet, because all Identifiers
         * in E1 are bounded to the Identifier in this child expression.
         */
        for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
        {
          if ( pExpression.getIdentifiers ( i ).equals ( this.identifier ) )
          {
            return ;
          }
        }
        /*
         * Search only in E1, because all Identifiers in E2 are bounded to the
         * Identifier in this child expression.
         */
        findExpression ( pExpression.getE1 ( ) ) ;
        return ;
      }
      /*
       * Search only in E2, because all Identifiers in E1 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pExpression.getE2 ( ) ) ;
    }
    else
    {
      findExpression ( pExpression.getE1 ( ) ) ;
      findExpression ( pExpression.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedLetRec}.
   * 
   * @param pExpression The input {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( CurriedLetRec pExpression )
  {
    if ( isInArray ( pExpression.getIdentifiers ( ) , this.identifier ) )
    {
      if ( pExpression.getIdentifiers ( 0 ).equals ( this.identifier ) )
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
      findExpression ( pExpression.getE2 ( ) ) ;
    }
    else
    {
      findExpression ( pExpression.getE1 ( ) ) ;
      findExpression ( pExpression.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedMeth}.
   * 
   * @param pExpression The input {@link CurriedMeth}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMeth ( CurriedMeth pExpression )
  {
    if ( isInArray ( pExpression.getIdentifiers ( ) , this.identifier ) )
    {
      /*
       * Search is finished, if the searched Identifier is also equal to another
       * bounded Identifier in the CurriedMeth, because all Identifiers in E are
       * bounded to the Identifier in this child expression.
       */
      for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals ( this.identifier ) )
        {
          return ;
        }
      }
      /*
       * Continue search in E.
       */
      findExpression ( pExpression.getE ( ) ) ;
    }
    else
    {
      findExpression ( pExpression.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  private final void findExpression ( Expression pExpression )
  {
    for ( Method method : this.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          FIND + pExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 1 ] ;
          argument [ 0 ] = pExpression ;
          method.invoke ( this , argument ) ;
          return ;
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
      }
    }
    /*
     * Search in all children.
     */
    Enumeration < Expression > children = pExpression.children ( ) ;
    while ( children.hasMoreElements ( ) )
    {
      findExpression ( children.nextElement ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Identifier}.
   * 
   * @param pExpression The input {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( Identifier pExpression )
  {
    if ( pExpression.getName ( ).equals ( this.identifier ) )
    {
      this.list.add ( pExpression ) ;
      pExpression.boundedExpression ( this.boundedExpression ) ;
      pExpression.boundedStart ( this.boundedStart ) ;
      pExpression.boundedEnd ( this.boundedEnd ) ;
      pExpression.boundedIdentifierIndex ( this.boundedIdentifierIndex ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Lambda}.
   * 
   * @param pExpression The input {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( Lambda pExpression )
  {
    if ( pExpression.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pExpression.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Let}.
   * 
   * @param pExpression The input {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( Let pExpression )
  {
    if ( pExpression.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pExpression.getE1 ( ) ) ;
    }
    else
    {
      findExpression ( pExpression.getE1 ( ) ) ;
      findExpression ( pExpression.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link LetRec}.
   * 
   * @param pExpression The input {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( LetRec pExpression )
  {
    if ( pExpression.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E1 and E2 are bounded to
       * the Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pExpression.getE1 ( ) ) ;
      findExpression ( pExpression.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Meth}.
   * 
   * @param pExpression The input {@link Meth}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMeth ( Meth pExpression )
  {
    findExpression ( pExpression.getE ( ) ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link MultiLambda}.
   * 
   * @param pExpression The input {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( MultiLambda pExpression )
  {
    if ( isInArray ( pExpression.getIdentifiers ( ) , this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pExpression.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link MultiLet}.
   * 
   * @param pExpression The input {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( MultiLet pExpression )
  {
    if ( isInArray ( pExpression.getIdentifiers ( ) , this.identifier ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pExpression.getE1 ( ) ) ;
    }
    else
    {
      findExpression ( pExpression.getE1 ( ) ) ;
      findExpression ( pExpression.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link ObjectExpr}.
   * 
   * @param pExpression The input {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findObjectExpr ( ObjectExpr pExpression )
  {
    if ( pExpression.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pExpression.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Recursion}.
   * 
   * @param pExpression The input {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( Recursion pExpression )
  {
    if ( pExpression.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pExpression.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Row}.
   * 
   * @param pExpression The input {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( Row pExpression )
  {
    boolean equalIdFound = false ;
    for ( int i = 0 ; i < pExpression.getExpressions ( ).length ; i ++ )
    {
      Expression child = pExpression.getExpressions ( i ) ;
      if ( child instanceof Attr )
      {
        Attr attr = ( Attr ) child ;
        findExpression ( attr ) ;
        if ( attr.getId ( ).equals ( this.identifier ) )
        {
          /*
           * Search is finished for Meth and CurriedMeth, because all
           * Identifiers in the Row are bounded to the Identifier in this Attr.
           * Identifiers in Attr can still be bound, so the search is not
           * finished.
           */
          equalIdFound = true ;
        }
      }
      else if ( child instanceof Meth )
      {
        if ( ! equalIdFound )
        {
          findExpression ( child ) ;
        }
      }
      else if ( child instanceof CurriedMeth )
      {
        if ( ! equalIdFound )
        {
          findExpression ( child ) ;
        }
      }
    }
  }


  /**
   * Returns the bounded {@link Identifier} in the {@link Expression}.
   * 
   * @param pIndex The index of the {@link Identifier}.
   * @return The bounded {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( int pIndex )
  {
    return this.list.get ( pIndex ) ;
  }


  /**
   * Returns true, if the given {@link Identifier} is in the given
   * {@link Identifier}s array, otherwise false.
   * 
   * @param pIdentifiers The {@link Identifier} array.
   * @param pId The single {@link Identifier}.
   * @return True, if the given {@link Identifier} is in the given
   *         {@link Identifier}s array, otherwise false.
   */
  private final boolean isInArray ( String [ ] pIdentifiers , String pId )
  {
    for ( String id : pIdentifiers )
    {
      if ( id.equals ( pId ) )
      {
        return true ;
      }
    }
    return false ;
  }


  /**
   * Returns the size of the list. The size is equal to the number of bounded
   * {@link Identifier}s.
   * 
   * @return The number of {@link Identifier}s.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
