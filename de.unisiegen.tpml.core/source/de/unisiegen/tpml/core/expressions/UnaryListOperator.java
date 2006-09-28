package de.unisiegen.tpml.core.expressions;

/**
 * Abstract base class for the various list operators, that have to be handled by the interpreters
 * and the type checker.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.UnaryOperator
 */
public abstract class UnaryListOperator extends UnaryOperator {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>UnaryListOperator</code> with the string representation given in
   * <code>text</code>.
   * 
   * @param text the string representation of the list operator.
   * 
   * @see UnaryOperator#UnaryOperator(String)
   */
  protected UnaryListOperator(String text) {
    super(text);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * Throws {@link UnsupportedOperationException} on every invocation, because the list operators must
   * be handled by the interpreters in a special way.
   * 
   * @throws UnsupportedOperationException on every invocation.
   * 
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    throw new UnsupportedOperationException("list operators must be handled by the interpreter");
  }
}
