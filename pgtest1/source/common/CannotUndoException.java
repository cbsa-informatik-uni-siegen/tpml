package common;

/**
 * This exception is thrown whenever the user tries to invoke the
 * {@link common.ProofModel#undo()} method on a proof model, but
 * the change cannot be undo or the undo history is empty.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.CannotRedoException
 */
public class CannotUndoException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 320857065330799474L;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>CannotUndoException</code> with
   * the specified <code>message</code>.
   * 
   * @param message the error message text.
   */
  CannotUndoException(String message) {
    super(message);
  }
}
