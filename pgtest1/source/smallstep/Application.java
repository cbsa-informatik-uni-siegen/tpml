package smallstep;

public class Application extends Expression {
  /**
   * Generates a new application.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }

  /**
   * Performs the substitution on <b>(APP)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param v the value to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Value v) {
    return new Application(this.e1.substitute(id, v), this.e2.substitute(id, v));
  }

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
        // prepend (APP-LEFT-EXN)
        ruleChain.prepend(Rule.APP_LEFT_EXN);
        return e1;
      }
      else {
        // prepend (APP-LEFT)
        ruleChain.prepend(Rule.APP_LEFT);
        return new Application(e1, this.e2);
      }
    }
    
    // if e1 is still not a value, then the
    // evaluation got stuck and there are no
    // more small steps to perform
    if (!(e1 instanceof Value))
      return new Application(e1, this.e2);

    // if we get here, e1 must be a value
    // and the rule chain is empty
    assert (e1 instanceof Value);
    assert (ruleChain.isEmpty());
    
    // evalute e2 (may already be a value)
    Expression e2 = this.e2.evaluate(ruleChain);
    
    // check if any rules were applied in the evaluation
    // of e2 (if not, then e2 was already a value), e2
    // may also be an exception to forward
    if (!ruleChain.isEmpty()) {
      if (e2 instanceof Exn) {
        // prepend (APP-RIGHT-EXN)
        ruleChain.prepend(Rule.APP_RIGHT_EXN);
        return e2;
      }
      else {
        // prepend (APP-RIGHT)
        ruleChain.prepend(Rule.APP_RIGHT);
        return new Application(e1, e2);
      }
    }
    
    // if e2 is still not a value, then the
    // evaluation got stuck and there are no
    // more small steps to perform
    if (!(e2 instanceof Value))
      return new Application(e1, e2);
    
    // if we get here, e1 and e2 must be
    // values and the rule chain is empty
    assert (e1 instanceof Value);
    assert (e2 instanceof Value);
    assert (ruleChain.isEmpty());
    
    // cast the expressions to values
    Value v1 = (Value)e1;
    Value v2 = (Value)e2;
    
    // perform the application
    return v1.applyTo(v2, ruleChain);
  }

  /**
   * Returns the string representation of the <b>(APP)</b>
   * expression.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String s1 = this.e1.toString();
    String s2 = this.e2.toString();

    // check for cascaded applications
    if (this.e1 instanceof Application) {
      Application a1 = (Application)this.e1;
      if (!(a1.e1 instanceof Operator) || !(a1.e2 instanceof Constant || a1.e2 instanceof Identifier))
        s1 = "(" + s1 + ")";
    }
    else if (!(this.e1 instanceof Constant) && !(this.e1 instanceof Identifier)) {
      // need to add parens to everything but constants and identifiers
      s1 = "(" + s1 + ")";
    }
    
    // same for the second expression
    if (!(this.e2 instanceof Constant) && !(this.e2 instanceof Identifier))
      s2 = "(" + s2 + ")";
    
    return s1 + " " + s2;
  }
  
  private Expression e1;
  private Expression e2;
}
