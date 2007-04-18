package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class SelfIdentifier extends Identifier
{
  /**
   * TODO
   */
  public SelfIdentifier ( )
  {
    super ( "self" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public SelfIdentifier clone ( )
  {
    return new SelfIdentifier ( ) ;
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
