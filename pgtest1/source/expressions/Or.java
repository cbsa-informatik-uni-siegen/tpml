package expressions;

/**
 * Represents the <b>(OR)</b> expression, which is syntactic sugar
 * for <pre>if e1 then true else e2</pre>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Or extends Expression {
  //
  // Attributes
  //

  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <b>(OR)</b> expression with the specified
   * operands <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Or(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Primitives
  //
  
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
    return new Or(this.e1.substitute(id, e), this.e2.substitute(id, e));
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
    return new Condition(this.e1, BooleanConstant.TRUE, this.e2);
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
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 1);
    builder.appendText(" || ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 2);
    return builder;
  }
}
