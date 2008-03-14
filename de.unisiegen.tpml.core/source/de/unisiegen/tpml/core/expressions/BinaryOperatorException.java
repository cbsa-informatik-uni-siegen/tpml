package de.unisiegen.tpml.core.expressions;


/**
 * This exception is thrown whenever a {@link BinaryOperator} is being applied
 * to invalid operands during the evaluation. For example if the arithmetic plus
 * operator is applied to <code>true</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 */
public final class BinaryOperatorException extends Exception
{

  /**
   * The unique serial version id.
   */
  private static final long serialVersionUID = 3206810160508438333L;


  /**
   * Cannot apply exception string.
   */
  private static final String CANNOT_APPLY_1 = "cannot apply "; //$NON-NLS-1$


  /**
   * Cannot apply exception string.
   */
  private static final String CANNOT_APPLY_2 = " to "; //$NON-NLS-1$


  /**
   * Cannot apply exception string.
   */
  private static final String CANNOT_APPLY_3 = " and "; //$NON-NLS-1$


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null"; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null"; //$NON-NLS-1$


  /**
   * String for the case that the operator is null.
   */
  private static final String OPERATOR_NULL = "operator is null"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( BinaryOperatorException.class );


  /**
   * The binary operator that failed to apply.
   * 
   * @see #getOperator()
   */
  private BinaryOperator operator;


  /**
   * The first operand.
   * 
   * @see #getE1()
   */
  private Expression e1;


  /**
   * The second operand.
   * 
   * @see #getE2()
   */
  private Expression e2;


  /**
   * Allocates a new <code>BinaryOperatorException</code>, which signals that
   * the specified binary <code>operator</code> could not be applied to the
   * operands <code>e1</code> and <code>e2</code>.
   * 
   * @param pOperator the {@link BinaryOperator} that failed to apply.
   * @param pExpression1 the first operand.
   * @param pExpression2 the second operand.
   * @throws NullPointerException if <code>operator</code>, <code>e1</code>
   *           or <code>e2</code> is <code>null</code>.
   */
  public BinaryOperatorException ( BinaryOperator pOperator,
      Expression pExpression1, Expression pExpression2 )
  {
    super ( CANNOT_APPLY_1 + pOperator + CANNOT_APPLY_2 + pExpression1
        + CANNOT_APPLY_3 + pExpression2 );
    if ( pOperator == null )
    {
      throw new NullPointerException ( OPERATOR_NULL );
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL );
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL );
    }
    this.operator = pOperator;
    this.e1 = pExpression1;
    this.e2 = pExpression2;
  }


  /**
   * Returns the caption of this {@link Exception}.
   * 
   * @return The caption of this {@link Exception}.
   */
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns the first operand of the operator application that failed.
   * 
   * @return the first operand.
   */
  public Expression getE1 ()
  {
    return this.e1;
  }


  /**
   * Returns the second operand of the operator application that failed.
   * 
   * @return the second operand.
   */
  public Expression getE2 ()
  {
    return this.e2;
  }


  /**
   * Returns the {@link BinaryOperator} that failed to apply.
   * 
   * @return the binary operator that failed to apply.
   */
  public BinaryOperator getOperator ()
  {
    return this.operator;
  }
}
