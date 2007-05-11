package de.unisiegen.tpml.core.subtyping;

import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.typeinference.TypeEquation;
import de.unisiegen.tpml.core.typeinference.UnifyException;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.Type;

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
	 * The {@link TypeEquation} that failed to unify.
	 * 
	 * @see #getTau1()
	 * @see #getTau2()
	 */
	private SubTypingProofNode node;

	//
	// Constructor (package)
	//

	/**
	 * Allocates a new {@link UnifyException} object to indicate that the unification of the
	 * {@link TypeEquation} <code>equationn</code> failed.
	 * 
	 * @param equation the {@link TypeEquation} that could not be unified.
	 */
	public SubTypingException ( final SubTypingProofNode pNode ) {

		super ( MessageFormat.format ( Messages
				.getString ( "SubTypingException.0" ), pNode ) ); //$NON-NLS-1$
		this.node = pNode;
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
