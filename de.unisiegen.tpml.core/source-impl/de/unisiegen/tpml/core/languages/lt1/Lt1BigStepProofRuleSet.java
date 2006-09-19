package de.unisiegen.tpml.core.languages.lt1;

import de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet;

/**
 * Big step proof rules for the <b>Lt1</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet
 * @see de.unisiegen.tpml.core.languages.lt1.Lt1Language
 */
public class Lt1BigStepProofRuleSet extends L1BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Lt1BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>Lt1</b> or a derived language.
   * 
   * @param language the for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet#L1BigStepProofRuleSet(L1Language)
   */
  public Lt1BigStepProofRuleSet(Lt1Language language) {
    super(language);
  }
}
