package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.expressions.Unify;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * This nodes with two types are needed in the type checker algorithm to check
 * subtype relations between this two given types.
 *
 * @author Benjamin Mies
 *
 */
public class DefaultTypeCheckerTypeProofNode extends AbstractTypeCheckerProofNode implements TypeCheckerTypeProofNode {

	//
	// Attributes
	//

	private MonoType type2;

	/**
	 * 
	 * Allocates a new <code>DefaultTypeCheckerTypeProofNode</code>
	 *
	 * @param pType the first type of this type checker proof node
	 * @param pType2 the second type of this type checker proof node
	 */
	public DefaultTypeCheckerTypeProofNode ( MonoType pType, MonoType pType2 ) {
		super ( new Unify ( ) );
		this.type = pType;
		this.type2 = pType2;
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerTypeProofNode#getType2()
	 */
	public MonoType getType2 ( ) {
		return this.type2;
	}

	/*	  public DefaultTypeCheckerTypeProofNode getChildAt(int childIndex){
	 return (DefaultTypeCheckerTypeProofNode) super.getChildAt ( childIndex );
	 }*/

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
		builder.append ( this.type );
		builder.append ( " <: " ); //$NON-NLS-1$
		builder.append ( this.type2 );
		if ( getRule ( ) != null ) {
			builder.append ( " (" + getRule ( ) + ")" ); //$NON-NLS-1$//$NON-NLS-2$
		}
		return builder.toString ( );
	}

}
