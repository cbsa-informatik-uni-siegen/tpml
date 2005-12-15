package smallstep;

/**
 * Implementations of an arithmetic operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class ArithmeticOperator extends Operator {
  /**
   * Performs the artihmetic operation on <code>c1</code> and <code>c2</code>.
   * 
   * @param c1 the first operand.
   * @param c2 the second operand.
   * @return the result.
   * 
   * @see smallstep.Operator#applyTo(smallstep.IntegerConstant, smallstep.IntegerConstant)
   */
  @Override
  public Expression applyTo(IntegerConstant c1, IntegerConstant c2) {
    assert (c1 != null);
    assert (c2 != null);

    if (this.op.equals("+"))
      return new IntegerConstant(c1.getNumber() + c2.getNumber());
    else if (this.op.equals("-"))
      return new IntegerConstant(c1.getNumber() - c2.getNumber());
    else if (this.op.equals("*"))
      return new IntegerConstant(c1.getNumber() * c2.getNumber());
    else {
      assert (this.op.equals("mod") || this.op.equals("/"));
      
      // verify that the second operand is not 0
      if (c2.getNumber() == 0)
        return Exn.DIVIDE_BY_ZERO;
      
      // perform the operation
      if (this.op.equals("mod"))
        return new IntegerConstant(c1.getNumber() % c2.getNumber());
      else
        return new IntegerConstant(c1.getNumber() / c2.getNumber());
    }
  }

  /**
   * Returns the string representation for an arithmetic operator.
   * @return the string representation for an arithmetic operator.
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return "(" + this.op + ")";
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
