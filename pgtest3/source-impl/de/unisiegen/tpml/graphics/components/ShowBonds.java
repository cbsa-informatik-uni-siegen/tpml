package de.unisiegen.tpml.graphics.components ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeName ;


/**
 * Calculates the bound {@link Identifier}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 */
public final class ShowBonds
{
  /**
   * Method name for getIdentifiers
   */
  private static final String GET_IDENTIFIERS = "getIdentifiers" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersBound
   */
  private static final String GET_IDENTIFIERS_BOUND = "getIdentifiersBound" ; //$NON-NLS-1$


  /**
   * Method name for getTypeNames
   */
  private static final String GET_TYPE_NAMES = "getTypeNames" ; //$NON-NLS-1$


  /**
   * Method name for getTypeNamesBound
   */
  private static final String GET_TYPE_NAMES_BOUND = "getTypeNamesBound" ; //$NON-NLS-1$


  /**
   * Method name for getTypes
   */
  private static final String GET_TYPES = "getTypes" ; //$NON-NLS-1$


  /**
   * The current loaded {@link Expression}.
   */
  private Expression expression = null ;


  /**
   * The current loaded {@link Type}.
   */
  private Type type = null ;


  /**
   * List of all Bonds in loaded {link Expression}.
   */
  private ArrayList < Bonds > result = null ;


  /**
   * Checks the given {@link Expression} for bound {@link Identifier}s.
   * 
   * @param pExpression The input {@link Expression}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void check ( Expression pExpression )
  {
    for ( Class < ? > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) )
      {
        try
        {
          // Invoke getIdentifiers
          Identifier [ ] id = ( Identifier [ ] ) pExpression.getClass ( )
              .getMethod ( GET_IDENTIFIERS , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
          // Invoke getIdentifiersBound
          ArrayList < ArrayList < Identifier >> bound = ( ArrayList < ArrayList < Identifier >> ) pExpression
              .getClass ( )
              .getMethod ( GET_IDENTIFIERS_BOUND , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
          // Create Bonds
          if ( bound == null )
          {
            return ;
          }
          PrettyAnnotation current ;
          for ( int i = 0 ; i < bound.size ( ) ; i ++ )
          {
            if ( bound.get ( i ) == null )
            {
              continue ;
            }
            if ( this.expression != null )
            {
              current = this.expression.toPrettyString ( )
                  .getAnnotationForPrintable ( id [ i ] ) ;
            }
            else if ( this.type != null )
            {
              current = this.type.toPrettyString ( ).getAnnotationForPrintable (
                  id [ i ] ) ;
            }
            else
            {
              return ;
            }
            Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
                .getEndOffset ( ) ) ;
            for ( Identifier boundId : bound.get ( i ) )
            {
              try
              {
                if ( this.expression != null )
                {
                  bonds.addPrettyAnnotation ( this.expression.toPrettyString ( )
                      .getAnnotationForPrintable ( boundId ) ) ;
                }
                else if ( this.type != null )
                {
                  bonds.addPrettyAnnotation ( this.type.toPrettyString ( )
                      .getAnnotationForPrintable ( boundId ) ) ;
                }
                else
                {
                  return ;
                }
              }
              catch ( IllegalArgumentException e )
              {
                /*
                 * Happens if a bound Identifier is not in the PrettyString. For
                 * example "object (self) val a = 0 ; method move = {< a = 2 >} ;
                 * end". The "self" binds the free Identifier "self" in the
                 * Duplication (method free in Duplication), but the free "self"
                 * in the Duplication is not present in the PrettyString.
                 */
              }
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
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        try
        {
          MonoType [ ] types = ( MonoType [ ] ) pExpression.getClass ( )
              .getMethod ( GET_TYPES , new Class [ 0 ] ).invoke ( pExpression ,
                  new Object [ 0 ] ) ;
          for ( MonoType tau : types )
          {
            check ( tau ) ;
          }
        }
        catch ( IllegalArgumentException e )
        {
          System.err.println ( "ShowBonds: IllegalArgumentException" ) ; //$NON-NLS-1$
        }
        catch ( SecurityException e )
        {
          System.err.println ( "ShowBonds: SecurityException" ) ; //$NON-NLS-1$
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "ShowBonds: IllegalAccessException" ) ; //$NON-NLS-1$
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "ShowBonds: InvocationTargetException" ) ; //$NON-NLS-1$
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
   * Checks the given {@link Type} for bound {@link TypeName}s.
   * 
   * @param pType The input {@link Type}.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void check ( Type pType )
  {
    for ( Class < ? > currentInterface : pType.getClass ( ).getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundTypeNames.class ) )
      {
        try
        {
          // Invoke getTypeNames
          TypeName [ ] typeNames = ( TypeName [ ] ) pType.getClass ( )
              .getMethod ( GET_TYPE_NAMES , new Class [ 0 ] ).invoke ( pType ,
                  new Object [ 0 ] ) ;
          // Invoke getTypeNamesBound
          ArrayList < ArrayList < TypeName >> bound = ( ArrayList < ArrayList < TypeName >> ) pType
              .getClass ( ).getMethod ( GET_TYPE_NAMES_BOUND , new Class [ 0 ] )
              .invoke ( pType , new Object [ 0 ] ) ;
          // Create Bonds
          if ( bound == null )
          {
            return ;
          }
          PrettyAnnotation current ;
          for ( int i = 0 ; i < bound.size ( ) ; i ++ )
          {
            if ( bound.get ( i ) == null )
            {
              continue ;
            }
            if ( this.expression != null )
            {
              current = this.expression.toPrettyString ( )
                  .getAnnotationForPrintable ( typeNames [ i ] ) ;
            }
            else if ( this.type != null )
            {
              current = this.type.toPrettyString ( ).getAnnotationForPrintable (
                  typeNames [ i ] ) ;
            }
            else
            {
              return ;
            }
            Bonds bonds = new Bonds ( current.getStartOffset ( ) , current
                .getEndOffset ( ) ) ;
            for ( TypeName boundTypeNames : bound.get ( i ) )
            {
              try
              {
                if ( this.expression != null )
                {
                  bonds.addPrettyAnnotation ( this.expression.toPrettyString ( )
                      .getAnnotationForPrintable ( boundTypeNames ) ) ;
                }
                else if ( this.type != null )
                {
                  bonds.addPrettyAnnotation ( this.type.toPrettyString ( )
                      .getAnnotationForPrintable ( boundTypeNames ) ) ;
                }
                else
                {
                  return ;
                }
              }
              catch ( IllegalArgumentException e )
              {
                /*
                 * Happens if a bound TypeName is not in the PrettyString.
                 */
              }
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
    for ( Type t : pType.children ( ) )
    {
      check ( t ) ;
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
      if ( this.expression != null )
      {
        check ( this.expression ) ;
      }
      else if ( this.type != null )
      {
        check ( this.type ) ;
      }
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


  /**
   * Set the {@link Type} to get the bonds.
   * 
   * @param pType The input {@link Type}.
   */
  public final void setType ( Type pType )
  {
    this.type = pType ;
  }
}
