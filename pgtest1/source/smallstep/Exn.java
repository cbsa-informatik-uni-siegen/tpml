package smallstep;

import java.util.Set;
import java.util.TreeSet;

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
   * @param e the expression to substitute.
   * @return the exception itself.
   */
  @Override
  public Expression substitute(String id, Expression e) {
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
   * Returns the empty set, since exceptions
   * cannot contain any free identifiers.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return new TreeSet<String>();
  }

  /**
   * Returns the pretty print priority of the exception.
   * @return the pretty print priority of the exception.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return 2;
  }
  
  /**
   * Returns the string representation of the exception.
   * @return the string representation of the exception.
   * @see smallstep.Expression#getPrettyPrintString()
   */
  @Override
  public String getPrettyPrintString() {
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
