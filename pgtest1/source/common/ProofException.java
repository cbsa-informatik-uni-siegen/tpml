package common;

/**
 * This exception is thrown when an error occurs
 * while proving attributes of an expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofException extends Exception {
  /**
   * The serial version id. 
   */
  private static final long serialVersionUID = -765882201403684253L;
  
  

  //
  // Constructor
  //
  
  /**
   * Allocates a new {@link ProofException} with the
   * specified <code>message</code>.
   * 
   * @param message the exception message.
   */
  public ProofException(String message) {
    super(message);
  }
  
  /**
   * Allocates a new {@link ProofException} with the specified
   * <code>message</code> and <code>exception</code>.
   * 
   * @param message the exception message.
   * @param exception the cause.
   */
  public ProofException(String message, Exception exception) {
    super(message, exception);
  }
}
