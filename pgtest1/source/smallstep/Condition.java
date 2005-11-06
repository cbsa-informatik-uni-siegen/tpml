package smallstep;

/**
 * Representation of a <b>(COND)</b> expression for
 * the small step interpreter.
 * 
 * @author bmeurer
 * @version $Id$
 */
public class Condition extends Expression {
  /**
   * Generates a new condition.
   * @param e0 the condition.
   * @param e1 the true case.
   * @param e2 the false case.
   */
  public Condition(Expression e0, Expression e1, Expression e2) {
    this.e0 = e0;
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * Performs the substitution for <b>(COND)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    Expression e0 = this.e0.substitute(id, v);
    Expression e1 = this.e1.substitute(id, v);
    Expression e2 = this.e2.substitute(id, v);
    return new Condition(e0, e1, e2);
  }

  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    assert (this.e0 instanceof Expression);
    assert (this.e1 instanceof Expression);
    assert (this.e2 instanceof Expression);
    
    // evalue e0 (may already be a value)
    Expression e0 = this.e0.evaluate(ruleChain);
    
    // check if any rules were applied in the evaluation
    // of e0 (if not, then e0 was already a value), e0
    // may also be an exception to forward
    if (!ruleChain.isEmpty()) {
      if (e0 instanceof Exn) {
        // prepend (COND-EVAL-EXN)
        ruleChain.prepend(Rule.COND_EVAL_EXN);
        return e0;
      }
      else {
        // prepend (COND-EVAL)
        ruleChain.prepend(Rule.COND_EVAL);
        return new Condition(e0, this.e1, this.e2);
      }
    }
    
    // if e0 is not a boolean constant, then
    // the evaluation got stuck and there are
    // no more small steps to perform
    if (!(e0 instanceof BooleanConstant))
      return new Condition(e0, this.e1, this.e2);
    
    // if we get here, e0 must be a boolean
    // constant and the rule chain is empty
    assert (e0 instanceof BooleanConstant);
    assert (ruleChain.isEmpty());
    
    // cast to boolean constant
    BooleanConstant v0 = (BooleanConstant)e0;
    
    // check if the conditional value is true
    if (v0.isTrue()) {
      // prepend (COND-TRUE)
      ruleChain.prepend(Rule.COND_TRUE);
      return this.e1;
    }
    else {
      // prepend (COND-FALSE)
      ruleChain.prepend(Rule.COND_FALSE);
      return this.e2;
    }
  }

  /**
   * Returns the string representation of the <b>(COND)</b>
   * expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String s0 = this.e0.toString();
    String s1 = this.e1.toString();
    String s2 = this.e2.toString();
    
    if (!(this.e0 instanceof Application) && !(this.e0 instanceof Value))
      s0 = "(" + s0 + ")";
    
    if (!(this.e1 instanceof Application) && !(this.e1 instanceof Value))
      s1 = "(" + s1 + ")";
    
    if (!(this.e2 instanceof Application) && !(this.e2 instanceof Value))
      s2 = "(" + s2 + ")";
    
    return "if " + s0 + " then " + s1 + " else " + s2;
  }

  private Expression e0;
  private Expression e1;
  private Expression e2;
}
