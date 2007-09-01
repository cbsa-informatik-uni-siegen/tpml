package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent send expressions.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Send extends Expression implements DefaultIdentifiers ,
    DefaultExpressions , SortedChildren
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
  private static final String CAPTION = Expression.getCaption ( Send.class ) ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new {@link Send}.
   * 
   * @param pExpression The child {@link Expression}.
   * @param pIdentifier The {@link Identifier}.
   */
  public Send ( Expression pExpression , Identifier pIdentifier )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL ) ;
    }
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( IDENTIFIER_NULL ) ;
    }
    if ( ! Identifier.Set.METHOD.equals ( pIdentifier.getSet ( ) ) )
    {
      throw new IllegalArgumentException ( WRONG_SET ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
    // Identifier
    this.identifiers = new Identifier [ ]
    { pIdentifier } ;
    this.identifiers [ 0 ].setParent ( this ) ;
  }


  /**
   * Allocates a new {@link Send}.
   * 
   * @param pExpression The child {@link Expression}.
   * @param pIdentifier The {@link Identifier}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Send ( Expression pExpression , Identifier pIdentifier ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression , pIdentifier ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send clone ( )
  {
    return new Send ( this.expressions [ 0 ].clone ( ) , this.identifiers [ 0 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Send )
    {
      Send other = ( Send ) pObject ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.identifiers [ 0 ]
          .equals ( other.identifiers [ 0 ] ) ) ) ;
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
   * Returns the child {@link Expression}.
   * 
   * @return The child {@link Expression}.
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
    commands.add ( new DefaultLatexCommand ( LATEX_SEND , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}#1\\ \\#\\ #2" , "e" , "m" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * Returns the {@link Identifier}s and {@link Expression}s in the right
   * sorting.
   * 
   * @return The {@link Identifier}s and {@link Expression}s in the right
   *         sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public ExpressionOrType [ ] getSortedChildren ( )
  {
    ExpressionOrType [ ] result = new ExpressionOrType [ 2 ] ;
    result [ 0 ] = this.expressions [ 0 ] ;
    result [ 1 ] = this.identifiers [ 0 ] ;
    return result ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( )
        + this.identifiers [ 0 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Send substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Send ( newE , this.identifiers [ 0 ] ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Send substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Send ( newE , this.identifiers [ 0 ] ) ;
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
        PRIO_SEND , LATEX_SEND , pIndent ,
        this.toPrettyString ( ).toString ( ) , this.expressions [ 0 ]
            .toPrettyString ( ).toString ( ) , this.identifiers [ 0 ]
            .toPrettyString ( ).toString ( ) ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_SEND_E ) ;
    builder.addBuilder ( this.identifiers [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_ID ) ;
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
          PRIO_SEND ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_SEND_E ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_HASHKEY ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
    }
    return this.prettyStringBuilder ;
  }
}
