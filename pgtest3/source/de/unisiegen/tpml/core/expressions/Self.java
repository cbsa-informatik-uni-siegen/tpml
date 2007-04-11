package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class Self extends Identifier
{
  /**
   * TODO
   */
  public Self ( )
  {
    super ( "self" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Self" ; //$NON-NLS-1$
  }
}
