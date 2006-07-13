package common.interpreters;

import common.ProofModel;
import common.ProofNode;

/**
 * Base interface for the big and small step interpreter
 * proof models.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.ProofModel
 * @see common.interpreters.InterpreterProofNode
 */
public interface InterpreterProofModel extends ProofModel {
  //
  // Accessors
  //
  
  /**
   * Returns <code>true</code> if memory operations, as
   * part of the imperative concepts, are used while
   * proving properties of a program.
   * 
   * If memory is enabled, the {@link ProofNode#getStore()}
   * method returns the {@link Store} at the given node.
   * Otherwise an invalid, usually empty store, will be
   * returned.
   * 
   * @return <code>true</code> if memory operations are
   *         used while proving properties of a program.
   *         
   * @see Store
   * @see ProofNode#getStore()         
   */
  public boolean isMemoryEnabled();
}
