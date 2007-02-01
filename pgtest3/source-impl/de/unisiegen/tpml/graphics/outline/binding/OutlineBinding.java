package de.unisiegen.tpml.graphics.outline.binding ;


import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;


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
   * The <code>String</code> for the beginning of the check methods.
   */
  private static final String CHECK = "check" ; //$NON-NLS-1$


  /**
   * The list of lists of {@link Identifier}s, which are bounded by the given
   * {@link Identifier} in the hole {@link Expression}.
   */
  private ArrayList < ArrayList < Identifier >> list ;


  /**
   * The hole {@link Expression}.
   */
  private Expression holeExpression ;


  /**
   * The list of {@link Identifier}s.
   */
  private ArrayList < String > identifierList ;


  /**
   * The list of {@link Expression}s.
   */
  private ArrayList < Expression > expressionList ;


  /**
   * Initilizes the lists and sets the hole {@link Expression}.
   * 
   * @param pHoleExpression The hole {@link Expression}.
   */
  public OutlineBinding ( Expression pHoleExpression )
  {
    this.list = new ArrayList < ArrayList < Identifier >> ( ) ;
    this.identifierList = new ArrayList < String > ( ) ;
    this.expressionList = new ArrayList < Expression > ( ) ;
    this.holeExpression = pHoleExpression ;
  }


  /**
   * Adds a {@link Expression} and the name of the {@link Identifier} to their
   * list. After adding all {@link Expression}s, the search prozess can be
   * started by calling the find method.
   * 
   * @param pExpression The {@link Expression}.
   * @param pId The {@link Identifier}.
   */
  public final void add ( Expression pExpression , String pId )
  {
    this.expressionList.add ( pExpression ) ;
    this.identifierList.add ( pId ) ;
  }


  /**
   * Search for an {@link Identifier} with the same name as the current,
   * starting with the second {@link Identifier}. If someone is found, the
   * current {@link Expression} and the current {@link Identifier} is set to
   * <code>null</code>, because he has no bindings.
   */
  @ SuppressWarnings ( "unused" )
  private final void checkCurriedLet ( )
  {
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


  /**
   * Search for an {@link Identifier} with the same name as the first
   * {@link Identifier}. If someone is found, the first {@link Identifier}
   * binds only in E2. Search for an {@link Identifier} with the same name as
   * the current, starting with the second {@link Identifier}. If someone is
   * found, the current {@link Expression} and the current {@link Identifier} is
   * set to <code>null</code>, because he has no bindings.
   */
  @ SuppressWarnings ( "unused" )
  private final void checkCurriedLetRec ( )
  {
    for ( int i = 1 ; i < this.identifierList.size ( ) ; i ++ )
    {
      if ( this.identifierList.get ( 0 )
          .equals ( this.identifierList.get ( i ) ) )
      {
        this.expressionList.set ( 0 , ( ( CurriedLetRec ) this.holeExpression )
            .getE2 ( ) ) ;
        break ;
      }
    }
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


  /**
   * Search for an {@link Identifier} with the same name as the current,
   * starting with the second {@link Identifier}. If someone is found, the
   * current {@link Expression} and the current {@link Identifier} is set to
   * <code>null</code>, because he has no bindings.
   */
  @ SuppressWarnings ( "unused" )
  private final void checkCurriedMeth ( )
  {
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


  /**
   * Checks if there are bindings in {@link Expression}s with more than one
   * {@link Identifier}.
   */
  private final void checkExpression ( )
  {
    for ( Method method : this.getClass ( ).getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          CHECK + this.holeExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 0 ] ;
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
      }
    }
  }


  /**
   * Search for an {@link Identifier} with the same name as the current,
   * starting with the first {@link Identifier}. If someone is found, the
   * current {@link Expression} and the current {@link Identifier} is set to
   * <code>null</code>, because he has no bindings.
   */
  @ SuppressWarnings ( "unused" )
  private final void checkMultiLambda ( )
  {
    checkMultiLet ( ) ;
  }


  /**
   * Search for an {@link Identifier} with the same name as the current,
   * starting with the first {@link Identifier}. If someone is found, the
   * current {@link Expression} and the current {@link Identifier} is set to
   * <code>null</code>, because he has no bindings.
   */
  private final void checkMultiLet ( )
  {
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


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   */
  public final void find ( )
  {
    /*
     * Checks if there are bindings in Expressions with more than one
     * Identifier.
     */
    checkExpression ( ) ;
    /*
     * Find the bindings in the expressions.
     */
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
        findExpression ( tmpList , this.expressionList.get ( i ) ,
            this.identifierList.get ( i ) ) ;
      }
      this.list.add ( tmpList ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLet ( ArrayList < Identifier > pResult ,
      CurriedLet pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( isInArray ( pExpression.getIdentifiers ( ) , pId ) ) )
    {
      if ( pExpression.getIdentifiers ( 0 ).equals ( pId ) )
      {
        /*
         * Search is finished, if the searched Identifier is also equal to
         * another bounded Identifier in the CurriedLet, because all Identifiers
         * in E1 are bounded to the Identifier in this child expression.
         */
        for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
        {
          if ( pExpression.getIdentifiers ( i ).equals ( pId ) )
          {
            return ;
          }
        }
        /*
         * Search only in E1, because all Identifiers in E2 are bounded to the
         * Identifier in this child expression.
         */
        findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
        return ;
      }
      /*
       * Search only in E2, because all Identifiers in E1 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
    else
    {
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedLetRec ( ArrayList < Identifier > pResult ,
      CurriedLetRec pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( isInArray ( pExpression.getIdentifiers ( ) , pId ) ) )
    {
      if ( pExpression.getIdentifiers ( 0 ).equals ( pId ) )
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
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
    else
    {
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findCurriedMeth ( ArrayList < Identifier > pResult ,
      CurriedMeth pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( isInArray ( pExpression.getIdentifiers ( ) , pId ) ) )
    {
      /*
       * Search is finished, if the searched Identifier is also equal to another
       * bounded Identifier in the CurriedMeth, because all Identifiers in E are
       * bounded to the Identifier in this child expression.
       */
      for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals ( pId ) )
        {
          return ;
        }
      }
      /*
       * Continue search in E.
       */
      findExpression ( pResult , pExpression.getE ( ) , pId ) ;
    }
    else
    {
      findExpression ( pResult , pExpression.getE ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  private final void findExpression ( ArrayList < Identifier > pResult ,
      Expression pExpression , String pId )
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
          argument [ 1 ] = pExpression ;
          argument [ 2 ] = pId ;
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
      findExpression ( pResult , children.nextElement ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findIdentifier ( ArrayList < Identifier > pResult ,
      Identifier pExpression , String pId )
  {
    if ( pExpression.getName ( ).equals ( pId ) )
    {
      pResult.add ( pExpression ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLambda ( ArrayList < Identifier > pResult ,
      Lambda pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( pExpression.getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pResult , pExpression.getE ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLet ( ArrayList < Identifier > pResult ,
      Let pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( pExpression.getId ( ).equals ( pId ) ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
    }
    else
    {
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findLetRec ( ArrayList < Identifier > pResult ,
      LetRec pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( pExpression.getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E1 and E2 are bounded to
       * the Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLambda ( ArrayList < Identifier > pResult ,
      MultiLambda pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( isInArray ( pExpression.getIdentifiers ( ) , pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pResult , pExpression.getE ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findMultiLet ( ArrayList < Identifier > pResult ,
      MultiLet pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( isInArray ( pExpression.getIdentifiers ( ) , pId ) ) )
    {
      /*
       * Search only in E1, because all Identifiers in E2 are bounded to the
       * Identifier in this child expression.
       */
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
    }
    else
    {
      findExpression ( pResult , pExpression.getE1 ( ) , pId ) ;
      findExpression ( pResult , pExpression.getE2 ( ) , pId ) ;
    }
  }


  /**
   * Finds the bounded {@link Identifier}s in the given {@link Expression}.
   * 
   * @param pResult The list of the bounded {@link Identifier}s.
   * @param pExpression The input {@link Expression}.
   * @param pId The name of the {@link Identifier}.
   */
  @ SuppressWarnings ( "unused" )
  private final void findRecursion ( ArrayList < Identifier > pResult ,
      Recursion pExpression , String pId )
  {
    if ( ( ! pExpression.equals ( this.holeExpression ) )
        && ( pExpression.getId ( ).equals ( pId ) ) )
    {
      /*
       * Search is finished, because all Identifiers in E are bounded to the
       * Identifier in this child expression.
       */
    }
    else
    {
      findExpression ( pResult , pExpression.getE ( ) , pId ) ;
    }
  }


  /**
   * Returns the bounded {@link Identifier} in the {@link Expression}.
   * 
   * @param pIdentifierIndex The index of the {@link Identifier}.
   * @param pBindingIndex The index of the binding.
   * @return The bounded {@link Identifier} in the {@link Expression}.
   */
  public final Identifier get ( int pIdentifierIndex , int pBindingIndex )
  {
    return this.list.get ( pIdentifierIndex ).get ( pBindingIndex ) ;
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


  /**
   * Returns the number of bindings from a given {@link Identifier}.
   * 
   * @param pIdentifierIndex The index of the {@link Identifier}.
   * @return The number of bindings from a given {@link Identifier}.
   */
  public final int size ( int pIdentifierIndex )
  {
    return this.list.get ( pIdentifierIndex ).size ( ) ;
  }
}
