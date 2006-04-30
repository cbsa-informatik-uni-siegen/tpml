package smallstep;

import common.ProofNode;
import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofNode extends ProofNode {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   */
  SmallStepProofNode(Expression expression) {
    super(expression);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if either the node is
   * already proven or the node is a value or an
   * exception, which means there are also no more
   * possible steps to perform.
   * 
   * @return <code>false</code> if more steps can be
   *         performed in the proof.
   *         
   * @see common.ProofNode#isProven()
   */
  @Override
  public boolean isProven() {
    return (super.isProven()
         || getExpression().isValue()
         || getExpression().isException());
  }
}
