package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents function types (so called <i>arrow types</i>) in our
 * type systems. Arrow types are composed of two types, the parameter type
 * <code>tau1</code> and the result type <code>tau2</code>, both
 * monomorphic types.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:420 $
 * @see BooleanType
 * @see IntegerType
 * @see MonoType
 */
public final class ArrowType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { 1 , 2 } ;


  /**
   * String for the case that tau1 is null.
   */
  private static final String TAU1_NULL = "tau1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that tau2 is null.
   */
  private static final String TAU2_NULL = "tau2 is null" ; //$NON-NLS-1$


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( ArrowType.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_ARROW_TYPE , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}#1\\ \\to\\ #2" , "tau1" , "tau2" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    return commands ;
  }


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new <code>ArrowType</code> with the types <code>tau1</code>
   * and <code>tau2</code>, which represents the type
   * <code>tau1 -&gt; tau2</code>.
   * 
   * @param pTau1 the parameter type.
   * @param pTau2 the result type.
   * @throws NullPointerException if either <code>pTau1</code> or
   *           <code>pTau2</code> are <code>null</code>.
   */
  public ArrowType ( MonoType pTau1 , MonoType pTau2 )
  {
    if ( pTau1 == null )
    {
      throw new NullPointerException ( TAU1_NULL ) ;
    }
    if ( pTau2 == null )
    {
      throw new NullPointerException ( TAU2_NULL ) ;
    }
    this.types = new MonoType [ ]
    { pTau1 , pTau2 } ;
    this.types [ 0 ].setParent ( this ) ;
    this.types [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>ArrowType</code> with the types <code>tau1</code>
   * and <code>tau2</code>, which represents the type
   * <code>tau1 -&gt; tau2</code>.
   * 
   * @param pTau1 the parameter type.
   * @param pTau2 the result type.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws NullPointerException if either <code>pTau1</code> or
   *           <code>pTau2</code> are <code>null</code>.
   */
  public ArrowType ( MonoType pTau1 , MonoType pTau2 , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTau1 , pTau2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ArrowType clone ( )
  {
    return new ArrowType ( this.types [ 0 ].clone ( ) , this.types [ 1 ]
        .clone ( ) ) ;
  }


  /**
   * Compares this arrow type to the <code>obj</code>. Returns
   * <code>true</code> if the <code>obj</code> is an <code>ArrowType</code>
   * with the same parameter and result types as this instance.
   * 
   * @param pObject another object.
   * @return <code>true</code> if this instance is equal to the specified
   *         <code>obj</code>.
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ArrowType )
    {
      ArrowType other = ( ArrowType ) pObject ;
      return ( this.types [ 0 ].equals ( other.types [ 0 ] ) && this.types [ 1 ]
          .equals ( other.types [ 1 ] ) ) ;
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
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = super.getLatexCommands ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    return commands ;
  }


  /**
   * Returns the parameter type <code>tau1</code> for this arrow type.
   * 
   * @return the parameter type <code>tau1</code>.
   * @see #getTau2()
   */
  public MonoType getTau1 ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the result type <code>tau2</code> for this arrow type.
   * 
   * @return the result type <code>tau2</code>.
   * @see #getTau1()
   */
  public MonoType getTau2 ( )
  {
    return this.types [ 1 ] ;
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
   * Returns a hash value for this arrow type, which is based on the hash values
   * for the parameter and result types.
   * 
   * @return a hash value for this arrow type.
   * @see Object#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.types [ 0 ].hashCode ( ) + this.types [ 1 ].hashCode ( ) ;
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
  public ArrowType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType newTau1 = this.types [ 0 ].substitute ( pTypeName , pTau ) ;
    MonoType newTau2 = this.types [ 1 ].substitute ( pTypeName , pTau ) ;
    return new ArrowType ( newTau1 , newTau2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public ArrowType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    return new ArrowType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ,
        this.types [ 1 ].substitute ( pTypeSubstitution ) ) ;
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
        PRIO_ARROW , LATEX_ARROW_TYPE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.types [ 0 ].toPrettyString ( ).toString ( ) ,
        this.types [ 1 ].toPrettyString ( ).toString ( ) ) ;
    builder
        .addBuilder ( this.types [ 0 ].toLatexStringBuilder (
            pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
            PRIO_ARROW_TAU1 ) ;
    builder
        .addBuilder ( this.types [ 1 ].toLatexStringBuilder (
            pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
            PRIO_ARROW_TAU2 ) ;
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
          PRIO_ARROW ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ARROW_TAU1 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_ARROW ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ARROW_TAU2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
