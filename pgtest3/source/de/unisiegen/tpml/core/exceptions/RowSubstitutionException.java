package de.unisiegen.tpml.core.exceptions ;


import de.unisiegen.tpml.core.Messages ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public final class RowSubstitutionException extends RuntimeException
{
  /**
   * TODO
   */
  private static final long serialVersionUID = 4717084402322482294L ;


  /**
   * TODO
   */
  public RowSubstitutionException ( )
  {
    super ( Messages.getString ( "Exception.1" ) ) ; //$NON-NLS-1$
  }
}
