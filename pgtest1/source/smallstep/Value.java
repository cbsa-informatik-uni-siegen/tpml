package smallstep;

public abstract class Value extends Expression {
  /**
   * Performs the substitution for values.
   * Since values are atomic, the substitution
   * is a noop and will just return the value itself.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return a reference to the value itself.
   */
  public final Expression substitute(String id, Value v) {
    return this;
  }

  /**
   * Performs the evaluation for values.
   * Since values are atomic (there are already
   * values), there's nothing to evaluate and the
   * method will just return the value itself.
   * 
   * @param ruleChain the rule chain.
   * @return a reference to the value itself.
   */
  public final Expression evaluate(RuleChain ruleChain) {
    return this;
  }
  
  /**
   * Returns <code>true</code> since a value is definitely
   * a value.
   * 
   * @return always <code>true</code>.
   */
  public final boolean isValue() {
    return true;
  }
}
