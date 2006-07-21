package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;
import expressions.BooleanConstant;
import expressions.Or;

/**
 * This class represents the big step rule <b>(OR-FALSE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class OrFalseRule extends AbstractOrRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>OrFalseRule</code> instance.
   */
  public OrFalseRule() {
    super("OR-FALSE");
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
        // let (OR-TRUE) handle the node
        BigStepProofRule rule = new OrTrueRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // add a child node for the second expression
        Or or = (Or)node.getExpression();
        context.addProofNode(node, or.getE2());
      }
    }
    else {
      super.update(context, node);
    }
  }
}
