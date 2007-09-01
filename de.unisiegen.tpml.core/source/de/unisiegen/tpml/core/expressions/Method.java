package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Instances of this class represent method expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public class Method extends Expression implements DefaultIdentifiers ,
    DefaultTypes , DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private static final int [ ] INDICES_ID = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * String for the case that the identifier is null.
   */
  private static final String IDENTIFIER_NULL = "identifier is null" ; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'method'" ; //$NON-NLS-1$


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Method.class ) ;


  /**
   * The identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  private MonoType [ ] types ;


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new {@link Method}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pTau The {@link Type}.
   * @param pExpression The child {@link Expression}.
   */
  public Method ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( IDENTIFIER_NULL ) ;
    }
    if ( ! Identifier.Set.METHOD.equals ( pIdentifier.getSet ( ) ) )
    {
      throw new IllegalArgumentException ( WRONG_SET ) ;
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL ) ;
    }
    // Identifier
    this.identifiers = new Identifier [ ]
    { pIdentifier } ;
    this.identifiers [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ ]
    { pTau } ;
    if ( this.types [ 0 ] != null )
    {
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new {@link Method}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pTau The {@link Type}.
   * @param pExpression The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Method ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifier , pTau , pExpression ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Method clone ( )
  {
    return new Method ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Method )
    {
      Method other = ( Method ) pObject ;
      return ( ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ) ;
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
   * Returns the sub {@link Expression}.
   * 
   * @return the sub {@link Expression}.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the sub {@link Expression}s.
   * 
   * @return the sub {@link Expression}s.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return INDICES_ID ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_METHOD , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{method}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_METHOD , 3 ,
        "\\ifthenelse{\\equal{#2}{}}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_METHOD //$NON-NLS-1$//$NON-NLS-2$
            + "\\ #1\\ =\\ #3\\ ;}" + LATEX_LINE_BREAK_NEW_COMMAND //$NON-NLS-1$
            + "{\\color{" + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_METHOD //$NON-NLS-1$ //$NON-NLS-2$
            + "\\ #1\\colon\\ #2\\ =\\ #3\\ ;}" , "m" , "tau" , "e" ) ) ; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    return commands ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  @ Override
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = super.getLatexPackages ( ) ;
    packages.add ( new DefaultLatexPackage ( "ifthen" ) ) ; //$NON-NLS-1$
    return packages ;
  }


  /**
   * Returns the sub {@link Type}.
   * 
   * @return the sub {@link Type}.
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
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( )
        + ( this.types [ 0 ] == null ? 0 : this.types [ 0 ].hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expressions [ 0 ].isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Method substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Method ( this.identifiers [ 0 ] , this.types [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Method substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Method ( this.identifiers [ 0 ] , newTau , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_METHOD , LATEX_METHOD , pIndent , this.toPrettyString ( )
            .toString ( ) , this.identifiers [ 0 ].toPrettyString ( )
            .toString ( ) , this.types [ 0 ] == null ? LATEX_EMPTY_STRING
            : this.types [ 0 ].toPrettyString ( ).toString ( ) ,
        this.expressions [ 0 ].toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.identifiers [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_ID ) ;
    if ( this.types [ 0 ] == null )
    {
      builder.addEmptyBuilder ( ) ;
    }
    else
    {
      builder.addBuilder ( this.types [ 0 ].toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
          PRIO_METHOD_TAU ) ;
    }
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_METHOD_E ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_METHOD ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_METHOD ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_METHOD_TAU ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_EQUAL ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_METHOD_E ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
    }
    return this.prettyStringBuilder ;
  }
}
