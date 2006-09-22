package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Instances of this class represent statements for sequential execution of commands in the expression
 * hierarchy.
 * 
 * The string representation for <code>Sequence</code>s is <code>e1;e2</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Sequence extends Expression {
  //
  // Attributes
  //
  
  /**
   * The first statement.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second statement.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Sequence</code> with the given expressions <code>e1</code> and <code>e2</code>.
   * 
   * @param e1 the first statement.
   * @param e2 the second statement.
   * 
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public Sequence(Expression e1, Expression e2) {
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
   * Returns the first statement.
   * 
   * @return the first statement.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second statement.
   * 
   * @return the second statement.
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
    return new Sequence(this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return new Sequence(this.e1.substitute(id, e), this.e2.substitute(id, e));
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_SEQUENCE);
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_SEQUENCE_E1);
    builder.addText("; ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_SEQUENCE_E2);
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
    if (obj instanceof Sequence) {
      Sequence other = (Sequence)obj;
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
