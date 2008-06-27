package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class represent the <code>Assign</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see Deref
 * @see Ref
 * @see BinaryOperator
 */
public final class Assign extends BinaryOperator
{

  /**
   * The assign operator exception string.
   */
  private static final String HANDLED = "assign operator must be handled by the interpreter"; //$NON-NLS-1$


  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * The keyword <code>:=</code>.
   */
  private static final String COLONEQUAL = ":="; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Assign.class );


  /**
   * Allocates a new <code>Assign</code> operator.
   */
  public Assign ()
  {
    super ( COLONEQUAL, PRIO_ASSIGN );
  }


  /**
   * Allocates a new <code>Assign</code> operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Assign ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc} The assign operator does not a machine equivalent like the
   * arithmetic or relational operators, but instead operates on a store. So
   * applying an assign operator to its operands must be implemented in the
   * interpreter. This method simply throws
   * {@link UnsupportedOperationException} on every invokation.
   * 
   * @throws UnsupportedOperationException on every invokation.
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @SuppressWarnings ( UNUSED )
  @Override
  public Expression applyTo ( Expression pExpression1, Expression pExpression2 )
      throws BinaryOperatorException
  {
    throw new UnsupportedOperationException ( HANDLED );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public Assign clone ()
  {
    return new Assign ();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }
}
