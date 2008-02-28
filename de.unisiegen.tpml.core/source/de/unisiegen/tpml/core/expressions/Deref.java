package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class represent the <code>!</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Assign
 * @see Ref
 * @see UnaryOperator
 */
public final class Deref extends UnaryOperator
{

  /**
   * The keyword <code>!</code>.
   */
  private static final String EXCLAMATION = "!"; //$NON-NLS-1$


  /**
   * The deref operator exception string.
   */
  private static final String HANDLED = "deref operator must be handled by the interpreter"; //$NON-NLS-1$


  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Deref.class );


  /**
   * Allocates a new <code>Deref</code> instance.
   */
  public Deref ()
  {
    super ( EXCLAMATION );
  }


  /**
   * Allocates a new <code>Deref</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Deref ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc} This method always throws
   * {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>!</code> operator requires special handling in
   * the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the
   *           <code>!</code> operator must be handled by the interpreter.
   * @see UnaryOperator#applyTo(Expression)
   */
  @SuppressWarnings ( UNUSED )
  @Override
  public Expression applyTo ( @SuppressWarnings ( UNUSED )
  Expression pExpression ) throws UnaryOperatorException
  {
    throw new UnsupportedOperationException ( HANDLED );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public Deref clone ()
  {
    return new Deref ();
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
