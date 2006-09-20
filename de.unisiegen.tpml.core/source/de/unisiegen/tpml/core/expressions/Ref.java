package de.unisiegen.tpml.core.expressions;

/**
 * The {@link #REF} constant in this class represents the <code>ref</code> operator in the expression
 * hierarchy.
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
  // Constants
  //
  
  /**
   * The single instance of the <code>Ref</code> class.
   * 
   * @see #Ref()
   */
  public static final Ref REF = new Ref();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Ref</code> instance.
   * 
   * @see #REF
   */
  private Ref() {
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
}
