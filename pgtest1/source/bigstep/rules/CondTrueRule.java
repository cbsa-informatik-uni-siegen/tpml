package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;
import expressions.BooleanConstant;
import expressions.Condition;
import expressions.Condition1;
import expressions.Expression;

/**
 * This class represents the <b>(COND-TRUE)</b> big step rule.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class CondTrueRule extends AbstractCondRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>CondTrueRule</code>.
   */
  public CondTrueRule() {
    super("COND-TRUE");
  }
  
  
  
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * @see bigstep.rules.AbstractCondRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the result of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.FALSE) {
        // let (COND-FALSE) handle the node
        BigStepProofRule rule = new CondFalseRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
      else if (result0.getValue() == BooleanConstant.TRUE) {
        // add next proof node for e1
        Expression e = node.getExpression();
        if (e instanceof Condition) {
          Condition condition = (Condition)e;
          context.addProofNode(node, condition.getE1());
        }
        else {
          Condition1 condition = (Condition1)e;
          context.addProofNode(node, condition.getE1());
        }
      }
    }
    else {
      super.update(context, node);
    }
  }
}
