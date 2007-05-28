package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeName ;


/**
 * Interface for classes whose instances have child {@link TypeName}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultTypeNames
{
  /**
   * Returns the {@link TypeName}s of this {@link Type}.
   * 
   * @return The {@link TypeName}s of this {@link Type}.
   */
  public TypeName [ ] getTypeNames ( ) ;


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypeNamesIndex ( ) ;
}
