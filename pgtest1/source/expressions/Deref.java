package expressions;

/**
 * The deref operator, written as <code>!</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Deref extends UnaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Deref</code> class.
   */
  public static final Deref DEREF = new Deref();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Deref</code> instance.
   * 
   * @see #DEREF
   */
  private Deref() {
    super("!");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * The <code>Deref</code> operator has no machine equivalent and
   * must be handled by the interpreter, since it requires access
   * to the memory store.
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
    throw new UnsupportedOperationException("deref operator must be handled by the interpreter");
  }
}
