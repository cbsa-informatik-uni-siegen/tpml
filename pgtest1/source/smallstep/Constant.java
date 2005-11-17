package smallstep;

import java.util.Set;
import java.util.TreeSet;

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
    return new TreeSet<String>();
  }
  
  /**
   * Returns the pretty printer priority for the constant.
   * @return the pretty printer priority for the constant.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return 2;
  }
}
