package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class TypeSubstitutionList {

	//
	//Constants
	//

	/**
	 * empty type substitution list
	 */
	public static final TypeSubstitutionList EMPTY_LIST = new TypeSubstitutionList ( );

	//
	// Attributes
	//

	/**
	 * The first TypeSubstitution in the list.
	 */
	private DefaultTypeSubstitution first;

	/**
	 * The remaining equations or <code>null</code>.
	 */
	private TypeSubstitutionList remaining;

	//
	// Constructors (private)
	//

	/**
	 * Allocates a new, empty<code>TypeSubstitutionList</code> .
	 */
	private TypeSubstitutionList ( ) {

		super ( );
	}

	/**
	 * 
	 * Allocates a new <code>TypeSubstitutionList</code> .
	 *
	 * @param pFirst the first type substitution of the new list
	 * @param pRemaining	the remaining list of substitutions
	 */
	private TypeSubstitutionList ( final DefaultTypeSubstitution pFirst, final TypeSubstitutionList pRemaining ) {

		if ( pFirst == null ) {
			throw new NullPointerException ( "first is null" ); //$NON-NLS-1$
		}
		if ( pRemaining == null ) {
			throw new NullPointerException ( "remaining is null" ); //$NON-NLS-1$
		}
		this.first = pFirst;
		this.remaining = pRemaining;
	}

	//
	// Base Methods
	//

	/**
	 * needed for output and debug
	 *
	 * @return string with all type substitutions
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {

		final StringBuilder builder = new StringBuilder ( 128 );
		builder.append ( '{' );
		for ( TypeSubstitutionList list = this; list != EMPTY_LIST; list = list.remaining ) {
			if ( list != this ) {
				builder.append ( ", " ); //$NON-NLS-1$
			}
			builder.append ( list.first.toString ( ) );
		}
		builder.append ( '}' );
		return builder.toString ( );
	}

	/**
	 * 
	 * add a new type substitution to this list
	 *
	 * @param s DefaultTypeSubstitution to add
	 * @return extended list of type substitutions
	 */
	public TypeSubstitutionList extend ( final DefaultTypeSubstitution s ) {

		return new TypeSubstitutionList ( s, this );

	}

	/**
	 * 
	 * get the head of this type substitution list
	 *
	 * @return first element of this list
	 */
	public DefaultTypeSubstitution getFirst ( ) {

		return this.first;
	}

	/**
	 * get the tail of the type substitution list
	 *
	 * @return the remaing list of substitutions
	 */
	public TypeSubstitutionList getRemaining ( ) {
		return this.remaining;
	}
}