package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typeinference.PrettyPrintPriorities;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Default implementation of the <code>SubTypingProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.subtyping.SubTypingProofModel}.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode
 */
public class DefaultSubTypingProofNode extends AbstractProofNode implements SubTypingProofNode, PrettyPrintPriorities {

	/**
	 * The subtype of this proof node
	 */
	private MonoType left;

	/**
	 * The supertype of this proof node
	 */
	private MonoType right;

	/**
	 * list of proof steps of this node
	 */
	private ProofStep[] steps = new ProofStep[0];

	/**
	 * Allocates a new proof node with the given types.
	 * @param pLeft the first MonoType of this node
	 * @param pRight the second MonoType of this node
	 * 
	 */
	public DefaultSubTypingProofNode ( MonoType pLeft, MonoType pRight ) {
		this.left = pLeft;
		this.right = pRight;
	}

	/**
	 * {@inheritDoc}de.unisiegen.tpml.core.ProofNode#isProven()
	 * 
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#isProven()
	 */
	public boolean isProven ( ) {
		return ( getSteps ( ).length > 0 );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#isFinished()
	 */
	public boolean isFinished ( ) {

		if ( !isProven ( ) ) {
			return false;
		}
		for ( int n = 0; n < getChildCount ( ); ++n ) {
			if ( ! ( getChildAt ( n ) ).isFinished ( ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getChildAt(int)
	 */
	@Override
	public DefaultSubTypingProofNode getChildAt ( final int childIndex ) {

		return ( DefaultSubTypingProofNode ) super.getChildAt ( childIndex );
	}

	/**
	 * get the proof steps of this node
	 * @return ProofStep[] steps
	 */
	public ProofStep[] getSteps ( ) {

		return this.steps;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType()
	 */
	public MonoType getType ( ) {
		return this.left;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.right;
	}

	/**
	 * 
	 * 	
	 * get the proof steps of this node
	 *
	 * @param pSteps new proof steps for this node
	 */
	public void setSteps ( ProofStep[] pSteps ) {
		this.steps = pSteps;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Mainly useful for debugging purposes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {

		final StringBuilder builder = new StringBuilder ( );

			builder.append ( this.left );
			builder.append ( " <: " ); //$NON-NLS-1$
			builder.append ( this.right );
			builder.append ( " " ); //$NON-NLS-1$
			if ( this.getSteps ( ).length > 0 )
				builder.append ( this.getSteps ( )[0].getRule ( ).toString ( ) );

		return builder.toString ( );

	}

	/**
	 * 
	 * {inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getRule()
	 */
	public SubTypingProofRule getRule ( ) {
		ProofStep[] proofSteps = getSteps ( );
		if ( proofSteps.length > 0 )
			return ( SubTypingProofRule ) proofSteps[0].getRule ( );
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
	 */
	@Override
	public DefaultSubTypingProofNode getLastLeaf ( ) {
		return ( DefaultSubTypingProofNode ) super.getLastLeaf ( );
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
