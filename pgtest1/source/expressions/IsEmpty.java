package expressions;

/**
 * The <code>is_empty</code> operator, when applied to a
 * list returns <code>true</code> if the list is empty,
 * or <code>false</code> if the list contains atleast
 * one item.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class IsEmpty extends UnaryListOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>IsEmpty</code> class.
   */
  public static final IsEmpty IS_EMPTY = new IsEmpty();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Constructs a new <code>IsEmpty</code> instance.
   * 
   * @see #IS_EMPTY
   */
  private IsEmpty() {
    super("is_empty");
  }
}
