package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.Expression;
import expressions.Lambda;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BetaValueRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>BetaValueRule</code> instance,
   * which represents the <b>(BETA-V)</b> big step proof
   * rule.
   */
  public BetaValueRule() {
    super(true, "BETA-V");
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
      // the expression must an application of lambda...
      Application application = (Application)node.getExpression();
      Lambda e1 = (Lambda)application.getE1();
      Expression e2 = application.getE2();
      
      // ...to a value
      if (e2.isValue()) {
        context.addProofNode(node, e1.getE().substitute(e1.getId(), e2));
      }
      else {
        // cannot apply
        super.apply(context, node);
      }
    }
    catch (ClassCastException e) {
      super.apply(context, node);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if the single child node is proven
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // the value of the child is the value for this node
      context.setProofNodeValue(node, ((BigStepProofNode)node.getChildAt(0)).getValue());
    }
  }
}
