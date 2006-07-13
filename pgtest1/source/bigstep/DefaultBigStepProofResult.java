package bigstep;

import common.interpreters.Store;

import expressions.Expression;

/**
 * Default implementation of the <code>BigStepProofResult</code>
 * interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see bigstep.BigStepProofResult
 */
final class DefaultBigStepProofResult implements BigStepProofResult {
  //
  // Attributes
  //
  
  /**
   * The resulting store of a proof node.
   * 
   * @see #getStore()
   */
  private Store store;
  
  /**
   * The resulting value of a proof node.
   * 
   * @see #getValue()
   */
  private Expression value;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofResult</code> with
   * the specified <code>store</code> and <code>value</code>.
   * 
   * @param store the resulting store of a big step node.
   * @param value the resulting value of a big step node.
   * 
   * @throws NullPointerException if <code>store</code> is
   *                              <code>null</code>.
   * 
   * @see #getStore()
   * @see #getValue()
   */
  DefaultBigStepProofResult(Store store, Expression value) {
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
    this.value = value;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofResult#getStore()
   */
  public Store getStore() {
    return this.store;
  }

  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofResult#getValue()
   */
  public Expression getValue() {
    return this.value;
  }
}
