package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

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
   * @param e the expression to substitute.
   * @return the new expression.
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> free = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId))
        newId = newId + "'";

      // perform the bound renaming
      Expression newE = this.e.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new Abstraction(newId, newE.substitute(id, e));
    }
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
    ruleChain.prepend(new Rule(this, Rule.BETA_VALUE));
    
    // perform the substitution
    return this.e.substitute(this.id, v);
  }
  
  /**
   * Returns the free identifiers minus the bound identifier.
   * @return the free identifiers minus the bound identifier.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e.free());
    set.remove(this.id);
    return set;
  }

  /**
   * Returns the pretty print format for the lambda abstraction,
   * which is <code>"lambda id.{0}"</code>.
   * @return the pretty print format for the lambda abstraction.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat("\u03bb" + this.id + ".{0}");
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
   * Returns the subexpression pretty print priorities.
   * @return the subexpression pretty print priorities.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns the subexpressions for the abstraction.
   * @return the subexpressions for the abstraction.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return new Expression[] { this.e };
  }

  // the internal structure
  private String id;
  private Expression e;
  
  // pretty print support
  private static final int PRETTY_PRINT_PRIORITIES[] = { 0 };
  private static final int PRETTY_PRINT_PRIORITY = 0;
}
