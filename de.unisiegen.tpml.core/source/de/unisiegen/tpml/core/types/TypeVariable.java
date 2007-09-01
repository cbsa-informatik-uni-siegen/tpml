package de.unisiegen.tpml.core.types ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents type variables in our type system. Type variables can
 * either be specified in the source code by the user, in which case the
 * <code>index</code> assigned to this variable is <code>0</code>, or
 * automatically generated by the type inference algorithm, in which case the
 * index will be a positive integer. The <code>offset</code> is the number of
 * the variable at the specified <code>index</code>. I.e. the first variable
 * specified by the user will have <code>0</code> for <code>index</code> and
 * <code>offset</code>, the second will have an <code>index</code> of
 * <code>0</code> and an <code>offset</code> of <code>1</code> and so on.
 * The <code>offset</code> determines the greek letter assigned to this
 * variable, i.e. α for <code>0</code>, β for <code>1</code> and so on.
 * Since the greek alphabet does only include 24 letters, an <code>offset</code>
 * greater than 23 results in a greek letter with the <code>index</code> plus
 * a number of ticks appended. I.e. α1' if <code>offset</code> is
 * <code>24</code> and <code>index</code> is <code>1</code>. If the
 * <code>index</code> is <code>0</code> only the greek letter determined
 * from the <code>offset</code> will be printed. Otherwise, for positive
 * indices, the greek letter plus the index will be printed.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:538 $
 * @see Type
 */
