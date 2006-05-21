package expressions;

/**
 * The <code>snd</code> operator is syntactic
 * sugar for <code>#2_2</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Snd extends Projection {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Snd</code> class.
   */
  public static final Snd SND = new Snd();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new instance of the <code>snd</code>
   * operator which is a special case of the projection
   * that returns the second item of a pair.
   * 
   * @see #SND
   */
  private Snd() {
    super(2, 2, "snd");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> since the <code>snd</code>
   * operator is syntactic sugar for <code>#2_2</code>.
   * 
   * @return always <code>true</code>.
   * 
   * @see expressions.Projection#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
  /**
   * Translates the <code>snd</code> operator to
   * the projection <code>#2_2</code>.
   * 
   * @return the {@link Projection} <code>#2_2</code>. 
   * 
   * @see expressions.Projection#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Projection(2, 2);
  }
}
