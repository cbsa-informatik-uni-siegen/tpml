package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Ref ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent reference types in our type system.
 * Reference types are types for expressions of type
 * {@link de.unisiegen.tpml.core.expressions.Location} as returned by the
 * {@link de.unisiegen.tpml.core.expressions.Ref} operator.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:345 $
 * @see Location
 * @see Ref
 * @see MonoType
 */
public final class RefType extends MonoType implements DefaultTypes
{
  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * String for the case that tau is null.
   */
  private static final String TAU_NULL = "tau is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( RefType.class ) ;


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new <code>RefType</code> for the monomorphic type
   * <code>tau</code>. I.e. if <code>tau</code> is <code>bool</code>,
   * the reference type will be <code>bool ref</code>.
   * 
   * @param pTau the monomorphic base type.
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   */
  public RefType ( MonoType pTau )
  {
    if ( pTau == null )
    {
      throw new NullPointerException ( TAU_NULL ) ;
    }
    this.types = new MonoType [ ]
    { pTau } ;
    this.types [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>RefType</code> for the monomorphic type
   * <code>tau</code>. I.e. if <code>tau</code> is <code>bool</code>,
   * the reference type will be <code>bool ref</code>.
   * 
   * @param pTau the monomorphic base type.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   */
  public RefType ( MonoType pTau , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pTau ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public RefType clone ( )
  {
    return new RefType ( this.types [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof RefType )
    {
      RefType other = ( RefType ) pObject ;
      return ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ;
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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_REF , 0 ,
        "\\textbf{ref}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_REF_TYPE , 1 , "#1\\ \\" //$NON-NLS-1$
        + LATEX_KEY_REF , "tau" ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * Returns the base type of the reference type.
   * 
   * @return the base type.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return ( ( this.types [ 0 ].hashCode ( ) + 13 ) * 17 ) / 7 ;
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
  public RefType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType newTau = this.types [ 0 ].substitute ( pTypeName , pTau ) ;
    return new RefType ( newTau ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see MonoType#substitute(TypeSubstitution)
   */
  @ Override
  public MonoType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    return new RefType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ) ;
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
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder (
          PRIO_REF , LATEX_REF_TYPE , pIndent , this.toPrettyString ( )
              .toString ( ) , this.types [ 0 ].toPrettyString ( ).toString ( ) ) ;
      this.latexStringBuilder.addBuilder ( this.types [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory , pIndent
              + LATEX_INDENT ) , PRIO_REF_TAU ) ;
    }
    return this.latexStringBuilder ;
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
          PRIO_REF ) ;
      this.prettyStringBuilder
          .addBuilder ( this.types [ 0 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_REF_TAU ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addType ( PRETTY_REF ) ;
    }
    return this.prettyStringBuilder ;
  }
}
