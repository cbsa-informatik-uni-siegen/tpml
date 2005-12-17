package smallstep;

/**
 * A boolean constant.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
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
   * Returns the pretty string builder for constants.
   * @return the pretty string builder for constants.
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected final PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant(isTrue() ? "true" : "false");
    return builder;
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
