package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class AttributeName extends Identifier
{
  /**
   * TODO
   * 
   * @param pName TODO
   */
  public AttributeName ( final String pName )
  {
    super ( pName ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Attribute-Name" ; //$NON-NLS-1$
  }
}
