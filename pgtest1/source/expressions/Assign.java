package expressions;

/**
 * The assign operator <code>:=</code>, which assigns a new value
 * to a memory location.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Assign extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Assign</code> operator.
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
    super(":=", 1);
  }
  
  
  
  //
  // Primitives
  //

  /**
   * The assign operator does not a machine equivalent like the arithmetic or
   * relational operators, but instead operates on a store. So applying an
   * assign operator to its operands must be implemented in the interpreter.
   * This method simply throws {@link UnsupportedOperationException} on every
   * invokation.
   * 
   * @param e1 the first operand.
   * @param e2 the second operand.
   * 
   * @return this method does not return normally.
   * 
   * @throws UnsupportedOperationException on every invokation.
   *
   * @see expressions.BinaryOperator#applyTo(expressions.Expression, expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    throw new UnsupportedOperationException("assign operator must be handled by the interpreter");
  }
}
