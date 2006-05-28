package expressions;

/**
 * This class represents a runtime exception for the
 * small step interpreter.
 * 
 * @author bmeurer
 * @version $Id$
 */
public final class Exn extends Expression {
  //
  // Constants
  //
  
  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn("divide_by_zero");
  
  
  
  //
  // Attributes
  //

  /**
   * The name of the exception.
   * 
   * @see #toString()
   */
  private String name;
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Exn</code> instance with
   * the specified <code>name</code>.
   * 
   * @param name the name of the exception.
   */
  private Exn(String name) {
    this.name = name;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code>, since an <code>Exn</code>
   * instance represents an exception in the program.
   * 
   * @return always <code>true</code>.
   *
   * @see expressions.Expression#isException()
   */
  @Override
  public boolean isException() {
    return true;
  }
  
  /**
   * Returns the pretty string builder for exceptions.
   * 
   * @return the pretty string builder for exceptions.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 2);
    builder.appendText(this.name);
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //

  /**
   * Returns the string representation of the
   * exception, which is simply the name of
   * the exception.
   * 
   * @return the string representation of the
   *         exception.
   *
   * @see expressions.Expression#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }
}
