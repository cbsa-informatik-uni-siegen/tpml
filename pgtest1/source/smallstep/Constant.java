package smallstep;

import java.util.Set;

/**
 * Abstract class to represent a constant expression
 * (only values can be constants).
 *
 * @author bmeurer
 * @version $Id$
 */
public abstract class Constant extends Value {
  /**
   * Returns the empty set, as constants
   * don't have any free identifiers.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return Expression.EMPTY_SET;
  }
  
  /**
   * Returns the pretty string builder for constants.
   * @return the pretty string builder for constants.
   * @see smallstep.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant(toString());
    return builder;
  }
}
