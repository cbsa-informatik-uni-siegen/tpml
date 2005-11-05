package smallstep;

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
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    Expression e1 = this.e1.substitute(id, v);
    Expression e2 = this.id.equals(id) ? this.e2 : this.e2.substitute(id, v);
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
        ruleChain.prepend(Rule.LET_EVAL_EXN);
        return e1;
      }
      else {
        // prepend (LET-EVAL)
        ruleChain.prepend(Rule.LET_EVAL);
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
    ruleChain.prepend(Rule.LET_EXEC);
    
    // ...and there we are
    return this.e2.substitute(this.id, v1);
  }

  /**
   * Returns the string representation of the <b>(LET)</b>
   * expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "let " + this.id + " = " + this.e1 + " in " + this.e2;
  }

  private String id;
  private Expression e1;
  private Expression e2;
}
