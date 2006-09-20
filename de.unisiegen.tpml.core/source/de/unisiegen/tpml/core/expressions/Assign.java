package de.unisiegen.tpml.core.expressions;

/**
 * The {@link #ASSIGN} constant in this class represents the <code>:=</code> operator in the expression
 * hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Deref
 * @see de.unisiegen.tpml.core.expressions.Ref
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 */
public final class Assign extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Assign</code> operator.
   * 
   * @see #Assign()
   */
  public static final Assign ASSIGN = new Assign();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Assign</code> operator.
   * 
   * @see #ASSIGN
   */
  private Assign() {
    super(":=", PRIO_ASSIGN);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * The assign operator does not a machine equivalent like the arithmetic or
   * relational operators, but instead operates on a store. So applying an
   * assign operator to its operands must be implemented in the interpreter.
   * This method simply throws {@link UnsupportedOperationException} on every
   * invokation.
   * 
   * @throws UnsupportedOperationException on every invokation.
   *
   * @see de.unisiegen.tpml.core.expressions.BinaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    throw new UnsupportedOperationException("assign operator must be handled by the interpreter");
  }
}
