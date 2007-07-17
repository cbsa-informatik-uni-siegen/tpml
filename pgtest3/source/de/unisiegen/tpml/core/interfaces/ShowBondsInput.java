package de.unisiegen.tpml.core.interfaces ;


import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference ;
import de.unisiegen.tpml.core.types.Type ;


/**
 * Interface for {@link Expression}s, {@link Type}s and
 * {@link TypeEquationTypeInference}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface ShowBondsInput extends
    ExpressionOrType
{
  /*
   * Only for classes which can handle Expressions, Types or
   * TypeEquationTypeInferences.
   */
}
