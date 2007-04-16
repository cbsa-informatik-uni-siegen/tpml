package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class SelfName extends Identifier
{
  /**
   * TODO
   */
  public SelfName ( )
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
