package bigstep.rules;

import common.ProofRuleException;
import expressions.Condition;
import expressions.Condition1;
import expressions.Expression;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * Abstract base class for both the {@link bigstep.rules.CondFalseRule}
 * and the {@link bigstep.rules.CondTrueRule}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
abstract class AbstractCondRule extends BigStepProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractCondRule</code> with
   * the specified <code>name</code>.
   * 
   * @param name the name of the cond rule, can be either
   *             <tt>"COND-TRUE"</tt> or <tt>"COND-FALSE"</tt>.
   */
  protected AbstractCondRule(String name) {
    super(false, name);
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
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    try {
      // can be applied to Condition and Condition1
      Expression e = node.getExpression();
      if (e instanceof Condition) {
        Condition condition = (Condition)e;
        context.addProofNode(node, condition.getE0());
      }
      else {
        Condition1 condition1 = (Condition1)e;
        context.addProofNode(node, condition1.getE0());
      }
    }
    catch (ClassCastException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly two proven child nodes
    if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
}
