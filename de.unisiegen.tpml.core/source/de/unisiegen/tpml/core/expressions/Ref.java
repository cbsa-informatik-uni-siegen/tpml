package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class represent the <code>ref</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Assign
 * @see Deref
 * @see UnaryOperator
 */
public final class Ref extends UnaryOperator
{

  /**
   * The keyword <code>ref</code>.
   */
  private static final String REF = "ref"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Ref.class );


  /**
   * The ref operator exception string.
   */
  private static final String HANDLED = "ref operator must be handled by the interpreter"; //$NON-NLS-1$


  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * Allocates a new <code>Ref</code> instance.
   */
  public Ref ()
  {
    super ( REF );
  }


  /**
   * Allocates a new <code>Ref</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Ref ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc} This method always throws
   * {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>ref</code> operator requires special handling
   * in the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the
   *           <code>ref</code> operator must be handled by the interpreter.
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
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
  public Ref clone ()
  {
    return new Ref ();
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
