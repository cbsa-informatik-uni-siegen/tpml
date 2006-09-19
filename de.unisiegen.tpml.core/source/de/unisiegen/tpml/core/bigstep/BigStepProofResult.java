package de.unisiegen.tpml.core.bigstep;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.Store;

/**
 * Instances of this class represent results for big step nodes in the big step interpreter. A
 * big step result consists of a value (an {@link de.unisiegen.tpml.core.expressions.Expression})
 * and the resulting {@link de.unisiegen.tpml.core.interpreters.Store}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 */
public final class BigStepProofResult {
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
   * @see #getValue();
   */
  private Expression value;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultBigStepProofResult</code> with the specified <code>store</code>
   * and <code>value</code>.
   * 
   * @param store the resulting store of a big step node.
   * @param value the resulting value of a big step node.
   * 
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   * 
   * @see #getStore()
   * @see #getValue()
   */
  BigStepProofResult(Store store, Expression value) {
    if (store == null) {
      throw new NullPointerException("store is null");
    }
    this.store = store;
    this.value = value;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the {@link Store} that is part of the result of a proven big step node.
   * 
   * @return the resulting store for a big step proof node.
   * 
   * @see Store
   */
  public Store getStore() {
    return this.store;
  }
  
  /**
   * Returns the value that is part of the result of a proven big step node.
   * 
   * @return the resulting value for a big step proof node.
   * 
   * @see Expression
   */
  public Expression getValue() {
    return this.value;
  }
}
