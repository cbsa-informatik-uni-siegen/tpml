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
  private Expression expression = null ;


  /**
   * List of all Bonds in loaded {link Expression}.
   */
  private ArrayList < Bonds > result = null ;


  /**
   * Checks the given {@link Expression} for bounded {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void check ( Expression pExpression )
  {
    for ( Class < Object > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundedIdentifiers.class ) )
      {
        try
        {
          // Invoke getIdentifiers
          Identifier [ ] id = ( Identifier [ ] ) pExpression.getClass ( )
              .getMethod ( "getIdentifiers" , new Class [ 0 ] ).invoke ( //$NON-NLS-1$
                  pExpression , new Object [ 0 ] ) ;
          // Invoke getBoundedIdentifiers
          ArrayList < ArrayList < Identifier >> bounded = ( ArrayList < ArrayList < Identifier >> ) pExpression
              .getClass ( ).getMethod (
                  "getBoundedIdentifiers" , new Class [ 0 ] ) //$NON-NLS-1$
              .invoke ( pExpression , new Object [ 0 ] ) ;
          // Create Bonds
          if ( bounded == null )
          {
            return ;
          }
          PrettyAnnotation current ;
          for ( int i = 0 ; i < bounded.size ( ) ; i ++ )
          {
            if ( bounded.get ( i ) == null )
            {
              continue ;
            }
            current = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( id [ i ] ) ;
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
    }
    for ( Expression e : pExpression.children ( ) )
    {
      check ( e ) ;
    }
  }


  /**
   * Returns a list with all bonds in the loaded {@link Expression}.
   * 
   * @return A list with all bonds in the loaded {@link Expression}.
   */
  public final ArrayList < Bonds > getAnnotations ( )
  {
    if ( this.result == null )
    {
      this.result = new ArrayList < Bonds > ( ) ;
      check ( this.expression ) ;
    }
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
  }
}
