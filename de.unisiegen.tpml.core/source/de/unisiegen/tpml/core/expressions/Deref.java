package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent the <code>!</code> operator in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Assign
 * @see de.unisiegen.tpml.core.expressions.Ref
 * @see de.unisiegen.tpml.core.expressions.UnaryOperator
 */
public final class Deref extends UnaryOperator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Deref</code> instance.
   */
  public Deref() {
    super("!");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * This method always throws {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>!</code> operator requires special handling in the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the <code>!</code> operator
   *                                       must be handled by the interpreter.
   *                                       
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    throw new UnsupportedOperationException("deref operator must be handled by the interpreter");
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public Deref clone() {
    return new Deref();
  }
}
