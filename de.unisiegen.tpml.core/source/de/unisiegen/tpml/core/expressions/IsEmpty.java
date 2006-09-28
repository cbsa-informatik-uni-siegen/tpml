package de.unisiegen.tpml.core.expressions;

/**
 * The <code>is_empty</code> operator, which, when applied to a list, returns <code>true</code>
 * if the list is empty, or <code>false</code> if the list contains atleast one item.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.UnaryListOperator
 * @see de.unisiegen.tpml.core.expressions.Hd
 * @see de.unisiegen.tpml.core.expressions.Tl
 */
public final class IsEmpty extends UnaryListOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>IsEmpty</code> class.
   * 
   * @see #IsEmpty()
   */
  public static final IsEmpty IS_EMPTY = new IsEmpty();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>IsEmpty</code> instance.
   * 
   * @see #IS_EMPTY
   */
  private IsEmpty() {
    super("is_empty");
  }
}
