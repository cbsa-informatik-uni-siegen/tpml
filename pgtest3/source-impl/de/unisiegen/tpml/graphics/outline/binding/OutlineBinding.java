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
   * The list of lists of {@link Identifier}s, which are bounded by the given
   * {@link Identifier} in the hole {@link Expression}.
   */
  private ArrayList < Identifier > list ;


  /**
   * The list of {@link Identifier}s.
   */
  private String identifier ;


  /**
   * The list of {@link Expression}s.
   */
  private Expression expression ;


  /**
   * Initilizes the lists and sets the hole {@link Expression}.
   */
  public OutlineBinding ( )
  {
    this.list = new ArrayList < Identifier > ( ) ;
  }


  /**
   * Adds a {@link Expression} and the name of the {@link Identifier} to their
   * list. After adding all {@link Expression}s, the search prozess can be
   * started by calling the find method.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findAttr ( Attr pExpression )
  {
    findExpression ( pExpression.getE ( ) ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( Identifier pExpression )
  {
    if ( pExpression.getName ( ).equals ( this.identifier ) )
    {
      this.list.add ( pExpression ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMeth ( Meth pExpression )
  {
    findExpression ( pExpression.getE ( ) ) ;
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
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
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRow ( Row pExpression )
  {
    for ( int i = 0 ; i < pExpression.getExpressions ( ).length ; i ++ )
    {
      if ( pExpression.getExpressions ( i ) instanceof Attr )
      {
        Attr attr = ( Attr ) pExpression.getExpressions ( i ) ;
        findExpression ( attr ) ;
        if ( attr.getId ( ).equals ( this.identifier ) )
        {
          /*
           * Search is finished, because all Identifiers in the Row are bounded
           * to the Identifier in this child expression.
           */
          return ;
        }
      }
      else
      {
        findExpression ( pExpression.getExpressions ( i ) ) ;
      }
    }
  }


  /**
   * Returns the bounded {@link Identifier} in the {@link Expression}.
   * 
   * @param pIdentifierIndex The index of the {@link Identifier}.
   * @return The bounded {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( int pIdentifierIndex )
  {
    return this.list.get ( pIdentifierIndex ) ;
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
   * Returns the size of the list. The size is equal to the number of
   * {@link Identifier}s.
   * 
   * @return The number of {@link Identifier}s.
   */
  public final int size ( )
  {
    return this.list.size ( ) ;
  }
}
