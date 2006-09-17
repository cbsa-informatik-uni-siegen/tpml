package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.types.MonoType;

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
  // Accessors
  //
  
  /**
   * Returns the type environment for this type node, that is, the environment in which the type
   * of the expression was determined.
   * 
   * @return the type environment for this type node.
   */
  public TypeEnvironment getEnvironment();
  
  /**
   * Returns the type for this type node, which is either a type variable or a concrete type.
   * 
   * @return the monomorphic type for this type node.
   */
  public MonoType getType();
  
  
  
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
