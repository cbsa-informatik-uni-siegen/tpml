package smallstep;

public class IntegerConstant extends smallstep.Constant {
  /**
   * Allocates a new integer constant with the given
   * <code>number</code>.
   * 
   * @param number the integer number.
   */
  public IntegerConstant(int number) {
    this.number = number;
  }
  
  /**
   * Returns the number represented by this integer constant.
   * @return the number represented by this integer constant.
   */
  public int getNumber() {
    return this.number;
  }
  
  /**
   * Returns the pretty printed string of the integer constant.
   * @return the pretty printed string of the integer constant.
   * @see smallstep.Expression#getPrettyPrintString()
   */
  @Override
  public String getPrettyPrintString() {
    return Integer.toString(this.number);
  }

  private int number;
}
