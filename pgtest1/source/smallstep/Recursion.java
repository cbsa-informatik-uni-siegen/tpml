/**
 * 
 */
package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of the <b>(REC)</b> rule.
 *
 * @author bmeurer
 * @version $Id$
 */
public class Recursion extends Expression {
  /**
   * Allocates a new <b>(REC)</b> expression.
   * @param id the identifier of the recursive expression.
   * @param e the expression.
   */
  public Recursion(String id, Expression e) {
    this.id = id;
    this.e = e;
  }
  
  /**
   * Performs the substitution for recursive expressions.
   * 
   * @param id the identifier.
   * @param e the expression to substitute.
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#substitute(java.lang.String, smallstep.Expression)
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
      return new Recursion(newId, newE.substitute(id, e));
    }
  }

  /**
   * Evaluates a <b>(REC)</b> expression using the <b>(UNFOLD)</b>
   * rule.
   * 
   * @param ruleChain the rule chain.
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#evaluate(smallstep.RuleChain)
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    
    // prepend the (UNFOLD) axiom
    ruleChain.prepend(new Rule(this, Rule.UNFOLD));
    
    // perform the substitution
    return this.e.substitute(this.id, this);
  }

  /**
   * Returns the free identifiers of the
   * subexpression minus the identifier of
   * the <b>(REC)</b> expression.
   *
   * @return the free identifiers.
   * 
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
   * Returns the pretty print format for the <b>(REC)</b> expression,
   * which is currently just <code>"rec id.{0}"</code>.
   * @return the pretty print format for the <b>(REC)</b> expression.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat("rec " + this.id + ".{0}");
  }

  /**
   * Returns the pretty print priority for the <b>(REC)</b> expression.
   * @return the pretty print priority for the <b>(REC)</b> expression.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns the subexpression priorities for the pretty print support,
   * which is right now just [0].
   * @return the subexpression priorities for the <b>(REC)</b> expression.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns the list of subexpressions for this expression.
   * @return the list of subexpressions for this expression.
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
