package de.unisiegen.tpml.core.identifiers ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;


/**
 * Interface for classes whose instances have multiple {@link Identifier}s
 * which bounds other {@link Identifier}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface BoundedIdentifiers
{
  /**
   * Returns a list of lists of in this {@link Expression} bounded
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bounded
   *         {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getBoundedIdentifiers ( ) ;


  /**
   * Returns the <code>pIndex</code>th list of in this {@link Expression}
   * bounded {@link Identifier}s.
   * 
   * @param pIndex The index of the list of {@link Identifier}s to return.
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  public ArrayList < Identifier > getBoundedIdentifiers ( int pIndex ) ;


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
}
