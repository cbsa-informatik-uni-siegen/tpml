package de.unisiegen.tpml.core.languages.l3unify;


import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;


/**
 * The type proof rules for the <code>L3</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id: L3UnifyProofRuleSet.java 2851 2008-05-08 15:28:12Z uhrhan $
 * @see AbstractUnifyProofRuleSet
 */
public class L3UnifyProofRuleSet extends AbstractUnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L3</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L3UnifyProofRuleSet ( L3UNIFYLanguage language )
  {
    super ( language );
    // register the type rules
  }
}
