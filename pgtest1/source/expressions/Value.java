package expressions;

public abstract class Value extends Expression {
  /**
   * Returns <code>true</code> as all classes derived
   * from {@link Value} are values.
   * 
   * @return always <code>true</code>.
   * 
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    return true;
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
   * @param e the application expression to which this applicable value belongs to.
   * @param ruleChain the chain to prepend the rules to.
   * @return the resulting value.
   */
  public Expression applyTo(Expression v, Application e, RuleChain ruleChain) {
    assert (v.isValue());
    assert (ruleChain.isEmpty());
    
    return e;
  }
}
