package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;

import expressions.Or;

/**
 * Abstract base class for the {@link bigstep.rules.OrFalseRule}
 * and {@link bigstep.rules.OrTrueRule} classes.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
abstract class AbstractOrRule extends BigStepProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractOrRule</code> with the specified
   * <code>name</code>.
   * 
   * @param name the name of the rule (either <tt>"OR-FALSE"</tt> or
   *             <tt>"OR-TRUE"</tt>).
   */
  protected AbstractOrRule(String name) {
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
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    // add the first proof node
    Or or = (Or)node.getExpression();
    context.addProofNode(node, or.getE1());
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
