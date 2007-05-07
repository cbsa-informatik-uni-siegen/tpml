package de.unisiegen.tpml.core.exceptions ;


import de.unisiegen.tpml.core.Messages ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class NotOnlyFreeVariableException extends RuntimeException
{
  /**
   * TODO
   */
  private static final long serialVersionUID = - 8872896954392140509L ;


  /**
   * TODO
   */
  public NotOnlyFreeVariableException ( )
  {
    super ( Messages.getString ( "Exception.0" ) ) ; //$NON-NLS-1$
  }
}
