package bigstep;

import javax.swing.tree.TreeNode;

import common.ProofStep;
import common.interpreters.AbstractInterpreterProofNode;
import common.interpreters.Store;

import expressions.Expression;

/**
 * Default implementation of the <code>BigStepProofNode</code> interface.
 * The class for nodes in a {@link bigstep.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.BigStepProofNode
 */
final class DefaultBigStepProofNode extends AbstractInterpreterProofNode implements BigStepProofNode {
  //
  // Attributes
  //
  
  /**
   * The result of the evaluation of the expression at this node.
   * May be either <code>null</code> if the node is not yet proven,
   * or a value (see {@link Expression#isValue()}) or an exception
   * (see {@link Expression#isException()}) with a store.
   * 
   * @see #getResult()
   * @see #setResult(BigStepProofResult)
   */
  private BigStepProofResult result;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the
   * specified <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * 
   * @throws NullPointerException if either <code>expression</code>
   *                              is <code>null</code>.
   */
  DefaultBigStepProofNode(Expression expression) {
    this(expression, Store.EMPTY_STORE);
  }
  
  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the
   * specified <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * 
   * @throws NullPointerException if either <code>expression</code> or
   *                              <code>store</code> is <code>null</code>.
   */
  DefaultBigStepProofNode(Expression expression, Store store) {
    super(expression, store);
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#isProven()
   */
  @Override
  public boolean isProven() {
    return (this.result != null);
  }

  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofNode#getResult()
   */
  public BigStepProofResult getResult() {
    return this.result;
  }
  
  /**
   * Sets the result for the expression at this node. The
   * <code>result</code> must be either <code>null</code> or
   * a {@link BigStepProofResult} with a valid store and a
   * value or an exception (according to the semantics of the
   * big step interpreter), otherwise an {@link IllegalArgumentException}
   * will be thrown.
   * 
   * @param result the new result for this node, or <code>null</code> to
   *               reset the result.
   *               
   * @throws IllegalArgumentException if <code>result</code> is invalid.
   * 
   * @see #getResult()
   */
  void setResult(BigStepProofResult result) {
    if (result != null && !result.getValue().isException() && !result.getValue().isValue()) {
      throw new IllegalArgumentException("result is invalid");
    }
    this.result = result;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofNode#getRule()
   */
  public BigStepProofRule getRule() {
    ProofStep[] steps = getSteps();
    if (steps.length > 0) {
      return (BigStepProofRule)steps[0].getRule();
    }
    else {
      return null;
    }
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getChildAt(int)
   */
  @Override
  public DefaultBigStepProofNode getChildAt(int childIndex) {
    return (DefaultBigStepProofNode)super.getChildAt(childIndex);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getParent()
   */
  @Override
  public DefaultBigStepProofNode getParent() {
    return (DefaultBigStepProofNode)super.getParent();
  }
  
  
  
  //
  // Tree Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getRoot()
   */
  @Override
  public DefaultBigStepProofNode getRoot() {
    return (DefaultBigStepProofNode)super.getRoot();
  }
  
  
  
  //
  // Child Queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getFirstChild()
   */
  @Override
  public DefaultBigStepProofNode getFirstChild() {
    return (DefaultBigStepProofNode)super.getFirstChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getLastChild()
   */
  @Override
  public DefaultBigStepProofNode getLastChild() {
    return (DefaultBigStepProofNode)super.getLastChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultBigStepProofNode getChildAfter(TreeNode aChild) {
    return (DefaultBigStepProofNode)super.getChildAfter(aChild);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getChildBefore(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultBigStepProofNode getChildBefore(TreeNode aChild) {
    return (DefaultBigStepProofNode)super.getChildBefore(aChild);
  }



  //
  // Leaf Queries
  //

  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getFirstLeaf()
   */
  @Override
  public DefaultBigStepProofNode getFirstLeaf() {
    return (DefaultBigStepProofNode)super.getFirstLeaf();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.AbstractProofNode#getLastLeaf()
   */
  @Override
  public DefaultBigStepProofNode getLastLeaf() {
    return (DefaultBigStepProofNode)super.getLastLeaf();
  }
}
