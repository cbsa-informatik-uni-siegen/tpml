package expressions;

/**
 * Represents a relational operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class RelationalOperator extends Operator {
  /**
   * Checks if both <code>c1</code> and <code>c2</code> are
   * <code>IntegerConstant</code>s.
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
   * Performs the relational operation on <code>c1</code> and <code>c2</code>
   * and returns a boolean constant.
   * 
   * @param c1 the first operand.
   * @param c2 the second operand.
   * @return the result.
   * 
   * @see expressions.Operator#applyTo(expressions.Constant, expressions.Constant)
   */
  @Override
  public final Expression applyTo(Constant c1, Constant c2) {
    // cast the operands to integer constants
    IntegerConstant ic1 = (IntegerConstant)c1;
    IntegerConstant ic2 = (IntegerConstant)c2;
   
    if (this.op.equals("="))
      return (ic1.getNumber() == ic2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals("<"))
      return (ic1.getNumber() < ic2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals(">"))
      return (ic1.getNumber() > ic2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals("<="))
      return (ic1.getNumber() <= ic2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else {
      assert (this.op.equals(">="));
      
      return (ic1.getNumber() >= ic2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
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
    return 2;
  }

  /**
   * Returns the string representation of the operator.
   * @return the string representation of the operator.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.op;
  }

  /**
   * The <b>(EQUALS)</b> operator.
   */
  public static final RelationalOperator EQUALS = new RelationalOperator("=");
  
  /**
   * The <b>(LOWER-THAN)</b> operator.
   */
  public static final RelationalOperator LOWER_THAN = new RelationalOperator("<");
  
  /**
   * The <b>(GREATER-THAN)</b> operator.
   */
  public static final RelationalOperator GREATER_THAN = new RelationalOperator(">");
  
  /**
   * The <b>(LOWER-EQUAL)</b> operator.
   */
  public static final RelationalOperator LOWER_EQUAL = new RelationalOperator("<=");
  
  /**
   * The <b>(GREATER-EQUAL)</b> operator.
   */
  public static final RelationalOperator GREATER_EQUAL = new RelationalOperator(">=");
  
  private RelationalOperator(final String op) {
    this.op = op;
  }
  
  private String op;
}
