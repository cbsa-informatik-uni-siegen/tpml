package de.unisiegen.tpml.core.languages.l2;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l1.L1Language;


/**
 * Big step proof rules for the <b>L2</b> and derived languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet
 */
public class L2BigStepProofRuleSet extends L1BigStepProofRuleSet
{

  /**
   * Allocates a new <code>L2BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L1</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L1BigStepProofRuleSet#L1BigStepProofRuleSet(L1Language)
   */
  public L2BigStepProofRuleSet ( L2Language language )
  {
    super ( language );
    // register the big step rules (order is important for guessing!)
    registerByMethodName ( L2Language.L2, "UNFOLD", "applyUnfold", //$NON-NLS-1$//$NON-NLS-2$
        "updateUnfold" ); //$NON-NLS-1$
  }


  /**
   * Applies the <b>(UNFOLD)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(UNFOLD)</b> rule to.
   */
  public void applyUnfold ( BigStepProofContext context, BigStepProofNode node )
  {
    // can only be applied to Recursions
    Recursion recursion = ( Recursion ) node.getExpression ();
    context.addProofNode ( node, recursion.getE ().substitute (
        recursion.getId (), recursion ) );
  }


  /**
   * Updates the <code>node</code> to which <b>(UNFOLD)</b> was applied
   * previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(UNFOLD)</b>.
   */
  public void updateUnfold ( BigStepProofContext context, BigStepProofNode node )
  {
    // forward the result from the child node (may be null)
    context.setProofNodeResult ( node, node.getChildAt ( 0 ).getResult () );
  }
}
