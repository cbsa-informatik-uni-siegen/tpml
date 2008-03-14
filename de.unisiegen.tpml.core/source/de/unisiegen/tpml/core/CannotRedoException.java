package de.unisiegen.tpml.core;


/**
 * This exception is thrown whenever the users tries to invoke the
 * {@link de.unisiegen.tpml.core.ProofModel#redo()} method, but the change
 * cannot be redone or the redo history is empty.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.CannotUndoException
 */
public final class CannotRedoException extends Exception
{

  //
  // Constants
  //

  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 3623984004272978001L;


  //
  // Constructor (package)
  //

  /**
   * Allocates a new <code>CannotRedoException</code> with the specified error
   * <code>message</code>.
   * 
   * @param message the error message text.
   */
  CannotRedoException ( String message )
  {
    super ( message );
  }
}
