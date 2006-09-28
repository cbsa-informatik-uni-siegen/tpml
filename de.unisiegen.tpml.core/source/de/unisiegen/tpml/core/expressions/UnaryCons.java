package de.unisiegen.tpml.core.expressions;

/**
 * The <code>cons</code> operator, which takes a pair, with an element and a list, and
 * constructs a new list.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public final class UnaryCons extends Constant {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>UnaryCons</code> class.
   * 
   * @see #UnaryCons()
   */
  public static final UnaryCons CONS = new UnaryCons();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Constructs a new <code>UnaryCons</code> instance.
   * 
   * @see #CONS
   */
  private UnaryCons() {
    super("cons");
  }
}
