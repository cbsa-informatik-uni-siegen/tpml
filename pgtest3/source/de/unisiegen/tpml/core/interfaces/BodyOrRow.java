package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Body ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;


/**
 * Interface for {@link Body}s and {@link Row}s. This interface is used in the
 * {@link Class} expression.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface BodyOrRow extends PrettyPrintable
{
  /*
   * Only for classes which can handle Bodies or Rows.
   */
}
