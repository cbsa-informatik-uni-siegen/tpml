package bigstep.rules;

import common.ProofRuleException;
import expressions.Recursion;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(UNFOLD)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnfoldRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>UnfoldRule</code> instance.
   */
  public UnfoldRule() {
    super(false, "UNFOLD");
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
    // can only be applied to Recursions
    Recursion recursion = (Recursion)node.getExpression();
    context.addProofNode(node, recursion.getE().substitute(recursion.getId(), recursion));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // forward the result from the child node (may be null)
    context.setProofNodeResult(node, node.getChildAt(0).getResult());
  }
}
