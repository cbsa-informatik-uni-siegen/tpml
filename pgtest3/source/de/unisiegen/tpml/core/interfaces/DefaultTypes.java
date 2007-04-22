package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Interface for classes whose instances have multiple {@link Identifier}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultTypes
{
  /**
   * TODO
   * 
   * @return TODO
   */
  public MonoType [ ] getTypes ( ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( ) ;
}
