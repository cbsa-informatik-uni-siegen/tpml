package de.unisiegen.tpml.core.expressions;


/**
 * This exception is thrown whenever an {@link UnaryOperator} is being applied
 * to an invalid operand during evaluation. For example if the Minus operator is
 * applied to <code>true</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryOperator
 */
public final class UnaryOperatorException extends Exception
{

  /**
   * The unique serial version id.
   */
  private static final long serialVersionUID = 441799802323616930L;


  /**
   * String for the case that the operator is null.
   */
  private static final String OPERATOR_NULL = "operator is null"; //$NON-NLS-1$


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null"; //$NON-NLS-1$


  /**
   * Cannot apply exception string.
   */
  private static final String CANNOT_APPLY_1 = "cannot apply "; //$NON-NLS-1$


  /**
   * Cannot apply exception string.
   */
  private static final String CANNOT_APPLY_2 = " to "; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( UnaryOperatorException.class );


  /**
   * The unary operator that failed to apply.
   * 
   * @see #getOperator()
   */
  private UnaryOperator operator;


  /**
   * The operand to which the unary operator could not be applied.
   * 
   * @see #getE()
   */
  private Expression e;


  /**
   * Convenience wrapper for
   * {@link #UnaryOperatorException(UnaryOperator, Expression, Throwable)},
   * which simply passes <code>null</code> for the <code>cause</code>.
   * 
   * @param pUnaryOperator the {@link UnaryOperator} that failed to apply.
   * @param pExpression the operand expression.
   * @throws NullPointerException if <code>operator</code> or <code>e</code>
   *           is <code>null</code>.
   */
  public UnaryOperatorException ( UnaryOperator pUnaryOperator,
      Expression pExpression )
  {
    this ( pUnaryOperator, pExpression, null );
  }


  /**
   * Allocates a new <code>UnaryOperatorException</code>, which signals that
   * the specified unary <code>operator</code> could not be applied to the
   * operand <code>e</code>.
   * 
   * @param pUnaryOperator the {@link UnaryOperator} that failed to apply.
   * @param pExpression the operand expression.
   * @param cause the cause of the exception or <code>null</code>.
   * @throws NullPointerException if <code>operator</code> or <code>e</code>
   *           is <code>null</code>.
   */
  public UnaryOperatorException ( UnaryOperator pUnaryOperator,
      Expression pExpression, Throwable cause )
  {
    super ( CANNOT_APPLY_1 + pUnaryOperator + CANNOT_APPLY_2 + pExpression,
        cause );
    if ( pUnaryOperator == null )
    {
      throw new NullPointerException ( OPERATOR_NULL );
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL );
    }
    this.operator = pUnaryOperator;
    this.e = pExpression;
  }


  /**
   * Returns the caption of this {@link Expression}.
   * 
   * @return The caption of this {@link Expression}.
   */
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns the operand to whch the operator could not be applied.
   * 
   * @return the operand of the failed application.
   */
  public Expression getE ()
  {
    return this.e;
  }


  /**
   * Returns the {@link UnaryOperator} that failed to apply.
   * 
   * @return the unary operator that failed to apply.
   */
  public UnaryOperator getOperator ()
  {
    return this.operator;
  }
}
