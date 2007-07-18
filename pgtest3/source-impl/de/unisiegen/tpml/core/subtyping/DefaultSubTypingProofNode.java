package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.Debug;

/**
 * Default implementation of the <code>SubTypingProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.subtyping.SubTypingProofModel}.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode
 */
public class DefaultSubTypingProofNode extends AbstractProofNode implements
		SubTypingProofNode {

	private MonoType type;

	private MonoType type2;

	/**
	 * list of proof steps of this node
	 */
	private ProofStep[] steps = new ProofStep[0];

	/**
	 * Allocates a new proof node with the given types.
	 * @param pType the first MonoType of this node
	 * @param pType2 the second MonoType of this node
	 * 
	 */
	public DefaultSubTypingProofNode ( MonoType pType, MonoType pType2 ) {
		this.type = pType;
		this.type2 = pType2;
	}

	/**
	 * inherit Doc de.unisiegen.tpml.core.ProofNode#isProven()
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
		return this.type;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.type2;
	}

	/**
	 * 
	 * 	
	 * get the proof steps of this node
	 *
	 * @param steps new proof steps for this node
	 */
	public void setSteps ( ProofStep[] steps ) {
		this.steps = steps;
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

		if ( Debug.isUserName ( Debug.BENJAMIN ) ) {
			String result = ""; //$NON-NLS-1$
			builder.append ( "<html>" ); //$NON-NLS-1$
			result += this.type;
			result = result.replaceAll ( "<", "&#60" ); //$NON-NLS-1$//$NON-NLS-2$
			builder.append ( result );
			result = ""; //$NON-NLS-1$
			builder.append ( "<b><font color=\"#FF0000\">" ); //$NON-NLS-1$
			builder.append ( " &#60: " ); //$NON-NLS-1$
			builder.append ( "</font></b>" ); //$NON-NLS-1$
			result += this.type2;
			if ( this.getSteps ( ).length > 0 )
				result += this.getSteps ( )[0].getRule ( ).toString ( );
			result = result.replaceAll ( "<", "&#60" ); //$NON-NLS-1$ //$NON-NLS-2$
			builder.append ( result );
			builder.append ( "</html>" ); //$NON-NLS-1$
		} else {
			builder.append ( this.type );
			builder.append ( " <: " ); //$NON-NLS-1$
			builder.append ( this.type2 );
			builder.append ( " " ); //$NON-NLS-1$
			if ( this.getSteps ( ).length > 0 )
				builder.append ( this.getSteps ( )[0].getRule ( ).toString ( ) );
		}

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

	public DefaultSubTypingProofNode getLastLeaf ( ) {
		return (DefaultSubTypingProofNode) super.getLastLeaf ( );
	}

	public PrettyString toPrettyString ( ) {
		// TODO Auto-generated method stub
		return null;
	}

}
