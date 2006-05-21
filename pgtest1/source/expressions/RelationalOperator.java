package expressions;

/**
 * Represents a relational operator.
 *
 * @author Benedikt Meurer
 * @version $Id:RelationalOperator.java 121 2006-04-28 16:45:27Z benny $
 */
public class RelationalOperator extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The equals operator.
   */
  public static final RelationalOperator EQUALS = new RelationalOperator("=");
  
  /**
   * The lower-than operator.
   */
  public static final RelationalOperator LOWER_THAN = new RelationalOperator("<");
  
  /**
   * The greater-than operator.
   */
  public static final RelationalOperator GREATER_THAN = new RelationalOperator(">");
  
  /**
   * The lower-equal operator.
   */
  public static final RelationalOperator LOWER_EQUAL = new RelationalOperator("<=");
  
  /**
   * The greater-equal operator.
   */
  public static final RelationalOperator GREATER_EQUAL = new RelationalOperator(">=");

  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>RelationalOperator</code> with the
   * specified string representation <code>op</code>.
   * 
   * @param op the string representation.
   */
  private RelationalOperator(String op) {
    super(op, 2);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.BinaryOperator#applyTo(expressions.Expression, expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    try {
      // determine the numeric values of the operands
      int n1 = ((IntegerConstant)e1).getNumber();
      int n2 = ((IntegerConstant)e2).getNumber();
      
      // perform the requested comparison
      if (this == EQUALS) {
        return (n1 == n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == LOWER_THAN) {
        return (n1 < n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == GREATER_THAN) {
        return (n1 > n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == LOWER_EQUAL) {
        return (n1 <= n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else if (this == GREATER_EQUAL) {
        return (n1 >= n2) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
      }
      else {
        // programming error
        throw new IllegalStateException("inconsistent arithmetic operator class");
      }
    }
    catch (ClassCastException e) {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException(this, e1, e2);
    }
  }
}
