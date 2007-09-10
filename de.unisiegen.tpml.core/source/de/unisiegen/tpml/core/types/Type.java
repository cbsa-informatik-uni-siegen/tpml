package de.unisiegen.tpml.core.types ;


import java.awt.Color ;
import java.util.ArrayList ;
import java.util.MissingResourceException ;
import java.util.ResourceBundle ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultTypeNames ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPackageList ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.Theme ;


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
    LatexPrintable , ShowBondsInput
{
  /**
   * The resource bundle.
   */
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle ( "de.unisiegen.tpml.core.types.messages" ) ; //$NON-NLS-1$


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
   * Gets the caption for the given class from the resource bundle.
   * 
   * @param pClass The given class.
   * @return The caption of the given class.
   */
  public static String getCaption ( java.lang.Class < ? > pClass )
  {
    try
    {
      return RESOURCE_BUNDLE.getString ( pClass.getSimpleName ( ) ) ;
    }
    catch ( MissingResourceException e )
    {
      return pClass.getSimpleName ( ) ;
    }
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_PARENTHESIS , 1 , "(#1)" , //$NON-NLS-1$
        "tau" ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    Color colorExpression = Theme.currentTheme ( ).getExpressionColor ( ) ;
    float red = ( float ) Math
        .round ( ( ( float ) colorExpression.getRed ( ) ) / 255 * 100 ) / 100 ;
    float green = ( float ) Math.round ( ( ( float ) colorExpression
        .getGreen ( ) ) / 255 * 100 ) / 100 ;
    float blue = ( float ) Math
        .round ( ( ( float ) colorExpression.getBlue ( ) ) / 255 * 100 ) / 100 ;
    instructions
        .add ( new DefaultLatexInstruction (
            "\\definecolor{" + LATEX_COLOR_EXPRESSION + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
                + red + "," //$NON-NLS-1$
                + green + "," //$NON-NLS-1$
                + blue + "}" , LATEX_COLOR_EXPRESSION + ": color of expression text" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    Color colorType = Theme.currentTheme ( ).getTypeColor ( ) ;
    red = ( float ) Math
        .round ( ( ( float ) colorType.getRed ( ) ) / 255 * 100 ) / 100 ;
    green = ( float ) Math
        .round ( ( ( float ) colorType.getGreen ( ) ) / 255 * 100 ) / 100 ;
    blue = ( float ) Math
        .round ( ( ( float ) colorType.getBlue ( ) ) / 255 * 100 ) / 100 ;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_TYPE + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}" , LATEX_COLOR_TYPE + ": color of types" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    Color colorKeyword = Theme.currentTheme ( ).getKeywordColor ( ) ;
    red = ( float ) Math
        .round ( ( ( float ) colorKeyword.getRed ( ) ) / 255 * 100 ) / 100 ;
    green = ( float ) Math
        .round ( ( ( float ) colorKeyword.getGreen ( ) ) / 255 * 100 ) / 100 ;
    blue = ( float ) Math
        .round ( ( ( float ) colorKeyword.getBlue ( ) ) / 255 * 100 ) / 100 ;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_KEYWORD + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}" , LATEX_COLOR_KEYWORD + ": color of keywords" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    Color colorIdentifier = Theme.currentTheme ( ).getIdentifierColor ( ) ;
    red = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getRed ( ) ) / 255 * 100 ) / 100 ;
    green = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getGreen ( ) ) / 255 * 100 ) / 100 ;
    blue = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getBlue ( ) ) / 255 * 100 ) / 100 ;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_IDENTIFIER + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}" , LATEX_COLOR_IDENTIFIER + ": color of keywords" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( LatexPackage.COLOR ) ;
    return packages ;
  }


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
   * The start offset of this {@link Expression} in the source code.
   * 
   * @see #getParserStartOffset()
   * @see #setParserStartOffset(int)
   */
  protected int parserStartOffset = - 1 ;


  /**
   * The end offset of this {@link Expression} in the source code.
   * 
   * @see #getParserEndOffset()
   * @see #setParserEndOffset(int)
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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    if ( this instanceof DefaultTypes )
    {
      commands.add ( ( ( DefaultTypes ) this ).getTypes ( ) ) ;
    }
    if ( this instanceof DefaultIdentifiers )
    {
      commands.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers ( ) ) ;
    }
    if ( this instanceof DefaultTypeNames )
    {
      commands.add ( ( ( DefaultTypeNames ) this ).getTypeNames ( ) ) ;
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    if ( this instanceof DefaultTypes )
    {
      instructions.add ( ( ( DefaultTypes ) this ).getTypes ( ) ) ;
    }
    if ( this instanceof DefaultIdentifiers )
    {
      instructions.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers ( ) ) ;
    }
    if ( this instanceof DefaultTypeNames )
    {
      instructions.add ( ( ( DefaultTypeNames ) this ).getTypeNames ( ) ) ;
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ( )
  {
    LatexPackageList packages = new LatexPackageList ( ) ;
    packages.add ( getLatexPackagesStatic ( ) ) ;
    if ( this instanceof DefaultTypes )
    {
      packages.add ( ( ( DefaultTypes ) this ).getTypes ( ) ) ;
    }
    if ( this instanceof DefaultIdentifiers )
    {
      packages.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers ( ) ) ;
    }
    if ( this instanceof DefaultTypeNames )
    {
      packages.add ( ( ( DefaultTypeNames ) this ).getTypeNames ( ) ) ;
    }
    return packages ;
  }


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
   * @see #setParserEndOffset(int)
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
   * @see #setParserStartOffset(int)
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
   * Sets the parser end offset.
   * 
   * @param pParserEndOffset The new parser end offset.
   * @see #getParserEndOffset()
   * @see #parserEndOffset
   */
  public void setParserEndOffset ( int pParserEndOffset )
  {
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Sets the parser start offset.
   * 
   * @param pParserStartOffset The new parser start offset.
   * @see #getParserStartOffset()
   * @see #parserStartOffset
   */
  public void setParserStartOffset ( int pParserStartOffset )
  {
    this.parserStartOffset = pParserStartOffset ;
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
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public abstract LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent ) ;


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
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public abstract PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory ) ;


  /**
   * Returns the string representation for this type. This method is mainly used
   * for debugging.
   * 
   * @return The pretty printed string representation for this type.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
