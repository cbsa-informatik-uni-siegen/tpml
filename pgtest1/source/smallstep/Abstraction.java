package smallstep;

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
    ruleChain.prepend(Rule.BETA_VALUE);
    
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
   * Returns the pretty print priority for the abstraction.
   * @return the pretty print priority for the abstraction.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return 0;
  }
  
  /**
   * Returns the string representation of the <b>(LAMBDA)</b> expression.
   * @return the string representation of the <b>(LAMBDA)</b> expression.
   * @see smallstep.Expression#getPrettyPrintString()
   */
  @Override
  public String getPrettyPrintString() {
    return "\u03bb" + this.id + "." + this.e.getPrettyPrintString();
  }

  private String id;
  private Expression e;
}
