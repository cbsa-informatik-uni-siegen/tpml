package bigstep.rules;

import common.ProofRuleException;
import expressions.Expression;
import expressions.Tuple;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(TUPLE)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TupleRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>TupleRule</code> instance.
   */
  public TupleRule() {
    super(false, "TUPLE");
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
      // can only be applied to Tuples
      Tuple tuple = (Tuple)node.getExpression();
      
      // add a child node for the first sub expression
      context.addProofNode(node, tuple.getExpressions(0));
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
    // determine the expression at this node
    Tuple tuple = (Tuple)node.getExpression();
    
    // check if all child nodes were created
    if (node.getChildCount() < tuple.getArity()) {
      // verify that the last child node is proven
      if (node.getLastChild().isProven()) {
        // add the next child node
        context.addProofNode(node, tuple.getExpressions(node.getChildCount()));
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
      context.setProofNodeResult(node, new Tuple(values));
    }
  }
}
