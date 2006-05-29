package expressions;

/**
 * The <code>hd</code> operator, which, when applied to
 * a list, returns the first item of the list, or raises
 * an <code>empty_list</code> exception if the list is
 * empty.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Hd extends UnaryListOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Hd</code>.
   */
  public static final Hd HD = new Hd();

  
  
  //
  // Constructor (private)
  //

  /**
   * Constructs a new <code>Hd</code> instance.
   * 
   * @see #HD
   */
  private Hd() {
    super("hd");
  }
}
