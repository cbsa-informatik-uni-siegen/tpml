package expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * A condition without an else block.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
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
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return (this.e0.containsReferences() || this.e1.containsReferences());
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
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
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e0.free());
    set.addAll(this.e1.free());
    return set;
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
