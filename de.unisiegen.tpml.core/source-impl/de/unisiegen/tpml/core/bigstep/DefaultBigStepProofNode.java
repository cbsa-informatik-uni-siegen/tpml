package de.unisiegen.tpml.core.bigstep;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * Default implementation of the <code>BigStepProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode
 * @see de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode
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
  // Constructors (package)
  //
  
  /**
   * Convenience wrapper for {@link #DefaultBigStepProofNode(Expression, Store)}, which passes
   * an empty {@link Store} for the <code>store</code> parameter.
   * 
   * @param expression the {@link Expression} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   */
  DefaultBigStepProofNode(Expression expression) {
    this(expression, new DefaultStore());
  }
  
  /**
   * Allocates a new <code>DefaultBigStepProofNode</code> with the specified
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link Store} for this node.
   * 
   * @throws NullPointerException if either <code>expression</code> or <code>store</code>
   *                              is <code>null</code>.
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
   * A big step proof node is proven if the {@link #result} is non-<code>null</code>.
   * 
   * @see de.unisiegen.tpml.core.ProofNode#isProven()
   */
  public boolean isProven() {
    return (this.result != null);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getResult()
   */
  public BigStepProofResult getResult() {
    return this.result;
  }
  
  /**
   * Sets the result for the expression at this node. The <code>result</code> must be either
   * <code>null</code> or a {@link BigStepProofResult} with a valid store and a value or an
   * exception (according to the semantics of the big step interpreter), otherwise an
   * {@link IllegalArgumentException} will be thrown.
   * 
   * @param result the new result for this node, or <code>null</code> to reset the result.
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
   * @see de.unisiegen.tpml.core.bigstep.BigStepProofNode#getRule()
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
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAt(int)
   */
  @Override
  public DefaultBigStepProofNode getChildAt(int childIndex) {
    return (DefaultBigStepProofNode)super.getChildAt(childIndex);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofNode#getParent()
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
   * @see de.unisiegen.tpml.core.AbstractProofNode#getRoot()
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
   * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstChild()
   */
  @Override
  public DefaultBigStepProofNode getFirstChild() {
    return (DefaultBigStepProofNode)super.getFirstChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastChild()
   */
  @Override
  public DefaultBigStepProofNode getLastChild() {
    return (DefaultBigStepProofNode)super.getLastChild();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildAfter(javax.swing.tree.TreeNode)
   */
  @Override
  public DefaultBigStepProofNode getChildAfter(TreeNode aChild) {
    return (DefaultBigStepProofNode)super.getChildAfter(aChild);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofNode#getChildBefore(javax.swing.tree.TreeNode)
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
   * @see de.unisiegen.tpml.core.AbstractProofNode#getFirstLeaf()
   */
  @Override
  public DefaultBigStepProofNode getFirstLeaf() {
    return (DefaultBigStepProofNode)super.getFirstLeaf();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.AbstractProofNode#getLastLeaf()
   */
  @Override
  public DefaultBigStepProofNode getLastLeaf() {
    return (DefaultBigStepProofNode)super.getLastLeaf();
  }
}
