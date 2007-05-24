package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l2.L2SubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;

/**
 * The type proof rules for the <code>L3</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L3SubTypingProofRuleSet extends L2SubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L3SubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L3</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L3SubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

		// register the type rules
		registerByMethodName ( L3Language.L3, "PODUCT", "applyProduct" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L3Language.L3, "LIST", "applyList" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(PRODUCT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyProduct ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		TupleType type;
		TupleType type2;

		type = ( TupleType ) node.getType ( );
		type2 = ( TupleType ) node.getType2 ( );

		MonoType[] types = type.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );

		if ( types.length == types2.length ) {
			for ( int i = 0; i < types.length; i++ ) {
				context.addProofNode ( node, types[i], types2[i] );
			}
		} else
			throw new SubTypingException ("Length of Product types not equal", node );
	}

	/**
	 * Applies the <b>(LIST)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyList ( SubTypingProofContext context, SubTypingProofNode node )
			throws SubTypingException {
		ListType type;
		ListType type2;

		type = ( ListType ) node.getType ( );
		type2 = ( ListType ) node.getType2 ( );

		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );

		context.addProofNode ( node, tau, tau2 );
	}
}
