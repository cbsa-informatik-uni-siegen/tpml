package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.SeenTypes;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * The Type Proof Node for the Minimal Typing Algorithm.
 * Containing two types, and a list of alreday seen types.
 *
 * @author Benjamin Mies
 * 
 * @see AbstractMinimalTypingProofNode
 *
 */
public class DefaultMinimalTypingTypesProofNode extends AbstractMinimalTypingProofNode implements
		MinimalTypingTypesProofNode {
	
	/**
	 * List of all already seen types
	 */
	private SeenTypes < DefaultSubType > seenTypes;

	/**
	 * Subtype Object, containing the subtype and supertype
	 */
	private DefaultSubType subtype;

	/**
	 * Allocates a new <code>DefaultMinimalTypingTypesProofNode</code> with the specified <code>types</code>.
	 * 
	 * @param pType the left{@link MonoType} for this node.
	 * @param pType2 the right {@link MonoType} for this node.
	 * 
	 */
	public DefaultMinimalTypingTypesProofNode ( MonoType pType, MonoType pType2 ) {
		super ( new Unify ( ) );
		this.subtype = new DefaultSubType ( pType, pType2 );
		this.seenTypes = new SeenTypes < DefaultSubType > ( );

	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getType()
	 */
	public MonoType getType ( ) {
		return this.subtype.getLeft ( );
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.subtype.getRight ( );
	}

	//
	// Base methods
	//

	/**
	 * {@inheritDoc}
	 * 
	 * Mainly useful for debugging purposes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {
		StringBuilder builder = new StringBuilder ( );
		builder.append ( this.subtype.getLeft ( ) );
		builder.append ( " &#60: " ); //$NON-NLS-1$
		builder.append ( this.subtype.getRight ( ) );
		if ( getRule ( ) != null ) {
			builder.append ( " (" + getRule ( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return builder.toString ( );
	}

	/**
	 * {@inheritDoc} 
	 * 
	 * @see de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode#getSeenTypes()
	 */
	public SeenTypes < DefaultSubType > getSeenTypes ( ) {
		return this.seenTypes;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode#getSubType()
	 */
	public DefaultSubType getSubType ( ) {
		return this.subtype;
	}

	/**
	 * 
	 * There is no type environment needed for this node. So null is returned.
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getEnvironment()
	 */
	public DefaultTypeEnvironment getEnvironment ( ) {
		return null;
	}

}
