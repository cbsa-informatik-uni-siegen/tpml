package expressions;

/**
 * The <code>not</code> operator, which negates a boolean value.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Not extends UnaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>Not</code> class.
   */
  public static final Not NOT = new Not();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>Not</code> operator.
   * 
   * @see #NOT
   */
  private Not() {
    super("not");
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
      // negate the value of the boolean constant
      return ((BooleanConstant)e).isTrue() ? BooleanConstant.FALSE : BooleanConstant.TRUE;
    }
    catch (ClassCastException exception) {
      // cast to boolean constant failed
      throw new UnaryOperatorException(this, e);
    }
  }
}
