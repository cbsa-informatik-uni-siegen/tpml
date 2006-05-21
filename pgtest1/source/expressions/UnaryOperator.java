package expressions;

/**
 * Abstract base class for unary operators.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class UnaryOperator extends Constant {
  //
  // Attributes
  //
  
  /**
   * The string representation of this unary operator.
   * 
   * @see #toString()
   */
  private String op;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Constructs an <code>UnaryOperator</code> with the
   * specified string representation <code>op</code>.
   * 
   * @param op the string representation.
   */
  protected UnaryOperator(String op) {
    this.op = op;
  }
  
  
  
  //
  // Primitives
  //

  /**
   * Applies this unary operator to the specified <code>e</code>. If
   * the operator cannot be applied to the expression <code>e</code>,
   * an {@link UnaryOperatorException} is thrown.
   * 
   * @param e the operand.
   * 
   * @return the resulting expression.
   * 
   * @throws UnaryOperatorException if the unary operator cannot be
   *                                applied to the expression <code>e</code>.
   */
  public abstract Expression applyTo(Expression e) throws UnaryOperatorException;
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant(this.op);
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns the string representation of this unary operator.
   * 
   * @return the string representation of this unary operator.
   *
   * @see expressions.Expression#toString()
   */
  @Override
  public final String toString() {
    return this.op;
  }
}
