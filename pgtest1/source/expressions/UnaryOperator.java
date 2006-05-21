package expressions;

/**
 * Abstract base class for unary operators.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class UnaryOperator extends Constant {
  //
  // Constructor (protected)
  //
  
  /**
   * Constructs an <code>UnaryOperator</code> with the
   * specified string representation <code>text</code>.
   * 
   * @param text the string representation.
   */
  protected UnaryOperator(String text) {
    super(text);
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
}
