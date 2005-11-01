package smallstep;

public class Abstraction extends Expression {
  /**
   * Generates a new abstraction.
   * @param id the name of the parameter.
   * @param e the expression.
   */
  public Abstraction(String id, Expression e) {
    this.id = id;
    this.e = e;
  }

  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id))
      return this;
    else
      return new Abstraction(this.id, this.e.substitute(id, e));
  }

  public Expression evaluate(RuleChain ruleChain) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @uml.property name="id"
   */
  private String id;

  /**
   * @uml.property name="e"
   */
  private Expression e;
}
