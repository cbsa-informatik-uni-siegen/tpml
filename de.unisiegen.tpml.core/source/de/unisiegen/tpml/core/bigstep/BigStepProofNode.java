package de.unisiegen.tpml.core.bigstep;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;

/**
 * Base interface to nodes in a {@link de.unisiegen.tpml.core.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 */
public interface BigStepProofNode extends InterpreterProofNode {
  //
  // Accessors
  //
  
  /**
   * Returns the result of the evaluation of the expression
   * at this node, which is <code>null</code> until the
   * node is proven.
   * 
   * Once the node is proven, this may be either a value
   * with a store or an exception with a store.
   * 
   * @return the result for this node or <code>null</code>.
   * 
   * @see Expression#isException()
   * @see Expression#isValue()
   */
  public BigStepProofResult getResult();
  
  /**
   * Convenience wrapper for the {@link ProofNode#getSteps()} method, which returns the
   * <code>BigStepProofRule</code> that was applied to this node or <code>null</code>
   * if no rule was applied to this node so far.
   * 
   * @return the big step rule that was applied to this node, or <code>null</code> if
   *         no rule was applied to this node.
   *
   * @see BigStepProofRule
   * @see de.unisiegen.tpml.core.ProofStep
   */
  public BigStepProofRule getRule();
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see ProofNode#getChildAt(int)
   */
  public BigStepProofNode getChildAt(int n);
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getParent()
   */
  public BigStepProofNode getParent();
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getRoot()
   */
  public BigStepProofNode getRoot();



  //
  // Child Queries
  //

  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getFirstChild()
   */
  public BigStepProofNode getFirstChild();
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getLastChild()
   */
  public BigStepProofNode getLastChild();
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public BigStepProofNode getChildAfter(TreeNode aChild);
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public BigStepProofNode getChildBefore(TreeNode aChild);



  //
  // Leaf Queries
  //

  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getFirstLeaf()
   */
  public BigStepProofNode getFirstLeaf();
  
  /**
   * {@inheritDoc}
   *
   * @see common.ProofNode#getLastLeaf()
   */
  public BigStepProofNode getLastLeaf();
}
