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
public final class ShowBonds
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
  private final void check ( Expression pExpression )
  {
    for ( Class < Object > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.identifiers.BoundedId.class ) )
      {
        checkId ( pExpression ) ;
        break ;
      }
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.identifiers.BoundedIdentifiers.class ) )
      {
        checkIdentifiers ( pExpression ) ;
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
   */
  @ SuppressWarnings ( "unchecked" )
  private final void checkId ( Expression pExpression )
  {
    try
    {
      // Invoke getId
      Identifier id = ( Identifier ) pExpression.getClass ( ).getMethod (
          "getId" , new Class [ 0 ] ).invoke ( pExpression , new Object [ 0 ] ) ; //$NON-NLS-1$
      // Invoke getBoundedId
      ArrayList < Identifier > bounded = ( ArrayList < Identifier > ) pExpression
          .getClass ( ).getMethod ( "getBoundedId" , new Class [ 0 ] ).invoke ( //$NON-NLS-1$
              pExpression , new Object [ 0 ] ) ;
      // Create Bonds
      PrettyAnnotation current = this.expression.toPrettyString ( )
          .getAnnotationForPrintable ( id ) ;
      Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
          .getEndOffset ( ) ) ;
      for ( Identifier boundedId : bounded )
      {
        bonds.addPrettyAnnotation ( this.expression.toPrettyString ( )
            .getAnnotationForPrintable ( boundedId ) ) ;
      }
      this.result.add ( bonds ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "ShowBonds: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "ShowBonds: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "ShowBonds: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "ShowBonds: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "ShowBonds: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Checks the given {@link Expression} for bounded multiple {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void checkIdentifiers ( Expression pExpression )
  {
    try
    {
      // Invoke getIdentifiers
      Identifier [ ] id = ( Identifier [ ] ) pExpression.getClass ( )
          .getMethod ( "getIdentifiers" , new Class [ 0 ] ).invoke ( //$NON-NLS-1$
              pExpression , new Object [ 0 ] ) ;
      // Invoke getBoundedIdentifiers
      ArrayList < ArrayList < Identifier >> bounded = ( ArrayList < ArrayList < Identifier >> ) pExpression
          .getClass ( ).getMethod ( "getBoundedIdentifiers" , new Class [ 0 ] ) //$NON-NLS-1$
          .invoke ( pExpression , new Object [ 0 ] ) ;
      // Create Bonds
      PrettyAnnotation current ;
      for ( int i = 0 ; i < bounded.size ( ) ; i ++ )
      {
        current = this.expression.toPrettyString ( ).getAnnotationForPrintable (
            id [ i ] ) ;
        Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
            .getEndOffset ( ) ) ;
        for ( Identifier boundedId : bounded.get ( i ) )
        {
          bonds.addPrettyAnnotation ( this.expression.toPrettyString ( )
              .getAnnotationForPrintable ( boundedId ) ) ;
        }
        this.result.add ( bonds ) ;
      }
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "ShowBonds: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "ShowBonds: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "ShowBonds: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "ShowBonds: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "ShowBonds: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Returns a list with all bonds in the loaded {@link Expression}.
   * 
   * @return A list with all bonds in the loaded {@link Expression}.
   */
  public final ArrayList < Bonds > getAnnotations ( )
  {
    return this.result ;
  }


  /**
   * Set the {@link Expression} to get the bonds.
   * 
   * @param pExpression The input {@link Expression}.
   */
  public final void setExpression ( Expression pExpression )
  {
    this.expression = pExpression ;
    this.result = new ArrayList < Bonds > ( ) ;
    check ( pExpression ) ;
  }
}
