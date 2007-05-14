package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
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
	 * TODO
	 *
	 */
	public DefaultSubTypingProofNode (MonoType pType, MonoType pType2 ) {
		type = pType;
		type2 = pType2;
	}

	/**
	 * inherit Doc de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven ( ) {
		return ( getSteps ( ).length > 0 );
	}
	
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

	public MonoType getType ( ) {
		return this.type;
	}

	public MonoType getType2 ( ) {
		return this.type2;
	}

	public void setSteps ( ProofStep[] steps ) {
		this.steps = steps;
	}
	
	@Override
	public String toString ( ) {

		final StringBuilder builder = new StringBuilder ( );
		//builder.append ( "<html>" ); //$NON-NLS-1$
		builder.append ( type );
		builder.append ( " < " ); //$NON-NLS-1$
		builder.append ( type2 );
		builder.append (" ");
		if (this.getSteps ( ).length > 0)
		builder.append ( this.getSteps()[0].getRule ( ).toString ( ) );
		//builder.append ( "</html>" ); //$NON-NLS-1$
		return builder.toString ( );
	}

}
