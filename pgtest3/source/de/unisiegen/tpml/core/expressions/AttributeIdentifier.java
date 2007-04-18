package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class AttributeIdentifier extends Identifier
{
  /**
   * TODO
   * 
   * @param pName TODO
   */
  public AttributeIdentifier ( String pName )
  {
    super ( pName ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public AttributeIdentifier clone ( )
  {
    return new AttributeIdentifier ( this.name ) ;
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
