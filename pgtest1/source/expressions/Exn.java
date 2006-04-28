package expressions;

import java.util.Set;

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
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    return Expression.EMPTY_SET;
  }
  
  /**
   * Returns the pretty string builder for exceptions.
   * @return the pretty string builder for exceptions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 2);
    builder.appendText(this.name);
    return builder;
  }

  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn("divide_by_zero");
  
  private Exn(final String name) {
    this.name = name;
  }
  
  // the name of the exception
  private String name;
}
