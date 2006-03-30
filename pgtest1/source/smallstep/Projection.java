package smallstep;

import java.util.Set;

/**
 * Presents a projection, which can be used to select
 * an element from a {@link smallstep.Tuple} expression.
 * 
 * The syntax is <code>#[arity]_[index]</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class Projection extends Value {
  /**
   * Allocates a new {@link Projection} with the given
   * <code>arity</code> and the <code>index</code> of 
   * the item that should be selected.
   * 
   * @param arity the arity of the tuple to which
   *              this projection can be applied.
   * @param index the index of the item to select
   *              from the tuple, starting with
   *              <code>1</code>.
   *              
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *                                  <code>index</code> is invalid.
   */
  public Projection(int arity, int index) {
    this(arity, index, Rule.PROJ);
  }
  
  /**
   * Constructor for derived classes ({@link Fst} and {@link Snd}),
   * which allows to specify the <code>ruleType</code>.
   * 
   * @param arity the arity.
   * @param index the index.
   * @param ruleType the {@link Rule.Type}.
   */
  protected Projection(int arity, int index, Rule.Type ruleType) {
    // validate the settings
    if (arity <= 0)
      throw new IllegalArgumentException("The arity of a projection must be greater than 0");
    else if (index <= 0 || index > arity)
      throw new IllegalArgumentException("The index of a projection must be greater than 0 and less than the arity");
    
    // apply the settings
    this.ruleType = ruleType;
    this.arity = arity;
    this.index = index;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see smallstep.Value#applyTo(smallstep.Expression, smallstep.Application, smallstep.RuleChain)
   */
  @Override
  public Expression applyTo(Expression v, Application e, RuleChain ruleChain) {
    // v must be a tuple
    if (v instanceof Tuple) {
      // cast to tuple and check that we can apply
      Tuple t = (Tuple)v;
      if (t.arity() != this.arity) {
        // cannot evaluate, invalid arity
        return super.applyTo(v, e, ruleChain);
      }
      else {
        // return the requested item from the tuple
        // using the (PROJ) or whatever appropriate
        // (i.e. (FST) or (SND)) smallstep rule
        ruleChain.prepend(new Rule(e, this.ruleType));
        return t.getExpressions()[this.index - 1];
      }
    }
    else {
      return super.applyTo(v, e, ruleChain);
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return Expression.EMPTY_SET;
  }

  /**
   * {@inheritDoc}
   * 
   * @see smallstep.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see smallstep.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return this;
  }

  /**
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendKeyword("#" + arity + "_" + index);
    return builder;
  }
  
  /**
   * Returns the arity.
   * 
   * @return the arity.
   */
  public int getArity() {
    return this.arity;
  }
  
  /**
   * Returns the index.
   * 
   * @return the index.
   */
  public int getIndex() {
    return this.index;
  }

  // member attributes
  private Rule.Type ruleType;
  private int arity;
  private int index;
}
