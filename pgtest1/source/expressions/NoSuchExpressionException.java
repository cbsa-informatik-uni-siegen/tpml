package expressions;

/**
 * Thrown if an attempt is made to access information about an
 * expression which does not exist or does not exist within a
 * given context.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class NoSuchExpressionException extends Exception {
  /**
   * Allocates a new <code>NoSuchExpressionException</code>
   * with the given <code>message</code>.
   * 
   * @param message detailed message about the exception.
   */
  NoSuchExpressionException(final String message) {
    super(message);
  }
  
  private static final long serialVersionUID = -3071041265908712642L;
}
