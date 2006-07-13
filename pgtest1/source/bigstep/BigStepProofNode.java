package bigstep;

import javax.swing.tree.TreeNode;

import common.ProofNode;
import common.interpreters.Store;

import expressions.Expression;

/**
 * Interface to the nodes in a {@link bigstep.BigStepProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface BigStepProofNode extends ProofNode {
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
   * Returns the {@link BigStepProofRule} that was applied to this proof
   * node, or <code>null</code> if no rule was applied to this node so
   * far.
   * 
   * This is a convenience method for the {@link ProofNode#getSteps()}
   * method, which simply returns the first proof steps rule.
   * 
   * @return the rule that was applied to this proof node, or <code>null</code>.
   * 
   * @see BigStepProofRule
   * @see ProofNode#getSteps()
   */
  public BigStepProofRule getRule();
  
  /**
   * Returns the {@link Store}, which is used to evaluate the expression
   * at this node (and possibly also used to evaluate sub expressions).
   * 
   * This is only meaningful if the expression to be proven contains
   * memory operations, i.e. references.
   * 
   * @return the store, which is used to evaluate this expression.
   */
  public Store getStore();
  
  
  
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
