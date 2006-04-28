package expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the <b>(OR)</b> expression, which is syntactic sugar
 * for <pre>if e0 then true else e1</pre>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Or extends Expression {
  /**
   * Allocates a new <b>(OR)</b> expression with the specified
   * operands <code>e0</code> and <code>e1</code>.
   * 
   * @param e0 the first operand.
   * @param e1 the second operand.
   */
  public Or(Expression e0, Expression e1) {
    this.e0 = e0;
    this.e1 = e1;
  }
  
  /**
   * Performs the substitution for <b>(OR)</b> expressions.
   * 
   * @param id the identifier.
   * @param e the expression to substitute for <code>id</code>.
   * 
   * @return the resulting expression.
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new Or(this.e0.substitute(id, e), this.e1.substitute(id, e));
  }

  /**
   * Evaluates the <b>(OR)</b> expression and prepends
   * appropriate rules to the <code>ruleChain</code>.
   * 
   * @param ruleChain target rule chain.
   * 
   * @return the resulting expression.
   * 
   * @see expressions.Expression#evaluate(expressions.RuleChain)
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    assert (this.e0 instanceof Expression);
    assert (this.e1 instanceof Expression);
    
    // evaluate e0 (may already be a value)
    Expression e0 = this.e0.evaluate(ruleChain);
    
    // check if any rules were applied in the evaluation
    // of e0 (if not, then e0 was already a value), e0
    // may also be an exception to forward
    if (!ruleChain.isEmpty()) {
      if (e0 instanceof Exn) {
        // prepend (OR-EVAL-EXN)
        ruleChain.prepend(new Rule(this, Rule.OR_EVAL_EXN));
        return e0;
      }
      else {
        // prepend (OR-EVAL)
        ruleChain.prepend(new Rule(this, Rule.OR_EVAL));
        return new Or(e0, this.e1);
      }
    }
    
    // if e0 is still not a boolean constant, then
    // the evaluation got stuck and there are no
    // more small steps to perform
    if (!(e0 instanceof BooleanConstant))
      return new Or(e0, this.e1);

    // if we get here, e0 must be a boolean
    // constant and the rule chain is empty
    assert (e0 instanceof BooleanConstant);
    assert (ruleChain.isEmpty());
    
    // now e0 is either true or false
    BooleanConstant c0 = (BooleanConstant)e0;
    if (c0.isTrue()) {
      ruleChain.prepend(new Rule(this, Rule.OR_TRUE));
      return BooleanConstant.TRUE;
    }
    else {
      ruleChain.prepend(new Rule(this, Rule.OR_FALSE));
      return this.e1;
    }
  }

  /**
   * Returns the set of free identifiers within the
   * subexpressions.
   * 
   * @return the set of free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> set = new TreeSet<String>();
    set.addAll(this.e0.free());
    set.addAll(this.e1.free());
    return set;
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
   * Returns <code>true</code> since <b>(OR)</b> is
   * syntactic sugar for <b>(COND)</b>.
   * 
   * @return <code>true</code>.
   * 
   * @see expressions.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
  /**
   * Translates the <b>(OR)</b> and its subexpressions to
   * a <b>(COND)</b> expression in the core syntax.
   * 
   * @return the new expression in the core syntax.
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Condition(this.e0, BooleanConstant.TRUE, this.e1);
  }

  /**
   * Returns the pretty string builder for <b>(OR)</b> expressions.
   * 
   * @return the pretty string builder.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 1);
    builder.appendBuilder(this.e0.toPrettyStringBuilder(), 1);
    builder.appendText(" || ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 2);
    return builder;
  }

  private Expression e0;
  private Expression e1;
}
