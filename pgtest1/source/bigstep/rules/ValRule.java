package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ValRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * TODO Add documentation here.
   */
  public ValRule() {
    super(true, "VAL");
  }
  
  
  
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#apply(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    if (node.getExpression().isValue()) {
      context.setProofNodeValue(node, node.getExpression());
    }
    else {
      super.apply(context, node);
    }
  }
}
