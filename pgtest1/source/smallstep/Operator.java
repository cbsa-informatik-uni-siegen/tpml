package smallstep;

public abstract class Operator extends Constant {
  /**
   * Checks whether this operator can be applied to two
   * constants of type <code>c1</code> and <code>c2</code>.
   * 
   * @param c1 class of the first constant.
   * @param c2 class of the second constant.
   * @return <code>true</code> if the operator can be applied
   *         to <code>c1</code> and <code>c2</code>.
   */
  public abstract boolean canApplyTo(Class c1, Class c2);
  
  /**
   * If <code>v</code> is an integer constant, a new applied operator
   * will be allocated and returned. Else, an application object will
   * be returned to indicate that the evaluation got stuck.
   * <code>ruleChain</code> will be left untouched in either case.
   *  
   * @param v the value to which the operator should be applied.
   * @param e the application expression to which this operator belongs to.
   * @param ruleChain the chain of rules.
   * @return either an applied operator or an application to indicate
   *         that the evaluation got stuck.
   *         
   * @see smallstep.Value#applyTo(smallstep.Value, smallstep.RuleChain)
   */
  @Override
  public final Expression applyTo(Value v, Application e, RuleChain ruleChain) {
    assert (v != null);
    assert (ruleChain.isEmpty());
    
    if (v instanceof IntegerConstant)
      return new AppliedOperator(this, (IntegerConstant)v);
    
    // fallback to default, which returns an application
    // to indicate that we got stuck.
    return super.applyTo(v, e, ruleChain);
  }

  /**
   * Applies the operator to the two values <code>c1</code>
   * and <code>c2</code> and returns the result, which
   * can be either a value or an exception.
   * 
   * @param c1 the first operand.
   * @param c2 the second operand.
   * @return the result or an expression.
   */
  public abstract Expression applyTo(Constant c1, Constant c2);
  
  /**
   * Returns the base pretty print priority for this operator.
   * 
   * @return the base pretty print priority for this operator.
   */
  public abstract int getPrettyPriority();
  
  /**
   * Returns the pretty string builder for operators.
   * @return the pretty string builder for operators.
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText("(" + toString() + ")");
    return builder;
  }
}
