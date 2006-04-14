package smallstep;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents an infix expression.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class InfixOperation extends Expression {
  /**
   * Allocates a new infix operation with the specified parameters.
   * 
   * @param op the operator.
   * @param e1 the first operand.
   * @param e2 the second operand.
   */
  public InfixOperation(Operator op, Expression e1, Expression e2) {
    this.op = op;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  /**
   * Performs the substitution on infix operations, which is pretty similar to
   * the substitution on <b>(APP)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to subsitute.
   * 
   * @return the new expression.
   * 
   * @see smallstep.Expression#substitute(java.lang.String, smallstep.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new InfixOperation(this.op, this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  /**
   * Evaluates an infix operation. The evaluation for infix
   * operations is pretty similar to the evaluation of
   * applications.
   * 
   * An infix operation <code>e1 op e2</code> is syntactic
   * sugar for <code>(op e1) e2</code>, so evaluating
   * <code>e1</code> always requires atleast one
   * <b>(APP-LEFT)</b>.
   * 
   * @param ruleChain the chain for small step rules.
   * 
   * @return the resulting expression.
   * 
   * @see smallstep.Expression#evaluate(smallstep.RuleChain)
   */
  @Override
  public Expression evaluate(RuleChain ruleChain) {
    assert (ruleChain.isEmpty());
    assert (this.e1 instanceof Expression);
    assert (this.e2 instanceof Expression);
    
    // evaluate e1 (may already be a valud)
    Expression e1 = this.e1.evaluate(ruleChain);
    
    // check if any rules were applied in the evaluation
    // of e1 (if not, then e1 was already a value), e1
    // may also be an exception to forward)
    if (!ruleChain.isEmpty()) {
      if (e1 instanceof Exn) {
        // prepend (APP-RIGHT-EXN) and (APP-LEFT-EXN)
        ruleChain.prepend(new Rule(this, Rule.APP_RIGHT_EXN));
        ruleChain.prepend(new Rule(this, Rule.APP_LEFT_EXN));
        return e1;
      }
      else {
        // prepend (APP-RIGHT) and (APP-LEFT)
        ruleChain.prepend(new Rule(this, Rule.APP_RIGHT));
        ruleChain.prepend(new Rule(this, Rule.APP_LEFT));
        return new InfixOperation(this.op, e1, this.e2);
      }
    }
    
    // if e1 is still not a constant, then the 
    // evaluation got stuck and there are no
    // more small steps to perform
    if (!(e1 instanceof Constant))
      return new InfixOperation(this.op, e1, this.e2);
    
    // if we get here, e1 must be a value
    // and the rule chain is empty
    assert (e1 instanceof Constant);
    assert (ruleChain.isEmpty());
    
    // evaluate e2 (may already be a value)
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
        return new InfixOperation(this.op, e1, e2);
      }
    }
    
    // if e2 is still not a constant, then the evaluation
    // got stuck and there are no more small steps to perform
    if (!(e2 instanceof Constant))
      return new InfixOperation(this.op, e1, e2);
    
    // if we get here, e1 and e2 must be
    // constants and the rule chain is empty
    assert (e1 instanceof Constant);
    assert (e2 instanceof Constant);
    assert (ruleChain.isEmpty());
    
    // cast the expressions to integer constants
    Constant c1 = (Constant)e1;
    Constant c2 = (Constant)e2;
    
    // check if the operator can be applied to the operands,
    // else the evaluation got stuck here
    if (!this.op.canApplyTo(c1.getClass(), c2.getClass()))
      return new InfixOperation(this.op, c1, c2);
    
    // perform the operation
    ruleChain.prepend(new Rule(this, Rule.OP));
    return this.op.applyTo(c1, c2);
  }

  /**
   * Returns the free identifiers of the two expressions
   * of the infix operation.
   * 
   * @return the free identifiers.
   * 
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
   * @return Returns the op.
   */
  public Operator getOp() {
    return this.op;
  }
  
  /**
   * Returns <code>true</code> since an infix operation
   * is syntactic sugar.
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
   * Translates the infix operation and the subexpressions
   * to the core syntax.
   * 
   * @return the new expression in the core syntax.
   * 
   * @see smallstep.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Application(new Application(this.op, this.e1), this.e2);
  }

  /**
   * Returns the pretty string builder for infix operations.
   * 
   * @return the pretty string builder for infix operations.
   * 
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, this.op.getPrettyPriority());
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), this.op.getPrettyPriority());
    builder.appendText(" " + this.op.toString() + " ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), this.op.getPrettyPriority() + 1);
    return builder;
  }

  private Operator op;
  private Expression e1;
  private Expression e2;
}
