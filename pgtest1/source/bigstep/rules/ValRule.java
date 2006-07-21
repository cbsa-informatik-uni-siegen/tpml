package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

/**
 * This class represents the big step rule <b>(VAL)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ValRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ValRule</code> instance.
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
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    // (VAL) can only be applied to values
    if (node.getExpression().isValue()) {
      context.setProofNodeResult(node, node.getExpression());
    }
    else {
      throw new ProofRuleException(node, this);
    }
  }
}
