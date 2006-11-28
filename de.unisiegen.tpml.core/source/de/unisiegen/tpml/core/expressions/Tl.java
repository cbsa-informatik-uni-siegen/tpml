package de.unisiegen.tpml.core.expressions;

/**
 * The <code>tl</code> operator, which, when applied to a list, returns the list without the
 * first item, or when applied to an empty list, raises the <code>empty_list</code> exception.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.UnaryListOperator
 * @see de.unisiegen.tpml.core.expressions.Hd
 * @see de.unisiegen.tpml.core.expressions.IsEmpty
 */
public final class Tl extends UnaryListOperator {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Tl</code> instance.
   */
  public Tl() {
    super("tl");
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
  public Tl clone() {
    return new Tl();
  }
}
