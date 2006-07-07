package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
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
    Expression e = node.getExpression();
    if (e instanceof Application) {
      Application application = (Application)e;
      context.addProofNode(node, application.getE1());
      context.addProofNode(node, application.getE2());
    }
    else if (e instanceof InfixOperation) {
      InfixOperation infixOperation = (InfixOperation)e;
      context.addProofNode(node, new Application(infixOperation.getOp(), infixOperation.getE1()));
      context.addProofNode(node, infixOperation.getE2());
    }
    else {
      super.apply(context, node);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.DefaultBigStepProofContext, bigstep.DefaultBigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly two proven child nodes for 
    // this node, or otherwise have 3 proven child nodes
    if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // add the third (v1 v2) node
      BigStepProofNode node0 = (BigStepProofNode)node.getChildAt(0);
      BigStepProofNode node1 = (BigStepProofNode)node.getChildAt(1);
      context.addProofNode(node, new Application(node0.getValue(), node1.getValue()));
    }
    else if (node.getChildCount() == 3 && node.getChildAt(2).isProven()) {
      // the value of the third child node is the value for this node
      BigStepProofNode node2 = (BigStepProofNode)node.getChildAt(2);
      context.setProofNodeValue(node, node2.getValue());
    }
  }
}
