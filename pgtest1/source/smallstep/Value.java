package smallstep;

public class Value extends Expression {
  /**
   * Performs the substitution for values.
   * Since most values are atomic, the substitution
   * is a noop by default and will just return the
   * value itself. There is one major exception to
   * this rule, the lambda abstraction (ABSTR),
   * which must perform the substitution on its
   * subexpression and therefore overrides this
   * default implementation.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return a reference to the value itself.
   */
  @Override
  public Expression substitute(String id, Value v) {
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
  @Override
  public final Expression evaluate(RuleChain ruleChain) {
    return this;
  }

  
  /**
   * Performs the application of <code>this</code> to <code>v</code>
   * and prepends the axiom that was used to <code>ruleChain</code>.
   * If <code>this</code> cannot be applied to <code>v</code>, then
   * <code>ruleChain</code> should be left empty and <code>null</code>
   * will be returned. Else the result of the application will be
   * returned (as Value).
   * 
   * The default implementation just returns an application and does
   * not prepend a rule to the <code>ruleChain</code>, which
   * means the evaluation will get stuck, so you will need to override
   * this method were in the appropriate places (e.g. the lambda
   * abstraction, operators, etc.).
   * 
   * @param v the value to which <code>this</code> should be applied.
   * @param ruleChain the chain to prepend the rules to.
   * @return the resulting value.
   */
  public Expression applyTo(Value v, RuleChain ruleChain) {
    assert (v instanceof Value);
    assert (ruleChain.isEmpty());
    
    return new Application(this, v);
  }
}
