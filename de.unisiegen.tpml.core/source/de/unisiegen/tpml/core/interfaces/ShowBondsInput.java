package de.unisiegen.tpml.core.interfaces;


import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typeinference.TypeSubType;
import de.unisiegen.tpml.core.types.Type;


/**
 * Interface for {@link Expression}s, {@link Type}s, {@link TypeEquation}s
 * and {@link TypeSubType}s.
 * 
 * @author Christian Fehler
 * @version $Rev:1350 $
 */
public interface ShowBondsInput extends ExpressionOrType
{
  /*
   * Only for classes which can handle Expressions, Types,
   * TypeEquationTypeInferences and TypeSubTypes.
   */
}
