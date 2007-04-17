package de.unisiegen.tpml.core.expressions ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public class VarName extends Identifier
{
  /**
   * TODO
   * 
   * @param pName TODO
   */
  public VarName ( final String pName )
  {
    super ( pName ) ;
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
