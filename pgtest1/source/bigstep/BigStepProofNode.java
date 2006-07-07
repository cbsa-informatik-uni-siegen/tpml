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
