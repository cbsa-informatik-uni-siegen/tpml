package expressions;

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
   * {@inheritDoc}
   * @see expressions.Expression#normalize()
   */
  @Override
  public Expression normalize() {
    // normalize the sub expression
    Expression e1 = this.e1.normalize();
    Expression e2 = this.e2.normalize();
    
    // check if we need to generate new infix operation
    if (e1 != this.e1 || e2 != this.e2) {
      return new InfixOperation(this.op, e1, e2);
    }
    else {
      return this;
    }
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
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new InfixOperation(this.op, this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  /**
   * Returns the free identifiers of the two expressions
   * of the infix operation.
   * 
   * @return the free identifiers.
   * 
   * @see expressions.Expression#free()
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
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return (this.e1.containsReferences() || this.e2.containsReferences());
  }
  
  /**
   * Returns <code>true</code> since an infix operation
   * is syntactic sugar.
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
   * Translates the infix operation and the subexpressions
   * to the core syntax.
   * 
   * @return the new expression in the core syntax.
   * 
   * @see expressions.Expression#translateSyntacticSugar()
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
   * @see expressions.Expression#toPrettyStringBuilder()
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
