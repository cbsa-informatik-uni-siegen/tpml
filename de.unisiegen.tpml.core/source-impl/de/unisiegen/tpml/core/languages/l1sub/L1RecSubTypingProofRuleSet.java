package de.unisiegen.tpml.core.languages.l1sub;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.subtyping.SubTypingException;
import de.unisiegen.tpml.core.subtypingrec.AbstractRecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofContext;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RecType;

/**
 * The subtype proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L1RecSubTypingProofRuleSet extends AbstractRecSubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L1SubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L1RecSubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

		// register the type rules
		registerByMethodName ( L1Language.L1, "ARROW", "applyArrow" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-MU-LEFT", "applyMuLeft" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-MU-RIGHT", "applyMuRight" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "REFL", "applyRefl" ); //$NON-NLS-1$ //$NON-NLS-2$
		registerByMethodName ( L1Language.L1, "S-ASSUME", "applyAssume" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Applies the <b>(REFL)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyRefl ( @SuppressWarnings("unused")
	RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		MonoType type;
		MonoType type2;

		type = node.getType ( );
		type2 = node.getType2 ( );

		if ( type.equals ( type2 ) )
			return;

		throw new SubTypingException ( "Types are not equal", node ); //$NON-NLS-1$

	}

	/**
	 * Applies the <b>(ARROW)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 */
	public void applyArrow ( RecSubTypingProofContext context,
			RecSubTypingProofNode node )  {
		ArrowType type;
		ArrowType type2;
		type = ( ArrowType ) node.getType ( );
		type2 = ( ArrowType ) node.getType2 ( );

		MonoType taul = type.getTau1 ( );
		MonoType taur = type.getTau2 ( );

		MonoType tau2l = type2.getTau1 ( );
		MonoType tau2r = type2.getTau2 ( );

		context.addProofNode ( node, tau2l, taul );
		context.addProofNode ( node, taur, tau2r );

		context.addSeenType ( node.getType ( ), node.getType2 ( ) );
	}

	/**
	 * Applies the <b>(S-ASSUME)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 * @throws SubTypingException throw Exception if rule can't be applied
	 */
	public void applyAssume ( @SuppressWarnings("unused")
	RecSubTypingProofContext context,
			RecSubTypingProofNode node ) throws SubTypingException {
		if ( node.getSeenTypes ( ).contains ( node.getSubType ( ) ) )
			return;
		throw new SubTypingException ( "Types not seen before", node ); //$NON-NLS-1$
	}

	/**
	 * Applies the <b>(S-MU-LEFT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 */
	public void applyMuLeft ( RecSubTypingProofContext context,
			RecSubTypingProofNode node ) {
		RecType rec = ( RecType ) node.getType ( );

		context.addProofNode ( node,
				rec.getTau ( ).substitute ( rec.getTypeName ( ), rec ), node.getType2 ( ) );
		
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );

	}

	/**
	 * Applies the <b>(S-MU-RIGHT)</b> rule to the <code>node</code> using the
	 * <code>context</code>.
	 * 
	 * @param context the subtyping proof context.
	 * @param node the subtyping proof node.
	 */
	public void applyMuRight ( RecSubTypingProofContext context,
			RecSubTypingProofNode node )  {
		RecType rec = ( RecType ) node.getType2 ( );

		context.addProofNode ( node, node.getType ( ), rec.getTau ( ).substitute ( rec
				.getTypeName ( ), rec ) );
		
		context.addSeenType ( node.getType ( ), node.getType2 ( ) );

	}
}
