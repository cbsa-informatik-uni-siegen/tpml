package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Represents the <b>(APP)</b> expression in the expression hierarchy.
 * 
 * The string representation for applications is <pre>e1 e2</pre>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Application extends Expression {
  //
  // Attributes
  //

  /**
   * The first, left-side expression.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second, right-side expression.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new application of <code>e1</code> to <code>e2</code>.
   * 
   * @param e1 the first expression (the operation).
   * @param e2 the second expression (the operand).
   * 
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is <code>null</code>.
   */
  public Application(Expression e1, Expression e2) {
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
   * Returns the first expression of the application.
   * @return the first expression of the application.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second expression of the application.
   * @return the second expression of the application.
   */
  public Expression getE2() {
    return this.e2;
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
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Application substitute(TypeSubstitution substitution) {
    return new Application(this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * Substitutes <code>e</code> for <code>id</code> in the two
   * sub expressions of the application.
   * 
   * @param id the identifier for which to substitute.
   * @param e the expression to substitute for <code>id</code>.
   * 
   * @return the resulting expression.
   * 
   * @see #getE1()
   * @see #getE2()
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Application substitute(String id, Expression e) {
    return new Application(this.e1.substitute(id, e), this.e2.substitute(id, e));
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_APPLICATION);
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_APPLICATION_E1);
    builder.addText(" ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_APPLICATION_E2);
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
    if (obj instanceof Application) {
      Application other = (Application)obj;
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
