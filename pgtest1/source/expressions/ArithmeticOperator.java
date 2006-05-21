package expressions;

/**
 * Implementation of an arithmetic operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ArithmeticOperator extends BinaryOperator {
  //
  // Constants
  //
  
  /**
   * The arithmetic plus operator.
   */
  public static final ArithmeticOperator PLUS = new ArithmeticOperator("+", 3);
  
  /**
   * The arithmetic minus operator.
   */
  public static final ArithmeticOperator MINUS = new ArithmeticOperator("-", 3);
  
  /**
   * The arithmetic multiplication operator.
   */
  public static final ArithmeticOperator MULT = new ArithmeticOperator("*", 4);
  
  /**
   * The arithmetic modulo operator.
   */
  public static final ArithmeticOperator MOD = new ArithmeticOperator("mod", 4);
  
  /**
   * The arithmetic division operator.
   */
  public static final ArithmeticOperator DIV = new ArithmeticOperator("/", 4);
  
  
  
  //
  // Constructor (private)
  //

  /**
   * Allocates a new <code>ArithmeticOperator</code> instance with
   * the specified operator string representation <code>op</code>.
   * 
   * @param op the operator string representation.
   * @param prettyPriority the pretty print priority for 
   *                       {@link InfixOperation}s with this
   *                       arithmetic operator.
   * 
   * @see BinaryOperator#BinaryOperator(int)
   */
  private ArithmeticOperator(String op, int prettyPriority) {
    super(op, prettyPriority);
  }
  
  
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.BinaryOperator#applyTo(expressions.Expression, expressions.Expression)
   */
  @Override
  public Expression applyTo(Expression e1, Expression e2) throws BinaryOperatorException {
    try {
      // determine the integer values of the operands
      int n1 = ((IntegerConstant)e1).getNumber();
      int n2 = ((IntegerConstant)e2).getNumber();
      
      // try to perform the application
      if (this == PLUS) {
        return new IntegerConstant(n1 + n2);
      }
      else if (this == MINUS) {
        return new IntegerConstant(n1 - n2);
      }
      else if (this == MULT) {
        return new IntegerConstant(n1 * n2);
      }
      else if (this == DIV) {
        return new IntegerConstant(n1 / n2);
      }
      else if (this == MOD) {
        return new IntegerConstant(n1 % n2);
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
    catch (ArithmeticException e) {
      // internal Java operators throw this on divide-by-zero
      return Exn.DIVIDE_BY_ZERO;
    }
  }
}
