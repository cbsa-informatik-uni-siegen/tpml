package expressions;

/**
 * Implementations of an arithmetic operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class ArithmeticOperator extends Operator {
  /**
   * Checks whether both <code>c1</code> and <code>c2</code> are of type
   * <code>IntegerConstant</code>.
   * 
   * @param c1 the class of the first operand.
   * @param c2 the class of the second operand.
   * 
   * @return <code>true</code> if both <code>c1</code> and <code>c2</code>
   *         are <code>IntegerConstant</code>s.
   * 
   * @see expressions.Operator#canApplyTo(java.lang.Class, java.lang.Class)
   */
  @Override
  public boolean canApplyTo(Class c1, Class c2) {
    return (c1 == IntegerConstant.class && c2 == IntegerConstant.class);
  }
  
  /**
   * Performs the artihmetic operation on <code>c1</code> and <code>c2</code>.
   * 
   * @param v1 the first operand.
   * @param v2 the second operand.
   * @return the result.
   * 
   * @see expressions.Operator#applyTo(Value, Value)
   */
  @Override
  public Expression applyTo(Value v1, Value v2) {
    // cast operands to integer constants
    IntegerConstant ic1 = (IntegerConstant)v1;
    IntegerConstant ic2 = (IntegerConstant)v2;

    if (this.op.equals("+"))
      return new IntegerConstant(ic1.getNumber() + ic2.getNumber());
    else if (this.op.equals("-"))
      return new IntegerConstant(ic1.getNumber() - ic2.getNumber());
    else if (this.op.equals("*"))
      return new IntegerConstant(ic1.getNumber() * ic2.getNumber());
    else {
      assert (this.op.equals("mod") || this.op.equals("/"));
      
      // verify that the second operand is not 0
      if (ic2.getNumber() == 0)
        return Exn.DIVIDE_BY_ZERO;
      
      // perform the operation
      if (this.op.equals("mod"))
        return new IntegerConstant(ic1.getNumber() % ic2.getNumber());
      else
        return new IntegerConstant(ic1.getNumber() / ic2.getNumber());
    }
  }

  /**
   * Returns the base pretty print priority for this operator.
   * 
   * @return the base pretty print priority for this operator.
   * 
   * @see expressions.Operator#getPrettyPriority()
   */
  @Override
  public int getPrettyPriority() {
    if (this.op.equals("+") || this.op.equals("-"))
      return 3;
    else
      return 4;
  }
  
  /**
   * Returns the string representation for an arithmetic operator.
   * @return the string representation for an arithmetic operator.
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return this.op;
  }
  
  /**
   * The <b>(PLUS)</b> operator.
   */
  public static final ArithmeticOperator PLUS = new ArithmeticOperator("+");
  
  /**
   * The <b>(MINUS)</b> operator.
   */
  public static final ArithmeticOperator MINUS = new ArithmeticOperator("-");
  
  /**
   * The <b>(MULT)</b> operator.
   */
  public static final ArithmeticOperator MULT = new ArithmeticOperator("*");
  
  /**
   * The <b>(MOD)</b> operator.
   */
  public static final ArithmeticOperator MOD = new ArithmeticOperator("mod");
  
  /**
   * The <b>(DIV)</b> operator.
   */
  public static final ArithmeticOperator DIV = new ArithmeticOperator("/");
  
  private ArithmeticOperator(final String op) {
    this.op = op;
  }
  
  private String op;
}
