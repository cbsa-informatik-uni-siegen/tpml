package smallstep;

/**
 * Represents a logic operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class LogicOperator extends Operator {
  /**
   * Returns <code>true</code> if both <code>c1</code> and <code>c2</code>
   * are <code>BooleanConstant</code>s.
   * 
   * @param c1 the class of the first operand.
   * @param c2 the class of the second operand.
   * 
   * @return <code>true</code> if both <code>c1</code> and <code>c2</code>
   *         are <code>BooleanConstant</code>s.
   * 
   * @see smallstep.Operator#canApplyTo(java.lang.Class, java.lang.Class)
   */
  @Override
  public boolean canApplyTo(Class c1, Class c2) {
    return (c1 == BooleanConstant.class && c2 == BooleanConstant.class);
  }

  /**
   * Applies the logic operator to <code>c1</code> and <code>c2</code>.
   * 
   * @param c1 the first operand.
   * @param c2 the second operand.
   * 
   * @return the resulting expression.
   * 
   * @see smallstep.Operator#applyTo(smallstep.Constant, smallstep.Constant)
   */
  @Override
  public Expression applyTo(Constant c1, Constant c2) {
    // cast the operands to boolean constants
    BooleanConstant bc1 = (BooleanConstant)c1;
    BooleanConstant bc2 = (BooleanConstant)c2;
    
    if (this.op.equals("&&"))
      return (bc1.isTrue() && bc2.isTrue()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else
      return (bc1.isTrue() || bc2.isTrue()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
  }

  /**
   * Returns the pretty print priority of the logic operator.
   * 
   * @return the pretty print priority of the logic operator.
   * 
   * @see smallstep.Operator#getPrettyPriority()
   */
  @Override
  public int getPrettyPriority() {
    return 1;
  }
  
  /**
   * Returns the string representation of the operator.
   * 
   * @return the string representation of the operator.
   * 
   * @see smallstep.Expression#toString()
   */
  @Override
  public final String toString() {
    return this.op;
  }
  
  /**
   * The <b>(AND)</b> operator.
   */
  public static final LogicOperator AND = new LogicOperator("&&");
  
  /**
   * The <b>(OR)</b> operator.
   */
  public static final LogicOperator OR = new LogicOperator("||");
  
  private LogicOperator(String op) {
    this.op = op;
  }

  private String op;
}
