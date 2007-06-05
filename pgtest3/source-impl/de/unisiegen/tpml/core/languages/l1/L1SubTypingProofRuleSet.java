package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * The subtype proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L1SubTypingProofRuleSet extends AbstractSubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L1SubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L1SubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

		// register the type rules
		registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "ARROW", "applyArrow" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(REFL)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyRefl ( SubTypingProofContext context, SubTypingProofNode node )
			throws SubTypingException {
		MonoType type;
		MonoType type2;

		type = node.getType ( );
		type2 = node.getType2 ( );

		if ( type.equals ( type2 ) )
			return;

		throw new SubTypingException ( Messages.getString ( "SubTypingException.5" ), node ); //$NON-NLS-1$

	}

	/**
	 * Applies the <b>(ARROW)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyArrow ( SubTypingProofContext context,
			SubTypingProofNode node ) throws SubTypingException {
		ArrowType type;
		ArrowType type2;
		type = ( ArrowType ) node.getType ( );
		type2 = ( ArrowType ) node.getType2 ( );

		MonoType taul = type.getTau1 ( );
		MonoType taur = type.getTau2 ( );

		MonoType tau2l = type2.getTau1 ( );
		MonoType tau2r = type2.getTau2 ( );

		context.addProofNode ( node, taul, tau2l );
		context.addProofNode ( node, taur, tau2r );
	}
}
