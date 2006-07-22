package types;

/**
 * This class represents the <tt>bool</tt> type in our
 * type system. Only a single instance of this class
 * exists at all times.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see #BOOL
 */
public final class BooleanType extends PrimitiveType {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>BooleanType</code>
   * class, which represents the <tt>bool</tt> type in
   * our type system.
   */
  public static final BooleanType BOOL = new BooleanType();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>BooleanType</code> instance.
   */
  private BooleanType() {
    super("bool");
  }
}
