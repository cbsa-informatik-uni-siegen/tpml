package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.UnitType;

/**
 * Represents a type equation. Used for the unification algorithm.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationListTypeChecker
 */
public final class TypeEquationTypeInference implements TypeFormula, PrettyPrintable,
		PrettyPrintPriorities {

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
	public TypeEquationTypeInference ( final MonoType left, final MonoType right ) {

		if ( left == null ) {
			throw new NullPointerException ( "left is null" ); //$NON-NLS-1$
		}
		if ( right == null ) {
			throw new NullPointerException ( "right is null" ); //$NON-NLS-1$
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
	public MonoType getLeft ( ) {

		return this.left;
	}

	/**
	 * Returns the monomorphic type on the right side.
	 * 
	 * @return the right side type.
	 */
	public MonoType getRight ( ) {

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
	 * @return the resulting {@link TypeEquationTypeInference}.
	 * 
	 * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
	 */
	public TypeFormula substitute (
			ArrayList < DefaultTypeSubstitution > substitutions ) {

		TypeEquationTypeInference newEqn = this.clone ( );

		for ( TypeSubstitution s : substitutions ) {
			newEqn.setLeft ( newEqn.getLeft ( ).substitute ( s ) );
			newEqn.setRight ( newEqn.getRight ( ).substitute ( s ) );
		}

		// apply the substitution to the left and the right side
		return newEqn;
	}

	//
	// Base methods
	//
	/**
	 * Clones this type equation, so that the result is an type equation equal to this
	 * type equation.
	 *
	 * @return a clone of this object.
	 * @see Object#clone()
	 */
	public TypeEquationTypeInference clone ( ) {
		return new TypeEquationTypeInference ( this.left, this.right );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the string representation for the type equation, which is primarily useful for debugging.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {

		return ( this.left + " = " + this.right ); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals ( final Object obj ) {

		if ( obj instanceof TypeEquationTypeInference ) {
			final TypeEquationTypeInference other = ( TypeEquationTypeInference ) obj;
			return ( this.left.equals ( other.left ) && this.right
					.equals ( other.right ) );
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ( ) {

		return this.left.hashCode ( ) + this.right.hashCode ( );
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getEnvironment()
	 */
	public DefaultTypeEnvironment getEnvironment ( ) {

		return new DefaultTypeEnvironment ( );
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
	 */
	public Expression getExpression ( ) {

		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getType()
	 */
	public MonoType getType ( ) {

		return new UnitType ( );
	}

	//
	// Pretty printing
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
	 */
	public final PrettyString toPrettyString ( ) {
		return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
				.toPrettyString ( );
	}

	PrettyStringBuilder toPrettyStringBuilder ( PrettyStringBuilderFactory factory ) {
		PrettyStringBuilder builder = factory.newBuilder ( this, PRIO_EQUATION );
		builder.addBuilder ( left.toPrettyStringBuilder ( factory ), PRIO_EQUATION );
		builder.addText ( " = " ); //$NON-NLS-1$
		builder
				.addBuilder ( right.toPrettyStringBuilder ( factory ), PRIO_EQUATION );

		return builder;
	}

	/**
	 * 
	 * set the left type of this type equation
	 *
	 * @param left MonoType to set as left type
	 */
	public void setLeft ( MonoType left ) {
		this.left = left;
	}

	/**
	 * 
	 * set the right type of this type equation
	 *
	 * @param right MonoType to set as right type
	 */
	public void setRight ( MonoType right ) {
		this.right = right;
	}

}
