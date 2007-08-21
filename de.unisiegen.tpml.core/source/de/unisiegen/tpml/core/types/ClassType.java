package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * This class represents class types in our type systems.
 * 
 * @author Christian Fehler
 * @version $Rev:1850 $
 */
public final class ClassType extends MonoType implements DefaultTypes
{
  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 , - 1 } ;


  /**
   * String for the case that tau is null.
   */
  private static final String TAU_NULL = "tau is null" ; //$NON-NLS-1$


  /**
   * String for the case that phi is null.
   */
  private static final String PHI_NULL = "phi is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( ClassType.class ) ;


  /**
   * String for the case that the type substitution is null.
   */
  private static final String TYPE_SUBSTITUTION_NULL = "type substitution is null" ; //$NON-NLS-1$


  /**
   * The children {@link Type}s of this {@link Type}.
   */
  private MonoType [ ] types ;


  /**
   * Allocates a new class type.
   * 
   * @param pTau The type.
   * @param pPhi The phi.
   * @throws NullPointerException if either <code>pTau</code> or
   *           <code>pPhi</code> are <code>null</code>.
   */
  public ClassType ( MonoType pTau , MonoType pPhi )
  {
    if ( pTau == null )
    {
      throw new NullPointerException ( TAU_NULL ) ;
    }
    if ( pPhi == null )
    {
      throw new NullPointerException ( PHI_NULL ) ;
    }
    this.types = new MonoType [ ]
    { pTau , pPhi } ;
    this.types [ 0 ].setParent ( this ) ;
    this.types [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new class type.
   * 
   * @param pTau The type.
   * @param pPhi The phi.
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   * @throws NullPointerException if either <code>pTau</code> or
   *           <code>pPhi</code> are <code>null</code>.
   */
  public ClassType ( MonoType pTau , MonoType pPhi , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pTau , pPhi ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public ClassType clone ( )
  {
    return new ClassType ( this.types [ 0 ].clone ( ) , this.types [ 1 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ClassType )
    {
      ClassType other = ( ClassType ) pObject ;
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
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( "boldZeta" , 0 , //$NON-NLS-1$
        "\\textbf{$\\zeta$}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_CLASS_TYPE , 2 ,
        "\\boldZeta( #1\\ \\colon\\ #2)" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.types [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.types [ 1 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns the sub phi.
   * 
   * @return The sub phi.
   */
  public MonoType getPhi ( )
  {
    return this.types [ 1 ] ;
  }


  /**
   * Returns the sub type.
   * 
   * @return The sub type.
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
  public ClassType substitute ( TypeName pTypeName , MonoType pTau )
  {
    MonoType newTau = this.types [ 0 ].substitute ( pTypeName , pTau ) ;
    MonoType newPhi = this.types [ 1 ].substitute ( pTypeName , pTau ) ;
    return new ClassType ( newTau , newPhi ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  @ Override
  public ClassType substitute ( TypeSubstitution pTypeSubstitution )
  {
    if ( pTypeSubstitution == null )
    {
      throw new NullPointerException ( TYPE_SUBSTITUTION_NULL ) ;
    }
    return new ClassType ( this.types [ 0 ].substitute ( pTypeSubstitution ) ,
        this.types [ 1 ].substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_CLASS , LATEX_CLASS_TYPE ) ;
      this.latexStringBuilder
          .addBuilder ( this.types [ 0 ]
              .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
              PRIO_CLASS_TAU ) ;
      this.latexStringBuilder.addBreak ( ) ;
      this.latexStringBuilder
          .addBuilder ( this.types [ 1 ]
              .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
              PRIO_CLASS_PHI ) ;
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
          PRIO_CLASS ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_ZETA ) ;
      this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CLASS_TAU ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CLASS_PHI ) ;
      this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
    }
    return this.prettyStringBuilder ;
  }
}
