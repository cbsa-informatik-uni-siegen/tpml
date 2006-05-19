package expressions;

import java.util.Set;
import java.util.TreeSet;

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
   * {@inheritDoc}
   * @see expressions.Expression#normalize()
   */
  @Override
  public Expression normalize() {
    // normalize the sub expressions
    Expression e0 = this.e0.normalize();
    Expression e1 = this.e1.normalize();
    Expression e2 = this.e2.normalize();
    
    // check if we need to generate a new condition
    if (e0 != this.e0 || e1 != this.e1 || e2 != this.e2) {
      return new Condition(e0, e1, e2);
    }
    else {
      return this;
    }
  }
  
  /**
   * Performs the substitution for <b>(COND)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * @return the new expression.
   */
  public Expression substitute(String id, Expression e) {
    Expression e0 = this.e0.substitute(id, e);
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.e2.substitute(id, e);
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
        ruleChain.prepend(new Rule(this, Rule.COND_EVAL_EXN));
        return e0;
      }
      else {
        // prepend (COND-EVAL)
        ruleChain.prepend(new Rule(this, Rule.COND_EVAL));
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
      ruleChain.prepend(new Rule(this, Rule.COND_TRUE));
      return this.e1;
    }
    else {
      // prepend (COND-FALSE)
      ruleChain.prepend(new Rule(this, Rule.COND_FALSE));
      return this.e2;
    }
  }

  /**
   * Returns the free identifiers of the
   * subexpressions.
   * @return the free identifiers.
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e0.free());
    set.addAll(this.e1.free());
    set.addAll(this.e2.free());
    return set;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return (this.e0.containsReferences() || this.e1.containsReferences() || this.e2.containsReferences());
  }
  
  /**
   * @return Returns the e0.
   */
  public Expression getE0() {
    return this.e0;
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
   * Returns the pretty string builder for conditions.
   * @return the pretty string builder for conditions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("if");
    builder.appendText(" ");
    builder.appendBuilder(this.e0.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("then");
    builder.appendText(" ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
    builder.appendText(" ");
    builder.appendKeyword("else");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
  
  // sub expressions
  private Expression e0;
  private Expression e1;
  private Expression e2;
}
