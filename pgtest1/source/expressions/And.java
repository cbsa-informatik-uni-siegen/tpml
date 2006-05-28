package expressions;

import expressions.annotation.SyntacticSugar;

/**
 * Represents the <b>(AND)</b> expression, which is syntactic
 * sugar for <pre>if e1 then e2 else false</pre>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class And extends Expression {
  /**
   * Allocates a new <b>(AND)</b> expression with the specified
   * operands <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public And(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
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
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new And(this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  /**
   * Returns the first expression.
   * 
   * @return Returns the e1.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second expression.
   * 
   * @return Returns the e2.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  /**
   * Translates the <b>(AND)</b> expression to its matching
   * <b>(COND)</b> expression.
   * 
   * @return the resulting <b>(COND)</b> expression.
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Condition(this.e1, this.e2, BooleanConstant.FALSE);
  }

  /**
   * Returns the pretty string builder for <b>(AND)</b>
   * expressions.
   * 
   * @return the pretty string builder.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 1);
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 1);
    builder.appendText(" && ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 2);
    return builder;
  }

  private Expression e1;
  private Expression e2;
}
