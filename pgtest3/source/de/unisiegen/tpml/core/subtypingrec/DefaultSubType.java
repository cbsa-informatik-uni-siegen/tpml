package de.unisiegen.tpml.core.subtypingrec;

import de.unisiegen.tpml.core.types.MonoType;

/**
 * 
 * The Default Subtype is needed for the subtyping algorithm. This object
 * contains a subtype and an overtype.
 *
 * @author Benjamin Mies
 *
 */
public class DefaultSubType {

	private MonoType subtype;

	private MonoType overtype;

	/**
	 * Allocates a new default subtype with the given types.
	 * @param type the subtype of this object
	 * @param type2 the overtype of this object
	 * 
	 */
	public DefaultSubType ( MonoType type, MonoType type2 ) {
		this.subtype = type;
		this.overtype = type2;
	}

	/**
	 * 
	 * Returns the overtype of this object
	 *
	 * @return the overtype of this object
	 */
	public MonoType getOvertype ( ) {
		return this.overtype;
	}

	/**
	 * 
	 * Returns the subtype of this object
	 *
	 * @return the subtype of this object
	 */
	public MonoType getSubtype ( ) {
		return this.subtype;
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

			return ( this.subtype.equals ( other.subtype ) && this.overtype
					.equals ( other.overtype ) );

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
		builder.append ( this.subtype );
		builder.append ( "<b><font color=\"#FF0000\">" ); //$NON-NLS-1$
		builder.append ( " &#60: " ); //$NON-NLS-1$
		builder.append ( "</font></b>" ); //$NON-NLS-1$
		builder.append ( this.overtype );
		return builder.toString ( );
	}

}
