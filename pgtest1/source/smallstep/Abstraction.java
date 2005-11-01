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

  /**
   * Performs the substitution for <b>(LAMBDA)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    if (this.id.equals(id))
      return this;
    else
      return new Abstraction(this.id, this.e.substitute(id, v));
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
