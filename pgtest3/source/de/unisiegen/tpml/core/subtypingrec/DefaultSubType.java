package de.unisiegen.tpml.core.subtypingrec;

import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typeinference.PrettyPrintPriorities;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * The Default Subtype is needed for the subtyping algorithm. This object
 * contains a left and an right.
 *
 * @author Benjamin Mies
 *
 */
public class DefaultSubType implements PrettyPrintable, PrettyPrintPriorities {
	
	/**
	 * The left type (subtype) of this subtype object
	 */
	private MonoType left;

	/**
	 * The right type (supertype) of this subtype object
	 */
	private MonoType right;

	/**
	 * Allocates a new default left with the given types.
	 * @param pLeft the left of this object
	 * @param pRight the right of this object
	 * 
	 */
	public DefaultSubType ( MonoType pLeft, MonoType pRight ) {
		this.left = pLeft;
		this.right = pRight;
	}

	/**
	 * 
	 * Returns the right type of this object
	 *
	 * @return the right (type) overtype of this object
	 */
	public MonoType getRight ( ) {
		return this.right;
	}

	/**
	 * 
	 * Returns the left type of this object
	 *
	 * @return the left type (subtype) of this object
	 */
	public MonoType getLeft ( ) {
		return this.left;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals ( Object pObject ) {
		if ( pObject instanceof DefaultSubType ) {
			DefaultSubType other = ( DefaultSubType ) pObject;

			return ( this.left.equals ( other.left ) && this.right.equals ( other.right ) );

		}
		return false;
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
		builder.append ( "<b><font color=\"#FF0000\">" ); //$NON-NLS-1$
		builder.append ( " &#60: " ); //$NON-NLS-1$
		builder.append ( "</font></b>" ); //$NON-NLS-1$
		builder.append ( this.right );
		return builder.toString ( );
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
		builder.addBuilder ( this.right.toPrettyStringBuilder ( factory ), PRIO_EQUATION );
		builder.addText ( " = " ); //$NON-NLS-1$
		builder.addBuilder ( this.left.toPrettyStringBuilder ( factory ), PRIO_EQUATION );
		return builder;
	}

}
