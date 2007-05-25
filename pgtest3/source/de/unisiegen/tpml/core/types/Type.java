package de.unisiegen.tpml.core.types ;


import java.lang.reflect.InvocationTargetException ;
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
   * Shared empty set, returned by {@link #getTypeVariablesFree()} if the type
   * does not contain any free type variables.
   */
  protected static final Set < TypeVariable > EMPTY_SET = Collections
      .unmodifiableSet ( Collections. < TypeVariable > emptySet ( ) ) ;


  /**
   * The <code>String</code> for an array of children.
   */
  private static final String GET_TYPES = "getTypes" ; //$NON-NLS-1$


  /**
   * Prefix of tau {@link Type}s.
   */
  public static final String PREFIX_TAU = "\u03C4" ; //$NON-NLS-1$


  /**
   * Prefix of phi {@link Type}s.
   */
  public static final String PREFIX_PHI = "\u03A6" ; //$NON-NLS-1$


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
   * of {@link #getTypeVariablesFree()}.
   * 
   * @see #getTypeVariablesFree()
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
   * The parent of this {@link Type}.
   * 
   * @see #getParent()
   * @see #setParent(PrettyPrintable)
   */
  protected PrettyPrintable parent = null ;


  /**
   * The start offset of this {@link Type} in the source code.
   */
  protected int parserStartOffset = - 1 ;


  /**
   * The end offset of this {@link Type} in the source code.
   */
  protected int parserEndOffset = - 1 ;


  /**
   * The prefix of this {@link Type}.
   * 
   * @see #getPrefix()
   */
  protected String prefix = null ;


  /**
   * The list of the free {@link TypeName}s in this {@link Type}.
   */
  protected ArrayList < TypeName > typeNamesFree = null ;


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
    if ( this.children == null )
    {
      this.children = new ArrayList < Type > ( ) ;
      for ( Class < Object > currentInterface : this.getClass ( )
          .getInterfaces ( ) )
      {
        if ( currentInterface
            .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
        {
          try
          {
            Type [ ] types = ( Type [ ] ) this.getClass ( ).getMethod (
                GET_TYPES , new Class [ 0 ] ).invoke ( this , new Object [ 0 ] ) ;
            this.children.addAll ( Arrays.asList ( types ) ) ;
          }
          catch ( IllegalArgumentException e )
          {
            System.err.println ( "Type: IllegalArgumentException" ) ; //$NON-NLS-1$
          }
          catch ( SecurityException e )
          {
            System.err.println ( "Type: SecurityException" ) ; //$NON-NLS-1$
          }
          catch ( IllegalAccessException e )
          {
            System.err.println ( "Type: IllegalAccessException" ) ; //$NON-NLS-1$
          }
          catch ( InvocationTargetException e )
          {
            System.err.println ( "Type: InvocationTargetException" ) ; //$NON-NLS-1$
          }
          catch ( NoSuchMethodException e )
          {
            System.err.println ( "Type: NoSuchMethodException" ) ; //$NON-NLS-1$
          }
        }
      }
    }
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
   * Returns the caption of this {@link Type}.
   * 
   * @return The caption of this {@link Type}.
   */
  public abstract String getCaption ( ) ;


  /**
   * Returns the parent of this {@link Type}.
   * 
   * @return The parent of this {@link Type}.
   * @see #parent
   * @see #setParent(PrettyPrintable)
   */
  public final PrettyPrintable getParent ( )
  {
    return this.parent ;
  }


  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   * @see #parserEndOffset
   */
  public int getParserEndOffset ( )
  {
    return this.parserEndOffset ;
  }


  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   * @see #parserStartOffset
   */
  public int getParserStartOffset ( )
  {
    return this.parserStartOffset ;
  }


  /**
   * Returns the prefix of this {@link Type}.
   * 
   * @return The prefix of this {@link Type}.
   * @see #prefix
   */
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_TAU ;
    }
    return this.prefix ;
  }


  /**
   * Returns a list of the free {@link TypeName}s in this {@link Type}.
   * 
   * @return A list of the free {@link TypeName}s in this {@link Type}.
   */
  public ArrayList < TypeName > getTypeNamesFree ( )
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ( ) ;
      for ( Type child : children ( ) )
      {
        this.typeNamesFree.addAll ( child.getTypeNamesFree ( ) ) ;
      }
    }
    return this.typeNamesFree ;
  }


  /**
   * Returns the set of free type variables present within this type. The
   * default implementation simply returns the empty set, and derived classes
   * will need to override this method to return the set of free type variables.
   * The returned set should be considered read-only by the caller and must not
   * be modified.
   * 
   * @return the set of free type variables within this type.
   */
  public Set < TypeVariable > getTypeVariablesFree ( )
  {
    return EMPTY_SET ;
  }


  /**
   * Sets the parent of this {@link Type}.
   * 
   * @param pParent The parent to set.
   * @see #parent
   * @see #getParent()
   */
  public final void setParent ( PrettyPrintable pParent )
  {
    this.parent = pParent ;
  }


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
