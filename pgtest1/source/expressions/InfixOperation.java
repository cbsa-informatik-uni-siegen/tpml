package expressions;

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
  public InfixOperation(BinaryOperator op, Expression e1, Expression e2) {
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
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new InfixOperation(this.op, this.e1.substitute(id, e), this.e2.substitute(id, e));
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
  public BinaryOperator getOp() {
    return this.op;
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

  private BinaryOperator op;
  private Expression e1;
  private Expression e2;
}
