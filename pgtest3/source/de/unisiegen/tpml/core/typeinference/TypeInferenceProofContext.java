package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * The proof context for the type inference. The proof context acts as an interface to the proof model
 * for the type rules.
 *
 * @author Benjammin Mies
 *
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel
 */
public interface TypeInferenceProofContext extends TypeCheckerProofContext {

	/**
	 * 
	 * Add a Substitution to the actual node
	 *
	 * @param s new TypeSubstitution
	 */
	public void addSubstitution(TypeSubstitution s);
	
	/**
	 * 
	 * Get all type substitutions added to this node so far
	 *
	 * @return substitutions ArrayList < DefaultTypeSubstitution > with all type substitutions 
	 */
	public ArrayList < DefaultTypeSubstitution >  getSubstitution();

	/**
	 * 
	 * Get all so far seen type equations of the actual node
	 *
	 * @return seenTypes SeenTypes < TypeEquationTypeInference > with all seen type equations
	 */
	public SeenTypes < TypeEquationTypeInference > getSeenTypes ( );

	/**
	 * 
	 * Add a type to the seen types of the actual node
	 *
	 * @param eqn the new type equation to add
	 */
	public void addSeenType ( TypeEquationTypeInference eqn );
	
	
}
