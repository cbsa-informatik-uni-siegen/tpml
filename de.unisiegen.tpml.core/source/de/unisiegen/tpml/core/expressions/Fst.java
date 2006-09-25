package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent the <code>fst</code> operator in the expression hierarchy,
 * which is syntactic sugar for <code>#2_1</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see #FST
 * @see de.unisiegen.tpml.core.expressions.Projection
 */
public final class Fst extends Projection {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Fst</code> class.
   * 
   * @see #Fst()
   */
  public static final Fst FST = new Fst();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new instance of the <code>fst</code> operator which is a special case of the projection
   * that returns the first item of a pair.
   * 
   * @see #FST
   */
  private Fst() {
    super(2, 1, "fst");
  }
}
