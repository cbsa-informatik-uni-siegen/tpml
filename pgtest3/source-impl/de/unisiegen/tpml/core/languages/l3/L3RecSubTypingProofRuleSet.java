package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2RecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.types.ListType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;

/**
 * The subtype proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L3RecSubTypingProofRuleSet extends L2RecSubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L1SubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L3RecSubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );
		
		unregister ( "REFL" ); //$NON-NLS-1$
		unregister ( "S-ASSUME" ); //$NON-NLS-1$
		
		// register the type rules
		registerByMethodName ( L3Language.L3, "PODUCT", "applyProduct" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L3Language.L3, "LIST", "applyList" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-ASSUME", "applyAssume" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(PRODUCT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyProduct ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		TupleType type;
		TupleType type2;

		type = ( TupleType ) node.getType ( );
		type2 = ( TupleType ) node.getType2 ( );

		MonoType[] types = type.getTypes ( );
		MonoType[] types2 = type2.getTypes ( );

		if ( types.length == types2.length ) {
			for ( int i = 0; i < types.length; i++ ) {
				context.addProofNode ( node, types[i], types2[i] );
				context.addSeenType ( node.getType ( ), node.getType2 ( ) );
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
	public void applyList ( RecSubTypingProofContext context, RecSubTypingProofNode node )
			throws SubTypingException {
		ListType type;
		ListType type2;

		type = ( ListType ) node.getType ( );
		type2 = ( ListType ) node.getType2 ( );

		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );

		context.addProofNode ( node, tau, tau2 );
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
		
	}
}
