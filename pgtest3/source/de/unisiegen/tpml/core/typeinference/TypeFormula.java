package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * TypeFormula can be a <code>TypeEquation</code> or a <code>DefaultTypeJudgement</code>. 
 * TypeFormula are needed for unification
 * 
 * @author Benjamin Mies
 *
 */
public interface TypeFormula {

	//
	// Attributes
	//

	/**
	 * get the Expression of this type formula
	 * @return Expression
	 */
	public Expression getExpression();

	/**
	 * 
	 * get the environment of this type formula
	 *
	 * @return DefaultTypeEnvironment
	 */
	public DefaultTypeEnvironment getEnvironment();

	/**
	 * 
	 * get the type of this type formula
	 *
	 * @return MonoType
	 */
	public MonoType getType();

	/**
	 * 
	 * return a string with all attributes of this formula
	 *
	 * @return String
	 */
	public String toString();

	/**
	 * 
	 * substitute equation or type of this type formula
	 *
	 * @param s TypeSubstitution
	 * @return
	 */
	public TypeEquation substitute(TypeSubstitution s);

}
