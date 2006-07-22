package expressions;

import common.prettyprinter.PrettyStringBuilder;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Application extends Expression {
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
   * Generates a new application.
   * @param e1 the first expression.
   * @param e2 the second expression.
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * An <code>Application</code> can be a value if it consists
   * of a binary operator and a value, or if it consists of a
   * <code>UnaryCons</code> operator and a value.
   * 
   * @return <code>true</code> if the application consists of
   *                           a binary operator and a value.
   *
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    return ((this.e1 instanceof BinaryOperator || this.e1 instanceof UnaryCons) && this.e2.isValue());
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

  /**
   * Returns the first sub expression.
   * 
   * @return the e1.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second sub expression.
   * 
   * @return the e2.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  /**
   * Returns the pretty string builder for applications.
   * @return the pretty string builder for applications.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 5);
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 5);
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 6);
    return builder;
  }
}
