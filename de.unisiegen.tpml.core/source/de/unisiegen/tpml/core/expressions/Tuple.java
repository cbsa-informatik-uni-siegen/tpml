package de.unisiegen.tpml.core.expressions;


import java.util.Arrays;

import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


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
  private static final String EXPRESSIONS_NULL = "expressions is null"; //$NON-NLS-1$


  /**
   * String for the case that one expression are null.
   */
  private static final String EXPRESSION_NULL = "one expression is null"; //$NON-NLS-1$


  /**
   * String for the case that the expressions are empty.
   */
  private static final String EXPRESSIONS_EMPTY = "expressions is empty"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Tuple.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_TUPLE, 1, "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}(#1)", "e1, ... , en" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return commands;
  }


  /**
   * The sub expressions.
   * 
   * @see #getExpressions()
   */
  private Expression [] expressions;


  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [] indicesE;


  /**
   * Allocates a new <code>Tuple</code> with the given
   * <code>expressions</code>.
   * 
   * @param pExpressions a non-empty array of {@link Expression}s.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   */
  public Tuple ( Expression [] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( EXPRESSIONS_NULL );
    }
    for ( Expression e : pExpressions )
    {
      if ( e == null )
      {
        throw new NullPointerException ( EXPRESSION_NULL );
      }
    }
    if ( pExpressions.length == 0 )
    {
      throw new IllegalArgumentException ( EXPRESSIONS_EMPTY );
    }
    this.expressions = pExpressions;
    this.indicesE = new int [ this.expressions.length ];
    for ( int i = 0 ; i < this.expressions.length ; i++ )
    {
      this.expressions [ i ].setParent ( this );
      this.indicesE [ i ] = i + 1;
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
  public Tuple ( Expression [] pExpressions, int pParserStartOffset,
      int pParserEndOffset )
  {
    this ( pExpressions );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public Tuple clone ()
  {
    Expression [] newExpressions = new Expression [ this.expressions.length ];
    for ( int i = 0 ; i < newExpressions.length ; i++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ();
    }
    return new Tuple ( newExpressions );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Tuple )
    {
      Tuple other = ( Tuple ) pObject;
      return Arrays.equals ( this.expressions, other.expressions );
    }
    return false;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   */
  public Expression [] getExpressions ()
  {
    return this.expressions;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [] getExpressionsIndex ()
  {
    return this.indicesE;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @Override
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = super.getLatexCommands ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @Override
  public int hashCode ()
  {
    return this.expressions.hashCode ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#isValue()
   */
  @Override
  public boolean isValue ()
  {
    for ( Expression e : this.expressions )
    {
      if ( !e.isValue () )
      {
        return false;
      }
    }
    return true;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @Override
  public Tuple substitute ( Identifier pId, Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable () )
    {
      throw new NotOnlyFreeVariableException ();
    }
    Expression [] newExpressions = new Expression [ this.expressions.length ];
    for ( int i = 0 ; i < newExpressions.length ; i++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId,
          pExpression );
    }
    return new Tuple ( newExpressions );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    StringBuilder body = new StringBuilder ();
    for ( int i = 0 ; i < this.expressions.length ; i++ )
    {
      if ( i > 0 )
      {
        body.append ( PRETTY_COMMA );
        body.append ( PRETTY_SPACE );
      }
      body.append ( this.expressions [ i ].toPrettyString ().toString () );
    }
    String descriptions[] = new String [ 2 + this.expressions.length ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body.toString ();
    for ( int i = 0 ; i < this.expressions.length ; i++ )
    {
      descriptions [ 2 + i ] = this.expressions [ i ].toPrettyString ()
          .toString ();
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_TUPLE, LATEX_TUPLE, pIndent, descriptions );
    builder.addBuilderBegin ();
    for ( int i = 0 ; i < this.expressions.length ; i++ )
    {
      if ( i > 0 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_COMMA );
        builder.addText ( LATEX_SPACE );
        builder.addBreak ();
      }
      builder.addBuilder ( this.expressions [ i ].toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ),
          PRIO_TUPLE_E );
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this,
          PRIO_TUPLE );
      this.prettyStringBuilder.addText ( PRETTY_LPAREN );
      for ( int i = 0 ; i < this.expressions.length ; i++ )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( PRETTY_COMMA );
          this.prettyStringBuilder.addText ( PRETTY_SPACE );
          this.prettyStringBuilder.addBreak ();
        }
        this.prettyStringBuilder.addBuilder ( this.expressions [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
            PRIO_TUPLE_E );
      }
      this.prettyStringBuilder.addText ( PRETTY_RPAREN );
    }
    return this.prettyStringBuilder;
  }
}
