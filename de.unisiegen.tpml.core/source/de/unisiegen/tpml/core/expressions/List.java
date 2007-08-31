package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import java.util.Vector ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Syntactic sugar for lists.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see EmptyList
 * @see Expression
 * @see UnaryCons
 */
public final class List extends Expression implements DefaultExpressions
{
  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null" ; //$NON-NLS-1$


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
  private static final String CAPTION = Expression.getCaption ( List.class ) ;


  /**
   * The expressions within this list.
   * 
   * @see #getExpressions()
   */
  private Expression [ ] expressions ;


  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * Creates a new <code>List</code> with <code>e1</code> as the first list
   * item. <code>e2</code> can either be the empty list, anoter
   * <code>List</code> instance, or an application of the {@link UnaryCons}
   * operator to a pair where the second item can again be interpreted as
   * <code>List</code> using this constructor.
   * 
   * @param pExpression1 the first item.
   * @param pExpression2 another list.
   * @throws ClassCastException if none of the above conditions match.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  List ( Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL ) ;
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL ) ;
    }
    // allocate a vector for the expressions of the list and prepend e1 as new
    // first item
    Vector < Expression > newExpressions = new Vector < Expression > ( ) ;
    newExpressions.add ( pExpression1 ) ;
    // now check e2
    if ( pExpression2 instanceof EmptyList )
    {
      // e2 is the empty list, nothing to append
    }
    else if ( pExpression2 instanceof List )
    {
      // e2 is a List, append the items
      newExpressions.addAll ( Arrays.asList ( ( ( List ) pExpression2 )
          .getExpressions ( ) ) ) ;
    }
    else
    {
      // e2 must be an application of unary cons to a pair
      Application app2 = ( Application ) pExpression2 ;
      Tuple tuple = ( Tuple ) app2.getExpressions ( ) [ 1 ] ;
      if ( ! ( app2.getExpressions ( ) [ 0 ] instanceof UnaryCons )
          || tuple.getExpressions ( ).length != 2 )
      {
        throw new ClassCastException ( ) ;
      }
      // turn the tuple into a list
      List list = new List ( tuple.getExpressions ( ) [ 0 ] , tuple
          .getExpressions ( ) [ 1 ] ) ;
      // and add the list items to our expressions
      newExpressions.addAll ( Arrays.asList ( list.getExpressions ( ) ) ) ;
    }
    // Expression
    this.expressions = newExpressions.toArray ( new Expression [ 0 ] ) ;
    this.indicesE = new int [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      this.indicesE [ i ] = i + 1 ;
      this.expressions [ i ].setParent ( this ) ;
    }
  }


  /**
   * Allocates a new <code>List</code> instance with the specified
   * <code>expressions</code>.
   * 
   * @param pExpressions a non empty array of {@link Expression}s.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   */
  public List ( Expression [ ] pExpressions )
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
      this.indicesE [ i ] = i + 1 ;
      this.expressions [ i ].setParent ( this ) ;
    }
  }


  /**
   * Allocates a new <code>List</code> instance with the specified
   * <code>expressions</code>.
   * 
   * @param pExpressions a non empty array of {@link Expression}s.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   * @throws NullPointerException if <code>expressions</code> is
   *           <code>null</code>.
   */
  public List ( Expression [ ] pExpressions , int pParserStartOffset ,
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
  public List clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new List ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof List )
    {
      List other = ( List ) pObject ;
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
   * Returns the list expressions.
   * 
   * @return the expressions.
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
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_LIST , 1 , "[#1]" , //$NON-NLS-1$
        "e1; ... ; en" ) ) ; //$NON-NLS-1$
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
    return Arrays.hashCode ( this.expressions ) ;
  }


  /**
   * Returns the first in the list.
   * 
   * @return the first expression in the list.
   * @see #tail()
   */
  public Expression head ( )
  {
    return this.expressions [ 0 ] ;
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
  public List substitute ( Identifier pId , Expression pExpression )
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
    return new List ( newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public List substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < newExpressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new List ( newExpressions ) ;
  }


  /**
   * Returns the list without the first expression which may be the empty list.
   * 
   * @return the list without the first expression.
   * @see #head()
   */
  public Expression tail ( )
  {
    if ( this.expressions.length > 1 )
    {
      Expression [ ] newExpressions = new Expression [ this.expressions.length - 1 ] ;
      for ( int i = 0 ; i < newExpressions.length ; i ++ )
      {
        newExpressions [ i ] = this.expressions [ i + 1 ] ;
      }
      return new List ( newExpressions ) ;
    }
    return new EmptyList ( ) ;
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
    StringBuilder body = new StringBuilder ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( i > 0 )
      {
        body.append ( PRETTY_SEMI ) ;
        body.append ( PRETTY_SPACE ) ;
      }
      body.append ( this.expressions [ i ].toPrettyString ( ).toString ( ) ) ;
    }
    String descriptions[] = new String [ 2 + this.expressions.length ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      descriptions [ 2 + i ] = this.expressions [ i ].toPrettyString ( )
          .toString ( ) ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_LIST , LATEX_LIST , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( i > 0 )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT )
            + LATEX_SEMI ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addBreak ( ) ;
      }
      builder.addBuilder ( this.expressions [ i ].toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) ,
          PRIO_LIST_E ) ;
    }
    builder.addBuilderEnd ( ) ;
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
          PRIO_LIST ) ;
      this.prettyStringBuilder.addText ( PRETTY_LBRACKET ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        if ( i > 0 )
        {
          this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          this.prettyStringBuilder.addBreak ( ) ;
        }
        this.prettyStringBuilder.addBuilder ( this.expressions [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LIST_E ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_RBRACKET ) ;
    }
    return this.prettyStringBuilder ;
  }
}
