package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;

/**
 * This is a node with an type equation instead of an type environment,
 * an expression and a type, needed to apply the unify rule in the
 * type inference algorithm
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
public interface TypeEquationProofNode extends TypeCheckerProofNode {

	/**
	 * 
	 * Get the type equation of this node
	 *
	 * @return equation type equation of this node
	 */
	public TypeEquationTypeInference getEquation ( );

	/**
	 * 
	 * Get the actual choosen mode
	 *
	 * @return mode boolean - actual choosen mode
	 */
	public boolean getMode ( );
}
