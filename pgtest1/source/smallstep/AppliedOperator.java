package smallstep;

/**
 * This class represents an applied operator, that is,
 * a binary operator which already knows its first
 * operand and can be applied to another operand to
 * produce the final result. 
 *
 * @author bmeurer
 * @version $Id$
 */
public class AppliedOperator extends Value {
  /**
   * Allocates a new applied operator, which combines
   * a binary operator with a single value. The result
   * can then be applied to another value.
   * 
   * @param operator the binary operator.
   * @param constant the first value, which must be an
   *                 integer constant in our simple
   *                 language.
   */
  public AppliedOperator(Operator operator, IntegerConstant constant) {
    assert (operator != null);
    assert (constant != null);
    
    this.operator = operator;
    this.constant = constant;
  }

  /**
   * Applies the applied operator to the value <code>v</code>
   * and adds the appropriate rule to the <code>ruleChain</code>.
   * 
   * <code>v</code> must be an integer constant, because we support
   * only binary integer operators in our small language.
   * 
   * @param v the value to which this applied operator should be applied.
   * @param ruleChain the chain of rules.
   * @return the result of the application.
   * 
   * @see smallstep.Value#applyTo(smallstep.Value, smallstep.RuleChain)
   */
  @Override
  public Expression applyTo(Value v, RuleChain ruleChain) {
    assert (v != null);
    assert (ruleChain.isEmpty());
    
    if (v instanceof IntegerConstant) {
      // we effectly use (OP) now
      ruleChain.prepend(Rule.OP);
      
      // and perform the operation
      return this.operator.applyTo(this.constant, (IntegerConstant)v);
    }
    else {
      // fallback to the default application and get stuck
      return super.applyTo(v, ruleChain);
    }
  }

  /**
   * Returns the string representation of the applied
   * operator.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    // use the Application.toString() logic
    Application application = new Application(this.operator, this.constant);
    return application.toString();
  }
  
  private Operator operator;
  private IntegerConstant constant;
}
