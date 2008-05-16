package de.unisiegen.tpml.core.languages.l2ounify;


import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;


/**
 * The type proof rules for the <code>L1</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id$
 * @see AbstractUnifyProofRuleSet
 */
public class L2OUnifyProofRuleSet extends AbstractUnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2OUnifyProofRuleSet ( L2OUNIFYLanguage language )
  {
    super ( language );
    // register the type rules
  }
}
