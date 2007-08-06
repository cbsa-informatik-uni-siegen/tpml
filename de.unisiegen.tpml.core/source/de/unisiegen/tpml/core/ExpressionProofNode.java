package de.unisiegen.tpml.core;

import javax.swing.tree.TreeNode;
import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Base interface for special proof nodes that have an expression associated with it and present the
 * fundamental parts of the {@link de.unisiegen.tpml.core.ExpressionProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.ExpressionProofModel
 * @see de.unisiegen.tpml.core.ProofNode
 */
public interface ExpressionProofNode extends ProofNode {
  //
  // Primitives
  //
  
  /**
   * Returns the {@link Expression} associated with this proof node. This is garantied to never return
   * <code>null</code>.
   * 
   * @return the {@link Expression} for this proof node.
   * 
   * @see #getSteps()
   */
  public Expression getExpression();
  
  /**
   * Returns the {@link ProofStep}s which were already performed on this proof node. The steps represent
   * the {@link ProofRule}s that were applied to this node already and the associated expressions (which
   * may be sub expressions of the expression associated with this proof node), to which the rules were
   * applied.
   * 
   * @return the {@link ProofStep}s or an empty array if no rules were applied to this node yet.
   * 
   * @see #getExpression()
   * @see #getRules()
   */
  public ProofStep[] getSteps();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
  public ExpressionProofNode getChildAt(int childIndex);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getParent()
   */
  public ExpressionProofNode getParent();
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getRoot()
   */
  public ExpressionProofNode getRoot();



  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstChild()
   */
  public ExpressionProofNode getFirstChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastChild()
   */
  public ExpressionProofNode getLastChild();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  public ExpressionProofNode getChildAfter(TreeNode aChild);
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  public ExpressionProofNode getChildBefore(TreeNode aChild);
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getFirstLeaf()
   */
  public ExpressionProofNode getFirstLeaf();
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
   */
  public ExpressionProofNode getLastLeaf();
}
