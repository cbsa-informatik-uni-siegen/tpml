package de.unisiegen.tpml.core.smallstep;

import java.util.List;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;

/**
 * Base interface to nodes in a {@link de.unisiegen.tpml.core.smallstep.SmallStepProofModel}.
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofModel
 */
public interface SmallStepProofNode extends InterpreterProofNode {
  //
  // Accessors
  //
  
  /**
   * Convenience wrapper for the {@link de.unisiegen.tpml.core.ProofNode#getSteps()} method, which
   * returns the <code>SmallStepProofRule</code>s that were already applied to this node or the
   * empty list if no rules were applied so far.
   * 
   * The rules are returned in the order of their application.
   * 
   * @return the small step rules that were applied to this node, or <code>null</code> if no
   *         rules were applied to this node.
   *         
   * @see SmallStepProofRule
   * @see de.unisiegen.tpml.core.ProofStep         
   */
  public List<SmallStepProofRule> getRules();
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public SmallStepProofNode getChildAt(int childIndex);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public SmallStepProofNode getParent();
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public SmallStepProofNode getRoot();
  
  
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public SmallStepProofNode getFirstChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public SmallStepProofNode getLastChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public SmallStepProofNode getChildAfter(TreeNode aChild);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public SmallStepProofNode getChildBefore(TreeNode aChild);
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public SmallStepProofNode getFirstLeaf();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public SmallStepProofNode getLastLeaf();
}
