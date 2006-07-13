package smallstep;

import common.AbstractProofNode;
import common.ProofModel;
import common.interpreters.MutableStore;
import common.interpreters.Store;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class SmallStepProofNode extends AbstractProofNode {
  //
  // Attributes
  //
  
  /**
   * The {@link MutableStore} for this node if memory operations are being
   * used throughout the proof. Otherwise this is an invalid, immutable
   * store which does not contain any memory locations.
   * 
   * @see #getStore()
   * @see #setStore(MutableStore)
   */
  private MutableStore store;


  
  //
  // Constructor
  //
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code>.
   * 
   * @param expression the {@link Expression} for this node.
   */
  SmallStepProofNode(Expression expression) {
    this(expression, MutableStore.EMPTY_STORE);
  }
  
  /**
   * Allocates a new small step proof node for the given
   * <code>expression</code> and <code>store</code>.
   * 
   * @param expression the {@link Expression} for this node.
   * @param store the {@link MutableStore} for this node.
   * 
   * @throws NullPointerException if <code>expression</code> or
   *                              <code>store</code> is <code>null</code>.
   */
  SmallStepProofNode(Expression expression, MutableStore store) {
    super(expression);
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if either the node is
   * already proven or the node is a value or an
   * exception, which means there are also no more
   * possible steps to perform.
   * 
   * @return <code>false</code> if more steps can be
   *         performed in the proof.
   *         
   * @see common.ProofNode#isProven()
   */
  @Override
  public boolean isProven() {
    return (super.isProven()
         || getExpression().isValue()
         || getExpression().isException());
  }
  
  /**
   * Returns the {@link Store} which specifies the memory allocation
   * for this node. The returned store is only valid if the memory
   * operations are enable for the proof, which can be checked
   * using the {@link ProofModel#isMemoryEnabled()} method.
   * 
   * @return the {@link Store} with the memory allocation for this
   *         proof node.
   * 
   * @see ProofModel#isMemoryEnabled()
   */
  public MutableStore getStore() {
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
  public void setStore(MutableStore store) {
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
  }
}
