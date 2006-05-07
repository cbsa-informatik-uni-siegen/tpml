package smallstep;

import common.ProofException;
import common.ProofNode;
import common.ProofStep;
import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofNode extends ProofNode {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code>. The <code>expression</code>
   * is normalized before creating the node.
   * 
   * @param expression the {@link Expression} for this node.
   */
  SmallStepProofNode(Expression expression) {
    super(expression.normalize());
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if either the node is
   * already proven or the node is a value or an
   * exception, which means there are also no more
   * possible steps to perform.
   * 
   * @return <code>false</code> if more steps can be
   *         performed in the proof.
   *         
   * @see common.ProofNode#isProven()
   */
  @Override
  public boolean isProven() {
    return (super.isProven()
         || getExpression().isValue()
         || getExpression().isException());
  }
  
  
  
  //
  // Actions
  //
  
  /**
   * Applies the specified <code>rule</code> to this node.
   * 
   * @param rule the {@link SmallStepProofRule} to apply.
   *
   * @return the new {@link ProofNode} that was added as
   *         child node when this node is completed with
   *         <code>rule</code> and the evaluation is not
   *         stuck. 
   * 
   * @throws ProofException if the <code>rule</code>
   */
  SmallStepProofNode apply(SmallStepProofRule rule) throws ProofException {
    // evaluate the expression and determine the proof steps
    SmallStepEvaluator evaluator = new SmallStepEvaluator(getExpression());
    Expression expression = evaluator.getExpression();
    ProofStep[] steps = evaluator.getSteps();
    
    // verify the completed steps
    int n;
    for (n = 0; n < this.steps.length; ++n) {
      if (this.steps[n].getRule() != steps[n].getRule())
        throw new IllegalStateException("Evaluated steps don't match completed steps");
    }
    
    // check if the rule is valid
    int m;
    for (m = n; m < steps.length; ++m) {
      if (steps[m].getRule() == rule)
        break;
    }
    
    // check if rule is invalid
    if (m >= steps.length) {
      throw new ProofException("Cannot apply " + rule.getName());
    }
    
    // add the new step(s) to the node
    this.steps = new ProofStep[m + 1];
    for (; m >= 0; --m)
      this.steps[m] = steps[m];
    
    // check if we're done with this node
    if (isProven()) {
      // add the child node for the next expression
      SmallStepProofNode node = new SmallStepProofNode(expression);
      this.children.add(node);
      node.parent = this;
      return node;
    }
    
    // not yet done with this node 
    return null;
  }
}
