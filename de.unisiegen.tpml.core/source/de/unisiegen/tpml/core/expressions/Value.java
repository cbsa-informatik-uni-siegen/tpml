package de.unisiegen.tpml.core.expressions;

/**
 * Abstract base class for expression classes, whose instances are always values,
 * for example {@link de.unisiegen.tpml.core.expressions.Lambda} expressions.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public abstract class Value extends Expression {
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * The implementation in the <code>Value</code> always returns <code>true</code>
   * and cannot be overwritten by derived classes.
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#isValue()
   */
  @Override
  public final boolean isValue() {
    return true;
  }
}
