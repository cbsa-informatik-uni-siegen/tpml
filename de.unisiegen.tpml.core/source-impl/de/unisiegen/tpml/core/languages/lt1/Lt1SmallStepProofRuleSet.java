package de.unisiegen.tpml.core.languages.lt1;

import de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet;

/**
 * Small step proof rules for the <b>Lt1</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.lt1.Lt1Language
 */
public class Lt1SmallStepProofRuleSet extends L1SmallStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Lt1SmallStepProofRuleSet</code> for the specified <code>language</code>,
   * which must be either <tt>Lt1</tt> or a derived language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1SmallStepProofRuleSet#L1SmallStepProofRuleSet(L1Language)
   */
  public Lt1SmallStepProofRuleSet(Lt1Language language) {
    super(language);
  }
}
