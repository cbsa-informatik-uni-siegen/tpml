package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet;

/**
 * The type proof rules for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet
 */
public class L3TypeCheckerProofRuleSet extends L2TypeCheckerProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3TypecheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the <code>L3</code> or a derived language.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L3TypeCheckerProofRuleSet(L3Language language) {
    super(language);
  }
}
