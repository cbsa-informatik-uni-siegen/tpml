package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Expression;
import expressions.List;

/**
 * This class represents the big step rule <b>(LIST)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ListRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ListRule</code> instance.
   */
  public ListRule() {
    super(false, "LIST");
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
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    // can only be applied to lists
    List list = (List)node.getExpression();
    
    // add a child node for the first expression
    context.addProofNode(node, list.getExpressions(0));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression at this node
    List list = (List)node.getExpression();
    
    // check if all child nodes were created
    if (node.getChildCount() < list.getExpressions().length) {
      // verify that the last child node is proven
      if (node.getLastChild().isProven()) {
        // add the next child node
        context.addProofNode(node, list.getExpressions(node.getChildCount()));
      }
    }
    else {
      // check if all child nodes are proven
      Expression[] values = new Expression[node.getChildCount()];
      for (int n = 0; n < values.length; ++n) {
        values[n] = node.getChildAt(n).getResult().getValue();
        if (values[n] == null) {
          // atleast one is not yet proven
          return;
        }
      }
      
      // all child nodes are proven, we're done
      context.setProofNodeResult(node, new List(values));
    }
  }
}
