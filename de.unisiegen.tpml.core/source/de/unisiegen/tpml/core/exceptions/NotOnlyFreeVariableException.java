package de.unisiegen.tpml.core.exceptions ;


import de.unisiegen.tpml.core.Messages ;


/**
 * This <code>RuntimeException</code> is thrown if the substitution is not
 * defined because of free attribute- or self-identifiers.
 * 
 * @author Christian Fehler
 */
public final class NotOnlyFreeVariableException extends RuntimeException
{
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = - 8872896954392140509L ;


  /**
   * Initializes the exception.
   */
  public NotOnlyFreeVariableException ( )
  {
    super ( Messages.getString ( "Exception.0" ) ) ; //$NON-NLS-1$
  }
}
