package de.unisiegen.tpml.core.languages.l4o;


import de.unisiegen.tpml.core.languages.l4.L4SmallStepProofRuleSet;


/**
 * Small step proof rules for the <code>L4O</code> language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l3.L3SmallStepProofRuleSet
 */
public class L4OSmallStepProofRuleSet extends L4SmallStepProofRuleSet
{

  /**
   * Allocates a new <code>L4OSmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L4O</tt> or a derived
   * language.
   * 
   * @param language the {@link de.unisiegen.tpml.core.languages.Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4OSmallStepProofRuleSet ( L4OLanguage language )
  {
    super ( language );
    // register small step rules
  }
}
