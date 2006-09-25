package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class represent the <code>snd</code> operator in the expression hierarchy,
 * which is syntactic sugar for <code>#2_2</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see #SND
 * @see de.unisiegen.tpml.core.expressions.Projection
 */
public final class Snd extends Projection {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Snd</code> class.
   * 
   * @see #Snd()
   */
  public static final Snd SND = new Snd();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new instance of the <code>snd</code> operator which is a special case of the projection
   * that returns the second item of a pair.
   * 
   * @see #SND
   */
  private Snd() {
    super(2, 2, "snd");
  }
}
