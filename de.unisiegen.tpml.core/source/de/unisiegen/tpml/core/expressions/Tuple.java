package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Instances of this class represent tuples in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Tuple extends Expression implements DefaultExpressions
{
  /**
   * String for the case that the expressions are null.
   */
  private static final String EXPRESSIONS_NULL = "expressions is null" ; //$NON-NLS-1$


  /**
   * String for the case that one expression are null.
   */
  private static final String EXPRESSION_NULL = "one expression is null" ; //$NON-NLS-1$


  /**
   * String for the case that the expressions are empty.
   */
  private static final String EXPRESSIONS_EMPTY = "expressions is empty" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Tuple.class ) ;


  /**
   * The sub expressions.
   * 
   * @see #getExpressions()
   */
  private Expression [ ] expressions ;


  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * Allocates a new <code>Tuple</code> with the given
   * <code>expressions</code>.
   * 
   * @param pExpressions a non-empty array of {@link Expression}s.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   */
  public Tuple ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( EXPRESSIONS_NULL ) ;
    }
    for ( Expression e : pExpressions )
    {
      if ( e == null )
      {
        throw new NullPointerException ( EXPRESSION_NULL ) ;
      }
    }
    if ( pExpressions.length == 0 )
    {
      throw new IllegalArgumentException ( EXPRESSIONS_EMPTY ) ;
    }
    this.expressions = pExpressions ;
    this.indicesE = new int [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      this.expressions [ i ].setParent ( this ) ;
      this.indicesE [ i ] = i + 1 ;
    }
  }


  /**
   * Allocates a new <code>Tuple</code> with the given
   * <code>expressions</code>.
   * 
   * @param pExpressions a non-empty array of {@link Expression}s.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   */
  public Tuple ( Expression [ ] pExpressions , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pExpressions ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Tuple clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Tuple ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Tuple )
    {
      Tuple other = ( Tuple ) pObject ;
      return Arrays.equals ( this.expressions , other.expressions ) ;
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
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
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
    return this.indicesE ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_TUPLE , 1 , "(#1)" , //$NON-NLS-1$
        "e1, ... , en" ) ) ; //$NON-NLS-1$
    for ( Expression child : this.expressions )
    {
      for ( LatexCommand command : child.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#isValue()
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ! e.isValue ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Tuple substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression ) ;
    }
    return new Tuple ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_TUPLE , LATEX_TUPLE ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      for ( int n = 0 ; n < this.expressions.length ; ++ n )
      {
        if ( n > 0 )
        {
          this.latexStringBuilder.addText ( LATEX_COMMA ) ;
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
          this.latexStringBuilder.addCanBreakHere ( ) ;
        }
        this.latexStringBuilder
            .addBuilder ( this.expressions [ n ]
                .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
                PRIO_TUPLE_E ) ;
      }
      this.latexStringBuilder.addBuilderEnd ( ) ;
    }
    return this.latexStringBuilder ;
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
          PRIO_TUPLE ) ;
      this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
      for ( int n = 0 ; n < this.expressions.length ; ++ n )
      {
        if ( n > 0 )
        {
          this.prettyStringBuilder.addText ( PRETTY_COMMA ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          this.prettyStringBuilder.addBreak ( ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.expressions [ n ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_TUPLE_E ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
    }
    return this.prettyStringBuilder ;
  }
}
