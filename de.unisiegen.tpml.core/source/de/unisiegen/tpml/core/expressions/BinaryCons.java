package de.unisiegen.tpml.core.expressions;


/**
 * The binary version of the <code>cons</code> operator, written as
 * <code>::</code> (two colons), which is syntactic sugar for the unary
 * <code>cons</code> operator.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see BinaryOperator
 * @see UnaryCons
 */
public final class BinaryCons extends BinaryOperator
{

  /**
   * The keyword <code>::</code>.
   */
  private static final String COLONCOLON = "::"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( BinaryCons.class );


  /**
   * Allocates a new <code>BinaryCons</code> instances.
   */
  public BinaryCons ()
  {
    super ( COLONCOLON, PRIO_BINARY_CONS );
  }


  /**
   * Allocates a new <code>BinaryCons</code> instances.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public BinaryCons ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @Override
  public Expression applyTo ( Expression pExpression1, Expression pExpression2 )
      throws BinaryOperatorException
  {
    try
    {
      // try to create a new list from e1 and e2
      return new List ( pExpression1, pExpression2 );
    }
    catch ( ClassCastException e )
    {
      // we're stuck
      throw new BinaryOperatorException ( this, pExpression1, pExpression2 );
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public BinaryCons clone ()
  {
    return new BinaryCons ();
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
