package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent the <code>ref</code> operator in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Assign
 * @see de.unisiegen.tpml.core.expressions.Deref
 * @see de.unisiegen.tpml.core.expressions.UnaryOperator
 */
public final class Ref extends UnaryOperator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Ref</code> instance.
   */
  public Ref() {
    super("ref");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * This method always throws {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>ref</code> operator requires special handling in the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the <code>ref</code> operator
   *                                       must be handled by the interpreter.
   *                                       
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    throw new UnsupportedOperationException("ref operator must be handled by the interpreter");
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public Ref clone() {
    return new Ref();
  }
}
