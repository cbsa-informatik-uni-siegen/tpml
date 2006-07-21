package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;
import expressions.BooleanConstant;

/**
 * This class represents the big step rule <b>(AND-FALSE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class AndFalseRule extends AbstractAndRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>AndFalseRule</code> instance.
   */
  public AndFalseRule() {
    super("AND-FALSE");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.rules.AbstractAndRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // let (AND-TRUE) handle the node
        BigStepProofRule rule = new AndTrueRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // we're done with this node
        context.setProofNodeResult(node, result0);
      }
    }
    else {
      super.update(context, node);
    }
  }
}
