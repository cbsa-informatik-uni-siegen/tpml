package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * DefaultTypeEquationProofNode is a presentation of a TypeCheckerProofNode
 * used for unification. This node owns a list of subtitutions and a list of
 * equations.
 * 
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeEquationProofNode extends DefaultTypeCheckerProofNode implements TypeCheckerProofNode {

	//
	// Attributes
	//
	
	/**
	 * list of collected type substitutions initialised with empty list
	 */
	private TypeSubstitutionList substitutions = TypeSubstitutionList.EMPTY_LIST;
	
	/**
	 * list of collected type equations
	 */
	private TypeEquation equation;
	
	//
	// Constructors
	//
	
	/**
	 * Allocates a new <code>DefaultTypeEquationProofNode</code> 
	 * 
	 * @param environment type environment of this node
	 * @param expression expression of this node
	 * @param type type of this node
	 * @param eqns equations of this node
	 * 
	 */
	public DefaultTypeEquationProofNode(TypeEnvironment environment, Expression expression, MonoType type, TypeEquation eqns) {
		super(environment, expression, type);
		equation = eqns;
	}
	
	/**
	 * 
	 * extend a new substitution to the list of substitutions for this node
	 *
	 * @param s1 type substitution to add to list
	 */
	public void addSubstitution(DefaultTypeSubstitution s1)
	{
		substitutions.extend(s1);
	}
	
	
	//
	// Accessors
	//
/**
 * get the type equation list of this node
 * @return equations TypeEquationList equations
 */
	public TypeEquation getEquation() {
		return this.equation;
	}

	/**
	 * 
	 * set a new type equation list
	 *
	 * @param equations type equation list to set as equations for this node
	
	public void setEquations(TypeEquationList equations) {
		this.equations = equations;
	}
	 */


	

}
