package expressions;

public abstract class Value extends Expression {
  /**
   * Returns <code>true</code> as all classes derived
   * from {@link Value} are values.
   * 
   * @return always <code>true</code>.
   * 
   * @see expressions.Expression#isValue()
   */
  @Override
  public boolean isValue() {
    return true;
  }
}
