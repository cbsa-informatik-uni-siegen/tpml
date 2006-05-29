package expressions;

/**
 * Abstract base class for the various list operators, that
 * have to be handled by the interpreters and the type
 * checker.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class UnaryListOperator extends UnaryOperator {
  //
  // Constructor (protected)
  //
  
  /**
   * Constructs a new <code>UnaryListOperator</code> with the 
   * string representation given in <code>text</code>.
   * 
   * @param text the string representation of the list operator.
   */
  protected UnaryListOperator(String text) {
    super(text);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * The list operators have no machine equivalent and must
   * be handled by the interpreter.
   *
   * This method always throws {@link UnsupportedOperationException}
   * to indicate that it should not be called.
   *
   * @param e the operand.
   * 
   * @return does not return normally.
   * 
   * @throws UnsupportedOperationException on every invokation.
   * 
   * @see expressions.UnaryOperator#applyTo(expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    throw new UnsupportedOperationException("list operators must be handled by the interpreter");
  }
}
