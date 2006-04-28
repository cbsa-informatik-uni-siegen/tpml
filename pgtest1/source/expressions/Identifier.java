package expressions;

import java.util.Set;
import java.util.TreeSet;

public class Identifier extends Expression {
  /**
   * Generates a new Identifier.
   * @param name the name of the identifier.
   */
  public Identifier(String name) {
    this.name = name;
  }

  /**
   * Performs the substitution for <b>(ID)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    if (this.name.equals(id))
      return e;
    else
      return this;
  }

  /**
   * Just returns the identifier expression.
   *
   * @param ruleChain the chain of rules.
   * @return this identifier expression.
   */
  public Expression evaluate(RuleChain ruleChain) {
    return this;
  }
  
  /**
   * Returns the set that contains this identifier.
   * @return the set that contains this identifier.
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.add(this.name);
    return set;
  }
  
  /**
   * Returns the name of the identifier.
   * @return the name of the identifier.
   */
  public final String getName() {
    return this.name;
  }

  /**
   * Returns the pretty string builder for identifiers.
   * @return the pretty string builder for identifiers.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText(this.name);
    return builder;
  }

  // the name of the identifier
  private String name;
}
