package common.interpreters;

import common.AbstractProofNode;

import expressions.Expression;

/**
 * Abstract base class for big and small step interpreter proof nodes,
 * used in the {@link common.interpreters.AbstractInterpreterProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.interpreters.AbstractInterpreterProofModel
 * @see common.interpreters.InterpreterProofNode
 */
public abstract class AbstractInterpreterProofNode extends AbstractProofNode implements InterpreterProofNode {
  //
  // Attributes
  //
  
  /**
   * The store used to prove the expression associated with this
   * proof node.
   *
   * @see #getStore()
   * @see common.ProofNode#getExpression()
   */
  private Store store;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractInterpreterProofNode</code> for the specified
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} to prove.
   * @param store the {@link Store} used to prove the <code>expression</code>.
   * 
   * @throws NullPointerException if either <code>expression</code> or
   *                              <code>store</code> is <code>null</code>.
   */
  protected AbstractInterpreterProofNode(Expression expression, Store store) {
    super(expression);
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.interpreters.InterpreterProofNode#getStore()
   */
  public Store getStore() {
    return this.store;
  }
  
  /**
   * Sets the new {@link Store} for this proof node.
   * 
   * @param store the new {@link Store} for this node.
   * 
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * 
   * @see #getStore()
   */
  public void setStore(Store store) {
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
  }
}
