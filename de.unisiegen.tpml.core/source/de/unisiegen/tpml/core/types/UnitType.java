package de.unisiegen.tpml.core.types;

/**
 * This class represents the <tt>unit</tt> type in our type system. Only a single instance of this class
 * exists at all times.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see #UNIT
 */
public final class UnitType extends PrimitiveType {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>UnitType</code> class,
   * which represents the <tt>unit</tt> type in our type
   * system.
   */
  public static final UnitType UNIT = new UnitType();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>UnitType</code> instance.
   * 
   * @see #UNIT
   */
  private UnitType() {
    super("unit");
  }
}
