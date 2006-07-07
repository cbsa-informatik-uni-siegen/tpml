package bigstep;

import expressions.Expression;

/**
 * Base interface for {@link bigstep.BigStepProofRule}s to interact with the
 * {@link bigstep.BigStepProofModel}s when applying a rule to a specified node
 * or updating a node after a rule application.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.DefaultBigStepProofNode
 * @see bigstep.BigStepProofRule
 */
public interface BigStepProofContext {
  /**
   * Adds a new {@link BigStepProofNode} for the specified <code>expression</code>
   * as child node for the given <code>node</code>.
   * 
   * The method takes care of registering the required redo and undo actions for
   * the {@link BigStepProofModel} associated with this proof context.
   * 
   * @param node the parent node for the new child node.
   * @param expression the {@link Expression} for which to create a new big step
   *                   proof node below the <code>node</code>.
   *                   
   * @throws NullPointerException if either the <code>node</code> or the
   *                              <code>expression</code> is <code>null</code>.                   
   */
  public void addProofNode(BigStepProofNode node, Expression expression);
  
  /**
   * Changes the big step proof rule of the specified <code>node</code> to the
   * given <code>rule</code>. This affects the proof steps of the <code>node</code>
   * (see the {@link common.ProofNode#getSteps()} method for details.
   * 
   * The method takes care of registering the required redo and undo actions for
   * the {@link BigStepProofModel} associated with this proof context.
   * 
   * @param node the node, whose associated proof step to change to a step for
   *             the specified <code>rule</code>.
   * @param rule the new rule to use for the proof step for <code>node</code>.
   */
  public void setProofNodeRule(BigStepProofNode node, BigStepProofRule rule);
  
  /**
   * Changes the value of the specified <code>node</code> to the given <code>value</code>.
   * 
   * The method takes care of registering the required redo and undo actions for
   * the {@link BigStepProofModel} associated with this proof context.
   * 
   * @param node the node, whose value to change to <code>value</code>.
   * @param value the new value for the specified <code>node</code>.
   * 
   * @throws NullPointerException if the <code>node</code> is <code>null</code>.
   */
  public void setProofNodeValue(BigStepProofNode node, Expression value);
}
