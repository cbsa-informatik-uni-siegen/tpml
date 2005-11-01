package smallstep;

public class Application extends Expression {
  /**
   * Generates a new application.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * Performs the substitution on <b>(APP)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    return new Application(this.e1.substitute(id, v), this.e2.substitute(id, v));
  }

  public Expression evaluate(RuleChain ruleChain) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @uml.property name="e1"
   */
  private Expression e1;

  /**
   * @uml.property name="e2"
   */
  private Expression e2;
}
