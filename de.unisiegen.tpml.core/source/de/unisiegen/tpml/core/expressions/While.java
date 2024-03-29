package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent <code>while</code> expressions in the
 * expression hierarchy that serve as syntactic sugar introduce with the
 * imperative concepts.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class While extends Expression implements DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , 2 } ;


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( While.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_WHILE , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{while}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_DO , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{do}}" ) ) ; //$NON-NLS-1$//$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_WHILE , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_WHILE + "\\ #1\\ \\" //$NON-NLS-1$//$NON-NLS-2$
        + LATEX_KEY_DO + "\\ #2" , "e1" , "e2" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * The first and second expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>While</code> instance with the specified
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression1 the conditional part.
   * @param pExpression2 the repeated statement.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public While ( Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL ) ;
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL ) ;
    }
    this.expressions = new Expression [ ]
    { pExpression1 , pExpression2 } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>While</code> instance with the specified
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression1 the conditional part.
   * @param pExpression2 the repeated statement.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public While ( Expression pExpression1 , Expression pExpression2 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression1 , pExpression2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public While clone ( )
  {
    return new While ( this.expressions [ 0 ].clone ( ) , this.expressions [ 1 ]
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof While )
    {
      While other = ( While ) obj ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
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
   * Returns the conditional part.
   * 
   * @return the conditional part.
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the repeated statement.
   * 
   * @return the loop body.
   */
  public Expression getE2 ( )
  {
    return this.expressions [ 1 ] ;
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
    return INDICES_E ;
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
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public While substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE1 = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pId , pExpression ) ;
    return new While ( newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public While substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new While ( newE1 , newE2 ) ;
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
        PRIO_WHILE , LATEX_WHILE , pIndent , this.toPrettyString ( )
            .toString ( ) , this.expressions [ 0 ].toPrettyString ( )
            .toString ( ) , this.expressions [ 1 ].toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_WHILE_E1 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , PRIO_WHILE_E2 ) ;
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
          PRIO_WHILE ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_WHILE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_WHILE_E1 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_DO ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_WHILE_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
