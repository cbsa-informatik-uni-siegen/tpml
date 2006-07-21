package bigstep.rules;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

import common.ProofRuleException;
import expressions.And;

/**
 * Abstract base class for the {@link bigstep.rules.AndFalseRule}
 * and {@link bigstep.rules.AndTrueRule} classes.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
abstract class AbstractAndRule extends BigStepProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractAndRule</code> with the specified
   * rule <code>name</code>.
   * 
   * @param name the name of the rule (either <tt>"AND-FALSE"</tt>
   *             or <tt>"AND-TRUE"</tt>).
   */
  protected AbstractAndRule(String name) {
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
  public final void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
    // add the first proof node
    And and = (And)node.getExpression();
    context.addProofNode(node, and.getE1());
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
