package smallstep;

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
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    if (this.name.equals(id))
      return v;
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
   * Returns the name of the identifier.
   * @return the name of the identifier.
   */
  public final String getName() {
    return this.name;
  }
  
  /**
   * Returns the string representation of the identifier.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getName();
  }  
  
  private String name;
}
