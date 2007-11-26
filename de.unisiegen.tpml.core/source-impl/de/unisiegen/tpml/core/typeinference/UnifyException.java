package de.unisiegen.tpml.core.typeinference;

import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.types.Type;

/**
 * Exception to indicate that an error occurred during the unification algorithm, i.e. that a type
 * equation could not be unified.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker#unify()
 */
public final class UnifyException extends Exception {

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
	private TypeEquationTypeInference equation;

	//
	// Constructor (package)
	//

	/**
	 * Allocates a new {@link UnifyException} object to indicate that the unification of the
	 * {@link TypeEquationTypeInference} <code>equationn</code> failed.
	 * 
	 * @param pEquation the {@link TypeEquationTypeInference} that could not be unified.
	 */
	public UnifyException ( final TypeEquationTypeInference pEquation ) {

		super ( MessageFormat.format (
				Messages.getString ( "UnificationException.0" ), pEquation.getLeft ( ), pEquation.getRight ( ) ) ); //$NON-NLS-1$
		this.equation = pEquation;
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
	public Type getTau1 ( ) {

		return this.equation.getLeft ( );
	}

	/**
	 * Returns the type on the right side of the type equation that
	 * could not be unified.
	 *  
	 * @return the type on the rightside of the type equation.
	 */
	public Type getTau2 ( ) {

		return this.equation.getRight ( );
	}
}
