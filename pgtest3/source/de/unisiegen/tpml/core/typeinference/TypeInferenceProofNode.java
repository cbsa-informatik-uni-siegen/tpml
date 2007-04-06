package de.unisiegen.tpml.core.typeinference;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule;

/**
 * Base interface to nodes in a {@link de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel}.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel
 */
public interface TypeInferenceProofNode extends ProofNode
{
  //
  // Accessors
  //
  /**
   * Returns <code>true</code> if this node and all subnodes are finished. A node is finished if
   * a {@link de.unisiegen.tpml.core.ProofRule} was applied and thereby a proper type is known.
   * 
   * @return <code>true</code> if finished.
   */
  public boolean isFinished();
  
  /**
   * Convenience wrapper for the {@link ProofNode#getSteps()} method, which returns the
   * <code>TypeCheckerProofRule</code> that was applied to this node or <code>null</code>
   * if no rule was applied to this node so far.
   * 
   * @return the type proof rule that was applied to this node, or <code>null</code> if
   *         no rule was applied to this node.
   *
   * @see TypeCheckerProofRule
   * @see de.unisiegen.tpml.core.ProofStep
   */
  public TypeCheckerProofRule getRule();
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public TypeInferenceProofNode getChildAt(int childIndex);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public TypeInferenceProofNode getParent();
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public TypeInferenceProofNode getRoot();
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public TypeInferenceProofNode getFirstChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public TypeInferenceProofNode getLastChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public TypeInferenceProofNode getChildAfter(TreeNode aChild);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public TypeInferenceProofNode getChildBefore(TreeNode aChild);
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public TypeInferenceProofNode getFirstLeaf();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public TypeInferenceProofNode getLastLeaf();
}
