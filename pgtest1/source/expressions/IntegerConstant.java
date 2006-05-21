package expressions;

/**
 * Represents integer constants.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class IntegerConstant extends expressions.Constant {
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
   * Returns the pretty string builder for constants.
   * @return the pretty string builder for constants.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant(Integer.toString(this.number));
    return builder;
  }

  private int number;
}
