package smallstep;


public class BooleanConstant extends Constant {
  /**
   * Returns <code>true</code> if the primitive value of
   * this boolean constant is <code>true</code>.
   * 
   * @return the primitive value of the boolean constant.
   */
  public final boolean isTrue() {
    return this.primitive;
  }

  /**
   * Returns the string representation of the boolean constant.
   * @return the string representation of the boolean constant.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return (isTrue() ? "true" : "false");
  }
  
  /**
   * The <b>(TRUE)</b> expression.
   */
  public static final BooleanConstant TRUE = new BooleanConstant(true);
  
  /**
   * The <b>(FALSE)</b> expression.
   */
  public static final BooleanConstant FALSE = new BooleanConstant(false);

  private BooleanConstant(boolean primitive) {
    this.primitive = primitive;
  }
  
  private boolean primitive;
}
