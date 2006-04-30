package smallstep;

import common.ProofModel;
import common.ProofNode;
import common.ProofRule;
import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofModel extends ProofModel {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof model, used to
   * prove that the <code>expression</code> evaluates
   * to a certain value.
   * 
   * @param expression the {@link Expression} for the
   *                   proof.
   */
  SmallStepProofModel(Expression expression) {
    super(new SmallStepProofNode(expression));
  }
  
  
  
  //
  // Actions
  //

  /**
   * @see common.ProofModel#prove(common.ProofRule, common.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) {
    // verify that the rule is a small step rule
    if (!(rule instanceof SmallStepProofRule)) {
      throw new IllegalArgumentException("The rule must be a small step proof rule");
    }
    
    // verify that the node is valid for the model
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node must be valid within the model");
    }
    
    // verify that the node is not already proven
    if (node.isProven()) {
      throw new IllegalArgumentException("The node is already proven");
    }
    
    
  }
}
