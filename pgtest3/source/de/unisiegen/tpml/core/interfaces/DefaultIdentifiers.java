package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * Interface for classes whose instances have multiple {@link Identifier}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface DefaultIdentifiers
{
  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiers ( ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getIdentifiersIndex ( ) ;
}
