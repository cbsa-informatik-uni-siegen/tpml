package de.unisiegen.tpml.core.typechecker;

import java.util.Set;
import java.util.TreeSet;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * Default implementation of the <code>TypeSubstitution</code> interface. This
 * is an internal class of the type checker. If you need to generate new
 * substitutions outside the generic type checker implementation, use the
 * {@link de.unisiegen.tpml.core.typechecker.TypeUtilities#newSubstitution(TypeVariable, MonoType)}
 * method instead.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1194 $
 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution
 */
public final class DefaultTypeSubstitution implements TypeSubstitution, PrettyPrintable {
	//
	// Constants
	//
	/**
	 * The empty type substitution, which does not contain any mappings.
	 * 
	 * @see #DefaultTypeSubstitution()
	 */
	public static final DefaultTypeSubstitution EMPTY_SUBSTITUTION = new DefaultTypeSubstitution ( );

	//
	// Attributes
	//
	/**
	 * The type variable at this level of the substitution.
	 */
	private TypeVariable tvar;

	/**
	 * The type to substitute for the <code>tvar</code>.
	 */
	private MonoType type;

	/**
	 * The remaining <code>(tvar,type)</code> pairs in the substitution.
	 */
	private DefaultTypeSubstitution parent;

	//
	// Constructors (package)
	//
	/**
	 * Allocates a new empty <code>DefaultTypeSubstitution</code>.
	 * 
	 * @see TypeSubstitution#EMPTY_SUBSTITUTION
	 */
	DefaultTypeSubstitution ( ) {
		super ( );
	}

	/**
	 * Convenience wrapper for the
	 * {@link #DefaultTypeSubstitution(TypeVariable, MonoType, DefaultTypeSubstitution)}
	 * constructor, which passes {@link #EMPTY_SUBSTITUTION} for the
	 * <code>parent</code> parameter.
	 * 
	 * @param tvar the type variable.
	 * @param type the (concrete) monomorphic type to substitute for
	 *          <code>tvar</code>.
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	public DefaultTypeSubstitution ( TypeVariable tvar, MonoType type ) {
		this ( tvar, type, EMPTY_SUBSTITUTION );
	}

	/**
	 * Allocates a new <code>DefaultTypeSubstitution</code> which represents a
	 * pair <code>(tvar,type)</code> and chains up to the specified
	 * <code>parent</code>.
	 * 
	 * @param tvar the type variable.
	 * @param type the (concrete) monomorphic type to substitute for
	 *          <code>tvar</code>.
	 * @param parent the parent substitution to chain up to.
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	DefaultTypeSubstitution ( TypeVariable tvar, MonoType type, DefaultTypeSubstitution parent ) {
		if ( tvar == null ) {
			throw new NullPointerException ( "tvar is null" ); //$NON-NLS-1$
		}
		if ( type == null ) {
			throw new NullPointerException ( "type is null" ); //$NON-NLS-1$
		}
		if ( parent == null ) {
			throw new NullPointerException ( "parent is null" ); //$NON-NLS-1$
		}
		this.tvar = tvar;
		this.type = type;
		this.parent = parent;
	}

	//
	// Primitives
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#compose(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
	 */
	public DefaultTypeSubstitution compose ( TypeSubstitution s ) {
		// if this is the empty substitution, the
		// result of the composition is simply s
		if ( this == EMPTY_SUBSTITUTION ) {
			return ( DefaultTypeSubstitution ) s;
		}
		// compose(parent, s)
		DefaultTypeSubstitution parentSubstitution = this.parent.compose ( s );
		// and prepend (name,type) pair
		return new DefaultTypeSubstitution ( this.tvar, this.type, parentSubstitution );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#free()
	 */
	public Set < TypeVariable > free ( ) {
		if ( this == EMPTY_SUBSTITUTION ) {
			return new TreeSet < TypeVariable > ( );
		}
		TreeSet < TypeVariable > free = new TreeSet < TypeVariable > ( );
		free.addAll ( this.type.getTypeVariablesFree ( ) );
		free.remove ( this.tvar );
		free.addAll ( this.parent.free ( ) );
		return free;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#get(de.unisiegen.tpml.core.types.TypeVariable)
	 */
	public MonoType get ( TypeVariable pTvar ) {
		if ( this == EMPTY_SUBSTITUTION ) {
			// reached the end of the substitution chain
			return pTvar;
		} else if ( this.tvar.equals ( pTvar ) ) {
			// we have a match here
			return this.type;
		} else {
			// check the parent substitution
			return this.parent.get ( pTvar );
		}
	}

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
		PrettyStringBuilder builder = factory.newBuilder ( this, 0 );
		builder.addBuilder ( this.type.toPrettyStringBuilder ( factory ), 0 );
		builder.addText ( "/" ); //$NON-NLS-1$
		builder.addBuilder ( this.tvar.toPrettyStringBuilder ( factory ), 0 );
		return builder;
	}

	/**
	 * {@inheritDoc} Mainly useful for debugging purposes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ( ) {
		return this.type.toString ( ) + "/" + this.tvar.toString ( ); //$NON-NLS-1$
	}
}
