package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Exception to indicate that an error occurred during the subtyping algorithm, i.e. 
 * 
 * @author Benjamin Mies
 * 
 */
public final class SubTypingException extends Exception {

	//
	// Constants
	//

	/**
	 * The unique serialization id of this class.
	 */
	private static final long serialVersionUID = -8919516367132400570L;

	//
	// Attributes
	//

	/**
	 * The {@link TypeEquationTypeInference} that failed to unify.
	 * 
	 * @see #getTau1()
	 * @see #getTau2()
	 */
	private SubTypingProofNode node;

	//
	// Constructor (package)
	//

	/**
	 * Allocates a new {@link SubTypingException} object to indicate that the subtyping failed.
	 * @param message the reason for throwing this exception
	 * 
	 * @param pNode the {@link SubTypingProofNode} where the error occurs.
	 */
	public SubTypingException ( final String message,
			final SubTypingProofNode pNode ) {

		super ( message );
		this.node = pNode;
	}

	/**
	 * Allocates a new {@link SubTypingException} object to indicate that the subtyping failed.
	 * @param message the reason for throwing this exception
	 * 
	 * @param pNode the {@link RecSubTypingProofNode} where the error occurs.
	 */
	public SubTypingException ( final String message,
			@SuppressWarnings("unused")
			final RecSubTypingProofNode pNode ) {
		super ( message );
	}

	//
	// Accessors
	//

	/**
	 * Returns the type on the left side of the type equation that
	 * could not be unified.
	 * 
	 * @return the type on the left side of the type equation.
	 */
	public MonoType getType ( ) {

		return this.node.getType ( );
	}

	/**
	 * Returns the type on the right side of the type equation that
	 * could not be unified.
	 *  
	 * @return the type on the rightside of the type equation.
	 */
	public MonoType getType2 ( ) {

		return this.node.getType2 ( );
	}

}
