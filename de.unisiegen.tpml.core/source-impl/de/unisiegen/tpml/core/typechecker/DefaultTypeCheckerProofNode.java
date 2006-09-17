package de.unisiegen.tpml.core.typechecker;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Default implementation of the <code>TypeCheckerProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofNode
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode
 */
final class DefaultTypeCheckerProofNode extends AbstractProofNode implements TypeCheckerProofNode {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultTypeCheckerProofNode</code> with the specified <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  DefaultTypeCheckerProofNode(Expression expression) {
    super(expression);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven() {
    return (getSteps().length > 0);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildAt(int childIndex) {
    return (DefaultTypeCheckerProofNode)super.getChildAt(childIndex);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
   */
  @Override
  public DefaultTypeCheckerProofNode getParent() {
    return (DefaultTypeCheckerProofNode)super.getParent();
  }
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
   */
  @Override
  public DefaultTypeCheckerProofNode getRoot() {
    return (DefaultTypeCheckerProofNode)super.getRoot();
  }
  
  
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
   */
  @Override
  public DefaultTypeCheckerProofNode getFirstChild() {
    return (DefaultTypeCheckerProofNode)super.getFirstChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
   */
  @Override
  public DefaultTypeCheckerProofNode getLastChild() {
    return (DefaultTypeCheckerProofNode)super.getLastChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildAfter(TreeNode aChild) {
    return (DefaultTypeCheckerProofNode)super.getChildAfter(aChild);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultTypeCheckerProofNode getChildBefore(TreeNode aChild) {
    return (DefaultTypeCheckerProofNode)super.getChildBefore(aChild);
  }
  
  
  
  //
  // Leaf Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
   */
  @Override
  public DefaultTypeCheckerProofNode getFirstLeaf() {
    return (DefaultTypeCheckerProofNode)super.getFirstLeaf();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
   */
  @Override
  public DefaultTypeCheckerProofNode getLastLeaf() {
    return (DefaultTypeCheckerProofNode)super.getLastLeaf();
  }
}
