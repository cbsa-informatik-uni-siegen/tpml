package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;
import expressions.BooleanConstant;

/**
 * This class represents the big step rule <b>(OR-TRUE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class OrTrueRule extends AbstractOrRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>OrTrueRule</code> instance.
   */
  public OrTrueRule() {
    super("OR-TRUE");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.rules.AbstractOrRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // we're done with this node
        context.setProofNodeResult(node, result0);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // let (OR-FALSE) handle the node
        BigStepProofRule rule = new OrFalseRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
    }
    else {
      super.update(context, node);
    }
  }
}
