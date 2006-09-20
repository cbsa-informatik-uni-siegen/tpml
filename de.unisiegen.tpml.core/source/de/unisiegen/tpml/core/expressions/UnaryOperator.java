package de.unisiegen.tpml.core.expressions;

/**
 * Abstract base class for unary operators in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public abstract class UnaryOperator extends Constant {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>UnaryOperator</code> with the specified string representation <code>text</code>.
   * 
   * @param text the string representation of the operator.
   * 
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * 
   * @see de.unisiegen.tpml.core.expressions.Constant#Constant(String)
   */
  protected UnaryOperator(String text) {
    super(text);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Applies this unary operator to the specified expression <code>e</code>. If the operator cannot
   * be applied to the expression <code>e</code>, and {@link UnaryOperatorException} is thrown.
   * 
   * @param e the operand expression.
   * 
   * @return the resulting expression.
   * 
   * @throws NullPointerException if <code>e</code> is <code>null</code>.
   * @throws UnaryOperatorException if the unary operator cannot be applied to the expression <code>e</code>.
   */
  public abstract Expression applyTo(Expression e) throws UnaryOperatorException;
}
