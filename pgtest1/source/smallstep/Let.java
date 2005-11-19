package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

public class Let extends Expression {
  /**
   * Generates a new let expression.
   * @param id the name of the identifier.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Let(String id, Expression e1, Expression e2) {
    this.id = id;
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * Performs the substitution for <b>(LET)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.id.equals(id) ? this.e2 : this.e2.substitute(id, e);
    return new Let(this.id, e1, e2);
  }

  /**
   * Evaluates the <b>(LET)</b> expression.
   * 
   * @param ruleChain the chain of rules.
   * @return the resulting expression.
   */
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    assert (this.e1 instanceof Expression);
    assert (this.e2 instanceof Expression);
    
    // evaluate e1 (may already be a value)
    Expression e1 = this.e1.evaluate(ruleChain);
    
    // check if any rules were applied in the evaluation
    // of e1 (if not, then e1 was already a value), e1
    // may also be an exception to forward
    if (!ruleChain.isEmpty()) {
      if (e1 instanceof Exn) {
        // prepend (LET-EVAL-EXN)
        ruleChain.prepend(new Rule(this, Rule.LET_EVAL_EXN));
        return e1;
      }
      else {
        // prepend (LET-EVAL)
        ruleChain.prepend(new Rule(this, Rule.LET_EVAL));
        return new Let(this.id, e1, this.e2);
      }
    }
    
    // if e1 is still not a value, then the
    // evaluation got stuck and there are no
    // more small steps to perform
    if (!(e1 instanceof Value))
      return new Let(this.id, e1, this.e2);
    
    // if we get here, e1 must be a value
    // and the rule chain is empty
    assert (e1 instanceof Value);
    assert (ruleChain.isEmpty());

    // cast e1 to a value
    Value v1 = (Value)e1;
    
    // we're going to perform (LET-EXEC)
    ruleChain.prepend(new Rule(this, Rule.LET_EXEC));
    
    // ...and there we are
    return this.e2.substitute(this.id, v1);
  }
  
  /**
   * Returns the free identifiers of
   * the subexpressions.
   * @return the free identifiers.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e2.free());
    set.remove(this.id);
    set.addAll(this.e1.free());
    return set;
  }

  /**
   * Returns <code>"let id={0} in {1}"</code>, which is used
   * to pretty print the let expressions.
   * @return the pretty print format for <b>(LET)</b>.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat("let " + this.id + " = {0} in {1}");
  }

  /**
   * Returns the pretty print priority of the <b>(LET)</b> expression.
   * @return the pretty print priority of the <b>(LET)</b> expression.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns the required subexpression pretty print priorities, which is [0,0]
   * for the <b>(LET)</b> expression.
   * @return the required subexpression pretty print priorities.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns the array of subexpressions for the <b>(LET)</b> expression.
   * @return the array of subexpressions for the <b>(LET)</b> expression.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return new Expression[] { this.e1, this.e2 };
  }

  private String id;
  private Expression e1;
  private Expression e2;
  
  // pretty print support
  private static final int PRETTY_PRINT_PRIORITIES[] = { 0, 0 };
  private static final int PRETTY_PRINT_PRIORITY = 0;
}
