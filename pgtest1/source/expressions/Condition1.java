package expressions;

import expressions.annotation.SyntacticSugar;

/**
 * A condition without an else block.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class Condition1 extends Expression {
  //
  // Attributes
  //
  
  /**
   * The conditional block.
   */
  private Expression e0;
  
  /**
   * The true block.
   */
  private Expression e1;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Condition1</code> instance.
   * 
   * @param e0 the condition.
   * @param e1 the true case.
   */
  public Condition1(Expression e0, Expression e1) {
    this.e0 = e0;
    this.e1 = e1;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Condition(this.e0, this.e1, UnitConstant.UNIT);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new Condition1(this.e0.substitute(id, e), this.e1.substitute(id, e));
  }

  /**
   * {@inheritDoc}
   * 
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
    return builder;
  }
  
  
  
  //
  // Accessors
  //
  
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
}
