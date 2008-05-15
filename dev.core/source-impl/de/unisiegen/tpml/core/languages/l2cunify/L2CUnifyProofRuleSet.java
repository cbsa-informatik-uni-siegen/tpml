package de.unisiegen.tpml.core.languages.l2cunify;


import de.unisiegen.tpml.core.unify.AbstractUnifyProofRuleSet;
import de.unisiegen.tpml.core.unify.UnifyProofContext;
import de.unisiegen.tpml.core.unify.UnifyProofNode;


/**
 * The type proof rules for the <code>L1</code> language.
 * 
 * @author Christian Uhrhan
 * @version $Id: L1UnifyProofRuleSet.java 2851 2008-05-08 15:28:12Z uhrhan $
 * @see AbstractUnifyProofRuleSet
 */
public class L2CUnifyProofRuleSet extends AbstractUnifyProofRuleSet
{

  /**
   * Allocates a new <code>L1UnifyProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2CUnifyProofRuleSet ( L2CUNIFYLanguage language )
  {
    super ( language );
    // register the type rules
    registerByMethodName ( L2CUNIFYLanguage.L2C_UNIFY, "MU-LEFT", "applyMULeft" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2CUNIFYLanguage.L2C_UNIFY,
        "MU-RIGHT", "applyMURight" ); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Applies the <b>(MU-LEFT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyMULeft ( UnifyProofContext context, UnifyProofNode pNode )
  {
  }


  /**
   * Applies the <b>(MU-RIGHT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the unify proof context.
   * @param pNode the unify proof node.
   */
  public void applyMURight ( UnifyProofContext context, UnifyProofNode pNode )
  {
  }
}
