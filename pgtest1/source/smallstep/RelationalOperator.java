package smallstep;

/**
 * Represents a relational operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class RelationalOperator extends Operator {
  /**
   * Performs the relational operation on <code>c1</code> and <code>c2</code>
   * and returns a boolean constant.
   * 
   * @param c1 the first operand.
   * @param c2 the second operand.
   * @return the result.
   * 
   * @see smallstep.Operator#applyTo(smallstep.IntegerConstant, smallstep.IntegerConstant)
   */
  @Override
  public final Expression applyTo(IntegerConstant c1, IntegerConstant c2) {
    assert (c1 != null);
    assert (c2 != null);
   
    if (this.op.equals("="))
      return (c1.getNumber() == c2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals("<"))
      return (c1.getNumber() < c2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals(">"))
      return (c1.getNumber() > c2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else if (this.op.equals("<="))
      return (c1.getNumber() <= c2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    else {
      assert (this.op.equals(">="));
      
      return (c1.getNumber() >= c2.getNumber()) ? BooleanConstant.TRUE : BooleanConstant.FALSE;
    }
  }
  
  /**
   * Returns the base pretty print priority for this operator.
   * 
   * @return the base pretty print priority for this operator.
   * 
   * @see smallstep.Operator#getPrettyPriority()
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
