package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofResult;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Application;
import expressions.Expression;
import expressions.InfixOperation;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class AppRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * TODO Add documentation here.
   */
  public AppRule() {
    super(false, "APP");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#apply(bigstep.DefaultBigStepProofContext, bigstep.DefaultBigStepProofNode)
   */
  @Override
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    try {
      Expression e = node.getExpression();
      if (e instanceof Application) {
        // well, applications are easy
        Application application = (Application)e;
        context.addProofNode(node, application.getE1());
      }
      else {
        // otherwise must be an infix operation
        InfixOperation infixOperation = (InfixOperation)e;
        context.addProofNode(node, new Application(infixOperation.getOp(), infixOperation.getE1()));
      }
    }
    catch (ClassCastException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.DefaultBigStepProofContext, bigstep.DefaultBigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression for the node
    Expression e = node.getExpression();
    
    // further operation depends on the number of child nodes
    if (node.getChildCount() == 1) {
      // check if the node is proven
      BigStepProofNode node0 = node.getChildAt(0);
      if (node0.isProven()) {
        if (e instanceof Application) {
          Application application = (Application)e;
          context.addProofNode(node, application.getE2(), node0.getResult().getStore());
        }
        else {
          InfixOperation infixOperation = (InfixOperation)e;
          context.addProofNode(node, infixOperation.getE2(), node0.getResult().getStore());
        }
      }
    }
    else if (node.getChildCount() == 2) {
      // check if the second node is proven
      BigStepProofNode node0 = node.getChildAt(0);
      BigStepProofNode node1 = node.getChildAt(1);
      if (node1.isProven()) {
        // add the third child node
        BigStepProofResult result0 = node0.getResult();
        BigStepProofResult result1 = node1.getResult();
        Application application = new Application(result0.getValue(), result1.getValue());
        context.addProofNode(node, application, result1.getStore());
      }
    }
    else if (node.getChildCount() == 3 && node.getChildAt(2).isProven()) {
      // the result of the third child node is the result for this node
      context.setProofNodeResult(node, node.getChildAt(2).getResult());
    }
  }
}
