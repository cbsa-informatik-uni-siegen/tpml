package smallstep;

import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

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
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    return new Application(this.e1.substitute(id, e), this.e2.substitute(id, e));
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
        ruleChain.prepend(new Rule(this, Rule.APP_LEFT_EXN));
        return e1;
      }
      else {
        // prepend (APP-LEFT)
        ruleChain.prepend(new Rule(this, Rule.APP_LEFT));
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
        ruleChain.prepend(new Rule(this, Rule.APP_RIGHT_EXN));
        return e2;
      }
      else {
        // prepend (APP-RIGHT)
        ruleChain.prepend(new Rule(this, Rule.APP_RIGHT));
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
   * Returns the free identifiers of the two expressions
   * of the application.
   * @return the free identifiers.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e1.free());
    set.addAll(this.e2.free());
    return set;
  }

  /**
   * Returns a format for <code>"{0} {1}"</code> which can be used
   * to pretty print an application.
   * @return the pretty print message format for applications.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return PRETTY_PRINT_FORMAT;
  }

  /**
   * Returns the return-pretty-print-priority for applications.
   * @return the return-pretty-print-priority for applications.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }

  /**
   * Returns the required subexpression pretty print priorities.
   * @return the required subexpression pretty print priorities.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }

  /**
   * Returns the two subexpression for the application.
   * @return the two subexpression for the application.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return new Expression[] { this.e1, this.e2 };
  }
  
  // sub expressions
  private Expression e1;
  private Expression e2;
  
  // pretty print support for applications
  private static MessageFormat PRETTY_PRINT_FORMAT = new MessageFormat("{0} {1}");
  private static int PRETTY_PRINT_PRIORITIES[] = { 1, 2 };
  private static int PRETTY_PRINT_PRIORITY = 1;
}
