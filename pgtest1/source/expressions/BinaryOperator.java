package expressions;

/**
 * Abstract base class for binary operators.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class BinaryOperator extends Constant {
  //
  // Attributes
  //
  
  /**
   * The string representation for this binary operator.
   * 
   * @see #toString()
   */
  private String op;
  
  /**
   * The base pretty print priority for this binary operator.
   * 
   * @see #getPrettyPriority()
   */
  private int prettyPriority;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Constructor for <code>BinaryOperator</code>s with the
   * specified <code>prettyPriority</code> used for pretty
   * printing of {@link InfixOperation}s.
   * 
   * @param op the string representation for this binary
   *           operator.
   * @param prettyPriority the pretty print priority for
   *                       {@link InfixOperation}s.
   */
  protected BinaryOperator(String op, int prettyPriority) {
    this.op = op;
    this.prettyPriority = prettyPriority;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Applies the binary operator to the operands <code>e1</code> and
   * <code>e2</code> and returns the resulting expression. If the
   * operator cannot be applied to <code>e1</code> or <code>e2</code>
   * because of a runtime type exception, a {@link BinaryOperatorException}
   * is thrown.
   * 
   * @param e1 the first operand.
   * @param e2 the second operand.
   * 
   * @return the resulting expression.
   * 
   * @throws BinaryOperatorException if the operator cannot be applied to
   *                                 <code>e1</code> or <code>e2</code>.
   */
  public abstract Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException;
  
  /**
   * Returns the base pretty print priority for this binary operator.
   * 
   * @return the base pretty print priority for this binary operator.
   */
  public final int getPrettyPriority() {
    return this.prettyPriority;
  }
  
  /**
   * Returns the pretty string builder for binary operators.
   * 
   * @return the pretty string builder for binary operators.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText("(");
    builder.appendConstant(this.op);
    builder.appendText(")");
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns the string representation for binary operators.
   * 
   * @return the string representation for binary operators.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return this.op;
  }
}
