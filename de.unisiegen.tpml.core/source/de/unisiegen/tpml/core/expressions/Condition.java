package de.unisiegen.tpml.core.expressions;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;


/**
 * Represents the conditional evaluation in the expression hierarchy. The string
 * representation for conditions is <code>if e0 then e1 else e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Condition extends Expression implements DefaultExpressions
{

  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [] INDICES_E = new int []
  { 0, 1, 2 };


  /**
   * String for the case that e0 is null.
   */
  private static final String E0_NULL = "e0 is null"; //$NON-NLS-1$


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null"; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Condition.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_IF, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{if}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_THEN, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{then}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_ELSE, 0,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{else}}" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_CONDITION, 3, "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_IF + "\\ #1\\ \\" //$NON-NLS-1$ //$NON-NLS-2$
        + LATEX_KEY_THEN + "\\ #2\\ \\" + LATEX_KEY_ELSE + "\\ #3", "e0", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        "e1", "e2" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return commands;
  }


  /**
   * The expressions.
   */
  private Expression [] expressions;


  /**
   * Allocates a new <code>Condition</code> with the specified <code>e0</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression0 the condition.
   * @param pExpression1 the <code>true</code> case.
   * @param pExpression2 the <code>false</code> case.
   * @throws NullPointerException if <code>e0</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Condition ( Expression pExpression0, Expression pExpression1,
      Expression pExpression2 )
  {
    if ( pExpression0 == null )
    {
      throw new NullPointerException ( E0_NULL );
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL );
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL );
    }
    this.expressions = new Expression []
    { pExpression0, pExpression1, pExpression2 };
    this.expressions [ 0 ].setParent ( this );
    this.expressions [ 1 ].setParent ( this );
    this.expressions [ 2 ].setParent ( this );
  }


  /**
   * Allocates a new <code>Condition</code> with the specified <code>e0</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pExpression0 the condition.
   * @param pExpression1 the <code>true</code> case.
   * @param pExpression2 the <code>false</code> case.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>e0</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Condition ( Expression pExpression0, Expression pExpression1,
      Expression pExpression2, int pParserStartOffset, int pParserEndOffset )
  {
    this ( pExpression0, pExpression1, pExpression2 );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public Condition clone ()
  {
    return new Condition ( this.expressions [ 0 ].clone (),
        this.expressions [ 1 ].clone (), this.expressions [ 2 ].clone () );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Condition )
    {
      Condition other = ( Condition ) pObject;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.expressions [ 1 ].equals ( other.expressions [ 1 ] ) ) && ( this.expressions [ 2 ]
          .equals ( other.expressions [ 2 ] ) ) );
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
   * Returns the conditional expression.
   * 
   * @return the conditional expression.
   */
  public Expression getE0 ()
  {
    return this.expressions [ 0 ];
  }


  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to
   * <code>true</code>.
   * 
   * @return the <code>true</code> case.
   */
  public Expression getE1 ()
  {
    return this.expressions [ 1 ];
  }


  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to
   * <code>false</code>.
   * 
   * @return the <code>false</code> case.
   */
  public Expression getE2 ()
  {
    return this.expressions [ 2 ];
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
    return INDICES_E;
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
    return this.expressions [ 0 ].hashCode ()
        + this.expressions [ 1 ].hashCode ()
        + this.expressions [ 2 ].hashCode ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @Override
  public Condition substitute ( Identifier pId, Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable () )
    {
      throw new NotOnlyFreeVariableException ();
    }
    Expression newE0 = this.expressions [ 0 ].substitute ( pId, pExpression );
    Expression newE1 = this.expressions [ 1 ].substitute ( pId, pExpression );
    Expression newE2 = this.expressions [ 2 ].substitute ( pId, pExpression );
    return new Condition ( newE0, newE1, newE2 );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @Override
  public Condition substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE0 = this.expressions [ 0 ].substitute ( pTypeSubstitution );
    Expression newE1 = this.expressions [ 1 ].substitute ( pTypeSubstitution );
    Expression newE2 = this.expressions [ 2 ].substitute ( pTypeSubstitution );
    return new Condition ( newE0, newE1, newE2 );
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
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_CONDITION, LATEX_CONDITION, pIndent, this.toPrettyString ()
            .toString (), this.expressions [ 0 ].toPrettyString ().toString (),
        this.expressions [ 1 ].toPrettyString ().toString (),
        this.expressions [ 2 ].toPrettyString ().toString () );
    builder
        .addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
            pLatexStringBuilderFactory, pIndent + LATEX_INDENT ),
            PRIO_CONDITION_E0 );
    builder.addBreak ();
    builder
        .addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
            pLatexStringBuilderFactory, pIndent + LATEX_INDENT ),
            PRIO_CONDITION_E1 );
    builder.addBreak ();
    builder
        .addBuilder ( this.expressions [ 2 ].toLatexStringBuilder (
            pLatexStringBuilderFactory, pIndent + LATEX_INDENT ),
            PRIO_CONDITION_E2 );
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
          PRIO_CONDITION );
      this.prettyStringBuilder.addKeyword ( PRETTY_IF );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
          PRIO_CONDITION_E0 );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBreak ();
      this.prettyStringBuilder.addKeyword ( PRETTY_THEN );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
          PRIO_CONDITION_E1 );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBreak ();
      this.prettyStringBuilder.addKeyword ( PRETTY_ELSE );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addBuilder ( this.expressions [ 2 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ),
          PRIO_CONDITION_E2 );
    }
    return this.prettyStringBuilder;
  }
}
