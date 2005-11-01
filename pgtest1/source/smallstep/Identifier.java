package smallstep;

public class Identifier extends Expression {
  /**
   * Generates a new Identifier.
   * @param id the name of the identifier.
   */
  public Identifier(String id) {
    this.id = id;
  }

  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id))
      return e;
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
