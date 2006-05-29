package expressions;

/**
 * The <code>cons</code> operator, which takes a pair, with an element
 * and a list, and constructs a new list.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnaryCons extends Constant {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>UnaryCons</code> class.
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
