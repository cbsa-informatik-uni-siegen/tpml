package de.unisiegen.tpml.core.types;

/**
 * This class represents the <tt>int</tt> type in our type system. Only a single instance of this class
 * exists at all times.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see #INT
 * @see de.unisiegen.tpml.core.types.PrimitiveType
 */
public final class IntegerType extends PrimitiveType {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>IntegerType</code> class, which represents the <tt>int</tt> type in
   * our type system.
   */
  public static final IntegerType INT = new IntegerType();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>IntegerType</code> instance.
   * 
   * @see #INT
   */
  private IntegerType() {
    super("int");
  }
}
