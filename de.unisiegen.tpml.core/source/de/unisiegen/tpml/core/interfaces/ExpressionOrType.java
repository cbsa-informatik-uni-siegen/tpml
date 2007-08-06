package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Interface for {@link Expression}s and {@link Type}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface ExpressionOrType extends PrettyPrintable
{
  /*
   * Only for classes which can handle Expressions or Types.
   */
}
