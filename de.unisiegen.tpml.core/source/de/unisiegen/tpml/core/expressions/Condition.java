package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Represents the conditional evaluation in the expression hierarchy.
 * 
 * The string representation for conditions is <code>if e0 then e1 else e2</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Condition extends Expression {
  //
  // Attributes
  //
  
  /**
   * The condition.
   * 
   * @see #getE0()
   */
  private Expression e0;
  
  /**
   * The first expression, which is evaluated if <code>e0</code> evaluates to <code>true</code>.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second expression, which is evaluated if <code>e0</code> evaluates to <code>false</code>.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Condition</code> with the specified <code>e0</code>, <code>e1</code>
   * and <code>e2</code>.
   * 
   * @param e0 the condition.
   * @param e1 the <code>true</code> case.
   * @param e2 the <code>false</code> case.
   * 
   * @throws NullPointerException if <code>e0</code>, <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public Condition(Expression e0, Expression e1, Expression e2) {
    if (e0 == null) {
      throw new NullPointerException("e0 is null");
    }
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    this.e0 = e0;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the conditional expression.
   * 
   * @return the conditional expression.
   */
  public Expression getE0() {
    return this.e0;
  }

  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to <code>true</code>.
   * 
   * @return the <code>true</code> case.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the expression that is evaluated if <code>e0</code> evaluates to <code>false</code>.
   * 
   * @return the <code>false</code> case.
   */
  public Expression getE2() {
    return this.e2;
  }
  
  
  
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Condition substitute(TypeSubstitution substitution) {
    return new Condition(this.e0.substitute(substitution), this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Condition substitute(String id, Expression e) {
    Expression e0 = this.e0.substitute(id, e);
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.e2.substitute(id, e);
    return new Condition(e0, e1, e2);
  }

  
  
  //
  // Pretty Printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_CONDITION);
    builder.addKeyword("if");
    builder.addText(" ");
    builder.addBuilder(this.e0.toPrettyStringBuilder(factory), PRIO_CONDITION_E0);
    builder.addBreak();
    builder.addText(" ");
    builder.addKeyword("then");
    builder.addText(" ");
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_CONDITION_E1);
    builder.addBreak();
    builder.addText(" ");
    builder.addKeyword("else");
    builder.addText(" ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_CONDITION_E2);
    return builder;
  }

  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Condition) {
      Condition other = (Condition)obj;
      return (this.e0.equals(other.e0) && this.e1.equals(other.e1) && this.e2.equals(other.e2));
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @Override
  public int hashCode() {
    return this.e0.hashCode() + this.e1.hashCode() + this.e2.hashCode();
  }
}
