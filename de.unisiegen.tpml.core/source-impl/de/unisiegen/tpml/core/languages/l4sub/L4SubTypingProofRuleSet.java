package de.unisiegen.tpml.core.languages.l4sub;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l3sub.L3SubTypingProofRuleSet;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.subtyping.SubTypingProofContext;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RefType;

/**
 * The subtype proof rules for the <code>L4</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L4SubTypingProofRuleSet extends L3SubTypingProofRuleSet {

  /**
   * Allocates a new <code>L4SubTypingProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L4</code> or a derived language.
   * @param mode the mode chosen by the user
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
	public L4SubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

    // register the type rules
		registerByMethodName ( L4Language.L4, "REF", "applyRef" ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
  /**
   * Applies the <b>(REF)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the subtyping proof context.
   * @param node the subtyping proof node.
   */
	public void applyRef ( SubTypingProofContext context,
			SubTypingProofNode node )  {
		RefType type;
		RefType type2;

		type = ( RefType ) node.getLeft ( );
		type2 = ( RefType ) node.getRight ( );
		
		MonoType tau = type.getTau ( );
		MonoType tau2 = type2.getTau ( );
		
		context.addProofNode ( node, tau, tau2 );
		
	}

}
