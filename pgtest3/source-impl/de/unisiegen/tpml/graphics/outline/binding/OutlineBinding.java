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
   * The {@link OutlineUnbound} from which the founded {@link Identifier}s
   * should be removed.
   */
  private OutlineUnbound outlineUnbound ;


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
   * @param pOutlineUnbound The {@link OutlineUnbound}.
   */
  public final void find ( Expression pExpression , String pId ,
      OutlineUnbound pOutlineUnbound )
  {
    this.expression = pExpression ;
    this.identifier = pId ;
    this.outlineUnbound = pOutlineUnbound ;
    findExpression ( this.expression ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedLet}.
   * 
   * @param pCurriedLet The input {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( CurriedLet pCurriedLet )
  {
    if ( pCurriedLet.getIdentifiers ( 0 ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, if the searched Identifier is also equal to another
       * bounded Identifier in the CurriedLet, because all Identifiers in E1 are
       * bounded to the Identifier in this child expression.
       */
      for ( int i = 1 ; i < pCurriedLet.getIdentifiers ( ).length ; i ++ )
      {
        if ( pCurriedLet.getIdentifiers ( i ).equals ( this.identifier ) )
        {
          return ;
        }
      }
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pCurriedLet.getE1 ( ) ) ;
    }
    else
    {
      boolean found = false ;
      for ( int i = 1 ; i < pCurriedLet.getIdentifiers ( ).length ; i ++ )
      {
        if ( pCurriedLet.getIdentifiers ( i ).equals ( this.identifier ) )
        {
          found = true ;
          break ;
        }
      }
      if ( found )
      {
        /*
         * Search only in E1, because all Identifiers in E2 are bounded to the
         * Identifier in this child expression.
         */
        findExpression ( pCurriedLet.getE1 ( ) ) ;
      }
      else
      {
        findExpression ( pCurriedLet.getE1 ( ) ) ;
        findExpression ( pCurriedLet.getE2 ( ) ) ;
      }
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedLetRec}.
   * 
   * @param pCurriedLetRec The input {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( CurriedLetRec pCurriedLetRec )
  {
    if ( pCurriedLetRec.getIdentifiers ( 0 ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E1 and E2 are bounded to
       * the Identifier in this child expression.
       */
    }
    else
    {
      boolean found = false ;
      for ( int i = 1 ; i < pCurriedLetRec.getIdentifiers ( ).length ; i ++ )
      {
        if ( pCurriedLetRec.getIdentifiers ( i ).equals ( this.identifier ) )
        {
          found = true ;
          break ;
        }
      }
      if ( found )
      {
        /*
         * Search only in E2, because all Identifiers in E1 are bounded to the
         * Identifier in this child expression.
         */
        findExpression ( pCurriedLetRec.getE2 ( ) ) ;
      }
      else
      {
        findExpression ( pCurriedLetRec.getE1 ( ) ) ;
        findExpression ( pCurriedLetRec.getE2 ( ) ) ;
      }
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link CurriedMethod}.
   * 
   * @param pCurriedMethod The input {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMethod ( CurriedMethod pCurriedMethod )
  {
    /*
     * Search is finished, if the searched Identifier is also equal to another
     * bounded Identifier in the CurriedMethod, because all Identifiers in E are
     * bounded to the Identifier in this child expression.
     */
    for ( int i = 1 ; i < pCurriedMethod.getIdentifiers ( ).length ; i ++ )
    {
      if ( pCurriedMethod.getIdentifiers ( i ).equals ( this.identifier ) )
      {
        return ;
      }
    }
    /*
     * Continue search in E.
     */
    findExpression ( pCurriedMethod.getE ( ) ) ;
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
   * @param pIdentifier The input {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( Identifier pIdentifier )
  {
    if ( pIdentifier.getName ( ).equals ( this.identifier ) )
    {
      this.list.add ( pIdentifier ) ;
      this.outlineUnbound.remove ( pIdentifier ) ;
      pIdentifier.boundedExpression ( this.boundedExpression ) ;
      pIdentifier.boundedStart ( this.boundedStart ) ;
      pIdentifier.boundedEnd ( this.boundedEnd ) ;
      pIdentifier.boundedIdentifierIndex ( this.boundedIdentifierIndex ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Lambda}.
   * 
   * @param pLambda The input {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( Lambda pLambda )
  {
    if ( pLambda.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pLambda.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Let}.
   * 
   * @param pLet The input {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( Let pLet )
  {
    if ( pLet.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pLet.getE1 ( ) ) ;
    }
    else
    {
      findExpression ( pLet.getE1 ( ) ) ;
      findExpression ( pLet.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link LetRec}.
   * 
   * @param pLetRec The input {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( LetRec pLetRec )
  {
    if ( pLetRec.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E1 and E2 are bounded to
       * the Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pLetRec.getE1 ( ) ) ;
      findExpression ( pLetRec.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link MultiLambda}.
   * 
   * @param pMultiLambda The input {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( MultiLambda pMultiLambda )
  {
    boolean found = false ;
    for ( String id : pMultiLambda.getIdentifiers ( ) )
    {
      if ( id.equals ( this.identifier ) )
      {
        found = true ;
        break ;
      }
    }
    /*
     * Search is finished, because all Identifiers in E are bounded to the
     * Identifier in this child expression.
     */
    if ( ! found )
    {
      findExpression ( pMultiLambda.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link MultiLet}.
   * 
   * @param pMultiLet The input {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( MultiLet pMultiLet )
  {
    boolean found = false ;
    for ( String id : pMultiLet.getIdentifiers ( ) )
    {
      if ( id.equals ( this.identifier ) )
      {
        found = true ;
        break ;
      }
    }
    if ( found )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pMultiLet.getE1 ( ) ) ;
    }
    else
    {
      findExpression ( pMultiLet.getE1 ( ) ) ;
      findExpression ( pMultiLet.getE2 ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link ObjectExpr}.
   * 
   * @param pObjectExpr The input {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findObjectExpr ( ObjectExpr pObjectExpr )
  {
    if ( pObjectExpr.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search can be continued, but only in Attributes, because all
       * Identifiers on the right side of Methods are bounded to this child
       * expression.
       */
      Row row = pObjectExpr.getE ( ) ;
      for ( Expression expr : row.getExpressions ( ) )
      {
        if ( expr instanceof Attribute )
        {
          findExpression ( expr ) ;
        }
      }
    }
    else
    {
      findExpression ( pObjectExpr.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Recursion}.
   * 
   * @param pRecursion The input {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( Recursion pRecursion )
  {
    if ( pRecursion.getId ( ).equals ( this.identifier ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pRecursion.getE ( ) ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Row}.
   * 
   * @param pRow The input {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( Row pRow )
  {
    boolean foundEqualAttrId = false ;
    for ( Expression expr : pRow.getExpressions ( ) )
    {
      if ( expr instanceof Attribute )
      {
        findExpression ( expr ) ;
        Attribute attribute = ( Attribute ) expr ;
        if ( attribute.getId ( ).equals ( this.identifier ) )
        {
          /*
           * Search is finished for Method and CurriedMethod, because all
           * Identifiers in the Row are bounded to the Identifier in this
           * Attribute. Identifiers in Attribute can still be bound, so the
           * search is not finished.
           */
          foundEqualAttrId = true ;
        }
      }
      /*
       * If the searched Identifier was not found before as an Identifier of an
       * Attribute, it can be searched in the child Expression.
       */
      else if ( ! foundEqualAttrId )
      {
        findExpression ( expr ) ;
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