public final class TypeVariable extends MonoType implements
    Comparable < TypeVariable >
{
  /**
   * The unused string.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * String for the case that the offset is negative.
   */
  private static final String OFFSET_NEGATIVE = "offset is negative" ; //$NON-NLS-1$


  /**
   * String for the case that the index is negative.
   */
  private static final String INDEX_NEGATIVE = "index is negative" ; //$NON-NLS-1$


  /**
   * String for the case that the offset is invalid.
   */
  private static final String OFFSET_INVALID = "Offset is invalid" ; //$NON-NLS-1$


  /**
   * The type variable alpha.
   */
  public static final TypeVariable ALPHA = new TypeVariable ( 0 , 0 ) ;


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( TypeVariable.class ) ;


  /**
   * Returns the greek letter that is assigned to the specified
   * <code>offset</code>.
   * 
   * @param pOffset the offset to translate to a greek letter.
   * @return the greek letter that should be used to represent the specified
   *         <code>offset</code>.
   * @throws IllegalArgumentException if <code>offset</code> is larger than
   *           <code>23</code> or negative.
   */
  private static char offsetToGreekLetter ( int pOffset )
  {
    switch ( pOffset )
    {
      case 0 :
        return 'α' ;
      case 1 :
        return 'β' ;
      case 2 :
        return 'γ' ;
      case 3 :
        return 'δ' ;
      case 4 :
        return 'ε' ;
      case 5 :
        return 'ζ' ;
      case 6 :
        return 'η' ;
      case 7 :
        return 'θ' ;
      case 8 :
        return 'ι' ;
      case 9 :
        return 'κ' ;
      case 10 :
        return 'λ' ;
      case 11 :
        return 'μ' ;
      case 12 :
        return 'ν' ;
      case 13 :
        return 'ξ' ;
      case 14 :
        return 'ο' ;
      case 15 :
        return 'π' ;
      case 16 :
        return 'ρ' ;
      case 17 :
        return 'σ' ;
      case 18 :
        return 'τ' ;
      case 19 :
        return 'υ' ;
      case 20 :
        return 'φ' ;
      case 21 :
        return 'χ' ;
      case 22 :
        return 'ψ' ;
      case 23 :
        return 'ω' ;
      default :
        throw new IllegalArgumentException ( OFFSET_INVALID ) ;
    }
  }


  /**
   * Returns the greek letter as a latex command that is assigned to the
   * specified <code>offset</code>.
   * 
   * @param pOffset the offset to translate to a greek letter.
   * @return the greek letter as a latex command that should be used to
   *         represent the specified <code>offset</code>.
   * @throws IllegalArgumentException if <code>offset</code> is larger than
   *           <code>23</code> or negative.
   */
  private static String offsetToGreekLetterLatex ( int pOffset )
  {
    switch ( pOffset )
    {
      case 0 :
        return "\\alpha" ;//$NON-NLS-1$
      case 1 :
        return "\\beta" ;//$NON-NLS-1$
      case 2 :
        return "\\gamma" ;//$NON-NLS-1$
      case 3 :
        return "\\delta" ;//$NON-NLS-1$
      case 4 :
        return "\\epsilon" ;//$NON-NLS-1$
      case 5 :
        return "\\zeta" ;//$NON-NLS-1$
      case 6 :
        return "\\eta" ;//$NON-NLS-1$
      case 7 :
        return "\\theta" ;//$NON-NLS-1$
      case 8 :
        return "\\iota" ;//$NON-NLS-1$
      case 9 :
        return "\\kappa" ;//$NON-NLS-1$
      case 10 :
        return "\\lambda" ;//$NON-NLS-1$
      case 11 :
        return "\\mu" ;//$NON-NLS-1$
      case 12 :
        return "\\nu" ;//$NON-NLS-1$
      case 13 :
        return "\\xi" ;//$NON-NLS-1$
      case 14 :
        // There is no latex command for omikron
        return "o" ; //$NON-NLS-1$
      case 15 :
        return "\\pi" ;//$NON-NLS-1$
      case 16 :
        return "\\rho" ;//$NON-NLS-1$
      case 17 :
        return "\\sigma" ;//$NON-NLS-1$
      case 18 :
        return "\\tau" ;//$NON-NLS-1$
      case 19 :
        return "\\upsilon" ;//$NON-NLS-1$
      case 20 :
        return "\\phi" ;//$NON-NLS-1$
      case 21 :
        return "\\chi" ;//$NON-NLS-1$
      case 22 :
        return "\\psi" ;//$NON-NLS-1$
      case 23 :
        return "\\omega" ;//$NON-NLS-1$
      default :
        throw new IllegalArgumentException ( OFFSET_INVALID ) ;
    }
  }


  /**
   * The index of the variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number for
   * variables generated by the type inference algorithm. See the description
   * above for further details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int index ;


  /**
   * The offset for the variable, which identifies the variable together with
   * the <code>index</code>. See the description above for further details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int offset ;


  /**
   * Allocates a new <code>TypeVariable</code> with the given
   * <code>index</code> and <code>offset</code>.
   * 
   * @param pIndex the index of the type variable, that is the step of the
   *          proof, in which it was created. A value of <code>0</code> means
   *          that the type variable was specified in the source code.
   * @param pOffset the offset, that is the number of type variables already
   *          allocated in the proof step identified by <code>index</code>.
   * @throws IllegalArgumentException if <code>index</code> or
   *           <code>offset</code> is negative.
   * @see TypeVariable
   * @see #index
   * @see #offset
   */
  public TypeVariable ( int pIndex , int pOffset )
  {
    if ( pIndex < 0 )
    {
      throw new IllegalArgumentException ( INDEX_NEGATIVE ) ;
    }
    if ( pOffset < 0 )
    {
      throw new IllegalArgumentException ( OFFSET_NEGATIVE ) ;
    }
    this.index = pIndex ;
    this.offset = pOffset ;
  }


  /**
   * Allocates a new <code>TypeVariable</code> with the given
   * <code>index</code> and <code>offset</code>.
   * 
   * @param pIndex the index of the type variable, that is the step of the
   *          proof, in which it was created. A value of <code>0</code> means
   *          that the type variable was specified in the source code.
   * @param pOffset the offset, that is the number of type variables already
   *          allocated in the proof step identified by <code>index</code>.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws IllegalArgumentException if <code>index</code> or
   *           <code>offset</code> is negative.
   * @see TypeVariable
   * @see #index
   * @see #offset
   */
  public TypeVariable ( int pIndex , int pOffset , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pIndex , pOffset ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public TypeVariable clone ( )
  {
    return new TypeVariable ( this.index , this.offset ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Comparable#compareTo(Object)
   */
  public int compareTo ( TypeVariable pTypeVariable )
  {
    if ( this.index < pTypeVariable.index )
    {
      return - 1 ;
    }
    else if ( this.index > pTypeVariable.index )
    {
      return 1 ;
    }
    else if ( this.offset < pTypeVariable.offset )
    {
      return - 1 ;
    }
    else if ( this.offset > pTypeVariable.offset )
    {
      return 1 ;
    }
    else
    {
      return 0 ;
    }
  }


  /**
   * Compares this type variable to the <code>obj</code>. Returns
   * <code>true</code> if <code>obj</code> is a type variable with the same
   * index and offset as this type variable. Otherwise <code>false</code> is
   * returned.
   * 
   * @param pObject another object.
   * @return <code>true</code> if <code>obj</code> is a type variable with
   *         the same index and offset.
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof TypeVariable )
    {
      TypeVariable other = ( TypeVariable ) pObject ;
      return ( this.index == other.index && this.offset == other.offset ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns the index of this variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number for
   * variables generated by the type inference algorithm. See the description of
   * the {@link TypeVariable} class for details about the <code>index</code>
   * and the <code>offset</code>.
   * 
   * @return the index of this variable.
   * @see #index
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_VARIABLE , 1 ,
        "\\textbf{\\color{" + LATEX_COLOR_TYPE + "}{$#1$}}" , "tvar" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * Returns the offset of this variable. See the description of the
   * {@link TypeVariable} class for details about the <code>index</code> and
   * the <code>offset</code>.
   * 
   * @return the offset of this variable.
   * @see #offset
   */
  public int getOffset ( )
  {
    return this.offset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#getTypeVariablesFree()
   */
  @ Override
  public ArrayList < TypeVariable > getTypeVariablesFree ( )
  {
    if ( this.typeVariablesFree == null )
    {
      this.typeVariablesFree = new ArrayList < TypeVariable > ( ) ;
      this.typeVariablesFree.add ( this ) ;
    }
    return this.typeVariablesFree ;
  }


  /**
   * Returns a hash value for this type variable, which is based on the index
   * and the offset of the type.
   * 
   * @return a hash value for this type variable.
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( this.index << 5 ) + this.offset ;
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
  @ Override
  public TypeVariable substitute ( @ SuppressWarnings ( UNUSED )
  TypeName pTypeName , @ SuppressWarnings ( UNUSED )
  MonoType pTau )
  {
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public MonoType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    // perform the substitution on this type variable
    MonoType tau = pTypeSubstitution.get ( this ) ;
    if ( ! tau.equals ( this ) )
    {
      // another type variable, substitute again
      tau = tau.substitute ( pTypeSubstitution ) ;
    }
    return tau ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_TYPE_VARIABLE , LATEX_TYPE_VARIABLE , pIndent , this
            .toPrettyString ( ).toString ( ) ) ;
    String type = offsetToGreekLetterLatex ( this.offset % 24 )
        + ( ( this.index > 0 ) ? String.valueOf ( this.index )
            : LATEX_EMPTY_STRING ) ;
    for ( int n = ( this.offset / 24 ) ; n > 0 ; -- n )
    {
      type = type + LATEX_BAR ;
    }
    builder.addText ( "{" + type + "}" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_TYPE_VARIABLE ) ;
      String type = offsetToGreekLetter ( this.offset % 24 )
          + ( ( this.index > 0 ) ? String.valueOf ( this.index )
              : PRETTY_EMPTY_STRING ) ;
      for ( int n = ( this.offset / 24 ) ; n > 0 ; -- n )
      {
        type = type + PRETTY_BAR ;
      }
      this.prettyStringBuilder.addType ( type ) ;
    }
    return this.prettyStringBuilder ;
  }
}
