package smallstep;

public class BooleanConstant extends Constant {
  /**
   * Allocates a new boolean constant that corresponds
   * to the given <code>primitiv</code> value.
   * 
   * @param primitive the primitive boolean value.
   */
  public BooleanConstant(boolean primitive) {
    this.primitive = primitive;
  }
  
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
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return isTrue() ? "true" : "false";
  }

  private boolean primitive;
}
