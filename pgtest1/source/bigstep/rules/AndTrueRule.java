package bigstep.rules;

import expressions.And;
import expressions.BooleanConstant;
import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(AND-TRUE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class AndTrueRule extends AbstractAndRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>AndTrueRule</code> instance.
   */
  public AndTrueRule() {
    super("AND-TRUE");
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
        // add a child node for the second expression
        And and = (And)node.getExpression();
        context.addProofNode(node, and.getE2());
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // let (AND-FALSE) handle the node
        BigStepProofRule rule = new AndFalseRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
    }
    else {
      super.update(context, node);
    }
  }
}
