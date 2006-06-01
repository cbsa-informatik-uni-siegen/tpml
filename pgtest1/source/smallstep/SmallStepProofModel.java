package smallstep;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Vector;

import common.AbstractProofModel;
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
    // determine the available small step rules
    Vector<ProofRule> rules = new Vector<ProofRule>();
    Field[] fields = SmallStepProofRule.class.getDeclaredFields();
    for (int n = 0; n < fields.length; ++n) {
      try {
        // check if this is a static final field
        if ((fields[n].getModifiers() & (Modifier.FINAL | Modifier.STATIC)) != 0) {
          Object value = fields[n].get(null);
          if (value instanceof SmallStepProofRule)
            rules.add((ProofRule)value);
        }
      }
      catch (IllegalAccessException e) {
        // nothing to do here
      }
    }
    return rules.toArray(new ProofRule[] {});
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
  public void guess(ProofNode node) {
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
      // hm, dunno... IllegalArgumentException for now
      throw new IllegalArgumentException("Cannot guess next proof step", exception);
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
    SmallStepEvaluator evaluator = new SmallStepEvaluator(node.getExpression(), node.getStore());
    
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
  
  private void apply(SmallStepProofRule rule, SmallStepProofNode node) throws ProofRuleException {
    // apply the rule to the specified node
    SmallStepProofNode child = node.apply(rule);
    
    // check if we have a new tree node
    if (child != null) {
      // add the child node to the node
      node.add(child);

      // tell the view about the new tree node
      int[] indices = { node.getIndex(child) };
      nodesWereInserted(node, indices);
    }
    
    // notify the view about the changes to this node
    nodeChanged(node);
  }
}
