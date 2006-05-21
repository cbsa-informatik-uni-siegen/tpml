package expressions;

/**
 * Presents a projection, which can be used to select
 * an element from a {@link expressions.Tuple} expression.
 * 
 * The syntax is <code>#[arity]_[index]</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class Projection extends UnaryOperator {
  //
  // Attributes
  //

  /**
   * The arity of the projection.
   * 
   * @see #getArity()
   */
  private int arity;
  
  /**
   * The index of the projection.
   * 
   * @see #getIndex()
   */
  private int index;
  
  
  
  //
  // Constructors
  //
  
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
    this(arity, index, "#" + arity + "_" + index);
  }
  
  /**
   * Allocates a new {@link Projection} with the given
   * <code>arity</code> and the <code>index</code> of 
   * the item that should be selected, and the string
   * representation <code>op</code>.
   * 
   * @param arity the arity of the tuple to which
   *              this projection can be applied.
   * @param index the index of the item to select
   *              from the tuple, starting with
   *              <code>1</code>.
   * @param op the string representation of the projectin.              
   *              
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *                                  <code>index</code> is invalid.
   */
  protected Projection(int arity, int index, String op) {
    super(op);
    
    // validate the settings
    if (arity <= 0)
      throw new IllegalArgumentException("The arity of a projection must be greater than 0");
    else if (index <= 0 || index > arity)
      throw new IllegalArgumentException("The index of a projection must be greater than 0 and less than the arity");
    
    this.arity = arity;
    this.index = index;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.UnaryOperator#applyTo(expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e) throws UnaryOperatorException {
    try {
      // determine the sub expressions of the tuple
      Expression[] expressions = ((Tuple)e).getExpressions();
      
      // verify that the arities match
      if (this.arity != expressions.length)
        throw new UnaryOperatorException(this, e);
      
      // return the sub expression at the index
      return expressions[this.index - 1];
    }
    catch (ClassCastException exception) {
      // cast of expression to tuple failed
      throw new UnaryOperatorException(this, e);
    }
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
}
