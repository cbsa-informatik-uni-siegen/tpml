package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l2.L2Language;

/**
 * Big step proof rules for the <b>L3</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet
 */
public class L3BigStepProofRuleSet extends L2BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L3</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L2Language)
   */
  public L3BigStepProofRuleSet(L3Language language) {
    super(language);
  }
}
