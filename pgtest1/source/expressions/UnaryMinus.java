package expressions;

/**
 * The unary minus operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class UnaryMinus extends UnaryOperator {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>UnaryMinus</code> class.
   */
  public static final UnaryMinus UMINUS = new UnaryMinus();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>UnaryMinus</code> instance.
   * 
   * @see #UMINUS
   */
  private UnaryMinus() {
    super("~-");
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
      // determine the numeric value of the expression
      int n = ((IntegerConstant)e).getNumber();
      
      // negate the numeric value
      return new IntegerConstant(-n);
    }
    catch (ClassCastException exception) {
      // cast to integer constant failed
      throw new UnaryOperatorException(this, e);
    }
  }
}
