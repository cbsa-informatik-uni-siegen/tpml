package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Interface for classes whose instances have child {@link Type}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultTypes
{
  /**
   * Returns the sub {@link Type}s.
   * 
   * @return the sub {@link Type}s.
   */
  public MonoType [ ] getTypes ( ) ;


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( ) ;
}
