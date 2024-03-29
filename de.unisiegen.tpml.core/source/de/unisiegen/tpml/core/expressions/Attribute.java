package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent attribute expressions.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class Attribute extends Expression implements BoundIdentifiers ,
    DefaultExpressions
{
  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'attribute'" ; //$NON-NLS-1$


  /**
   * String for the case that the identifier is null.
   */
  private static final String IDENTIFIER_NULL = "identifier is null" ; //$NON-NLS-1$


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Attribute.class ) ;


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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_VAL , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{val}}" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_ATTRIBUTE , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_VAL //$NON-NLS-1$
        + "\\ #1\\ =\\ #2\\ ;" , "a" , "e" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new {@link Attribute}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pExpression The child {@link Expression}.
   */
  public Attribute ( Identifier pIdentifier , Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( IDENTIFIER_NULL ) ;
    }
    if ( ! Identifier.Set.ATTRIBUTE.equals ( pIdentifier.getSet ( ) ) )
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
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new {@link Attribute}.
   * 
   * @param pIdentifier The {@link Identifier}.
   * @param pExpression The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Attribute ( Identifier pIdentifier , Expression pExpression ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pIdentifier , pExpression ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attribute clone ( )
  {
    return new Attribute ( this.identifiers [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Attribute )
    {
      Attribute other = ( Attribute ) pObject ;
      return ( ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) ) && ( this.expressions [ 0 ]
          .equals ( other.expressions [ 0 ] ) ) ) ;
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
   * Returns a list of all {@link Attribute} {@link Identifier}s in the domain
   * of this {@link Expression}.
   * 
   * @return A list of all {@link Attribute} {@link Identifier}s in the domain
   *         of this {@link Expression}.
   */
  @ Override
  public ArrayList < Identifier > getDomA ( )
  {
    if ( this.domA == null )
    {
      this.domA = new ArrayList < Identifier > ( ) ;
      this.domA.add ( this.identifiers [ 0 ] ) ;
      this.domA.addAll ( this.expressions [ 0 ].getDomA ( ) ) ;
    }
    return this.domA ;
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
   * Returns the {@link Identifier} of this {@link Expression}.
   * 
   * @return The {@link Identifier} of this {@link Expression}.
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
   * Returns a list of lists of in this {@link Expression} bound
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bound
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundIdList = ( ( Row ) this.parent )
          .getIdentifiersBound ( this ) ;
      // Set the bound Identifier to an Attribute-Identifier
      for ( Identifier boundId : boundIdList )
      {
        boundId.setSet ( Identifier.Set.ATTRIBUTE ) ;
      }
      this.boundIdentifiers.add ( boundIdList ) ;
    }
    return this.boundIdentifiers ;
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
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = super.getLatexCommands ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    return commands ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( ) ;
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
  public Attribute substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Attribute ( this.identifiers [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Attribute substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Attribute ( this.identifiers [ 0 ] , newE ) ;
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
        PRIO_ATTRIBUTE , LATEX_ATTRIBUTE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.identifiers [ 0 ].toPrettyString ( )
            .toString ( ) , this.expressions [ 0 ].toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.identifiers [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_ID ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
        PRIO_ATTRIBUTE_E ) ;
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
          PRIO_ATTRIBUTE ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_VAL ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_EQUAL ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ATTRIBUTE_E ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
    }
    return this.prettyStringBuilder ;
  }
}
