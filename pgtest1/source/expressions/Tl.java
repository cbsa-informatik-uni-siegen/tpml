package expressions;

/**
 * The <code>tl</code> operator, which, when applied to
 * a list, returns the list without the first item, or
 * when applied to an empty list, raises the <code>empty_list</code>
 * exception.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Tl extends UnaryListOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Tl</code> class.
   */
  public static final Tl TL = new Tl();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Constructs a new <code>Tl</code> instance.
   * 
   * @see #TL
   */
  private Tl() {
    super("tl");
  }
}
