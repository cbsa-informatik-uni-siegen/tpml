package de.unisiegen.tpml.core.types ;


import java.beans.Introspector ;
import java.beans.PropertyDescriptor ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Collections ;
import java.util.Enumeration ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Abstract base class for all types used within the type checker and the
 * various parsers.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:296 $
 * @see PrettyPrintable
 */
public abstract class Type implements PrettyPrintable , PrettyPrintPriorities
{
  /**
   * Shared empty set, returned by {@link #free()} if the type does not contain
   * any free type variables.
   */
  protected static final Set < TypeVariable > EMPTY_SET = Collections
      .unmodifiableSet ( Collections. < TypeVariable > emptySet ( ) ) ;


  /**
   * The <code>String</code> for an array of children.
   */
  private static final String GET_TAU_N = "getTypes" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GET_TAU_X = "getTau[0-9]*" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GET_PHI_X = "getPhi[0-9]*" ; //$NON-NLS-1$


  /**
   * Cached vector of sub types, so the children do not need to be determined on
   * every invocation of {@link #children()}.
   * 
   * @see #children()
   */
  private ArrayList < Type > children = null ;


  /**
   * Cached <code>TreeSet</code> of the free {@link TypeVariable}s, so the
   * free {@link TypeVariable} do not need to be determined on every invocation
   * of {@link #free()}.
   * 
   * @see #free()
   */
  protected TreeSet < TypeVariable > free = null ;


  /**
   * Cached {@link PrettyStringBuilder}, so the {@link PrettyStringBuilder} do
   * not need to be determined on every invocation of
   * {@link #toPrettyStringBuilder(PrettyStringBuilderFactory)}.
   * 
   * @see #toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  protected PrettyStringBuilder prettyStringBuilder = null ;


  /**
   * Constructor for all types.
   */
  protected Type ( )
  {
    super ( ) ;
  }


  /**
   * Returns an enumeration for the direct ancestor types, the direct children,
   * of this type. The enumeration is generated using the bean properties for
   * every {@link Type} derived class. For example, {@link ArrowType} provides
   * <code>getTau1()</code> and <code>getTau2()</code>. It also supports
   * arrays of types, as used in the {@link TupleType} class.
   * 
   * @return an {@link Enumeration} for the direct ancestor types of this type.
   */
  public final ArrayList < Type > children ( )
  {
    // check if we already determined the children
    if ( this.children == null )
    {
      try
      {
        this.children = new ArrayList < Type > ( ) ;
        PropertyDescriptor [ ] properties = Introspector.getBeanInfo (
            getClass ( ) , Type.class ).getPropertyDescriptors ( ) ;
        for ( PropertyDescriptor property : properties )
        {
          java.lang.reflect.Method method = property.getReadMethod ( ) ;
          if ( ( method.getName ( ).matches ( GET_TAU_X ) )
              || ( method.getName ( ).equals ( GET_TAU_N ) )
              || ( method.getName ( ).matches ( GET_PHI_X ) ) )
          {
            Object value = property.getReadMethod ( ).invoke ( this ) ;
            if ( value instanceof Type [ ] )
            {
              this.children.addAll ( Arrays.asList ( ( Type [ ] ) value ) ) ;
            }
            else if ( value instanceof Type )
            {
              this.children.add ( ( Type ) value ) ;
            }
          }
        }
      }
      catch ( RuntimeException exception )
      {
        throw exception ;
      }
      catch ( Exception exception )
      {
        throw new RuntimeException ( exception ) ;
      }
    }
    // return an enumeration for the children
    return this.children ;
  }


  /**
   * Clones this type, so that the result is an type equal to this type, but
   * with a different object identity. This is used in the substitution of type
   * to be able to distinguish different appearances of the same type in the
   * pretty printer, as required by the highlighting of bound variables.
   * 
   * @return a deep clone of this object.
   * @see Object#clone()
   */
  @ Override
  public abstract Type clone ( ) ;


  /**
   * Returns the set of free type variables present within this type. The
   * default implementation simply returns the empty set, and derived classes
   * will need to override this method to return the set of free type variables.
   * The returned set should be considered read-only by the caller and must not
   * be modified.
   * 
   * @return the set of free type variables within this type.
   */
  public Set < TypeVariable > free ( )
  {
    return EMPTY_SET ;
  }


  /**
   * Returns the caption of this {@link Type}.
   * 
   * @return The caption of this {@link Type}.
   */
  public abstract String getCaption ( ) ;


  /**
   * Applies the <code>substitution</code> to this type and returns the
   * resulting type, which may be this type itself if either this type does not
   * contain any type variables or no type variables that are present in the
   * <code>substitution</code>.
   * 
   * @param pTypeSubstitution the <code>TypeSubstitution</code> to apply to
   *          this type.
   * @return the new type.
   * @throws NullPointerException if the <code>pTypeSubstitution</code> is
   *           <code>null</code>.
   */
  public abstract Type substitute ( TypeSubstitution pTypeSubstitution ) ;


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * Returns the pretty string builder used to pretty print this type. The
   * pretty string builder must be allocated from the specified
   * <code>factory</code>, which is currently always the default factory, but
   * may also be another factory in the future.
   * 
   * @param pPrettyStringBuilderFactory the {@link PrettyStringBuilderFactory}
   *          used to allocate the required pretty string builders to pretty
   *          print this type.
   * @return the pretty string builder used to pretty print this type.
   * @see #toPrettyString()
   * @see PrettyStringBuilder
   * @see PrettyStringBuilderFactory
   */
  public abstract PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory ) ;


  /**
   * Returns the string representation for this type. This method is mainly used
   * for debugging.
   * 
   * @return the pretty printed string representation for this type.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
