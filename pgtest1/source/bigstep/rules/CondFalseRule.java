package bigstep.rules;

import expressions.BooleanConstant;
import expressions.Condition;
import expressions.Expression;
import expressions.UnitConstant;
import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;

/**
 * This class represents the <b>(COND-FALSE)</b> big step rule.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class CondFalseRule extends AbstractCondRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>CondFalseRule</code>.
   */
  public CondFalseRule() {
    super("COND-FALSE");
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
    // Check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // let (COND-TRUE) handle the node
        BigStepProofRule rule = new CondTrueRule();
        context.setProofNodeRule(node, rule);
        rule.update(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        Expression e = node.getExpression();
        if (e instanceof Condition) {
          // add next proof node for e2
          Condition condition = (Condition)e;
          context.addProofNode(node, condition.getE2());
        }
        else {
          // result is the unit constant
          context.setProofNodeResult(node, UnitConstant.UNIT);
        }
      }
    }
    else {
      super.update(context, node);
    }
  }
}
