package de.unisiegen.tpml.core.subtypingrec;

import java.util.ArrayList;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.subtyping.ProofStep;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.subtyping.SubTypingProofRule;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.Debug;

/**
 * Default implementation of the <code>RecSubTypingProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel}.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode
 */
public class DefaultRecSubTypingProofNode extends AbstractProofNode implements
		RecSubTypingProofNode, SubTypingProofNode {

	private DefaultSubType type;

	private ArrayList < DefaultSubType > seenTypes;

	/**
	 * list of proof steps of this node
	 */
	private ProofStep[] steps = new ProofStep[0];

	/**
	 * Allocates a new proof step with the given <code>expression</code> and the specified <code>rule</code>.
	 * @param pType the first MonoType of this node
	 * @param pType2 the second MonoType of this node
	 * @param pSeenTypes list of all so far seen types
	 * 
	 */
	public DefaultRecSubTypingProofNode ( MonoType pType, MonoType pType2,
			ArrayList < DefaultSubType > pSeenTypes ) {
		type = new DefaultSubType ( pType, pType2 );
		seenTypes = pSeenTypes;
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
	public DefaultRecSubTypingProofNode getChildAt ( final int childIndex ) {

		return ( DefaultRecSubTypingProofNode ) super.getChildAt ( childIndex );
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
	 * 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSubType()
	 */
	public DefaultSubType getSubType ( ) {
		return this.type;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType()
	 */
	public MonoType getType ( ) {
		return this.type.getSubtype ( );
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.type.getOvertype ( );
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
			builder.append ( "<html>" ); //$NON-NLS-1$
			builder.append ( "[ A = " ); //$NON-NLS-1$
			for ( DefaultSubType subtype : seenTypes ) {
				builder.append ( "<b><font color=\"#0000FF\">(</font></b>" ); //$NON-NLS-1$ )
				builder.append ( " ( " + subtype + " ) " ); //$NON-NLS-1$ //$NON-NLS-2$
				builder.append ( "<b><font color=\"#0000FF\">)</font></b>" ); //$NON-NLS-1$ )
			}
			builder.append ( " ]" ); //$NON-NLS-1$
			builder.append ( "<br>" ); //$NON-NLS-1$
			String result = ""; //$NON-NLS-1$
			result += type.getSubtype ( );
			result = result.replaceAll ( "<", "&#60" ); //$NON-NLS-1$//$NON-NLS-2$
			builder.append ( result );
			result = ""; //$NON-NLS-1$
			builder.append ( "<b><font color=\"#FF0000\">" ); //$NON-NLS-1$
			builder.append ( " &#60: " ); //$NON-NLS-1$
			builder.append ( "</font></b>" ); //$NON-NLS-1$
			result += type.getOvertype ( );
			builder.append ( " " ); //$NON-NLS-1$
			if ( this.getSteps ( ).length > 0 )
				result += this.getSteps ( )[0].getRule ( ).toString ( );
			result = result.replaceAll ( "<", "&#60" ); //$NON-NLS-1$ //$NON-NLS-2$
			builder.append ( result );
			builder.append ( "</html>" ); //$NON-NLS-1$
		} else {
			builder.append ( "[ A = " ); //$NON-NLS-1$
			for ( DefaultSubType subtype : seenTypes ) {
				builder.append ( " ( " + subtype + " ) " ); //$NON-NLS-1$//$NON-NLS-2$
			}
			builder.append ( " ]" ); //$NON-NLS-1$
			builder.append ( "\n" ); //$NON-NLS-1$
			builder.append ( type.getSubtype ( ) );
			builder.append ( " <: " ); //$NON-NLS-1$
			builder.append ( type.getOvertype ( ) );
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
	 * @see de.unisiegen.tpml.core.subtyping.RecSubTypingProofNode#getRule()
	 */
	public SubTypingProofRule getRule ( ) {
		ProofStep[] steps = getSteps ( );
		if ( steps.length > 0 ) {
			return ( SubTypingProofRule ) steps[0].getRule ( );
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc} 
	 * 
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSeenTypes()
	 */
	public ArrayList < DefaultSubType > getSeenTypes ( ) {
		return this.seenTypes;
	}

}
