package expressions;


public class UnitConstant extends Constant {
  /**
   * Returns the pretty string builder for constants.
   * @return the pretty string builder for constants.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant("()");
    return builder;
  }
  
  /**
   * The <b>(UNIT)</b> value.
   */
  public static UnitConstant UNIT = new UnitConstant();
  
  private UnitConstant() {
  }
}
