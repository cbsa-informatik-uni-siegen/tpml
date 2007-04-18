package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class VarIdentifier extends Identifier
{
  /**
   * TODO
   * 
   * @param pName TODO
   */
  public VarIdentifier ( String pName )
  {
    super ( pName ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public VarIdentifier clone ( )
  {
    return new VarIdentifier ( this.name ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Variable" ; //$NON-NLS-1$
  }
}
