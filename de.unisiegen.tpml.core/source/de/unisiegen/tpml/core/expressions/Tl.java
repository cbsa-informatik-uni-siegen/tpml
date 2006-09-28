package de.unisiegen.tpml.core.expressions;

/**
 * The <code>tl</code> operator, which, when applied to a list, returns the list without the
 * first item, or when applied to an empty list, raises the <code>empty_list</code> exception.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.UnaryListOperator
 * @see de.unisiegen.tpml.core.expressions.Hd
 * @see de.unisiegen.tpml.core.expressions.IsEmpty
 */
public final class Tl extends UnaryListOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Tl</code> class.
   * 
   * @see #Tl()
   */
  public static final Tl TL = new Tl();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Tl</code> instance.
   * 
   * @see #TL
   */
  private Tl() {
    super("tl");
  }
}
