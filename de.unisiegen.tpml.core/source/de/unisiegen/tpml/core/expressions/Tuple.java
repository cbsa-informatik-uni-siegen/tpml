package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Instances of this class represent tuples in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Tuple extends Expression {
  //
  // Attributes
  //
  
  /**
   * The sub expressions.
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression[] expressions;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Tuple</code> with the given <code>expressions</code>.
   * 
   * @param expression a non-empty array of {@link Expression}s.
   * 
   * @throws NullPointerException if <code>expressions</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>expressions</code> is empty.
   */
  public Tuple(Expression[] expressions) {
    if (expressions == null) {
      throw new NullPointerException("expressions is null");
    }
    if (expressions.length == 0) {
      throw new IllegalArgumentException("expressions is empty");
    }
    this.expressions = expressions;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   * 
   * @see #getExpressions(int)
   */
  public Expression[] getExpressions() {
    return this.expressions;
  }
  
  /**
   * Returns the <code>n</code>th sub expression.
   * 
   * @param n the index of the expression to return.
   * 
   * @return the <code>n</code>th sub expression.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of bounds.
   * 
   * @see #getExpressions()
   */
  public Expression getExpressions(int n) {
    return this.expressions[n];
  }

  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Tuple substitute(String id, Expression e) {
    Expression[] expressions = new Expression[this.expressions.length];
    for (int n = 0; n < expressions.length; ++n) {
      expressions[n] = this.expressions[n].substitute(id, e);
    }
    return new Tuple(expressions);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    for (Expression e : this.expressions) {
      if (!e.isValue()) {
        return false;
      }
    }
    return true;
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_TUPLE);
    builder.addText("(");
    for (int n = 0; n < this.expressions.length; ++n) {
      if (n > 0) {
        builder.addText(", ");
        builder.addBreak();
      }
      builder.addBuilder(this.expressions[n].toPrettyStringBuilder(factory), PRIO_TUPLE_E);
    }
    builder.addText(")");
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
    if (obj instanceof Tuple) {
      Tuple other = (Tuple)obj;
      return (this.expressions.equals(other.expressions));
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
    return this.expressions.hashCode();
  }
}
