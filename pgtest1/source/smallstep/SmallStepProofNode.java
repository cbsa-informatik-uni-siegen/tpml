package smallstep;

import common.AbstractProofNode;
import common.ProofModel;
import common.ProofNode;
import common.ProofRuleException;
import common.ProofStep;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofNode extends AbstractProofNode {
  //
  // Attributes
  //
  
  /**
   * The {@link DefaultStore} for this node if memory operations are being
   * used throughout the proof. Otherwise this is an invalid, immutable
   * store which does not contain any memory locations.
   * 
   * @see #getStore()
   * @see #setStore(DefaultStore)
   */
  private DefaultStore store;


  
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   */
  SmallStepProofNode(Expression expression) {
    this(expression, DefaultStore.EMPTY_STORE);
  }
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link DefaultStore} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> or
   *                              <code>store</code> is <code>null</code>.
   */
  SmallStepProofNode(Expression expression, DefaultStore store) {
    super(expression);
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
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
  
  /**
   * Returns the {@link Store} which specifies the memory allocation
   * for this node. The returned store is only valid if the memory
   * operations are enable for the proof, which can be checked
   * using the {@link ProofModel#isMemoryEnabled()} method.
   * 
   * @return the {@link Store} with the memory allocation for this
   *         proof node.
   * 
   * @see ProofModel#isMemoryEnabled()
   */
  public DefaultStore getStore() {
    return this.store;
  }
  
  /**
   * Sets the new {@link Store} for this proof node.
   * 
   * @param store the new {@link Store} for this node.
   * 
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * 
   * @see #getStore()
   */
  public void setStore(DefaultStore store) {
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
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
   * @throws IllegalStateException if the node is already proven
   *                               or cannot be proven.
   * @throws ProofRuleException if the <code>rule</code>
   *                            cannot be applied here.
   */
  SmallStepProofNode apply(SmallStepProofRule rule) throws ProofRuleException {
    // evaluate the expression and determine the proof steps
    SmallStepEvaluator evaluator = new SmallStepEvaluator(getExpression(), getStore());
    Expression expression = evaluator.getExpression();
    ProofStep[] evaluatedSteps = evaluator.getSteps();
    
    // determine the completed steps for the node
    ProofStep[] completedSteps = getSteps();
    
    // check if the node is already completed
    if (completedSteps.length >= evaluatedSteps.length) {
      throw new IllegalStateException("Cannot prove an already proven node");
    }
    
    // verify the completed steps
    int n;
    for (n = 0; n < completedSteps.length; ++n) {
      if (completedSteps[n].getRule() != evaluatedSteps[n].getRule())
        throw new IllegalStateException("Completed steps don't match evaluated steps");
    }

    // check if the rule is valid, accepting regular meta-rules for EXN rules
    int m;
    for (m = n; m < evaluatedSteps.length; ++m) {
      if (evaluatedSteps[m].getRule() == rule || evaluatedSteps[m].getRule() == rule.getExnRule())
        break;
    }
    
    // check if rule is invalid
    if (m >= evaluatedSteps.length) {
      throw new ProofRuleException(this, rule);
    }
    
    // add the new step(s) to the node
    ProofStep[] newSteps = new ProofStep[m + 1];
    System.arraycopy(evaluatedSteps, 0, newSteps, 0, m + 1);
    setSteps(newSteps);
    
    // check if we're done with this node
    if (isProven()) {
      // return the node for the next expression
      // add the child node for the next expression
      return new SmallStepProofNode(expression, evaluator.getStore());
    }
    
    // not yet done with this node 
    return null;
  }
}
