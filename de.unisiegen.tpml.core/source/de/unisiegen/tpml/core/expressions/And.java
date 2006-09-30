package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Instances of this class represent and expressions, printed as <code>e1 &amp;&amp; e2</code>. This
 * is syntactic sugar for <code>if e1 then e2 else false</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Condition
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.expressions.Or
 */
public final class And extends Expression {
  //
  // Attributes
  //
  
  /**
   * The left-side expression.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The right-side expression.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>And</code> instance with the specified <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the left side expression.
   * @param e2 the right side expression.
   * 
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public And(Expression e1, Expression e2) {
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the left side expression.
   * 
   * @return the left side expression.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the right side expression.
   * 
   * @return the right side expression.
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
  public Expression substitute(TypeSubstitution substitution) {
    return new And(this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new And(this.e1.substitute(id, e), this.e2.substitute(id, e));
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_AND);
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_AND_E1);
    builder.addText(" && ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_AND_E2);
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
    if (obj instanceof And) {
      And other = (And)obj;
      return (this.e1.equals(other.e1) && this.e2.equals(other.e2));
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
    return this.e1.hashCode() + this.e2.hashCode();
  }
}
