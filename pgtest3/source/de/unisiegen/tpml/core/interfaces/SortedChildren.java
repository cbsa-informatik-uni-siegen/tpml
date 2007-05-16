package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;


/**
 * Interface for classes whose instances have sorted {@link PrettyPrintable}s.
 * For example {@link Duplication}.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface SortedChildren
{
  /**
   * Returns the sorted sub {@link PrettyPrintable}s.
   * 
   * @return the sorted sub {@link PrettyPrintable}s.
   */
  public PrettyPrintable [ ] getSortedChildren ( ) ;
}
