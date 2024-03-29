package de.unisiegen.tpml.core.languages.l2sub;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l1sub.L1SubTypingProofRuleSet;

/**
 * The subtype proof rules for the <code>L2</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.subtyping.AbstractSubTypingProofRuleSet
 */
public class L2SubTypingProofRuleSet extends L1SubTypingProofRuleSet {

  /**
   * Allocates a new <code>L2SubTypingProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L2</code> or a derived language.
   * @param mode the mode chosen by the user
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
	public L2SubTypingProofRuleSet ( Language language, boolean mode ) {
		super ( language, mode );
	}

}
