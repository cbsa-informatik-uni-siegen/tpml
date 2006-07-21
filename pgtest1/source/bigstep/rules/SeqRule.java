package bigstep.rules;

import common.ProofRuleException;
import expressions.Sequence;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(SEQ)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class SeqRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>SeqRule</code> instance.
   */
  public SeqRule() {
    super(false, "SEQ");
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
    // can only be applied to Sequences
    Sequence sequence = (Sequence)node.getExpression();
    
    // add a proof node for e1
    context.addProofNode(node, sequence.getE1());
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if the first child node is proven
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // add a proof node for e2
      Sequence sequence = (Sequence)node.getExpression();
      context.addProofNode(node, sequence.getE2());
    }
    else if (node.getChildCount() == 2) {
      // forward the result of e2
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
}
