package smallstep;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the <b>(AND)</b> expression, which is syntactic
 * sugar for <pre>if e0 then e1 else false</pre>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class And extends Expression {
  /**
   * Allocates a new <b>(AND)</b> expression with the specified
   * operands <code>e0</code> and <code>e1</code>.
   * 
   * @param e0 the first operand.
   * @param e1 the second operand.
   */
  public And(Expression e0, Expression e1) {
    this.e0 = e0;
    this.e1 = e1;
  }
  
  /**
   * Substitutes <code>e</code> for <code>id</code> within the subexpressions
   * of this <b>(AND)</b> expression.
   * 
   * @param id the identifier.
   * @param e the expression to substitute for <code>id</code>.
   * 
   * @return the resulting expression. 
   * 
   * @see smallstep.Expression#substitute(java.lang.String, smallstep.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new And(this.e0.substitute(id, e), this.e1.substitute(id, e));
  }

  /**
   * Evaluates the expression and adds applied rules to the
   * <code>ruleChain</code>.
   * 
   * @param ruleChain target chain of rules.
   * 
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#evaluate(smallstep.RuleChain)
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
        // prepend (AND-EVAL-EXN)
        ruleChain.prepend(new Rule(this, Rule.AND_EVAL_EXN));
        return e0;
      }
      else {
        // prepend (AND-EVAL)
        ruleChain.prepend(new Rule(this, Rule.AND_EVAL));
        return new And(e0, this.e1);
      }
    }
    
    // if e0 is still not a boolean constant, then
    // the evaluation got stuck and there are no
    // more small steps to perform
    if (!(e0 instanceof BooleanConstant))
      return new And(e0, this.e1);

    // if we get here, e0 must be a boolean
    // constant and the rule chain is empty
    assert (e0 instanceof BooleanConstant);
    assert (ruleChain.isEmpty());
    
    // now e0 is either true or false
    BooleanConstant c0 = (BooleanConstant)e0;
    if (c0.isTrue()) {
      ruleChain.prepend(new Rule(this, Rule.AND_TRUE));
      return this.e1;
    }
    else {
      ruleChain.prepend(new Rule(this, Rule.AND_FALSE));
      return BooleanConstant.FALSE;
    }
  }

  /**
   * Returns the set of free identifiers for the
   * <code>&&</code> expression.
   * 
   * @return the set of free identifiers.
   * 
   * @see smallstep.Expression#free()
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
   * Returns <code>true</code> since <b>(AND)</b> is
   * syntactic sugar for <b>(COND)</b>.
   * 
   * @return <code>true</code>.
   * 
   * @see smallstep.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
  /**
   * Translates the <b>(AND)</b> expression to its matching
   * <b>(COND)</b> expression.
   * 
   * @return the resulting <b>(COND)</b> expression.
   * 
   * @see smallstep.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Condition(this.e0.translateSyntacticSugar(), this.e1.translateSyntacticSugar(), BooleanConstant.FALSE);
  }

  /**
   * Returns the pretty string builder for <b>(AND)</b>
   * expressions.
   * 
   * @return the pretty string builder.
   * 
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 1);
    builder.appendBuilder(this.e0.toPrettyStringBuilder(), 1);
    builder.appendText(" && ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 2);
    return builder;
  }

  private Expression e0;
  private Expression e1;
}
