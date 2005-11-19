package smallstep;


public class UnitConstant extends Constant {
  /**
   * Returns the string representation of the unit constant.
   * @return the string representation of the unit constant.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "()";
  }
  
  /**
   * The <b>(UNIT)</b> value.
   */
  public static UnitConstant UNIT = new UnitConstant();
  
  private UnitConstant() {
  }
}
