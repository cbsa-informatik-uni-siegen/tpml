package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Instances of this class represent <code>while</code> expressions in the expression hierarchy that
 * serve as syntactic sugar introduce with the imperative concepts.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class While extends Expression {
  //
  // Attributes
  //
  
  /**
   * The conditional part.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The statement part.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>While</code> instance with the specified <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the conditional part.
   * @param e2 the repeated statement.
   * 
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public While(Expression e1, Expression e2) {
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
   * Returns the conditional part.
   * 
   * @return the conditional part.
   */
  public Expression getE1() {
    return this.e1;
  }

  /**
   * Returns the repeated statement.
   * 
   * @return the loop body.
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
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public While clone() {
    return new While(this.e1.clone(), this.e2.clone());
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Expression substitute(TypeSubstitution substitution) {
    Expression e1 = this.e1.substitute(substitution);
    Expression e2 = this.e2.substitute(substitution);
    return (this.e1 == e1 && this.e2 == e2) ? this : new While(e1, e2);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.e2.substitute(id, e);
    return (this.e1 == e1 && this.e2 == e2) ? this : new While(e1, e2);
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_WHILE);
    builder.addKeyword("while");
    builder.addText(" ");
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_WHILE_E1);
    builder.addBreak();
    builder.addText(" ");
    builder.addKeyword("do");
    builder.addText(" ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_WHILE_E2);
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
    if (obj instanceof While) {
      While other = (While)obj;
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
