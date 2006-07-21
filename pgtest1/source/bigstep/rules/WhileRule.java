package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Condition1;
import expressions.Sequence;
import expressions.While;

/**
 * This class represents the big step rule <b>(WHILE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class WhileRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>WhileRule</code> instance.
   */
  public WhileRule() {
    super(false, "WHILE");
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
    // can only be applied to While
    While loop = (While)node.getExpression();
    
    // add the translated child node
    context.addProofNode(node, new Condition1(loop.getE1(), new Sequence(loop.getE2(), loop)));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if the child node is proven
    if (node.getChildAt(0).isProven()) {
      // forward the proof result
      context.setProofNodeResult(node, node.getChildAt(0).getResult());
    }
  }
}
