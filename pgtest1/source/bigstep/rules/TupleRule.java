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
      
      // add nodes for all sub expressions
      for (Expression e : tuple.getExpressions()) {
        context.addProofNode(node, e);
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
    // check if all child nodes are proven
    Expression[] values = new Expression[node.getChildCount()];
    for (int n = 0; n < values.length; ++n) {
      values[n] = ((BigStepProofNode)node.getChildAt(n)).getValue();
      if (values[n] == null) {
        // atleast one is not yet proven
        return;
      }
    }
    
    // all child nodes are proven, we're done
    context.setProofNodeValue(node, new Tuple(values));
  }
}
