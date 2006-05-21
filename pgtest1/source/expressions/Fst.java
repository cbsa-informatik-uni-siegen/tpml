package expressions;

/**
 * The <code>fst</code> operator is syntactic
 * sugar for <code>#2_1</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Fst extends Projection {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Fst</code> class.
   */
  public static final Fst FST = new Fst();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new instance of the <code>fst</code>
   * operator which is a special case of the projection
   * that returns the first item of a pair.
   * 
   * @see #FST
   */
  private Fst() {
    super(2, 1, "fst");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> since the <code>fst</code>
   * operator is syntactic sugar for <code>#2_1</code>.
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
   * Translates the <code>fst</code> operator to
   * the projection <code>#2_1</code>.
   * 
   * @return the {@link Projection} <code>#2_1</code>. 
   * 
   * @see expressions.Projection#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Projection(2, 1);
  }
}
