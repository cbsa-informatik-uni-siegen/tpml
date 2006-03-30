package smallstep;

/**
 * The <code>fst</code> operator is syntactic
 * sugar for <code>#2_1</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Fst extends Projection {
  /**
   * Allocates a new instance of the <code>fst</code>
   * operator which is a special case of the projection
   * that returns the first item of a pair.
   */
  public Fst() {
    super(2, 1, Rule.FST);
  }
  
  /**
   * Returns <code>true</code> since the <code>fst</code>
   * operator is syntactic sugar for <code>#2_1</code>.
   * 
   * @return always <code>true</code>.
   * 
   * @see smallstep.Projection#containsSyntacticSugar()
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
   * @see smallstep.Projection#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return new Projection(2, 1);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see smallstep.Projection#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendKeyword("fst");
    return builder;
  }
}
