package expressions;

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
   * {@inheritDoc}
   * @see expressions.Expression#normalize()
   */
  @Override
  public Expression normalize() {
    // normalize the sub expression
    Expression e1 = this.e1.normalize();
    Expression e2 = this.e2.normalize();
    
    // check if we need to generate new let
    if (e1 != this.e1 || e2 != this.e2) {
      return new Let(this.id, e1, e2);
    }
    else {
      return this;
    }
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
    if (!e1.isValue())
      return new Let(this.id, e1, this.e2);
    
    // if we get here, e1 must be a value
    // and the rule chain is empty
    assert (e1.isValue());
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
   * @see expressions.Expression#free()
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
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return (this.e1.containsReferences() || this.e2.containsReferences());
  }
  
  /**
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * @return Returns the e1.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * @return Returns the e2.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  /**
   * Returns the pretty string builder for let expressions.
   * @return the pretty string builder for let expressions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("let");
    builder.appendText(" " + this.id + " = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("in");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
  
  private String id;
  private Expression e1;
  private Expression e2;
}
