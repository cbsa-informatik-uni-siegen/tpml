package de.unisiegen.tpml.core.languages.l4unify;


import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;


/**
 * The type proof rules for the <code>L4</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see AbstractUnifyProofRuleSet
 */
public class L4UnifyProofRuleSet extends AbstractUnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L4UnifyProofRuleSet ( L4UNIFYLanguage language )
  {
    super ( language );
    // register the type rules
  }
}
