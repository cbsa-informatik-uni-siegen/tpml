package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * Represents a type equation. Used for the unification algorithm.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationList
 */
public final class TypeEquation implements TypeFormula {

	//
	// Attributes
	//

	/**
	 * The monomorphic type on the left side.
	 * 
	 * @see #getLeft()
	 */
	private MonoType left;

	/**
	 * The monomorphic type on the right side.
	 * 
	 * @see #getRight()
	 */
	private MonoType right;

	//
	// Constructor (package)
	//

	/**
	 * Allocates a new <code>TypeEquation</code> with the given <code>left</code> and <code>right</code>
	 * side types.
	 * 
	 * @param left the monomorphic type on the left side.
	 * @param right the monomorphic type on the right side.
	 * 
	 * @throws NullPointerException if <code>left</code> or <code>right</code> is <code>null</code>.
	 */
	public TypeEquation(final MonoType left, final MonoType right) {

		if (left == null) {
			throw new NullPointerException("left is null"); //$NON-NLS-1$
		}
		if (right == null) {
			throw new NullPointerException("right is null"); //$NON-NLS-1$
		}
		this.left = left;
		this.right = right;
	}

	//
	// Accessors
	//

	/**
	 * Returns the monomorphic type on the left side.
	 * 
	 * @return the left side type.
	 */
	public MonoType getLeft() {

		return this.left;
	}

	/**
	 * Returns the monomorphic type on the right side.
	 * 
	 * @return the right side type.
	 */
	public MonoType getRight() {

		return this.right;
	}

	//
	// Primitives
	//

	/**
	 * Applies the {@link TypeSubstitution} <code>s</code> to the types on both sides of the equation and
	 * returns the resulting equation.
	 * 
	 * @param s the {@link TypeSubstitution} to apply.
	 * 
	 * @return the resulting {@link TypeEquation}.
	 * 
	 * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
	 */
	public TypeEquation substitute(final TypeSubstitution s) {

		// apply the substitution to the left and the right side
		return new TypeEquation(this.left.substitute(s), this.right.substitute(s));
	}

	//
	// Base methods
	//

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the string representation for the type equation, which is primarily useful for debugging.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return (this.left + " = " + this.right); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof TypeEquation) {
			final TypeEquation other = (TypeEquation) obj;
			return (this.left.equals(other.left) && this.right.equals(other.right));
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return this.left.hashCode() + this.right.hashCode();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getEnvironment()
	 */
	public DefaultTypeEnvironment getEnvironment() {

		return new DefaultTypeEnvironment();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
	 */
	public Expression getExpression() {

		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getType()
	 */
	public MonoType getType() {

		return new UnitType();
	}

}
