package smallstep;

import java.text.MessageFormat;
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
   * Returns the pretty print format for constants.
   * @return the pretty print format for constants.
   * @see smallstep.Expression#getPrettyPrintFormat()
   */
  @Override
  public MessageFormat getPrettyPrintFormat() {
    return new MessageFormat(toString());
  }
  
  /**
   * Returns the pretty print priority for constants.
   * @return the pretty print priority for constants.
   * @see smallstep.Expression#getPrettyPrintPriority()
   */
  @Override
  public int getPrettyPrintPriority() {
    return PRETTY_PRINT_PRIORITY;
  }
  
  /**
   * Returns an empty array, since constants have no subexpressions.
   * @return an empty array, since constants have no subexpressions.
   * @see smallstep.Expression#getSubExpressionPriorities()
   */
  @Override
  public int[] getSubExpressionPriorities() {
    return PRETTY_PRINT_PRIORITIES;
  }
  
  /**
   * Returns an empty array, since constants have no subexpressions.
   * @return an empty array, since constants have no subexpressions.
   * @see smallstep.Expression#getSubExpressions()
   */
  @Override
  public Expression[] getSubExpressions() {
    return Expression.EMPTY_ARRAY;
  }
  
  // pretty printer support
  private static final int PRETTY_PRINT_PRIORITIES[] = new int[0];
  private static final int PRETTY_PRINT_PRIORITY = 2;
}
