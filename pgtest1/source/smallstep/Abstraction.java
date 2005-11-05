package smallstep;

public class Abstraction extends Value {
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
  @Override
  public Expression substitute(String id, Value v) {
    if (this.id.equals(id))
      return this;
    else
      return new Abstraction(this.id, this.e.substitute(id, v));
  }

  /**
   * Applies the lambda abstraction to the value <code>v</code>
   * and prepends the <b>(BETA-VALUE)</b> rule to the <code>ruleChain</code>.
   * Applying a lambda abstraction to a value will always succeed.
   *  
   * @param v the value to which the lambda abstraction should be applied.
   * @param ruleChain the chain of rules.
   * @return the applied abstraction.
   * 
   * @see smallstep.Value#applyTo(smallstep.Value, smallstep.RuleChain)
   */
  @Override
  public Expression applyTo(Value v, RuleChain ruleChain) {
    assert (v instanceof Value);
    assert (ruleChain.isEmpty());
    
    // prepend the (BETA-VALUE) rule
    ruleChain.prepend(Rule.BETA_VALUE);
    
    // perform the substitution
    return this.e.substitute(this.id, v);
  }

  /**
   * Returns the string representation of the <b>(LAMBDA)</b>
   * expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(lambda " + this.id + "." + this.e.toString() + ")";
  }

  private String id;
  private Expression e;
}
