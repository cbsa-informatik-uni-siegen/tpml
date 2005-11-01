package smallstep;

public class Let extends Expression {
  /**
   * Generates a new let expression.
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Let(String id, Expression e1, Expression e2) {
    this.id = id;
    this.e1 = e1;
    this.e2 = e2;
  }

  public Expression substitute(String id, Expression e) {
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.id.equals(id) ? this.e2 : this.e2.substitute(id, e);
    return new Let(this.id, e1, e2);
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
   * @uml.property name="e1"
   */
  private Expression e1;

  /**
   * @uml.property name="e2"
   */
  private Expression e2;
}
