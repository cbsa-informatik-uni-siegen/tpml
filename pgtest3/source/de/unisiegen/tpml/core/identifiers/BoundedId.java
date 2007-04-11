package de.unisiegen.tpml.core.identifiers ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * Interface for classes whose instances have one {@link Identifier} which
 * bounds other {@link Identifier}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface BoundedId
{
  /**
   * Returns a list of in this {@link Expression} bounded {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  public ArrayList < Identifier > getBoundedId ( ) ;


  /**
   * Returns the {@link Identifier} of this {@link Expression}.
   * 
   * @return The {@link Identifier} of this {@link Expression}.
   */
  public Identifier getId ( ) ;
}
