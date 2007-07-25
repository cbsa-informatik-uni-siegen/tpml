package de.unisiegen.tpml.core.typeinference;

import java.util.ArrayList;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * This type of type formula is needed to check subtype relations.
 *
 * @author Benjamin Mies
 *
 */
public class TypeSubType implements ShowBondsInput, TypeFormula, PrettyPrintable, PrettyPrintPriorities {

	//
	// Attributes
	//

	/**
	 * The left type (subtype) of this type formula
	 */
	private MonoType left;

	/**
	 * The right type (supertype) of this type formula
	 */
	private MonoType right;

	/**
	 * 
	 * Allocates a new <code>TypeSubType</code>
	 *
	 * @param pType the first type of this sub type node
	 * @param pType2 the second type of this sub type 
	 */
	public TypeSubType ( MonoType pType, MonoType pType2 ) {
		this.left = pType;
		this.right = pType2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getEnvironment()
	 */
	public DefaultTypeEnvironment getEnvironment ( ) {
		return new DefaultTypeEnvironment ( );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getExpression()
	 */
	public Expression getExpression ( ) {
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#getType()
	 */
	public MonoType getType ( ) {
		return this.left;
	}

	/**
	 * 
	 * Get the second type of this node.
	 *
	 * @return the second type of this node.
	 */
	public MonoType getType2 ( ) {
		return this.right;
	}

	/**
	 * 
	 * There is nothing to substitute. So the Object itself will be returned.
	 * 
	 * @return this unchanged object.
	 *
	 * @see de.unisiegen.tpml.core.typeinference.TypeFormula#substitute(java.util.ArrayList)
	 */
	public TypeSubType substitute ( @SuppressWarnings ( "unused" )
	ArrayList < TypeSubstitution > s ) {
		return this;
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
		return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) ).toPrettyString ( );
	}

	/**
	 * {@inheritDoc} Returns the string representation for the left equation,
	 * which is primarily useful for debugging.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {

		return this.left + " <: " + this.right; //$NON-NLS-1$ 
	}

	/**
	 * Returns the {@link PrettyStringBuilder}.
	 * 
	 * @param factory The {@link PrettyStringBuilderFactory}.
	 * @return The {@link PrettyStringBuilder}.
	 */
	private PrettyStringBuilder toPrettyStringBuilder ( PrettyStringBuilderFactory factory ) {
		PrettyStringBuilder builder = factory.newBuilder ( this, PRIO_EQUATION );
		builder.addBuilder ( this.left.toPrettyStringBuilder ( factory ), PRIO_EQUATION );
		builder.addText ( " <: " ); //$NON-NLS-1$
		builder.addBuilder ( this.right.toPrettyStringBuilder ( factory ), PRIO_EQUATION );
		return builder;
	}

}
