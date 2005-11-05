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
   * @return the number represented by this integer constant-
   */
  public int getNumber() {
    return this.number;
  }
  
  /**
   * Returns the string representation of the integer value.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return Integer.toString(this.number);
  }

  private int number;
}
