package de.unisiegen.tpml.core.interfaces ;


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
public interface BoundedIdentifiers extends DefaultIdentifiers
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
}
