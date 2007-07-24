package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * A proof step represents an application of a {@link de.unisiegen.tpml.core.ProofRule} to an
 * {@link de.unisiegen.tpml.core.expressions.Expression}.
 *
 * @author Benedikt Meurer
 * @version $Rev: 471 $
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.ProofRule
 */
public final class ProofStep {
	//
	// Attributes
	//

	private MonoType type;

	private MonoType type2;

	/**
	 * The {@link ProofRule} that was applied to an {@link Expression} to advance in the proof.
	 * 
	 * @see #getRule()
	 */
	private ProofRule rule;

	//
	// Constructor
	//

	/**
	 * Allocates a new proof step with the given <code>expression</code> and the specified <code>rule</code>.
	 * @param type the first MonoType of the node
	 * @param type2 the second MonoType of the node
	 * @param rule the ProofRule apllied to the node
	 * 
	 * 
	 * @throws NullPointerException if <code>expression</code> or <code>rule</code> is <code>null</code>.
	 */
	public ProofStep ( MonoType type, MonoType type2, ProofRule rule ) {
		if ( rule == null ) {
			throw new NullPointerException ( "rule is null" ); //$NON-NLS-1$
		}
		if ( type == null ) {
			throw new NullPointerException ( "type is null" ); //$NON-NLS-1$
		}
		if ( type2 == null ) {
			throw new NullPointerException ( "type2 is null" ); //$NON-NLS-1$
		}
		this.type = type;
		this.type2 = type2;
		this.rule = rule;
	}

	//
	// Primitives
	//

	/**
	 * Returns the {@link ProofRule} that was applied to an
	 * {@link Expression} to advance in the proof.
	 * 
	 * @return the proof rule that was applied in this step.
	 * 
	 * @see #getExpression()
	 */
	public ProofRule getRule ( ) {
		return this.rule;
	}

	//
	// Base methods
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals ( Object obj ) {
		if ( obj instanceof ProofStep ) {
			ProofStep other = ( ProofStep ) obj;
			return ( this.type.equals ( other.type ) || this.type2.equals ( other.type2 ) || this.rule
					.equals ( other.rule ) );
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
		return this.type.hashCode ( ) + this.type2.hashCode ( ) + this.rule.hashCode ( );
	}

	/**
	 * 
	 * get the first type of the subtyping proof node
	 *
	 * @return type first MonoType of the node
	 */
	public MonoType getType ( ) {
		return this.type;
	}

	/**
	 * 
	 * get the second type of the subtyping proof node
	 *
	 * @return type2 second MonoType of the node
	 */
	public MonoType getType2 ( ) {
		return this.type2;
	}
}
