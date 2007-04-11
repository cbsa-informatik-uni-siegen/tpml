package de.unisiegen.tpml.graphics.components ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;


/**
 * Calculates the bounded {@link Identifier}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public class ShowBonds
{
  /**
   * The current loaded {@link Expression}.
   */
  private Expression expression ;


  /**
   * List of all Bonds in loaded {link Expression}.
   */
  private ArrayList < Bonds > result ;


  /**
   * Checks the given {@link Expression} for bounded {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  private void check ( Expression pExpression )
  {
    for ( java.lang.reflect.Method method : pExpression.getClass ( )
        .getMethods ( ) )
    {
      if ( method.getName ( ).equals ( "getId" ) ) //$NON-NLS-1$
      {
        checkId ( pExpression , method ) ;
        break ;
      }
      if ( method.getName ( ).equals ( "getIdentifiers" ) ) //$NON-NLS-1$
      {
        checkIdentifiers ( pExpression , method ) ;
        break ;
      }
    }
    for ( Expression e : pExpression.children ( ) )
    {
      check ( e ) ;
    }
  }


  /**
   * Checks the given {@link Expression} for bounded single {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   * @param pMethod The {@link java.lang.reflect.Method} of the single
   *          {@link Identifier}.
   */
  @ SuppressWarnings ( "unchecked" )
  private void checkId ( Expression pExpression ,
      java.lang.reflect.Method pMethod )
  {
    Identifier id = null ;
    ArrayList < Identifier > bounded = null ;
    try
    {
      Object [ ] argument = new Object [ 0 ] ;
      id = ( Identifier ) pMethod.invoke ( pExpression , argument ) ;
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
    for ( java.lang.reflect.Method method : pExpression.getClass ( )
        .getMethods ( ) )
    {
      if ( method.getName ( ).equals ( "getBoundedId" ) ) //$NON-NLS-1$
      {
        try
        {
          Object [ ] argument = new Object [ 0 ] ;
          bounded = ( ArrayList < Identifier > ) method.invoke ( pExpression ,
              argument ) ;
          break ;
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
    if ( ( id != null ) && ( bounded != null ) )
    {
      PrettyAnnotation pa = this.expression.toPrettyString ( )
          .getAnnotationForPrintable ( id ) ;
      Bonds bonds = new Bonds ( pa.getStartOffset ( ) , pa.getEndOffset ( ) ) ;
      for ( int i = 0 ; i < bounded.size ( ) ; i ++ )
      {
        PrettyAnnotation current = this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( bounded.get ( i ) ) ;
        bonds.addPrettyAnnotation ( current ) ;
      }
      this.result.add ( bonds ) ;
    }
  }


  /**
   * Checks the given {@link Expression} for bounded multiple {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   * @param pMethod The {@link java.lang.reflect.Method} of the multiple
   *          {@link Identifier}s.
   */
  @ SuppressWarnings ( "unchecked" )
  private void checkIdentifiers ( Expression pExpression ,
      java.lang.reflect.Method pMethod )
  {
    Identifier [ ] id = null ;
    ArrayList < ArrayList < Identifier >> bounded = null ;
    try
    {
      Object [ ] argument = new Object [ 0 ] ;
      id = ( Identifier [ ] ) pMethod.invoke ( pExpression , argument ) ;
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
    for ( java.lang.reflect.Method method : pExpression.getClass ( )
        .getMethods ( ) )
    {
      if ( method.getName ( ).equals ( "getBoundedIdentifiers" ) ) //$NON-NLS-1$
      {
        try
        {
          Object [ ] argument = new Object [ 0 ] ;
          bounded = ( ArrayList < ArrayList < Identifier >> ) method.invoke (
              pExpression , argument ) ;
          break ;
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
    if ( ( id != null ) && ( bounded != null ) )
    {
      for ( int i = 0 ; i < bounded.size ( ) ; i ++ )
      {
        PrettyAnnotation pa = this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( id [ i ] ) ;
        Bonds bonds = new Bonds ( pa.getStartOffset ( ) , pa.getEndOffset ( ) ) ;
        for ( int j = 0 ; j < bounded.get ( i ).size ( ) ; j ++ )
        {
          PrettyAnnotation current = this.expression.toPrettyString ( )
              .getAnnotationForPrintable ( bounded.get ( i ).get ( j ) ) ;
          bonds.addPrettyAnnotation ( current ) ;
        }
        this.result.add ( bonds ) ;
      }
    }
  }


  /**
   * Returns a list with all bonds in the loaded {@link Expression}.
   * 
   * @return A list with all bonds in the loaded {@link Expression}.
   */
  public ArrayList < Bonds > getAnnotations ( )
  {
    return this.result ;
  }


  /**
   * Set the {@link Expression} to get the bonds.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public void setExpression ( Expression pExpression )
  {
    this.expression = pExpression ;
    this.result = new ArrayList < Bonds > ( ) ;
    check ( pExpression ) ;
  }
}
