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
   * Returns the <code>pIndex</code>th {@link Identifier} of this
   * {@link Expression}.
   * 
   * @param pIndex The index of the {@link Identifier} to return.
   * @return The <code>pIndex</code>th {@link Identifier} of this
   *         {@link Expression}.
   */
  public Identifier getIdentifiers ( int pIndex ) ;


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getIdentifiersIndex ( ) ;
}
