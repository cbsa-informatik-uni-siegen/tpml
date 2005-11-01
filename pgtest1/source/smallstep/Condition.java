package smallstep;

public class Condition extends Expression {
  /**
   * Generates a new condition.
   * @param e0 the condition.
   * @param e1 the true case.
   * @param e2 the false case.
   */
  public Condition(Expression e0, Expression e1, Expression e2) {
    this.e0 = e0;
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * Performs the substitution for <b>(COND)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    Expression e0 = this.e0.substitute(id, v);
    Expression e1 = this.e1.substitute(id, v);
    Expression e2 = this.e2.substitute(id, v);
    return new Condition(e0, e1, e2);
  }

  public Expression evaluate(RuleChain ruleChain) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @uml.property name="e0"
   */
  private Expression e0;

  /**
   * @uml.property name="e1"
   */
  private Expression e1;

  /**
   * @uml.property name="e2"
   */
  private Expression e2;
}
