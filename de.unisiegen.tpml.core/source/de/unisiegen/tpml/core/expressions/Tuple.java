package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
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
   * The keyword <code>(</code>.
   */
  private static final String LPAREN = "(" ; //$NON-NLS-1$


  /**
   * The keyword <code>)</code>.
   */
  private static final String RPAREN = ")" ; //$NON-NLS-1$


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
   * The space string.
   */
  private static final String SPACE = " " ; //$NON-NLS-1$


  /**
   * The comma string.
   */
  private static final String COMMA = "," ; //$NON-NLS-1$


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
      this.prettyStringBuilder.addText ( LPAREN ) ;
      for ( int n = 0 ; n < this.expressions.length ; ++ n )
      {
        if ( n > 0 )
        {
          this.prettyStringBuilder.addText ( COMMA ) ;
          this.prettyStringBuilder.addText ( SPACE ) ;
          this.prettyStringBuilder.addBreak ( ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.expressions [ n ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_TUPLE_E ) ;
      }
      this.prettyStringBuilder.addText ( RPAREN ) ;
    }
    return this.prettyStringBuilder ;
  }
}
