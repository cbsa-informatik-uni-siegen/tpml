package smallstep;

public class UnitConstant extends Constant {
  /**
   * Returns the pretty print string of the <b>(UNIT)</b> constant.
   * @return the pretty print string of the <b>(UNIT)</b> constant.
   * @see smallstep.Expression#getPrettyPrintString()
   */
  @Override
  public String getPrettyPrintString() {
    return "()";
  }
  
  /**
   * The <b>(UNIT)</b> value.
   */
  public static UnitConstant UNIT = new UnitConstant();
  
  private UnitConstant() {
  }
}
