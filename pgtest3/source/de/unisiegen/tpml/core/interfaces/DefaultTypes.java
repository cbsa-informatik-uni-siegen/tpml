package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Interface for classes whose instances have multiple {@link Identifier}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultTypes
{
  /**
   * Prefix of tau {@link Type}s.
   */
  public static final String PREFIX_TAU = "\u03C4" ; //$NON-NLS-1$


  /**
   * Prefix of phi {@link Type}s.
   */
  public static final String PREFIX_PHI = "\u03A6" ; //$NON-NLS-1$


  /**
   * TODO
   * 
   * @return TODO
   */
  public MonoType [ ] getTypes ( ) ;


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   */
  public MonoType getTypes ( int pIndex ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public String [ ] getTypesPrefix ( ) ;
}
