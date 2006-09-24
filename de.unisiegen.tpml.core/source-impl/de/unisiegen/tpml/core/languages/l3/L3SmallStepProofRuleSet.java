package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet;

/**
 * Small step proof rules for the <code>L3</code> language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet
 */
public class L3SmallStepProofRuleSet extends L2SmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>L3</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2SmallStepProofRuleSet#L2SmallStepProofRuleSet(L2Language)
   */
  public L3SmallStepProofRuleSet(L3Language language) {
    super(language);
  }
}
