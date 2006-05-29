package expressions;

/**
 * The constant for the empty list, represented as
 * <code>[]</code>, which must be handled differently
 * from the <code>List</code> syntactic sugar.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class EmptyList extends Constant {
  //
  // Constant
  //
  
  /**
   * The single instance of the <code>EmptyList</code> class.
   */
  public static final EmptyList EMPTY_LIST = new EmptyList();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Constructs a new <code>EmptyList</code> instance.
   * 
   * @see #EMPTY_LIST
   */
  private EmptyList() {
    super("[]");
  }
}
