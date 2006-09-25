package de.unisiegen.tpml.core.expressions;

/**
 * This exception is thrown whenever an {@link expressions.UnaryOperator} is
 * being applied to an invalid operand during evaluation. For example if the
 * {@link expressions.UnaryMinus} operator is applied to <code>true</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.UnaryOperator
 */
public final class UnaryOperatorException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serial version id.
   */
  private static final long serialVersionUID = 441799802323616930L;
  
  
  
  //
  // Attributes
  //
  
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
  
  
  
  //
  // Constructor
  //
  
  /**
   * Convenience wrapper for {@link #UnaryOperatorException(UnaryOperator, Expression, Throwable)}, which
   * simply passes <code>null</code> for the <code>cause</code>.
   * 
   * @param operator the {@link UnaryOperator} that failed to apply.
   * @param e the operand expression.
   * 
   * @throws NullPointerException if <code>operator</code> or <code>e</code> is <code>null</code>.
   */
  public UnaryOperatorException(UnaryOperator operator, Expression e) {
    this(operator, e, null);
  }

  /**
   * Allocates a new <code>UnaryOperatorException</code>, which signals that the specified unary 
   * <code>operator</code> could not be applied to the operand <code>e</code>.
   * 
   * @param operator the {@link UnaryOperator} that failed to apply.
   * @param e the operand expression.
   * @param cause the cause of the exception or <code>null</code>.
   * 
   * @throws NullPointerException if <code>operator</code> or <code>e</code> is <code>null</code>.
   */
  public UnaryOperatorException(UnaryOperator operator, Expression e, Throwable cause) {
    super("Cannot apply " + operator + " to " + e, cause);
    if (operator == null) {
      throw new NullPointerException("operator is null");
    }
    if (e == null) {
      throw new NullPointerException("e is null");
    }
    this.operator = operator;
    this.e = e;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the {@link UnaryOperator} that failed to apply.
   * 
   * @return the unary operator that failed to apply.
   */
  public UnaryOperator getOperator() {
    return this.operator;
  }
  
  /**
   * Returns the operand to whch the operator could not be applied.
   * 
   * @return the operand of the failed application.
   */
  public Expression getE() {
    return this.e;
  }
}
