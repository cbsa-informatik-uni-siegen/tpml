package de.unisiegen.tpml.core.languages.l2subtype;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1subtype.L1RecSubTypingProofRuleSet;

/**
 * The subtype proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L2RecSubTypingProofRuleSet extends L1RecSubTypingProofRuleSet {

	/**
	 * Allocates a new <code>L1SubTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @param mode the mode chosen by the user
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2RecSubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );

	}
}
