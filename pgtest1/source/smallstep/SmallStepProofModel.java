package smallstep;

import common.AbstractProofModel;
import common.ProofGuessException;
import common.ProofModel;
import common.ProofNode;
import common.ProofRule;
import common.ProofRuleException;
import common.ProofStep;

import expressions.Expression;

/**
 * Small step version of the {@link ProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofModel extends AbstractProofModel {
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof model, used to
   * prove that the <code>expression</code> evaluates
   * to a certain value.
   * 
   * @param expression the {@link Expression} for the
   *                   proof.
   */
  SmallStepProofModel(Expression expression) {
    super(new SmallStepProofNode(expression));
    
    // tell the view that stores should be displayed
    // if the supplied expression contains any refe-
    // rences or memory locations
    setMemoryEnabled(expression.containsReferences());
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#getRules()
   */
  @Override
  public ProofRule[] getRules() {
    return SmallStepProofRule.getRules();
  }
  
  /**
   * TODO
   * 
   * @return
   */
  public ProofRule[] getMatchingRules() {
    // FIXME
    return null;
  }
  
  
  
  //
  // Actions
  //

  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#guess(common.ProofNode)
   */
  @Override
  public void guess(ProofNode node) throws ProofGuessException {
    // guess the remaining steps for the node
    ProofStep[] remainingSteps = remaining(node);
    
    // check if the node is already completed
    if (remainingSteps.length == 0) {
      throw new IllegalStateException("Cannot prove an already proven node");
    }
    
    // try to prove using the guessed rule
    try {
      // apply the last rule for the evaluated steps to the node
      apply((SmallStepProofRule)remainingSteps[remainingSteps.length - 1].getRule(), (SmallStepProofNode)node);
    }
    catch (ProofRuleException exception) {
      // failed to guess
      throw new ProofGuessException(node);
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see common.ProofModel#prove(common.ProofRule, common.ProofNode)
   */
  @Override
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException {
    // verify that the rule is a small step rule
    if (!(rule instanceof SmallStepProofRule)) {
      throw new IllegalArgumentException("The rule must be a small step proof rule");
    }
    
    // verify that the node is valid for the model
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node is invalid for the model");
    }
    
    // apply the rule to the specified node
    apply((SmallStepProofRule)rule, (SmallStepProofNode)node);
  }
  
  /**
   * Returns the remaining {@link ProofStep}s required to prove the specified
   * <code>node</code>. This method is used to guess the next step, see the
   * {@link #guess(ProofNode)} method for further details, and in the user
   * interface, to highlight the next expression. 
   * 
   * @param node the {@link ProofNode} for which to return the remaining
   *             steps required to prove the <code>node</code>.
   * 
   * @return the remaining {@link ProofStep}s required to prove the
   *         <code>node</code>, or an empty array if the <code>node</code>
   *         is already proven or the evaluation is stuck.
   * 
   * @throws IllegalArgumentException if the <code>node</code> is invalid
   *                                  for this model.
   */
  public ProofStep[] remaining(ProofNode node) {
    // verify that the node is valid for the model
    if (!this.root.isNodeRelated(node)) {
      throw new IllegalArgumentException("The node is invalid for the model");
    }
    
    // evaluate the next step for the node
    SmallStepEvaluator evaluator = new SmallStepEvaluator(node.getExpression(), ((SmallStepProofNode)node).getStore());
    
    // determine the evaluated/completed steps
    ProofStep[] evaluatedSteps = evaluator.getSteps();
    ProofStep[] completedSteps = node.getSteps();
    
    // generate the remaining steps
    ProofStep[] remainingSteps = new ProofStep[evaluatedSteps.length - completedSteps.length];
    System.arraycopy(evaluatedSteps, completedSteps.length, remainingSteps, 0, remainingSteps.length);
    return remainingSteps;
  }
  
  
  //
  // Internals
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofModel#addUndoableTreeEdit(common.AbstractProofModel.UndoableTreeEdit)
   */
  @Override
  protected void addUndoableTreeEdit(UndoableTreeEdit edit) {
    // perform the redo of the edit
    edit.redo();
    
    // add to the undo history
    super.addUndoableTreeEdit(edit);
  }

  /**
   * Applies the specified <code>rule</code> to the specified <code>node</code>.
   * 
   * @param rule the {@link SmallStepProofRule} to apply.
   * @param node the {@link SmallStepProofNode} to which to apply the
   *             <code>rule</code> (must be part of this model).
   *             
   * @throws ProofRuleException if the <code>rule</code> cannot be applied to the
   *                            <code>node</code>.
   */
  private void apply(SmallStepProofRule rule, final SmallStepProofNode node) throws ProofRuleException {
    // evaluate the expression and determine the proof steps
    SmallStepEvaluator evaluator = new SmallStepEvaluator(node.getExpression(), node.getStore());
    Expression expression = evaluator.getExpression();
    ProofStep[] evaluatedSteps = evaluator.getSteps();
    
    // determine the completed steps for the node
    final ProofStep[] completedSteps = node.getSteps();
    
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
      throw new ProofRuleException(node, rule);
    }
    
    // determine the new step(s) for the node
    final ProofStep[] newSteps = new ProofStep[m + 1];
    System.arraycopy(evaluatedSteps, 0, newSteps, 0, m + 1);

    // check if the node is finished (the last
    // step is an application of an axiom rule)
    if (newSteps[m].getRule().isAxiom()) {
      // create the child node for the node
      final SmallStepProofNode child = new SmallStepProofNode(expression, evaluator.getStore());
      
      // add the undoable edit
      addUndoableTreeEdit(new UndoableTreeEdit() {
        public void redo() {
          // apply the new steps and add the child
          node.setSteps(newSteps);
          node.add(child);
          nodesWereInserted(node, new int[] { node.getIndex(child) });
          nodeChanged(node);
        }
        
        public void undo() {
          // remove the child and revert the steps
          int[] indices = { node.getIndex(child) };
          node.remove(child);
          nodesWereRemoved(node, indices, new Object[] { child });
          node.setSteps(completedSteps);
          nodeChanged(node);
        }
      });
    }
    else {
      // add the undoable edit
      addUndoableTreeEdit(new UndoableTreeEdit() {
        public void redo() {
          // apply the new steps
          node.setSteps(newSteps);
          nodeChanged(node);
        }
        
        public void undo() {
          // revert to the previous steps
          node.setSteps(completedSteps);
          nodeChanged(node);
        }
      });
    }
  }
}
