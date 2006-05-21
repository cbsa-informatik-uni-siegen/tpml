package expressions;

/**
 * This exception is thrown whenever a {@link expressions.BinaryOperator} is
 * being applied to invalid operands during the evaluation. For example if
 * the arithmetic plus operator is applied to <code>true</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BinaryOperatorException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serial version id.  
   */
  private static final long serialVersionUID = -3520814619729333003L;
  
  
  
  //
  // Attributes
  //
  
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

  
  
  //
  // Constructor
  //

  /**
   * Allocates a new <code>BinaryOperatorException</code>, which
   * signals that the specified binary <code>operator</code> could
   * not be applied to the operands <code>e1</code> and <code>e2</code>.
   * 
   * @param operator the {@link BinaryOperator} that failed to apply.
   * @param e1 the first operand.
   * @param e2 the second operand.
   */
  public BinaryOperatorException(BinaryOperator operator, Expression e1, Expression e2) {
    super("Cannot apply " + operator + " to " + e1 + " and " + e2);
    this.operator = operator;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the {@link BinaryOperator} that failed to apply.
   * 
   * @return the binary operator that failed to apply.
   */
  public BinaryOperator getOperator() {
    return this.operator;
  }
  
  /**
   * Returns the first operand of the operator application
   * that failed.
   * 
   * @return the first operand.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second operand of the operator application
   * that failed.
   * 
   * @return the second operand.
   */
  public Expression getE2() {
    return this.e2;
  }
}
