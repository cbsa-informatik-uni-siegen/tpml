package expressions;

/**
 * This exception is thrown whenever an {@link expressions.UnaryOperator} is
 * being applied to an invalid operand during evaluation. For example if the
 * {@link expressions.UnaryMinus} operator is applied to <code>true</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnaryOperatorException extends Exception {
  //
  // Constants
  // 

  /**
   * The unique serial version id.
   */
  private static final long serialVersionUID = 1694345280258531879L;
  
  
  
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
   * Allocates a new <code>UnaryOperatorException</code> which signals that
   * the application of <code>operator</code> to the operand <code>e</code>
   * failed.
   * 
   * @param operator the {@link UnaryOperator} that failed.
   * @param e the operand.
   */
  public UnaryOperatorException(UnaryOperator operator, Expression e) {
    super("Cannot apply " + operator + " " + e);
    this.operator = operator;
    this.e = e;
  }
  
  
  
  //
  // Primitives
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
   * Returns the operand to which the operator could not be applied.
   * 
   * @return the operand of the failed application.
   */
  public Expression getE() {
    return this.e;
  }
}
