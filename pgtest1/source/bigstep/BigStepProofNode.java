package bigstep;

import common.ProofNode;
import expressions.Expression;

/**
 * Interface to the nodes in a {@link bigstep.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface BigStepProofNode extends ProofNode {
  /**
   * Returns the {@link BigStepProofRule} that was applied to this proof
   * node, or <code>null</code> if no rule was applied to this node so
   * far.
   * 
   * This is a convenience method for the {@link ProofNode#getSteps()}
   * method, which simply returns the first proof steps rule.
   * 
   * @return the rule that was applied to this proof node, or <code>null</code>.
   * 
   * @see BigStepProofRule
   * @see ProofNode#getSteps()
   */
  public BigStepProofRule getRule();
  
  /**
   * Returns the resulting value of the expression at
   * this node, which is <code>null</code> until the
   * node is proven.
   * 
   * Once the node is proven, this may be either a
   * value or an exception.
   * 
   * @return the value for this node or <code>null</code>.
   * 
   * @see Expression#isException()
   * @see Expression#isValue()
   */
  public Expression getValue();
}
