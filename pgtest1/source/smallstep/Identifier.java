package smallstep;

public class Identifier extends Expression {
  /**
   * Generates a new Identifier.
   * @param id the name of the identifier.
   */
  public Identifier(String id) {
    this.id = id;
  }

  /**
   * Performs the substitution for <b>(ID)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    if (this.id.equals(id))
      return v;
    else
      return this;
  }

  public Expression evaluate(RuleChain ruleChain) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @uml.property name="id"
   */
  private String id;
}
