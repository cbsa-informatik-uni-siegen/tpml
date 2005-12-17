package smallstep;

import java.util.Set;
import java.util.TreeSet;

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
  public AppliedOperator(Operator operator, Constant constant) {
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
   * @param e the application expression to which this applied operator belongs to.
   * @param ruleChain the chain of rules.
   * @return the result of the application.
   * 
   * @see smallstep.Value#applyTo(smallstep.Value, smallstep.RuleChain)
   */
  @Override
  public Expression applyTo(Value v, Application e, RuleChain ruleChain) {
    assert (v != null);
    assert (ruleChain.isEmpty());
    
    if (this.operator.canApplyTo(this.constant.getClass(), v.getClass())) {
      // we effectly use (OP) now
      ruleChain.prepend(new Rule(e, Rule.OP));
      
      // and perform the operation
      return this.operator.applyTo(this.constant, (Constant)v);
    }
    else {
      // fallback to the default application and get stuck
      return super.applyTo(v, e, ruleChain);
    }
  }

  /**
   * Returns the empty set.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return new TreeSet<String>();
  }

  /**
   * Returns the pretty string builder for applied operators.
   * @return the pretty string builder for applied operators.
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 5);
    builder.appendBuilder(this.operator.toPrettyStringBuilder(), 5);
    builder.appendText(" ");
    builder.appendBuilder(this.constant.toPrettyStringBuilder(), 6);
    return builder;
  }
  
  // the internal structure
  private Operator operator;
  private Constant constant;
}
