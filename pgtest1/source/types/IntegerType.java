package types;

/**
 * This class represents the <tt>int</tt> type in our
 * type system. Only a single instance of this class
 * exists at all times.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see #INT
 */
public final class IntegerType extends PrimitiveType {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>IntegerType</code>
   * class, which represents the <tt>int</tt> type in
   * our type system.
   */
  public static final IntegerType INT = new IntegerType();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>IntegerType</code> instance.
   */
  private IntegerType() {
    super("int");
  }
}
