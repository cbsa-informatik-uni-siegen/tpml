package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Duplication ;


/**
 * Interface for classes whose instances have sorted {@link ExpressionOrType}s.
 * For example {@link Duplication}.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface SortedChildren
{
  /**
   * Returns the sorted sub {@link ExpressionOrType}s.
   * 
   * @return the sorted sub {@link ExpressionOrType}s.
   */
  public ExpressionOrType [ ] getSortedChildren ( ) ;
}
