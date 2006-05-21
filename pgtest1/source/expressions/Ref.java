package expressions;


/**
 * The <code>ref</code> operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Ref extends UnaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Ref</code> class.
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
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return true;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * The <code>Ref</code> operator has no machine equivalent and
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
    throw new UnsupportedOperationException("ref operator must be handled by the interpreter");
  }
}
