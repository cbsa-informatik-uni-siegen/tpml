package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrTypeOrTypeEquationTypeInference ;
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
public abstract class Type implements PrettyPrintable , PrettyPrintPriorities ,
    ExpressionOrTypeOrTypeEquationTypeInference
{
  /**
   * Prefix of tau {@link Type}s.
   */
  public static final String PREFIX_TAU = "\u03C4" ; //$NON-NLS-1$


  /**
   * Prefix of {@link TypeName}s.
   */
  public static final String PREFIX_TYPE_NAME = "t" ; //$NON-NLS-1$


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
   * The list of the free {@link TypeName}s in this {@link Type}.
   */
  protected ArrayList < TypeName > typeNamesFree = null ;


  /**
   * Cached <code>ArrayList</code> of the free {@link TypeVariable}s, so the
   * free {@link TypeVariable} do not need to be determined on every invocation
   * of {@link #getTypeVariablesFree()}.
   * 
   * @see #getTypeVariablesFree()
   */
  protected ArrayList < TypeVariable > typeVariablesFree = null ;


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
   * A list of lists of bound {@link TypeName}s in this {@link Type}.
   */
  protected ArrayList < ArrayList < TypeName >> boundTypeNames = null ;


  /**
   * Constructor for all types.
   */
  protected Type ( )
  {
    super ( ) ;
  }


  /**
   * Returns an {@link ArrayList} of the child {@link Type}s.
   * 
   * @return An {@link ArrayList} of the child {@link Type}s.
   */
  public final ArrayList < Type > children ( )
  {
    if ( this.children == null )
    {
      if ( this instanceof DefaultTypes )
      {
        Type [ ] types = ( ( DefaultTypes ) this ).getTypes ( ) ;
        this.children = new ArrayList < Type > ( types.length ) ;
        for ( Type type : types )
        {
          this.children.add ( type ) ;
        }
      }
      else
      {
        this.children = new ArrayList < Type > ( 0 ) ;
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
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public abstract boolean equals ( Object pObject ) ;


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
   * Returns a list of the free {@link TypeVariable}s in this {@link Type}.
   * 
   * @return A list of the free {@link TypeVariable}s in this {@link Type}.
   */
  public ArrayList < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.typeVariablesFree == null )
    {
      this.typeVariablesFree = new ArrayList < TypeVariable > ( ) ;
      for ( Type child : children ( ) )
      {
        this.typeVariablesFree.addAll ( child.getTypeVariablesFree ( ) ) ;
      }
    }
    return this.typeVariablesFree ;
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
   * Substitutes the type <code>pTau</code> for the {@link TypeName}
   * <code>pTypeName</code> in this type, and returns the resulting type. The
   * resulting type may be a new <code>Type</code> object or if no
   * substitution took place, the same object. The method operates recursively.
   * 
   * @param pTypeName The {@link TypeName}.
   * @param pTau The {@link MonoType}.
   * @return The resulting {@link Type}.
   */
  public abstract Type substitute ( TypeName pTypeName , MonoType pTau ) ;


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
