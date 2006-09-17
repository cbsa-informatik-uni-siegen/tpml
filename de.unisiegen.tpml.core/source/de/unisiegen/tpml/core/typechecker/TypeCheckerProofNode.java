package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;

/**
 * Base interface to nodes in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
public interface TypeCheckerProofNode extends ProofNode {
  //
  // Primitives
  //

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public TypeCheckerProofNode getChildAt(int childIndex);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public TypeCheckerProofNode getParent();
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public TypeCheckerProofNode getRoot();
  
  
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public TypeCheckerProofNode getFirstChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public TypeCheckerProofNode getLastChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public TypeCheckerProofNode getChildAfter(TreeNode aChild);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public TypeCheckerProofNode getChildBefore(TreeNode aChild);
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public TypeCheckerProofNode getFirstLeaf();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public TypeCheckerProofNode getLastLeaf();
}
