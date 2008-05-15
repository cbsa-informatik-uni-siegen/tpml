package de.unisiegen.tpml.core.languages.l2unify;


import de.unisiegen.tpml.core.languages.l1unify.L1UnifyProofRuleSet;
import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;


/**
 * The type proof rules for the <code>L1</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id: L1UnifyProofRuleSet.java 2851 2008-05-08 15:28:12Z uhrhan $
 * @see AbstractUnifyProofRuleSet
 */
public class L2UnifyProofRuleSet extends L1UnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2UnifyProofRuleSet ( L2UNIFYLanguage language )
  {
    super ( language );
  }
}
