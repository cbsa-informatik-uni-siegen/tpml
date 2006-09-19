package de.unisiegen.tpml.core.expressions;

/**
 * Abstract base class for binary operators. Binary operators, like unary operators, are always
 * constants, and as such, are always values.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public abstract class BinaryOperator extends Constant {
  //
  // Attributes
  //
  
  /**
   * The base pretty print priority for this binary operator, when used within
   * an infix operation.
   * 
   * @see #getPrettyPriority()
   * @see de.unisiegen.tpml.core.expressions.InfixOperation
   */
  private int prettyPriority;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>BinaryOperator</code> with the specified <code>prettyPriority</code> used
   * for pretty printing of {@link de.unisiegen.tpml.core.expression.InfixOperation}s.
   * 
   * @param text the string representation for this binary operator.
   * @param prettyPriority the pretty print priority for infix operations.
   * 
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  protected BinaryOperator(String text, int prettyPriority) {
    super(text);
    this.prettyPriority = prettyPriority;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the base pretty print priority for this binary operator, when used within an
   * {@link de.unisiegen.tpml.core.expressions.InfixOperation}.
   * 
   * @return the base pretty print priority for this binary operator.
   */
  public int getPrettyPriority() {
    return this.prettyPriority;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Applies the binary operator to the operands <code>e1</code> and <code>e2</code> and returns the
   * resulting expression. If the operator cannot be applied to <code>e1</code> and <code>e2</code>
   * because of a runtime type exception, a {@link BinaryOperatorException} is thrown.
   * 
   * @param e1 the first operand.
   * @param e2 the second operand.
   * 
   * @return the resulting expression.
   * 
   * @throws BinaryOperatorException if the operator cannot be applied to <code>e1</code> and <code>e2</code>.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public abstract Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException;
}
