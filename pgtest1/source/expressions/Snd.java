package expressions;

/**
 * The <code>snd</code> operator is syntactic
 * sugar for <code>#2_2</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Snd extends Projection {
  /**
   * Allocates a new instance of the <code>snd</code>
   * operator which is a special case of the projection
   * that returns the second item of a pair.
   */
  public Snd() {
    super(2, 2, Rule.SND);
  }
  
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
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Projection#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendKeyword("snd");
    return builder;
  }
}
