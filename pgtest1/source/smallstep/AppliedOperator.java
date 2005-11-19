package smallstep;

import java.text.MessageFormat;
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
   * Returns the empty set.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return new TreeSet<String>();
  }

  /**
   * Returns the pretty print format for an applied operator.
   * @return the pretty print format for an applied operator.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return PRETTY_PRINT_FORMAT;
  }

  /**
   * Returns the pretty print priority for this expression.
   * @return the pretty print priority for this expression.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns the subexpression priorities for an applied operator,
   * which is the same as for applications, [1,2].
   * @return the subexpression priorities for an applied operator.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns the subexpressions for an applied operator.
   * @return the subexpressions for an applied operator.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return new Expression[] { this.operator, this.constant };
  }
  
  // the internal structure
  private Operator operator;
  private IntegerConstant constant;
  
  // pretty print support
  private static final MessageFormat PRETTY_PRINT_FORMAT = new MessageFormat("{0} {1}");
  private static final int PRETTY_PRINT_PRIORITIES[] = { 1, 2 };
  private static final int PRETTY_PRINT_PRIORITY = 1;
}
