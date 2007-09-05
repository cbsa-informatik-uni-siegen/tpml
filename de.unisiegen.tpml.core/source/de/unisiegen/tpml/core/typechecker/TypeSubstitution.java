package de.unisiegen.tpml.core.typechecker;

import java.util.Set;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * Instances of this class are used to represents substitutions on types. In the
 * future, this could be turned in a generalized substitution concept, and could
 * be used to substitute both on types and expressions.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.types.TypeVariable
 */
public interface TypeSubstitution extends PrettyPrintable, LatexPrintable {
	/**
	 * Returns the composition of this {@link TypeSubstitution} and <code>s</code>.
	 * The composition will apply pairs from this substitution first and
	 * afterwards the ones from <code>s</code>.
	 * 
	 * @param s another {@link TypeSubstitution}.
	 * @return the composition of this and <code>s</code>.
	 * @throws NullPointerException if <code>s</code> is <code>null</code>.
	 */
	public TypeSubstitution compose ( TypeSubstitution s );

	/**
	 * Returns the set of free type variables in this {@link TypeSubstitution},
	 * that is the free identifiers in all types present in this substitution.
	 * 
	 * @return the set of free type variables.
	 */
	public Set < TypeVariable > free ( );

	/**
	 * Returns the monomorphic type that is specified for the given
	 * <code>tvar</code> in this substitution or the <code>tvar</code> itself
	 * if no other type is specified for the <code>tvar</code>.
	 * 
	 * @param tvar the type variable for which to lookup the type to substitute.
	 * @return the substituted type for the <code>tvar</code> or the
	 *         <code>tvar</code> itself if no type is registered for the type
	 *         variable.
	 * @throws NullPointerException if <code>tvar</code> is <code>null</code>.
	 */
	public MonoType get ( TypeVariable tvar );
}
