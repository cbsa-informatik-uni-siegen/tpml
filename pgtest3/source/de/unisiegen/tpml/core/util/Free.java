package de.unisiegen.tpml.core.util ;


import java.util.Set ;


/**
 * TODO
 * 
 * @author Christian Fehler
 */
public abstract class Free
{
  /**
   * TODO
   * 
   * @param pOldIdentifier TODO
   * @param pNegativeSet TODO
   * @return TODO
   */
  public static String newIdentifier ( String pOldIdentifier ,
      Set < String > pNegativeSet )
  {
    String newIdentifier = pOldIdentifier ;
    while ( pNegativeSet.contains ( newIdentifier ) )
    {
      newIdentifier = newIdentifier + "'" ; //$NON-NLS-1$
    }
    return newIdentifier ;
  }
}
