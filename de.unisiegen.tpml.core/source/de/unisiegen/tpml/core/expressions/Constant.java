package de.unisiegen.tpml.core.expressions;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Abstract class to represent a constant expression (only values can be constants).
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public abstract class Constant extends Value {
  //
  // Attributes
  //
  
  /**
   * The text representation of the constant.
   * 
   * @see #toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  private String text;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>Constant</code> with the string representation given in <code>text</code>.
   * 
   * @param text the string representation of the constant.
   * 
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  protected Constant(String text) {
    if (text == null) {
      throw new NullPointerException("text is null");
    }
    this.text = text;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * Substitution below constants is not possible, so for <code>Constant</code>s this method
   * will always return the constant itself.
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public final Expression substitute(String id, Expression e) {
    return this;
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
  protected PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_CONSTANT);
    builder.addConstant(this.text);
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
    if (obj instanceof Constant) {
      Constant other = (Constant)obj;
      return (this.text.equals(other.text) && getClass().equals(other.getClass()));
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
    return this.text.hashCode() + getClass().hashCode();
  }

}
