package smallstep;

/**
 * This class represents a runtime exception for the
 * small step interpreter.
 * 
 * @author bmeurer
 * @version $Id$
 */
public class Exn extends Expression {
  /**
   * Just returns the expression itself, since no
   * substitution is possible on exceptions.
   * 
   * @param id the identifier.
   * @param v the value.
   * @return the exception itself.
   */
  @Override
  public Expression substitute(String id, Value v) {
    // cannot substitute anything here
    return this;
  }

  /**
   * Just returns the expression itself, since no
   * evaluation is possible on exceptions. Nothing
   * is added to the <code>ruleChain</code>.
   * 
   * @param ruleChain the chain of rules.
   * @return the exception itself.
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    // cannot evaluate an exception
    return this;
  }

  /**
   * Returns the string representation of the exception.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn("divide_by_zero");
  
  private Exn(final String name) {
    this.name = name;
  }
  
  private String name;
}
