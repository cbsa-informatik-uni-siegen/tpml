package de.unisiegen.tpml.core.languages.l2unify;


import de.unisiegen.tpml.core.languages.l1unify.L1UnifyProofRuleSet;
import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;


/**
 * The type proof rules for the <code>L2Unify</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see AbstractUnifyProofRuleSet
 */
public class L2UnifyProofRuleSet extends L1UnifyProofRuleSet
{

  /**
   * Allocates a new <code>L2UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L2Unify</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2UnifyProofRuleSet ( L2UNIFYLanguage language )
  {
    super ( language );
  }
}
