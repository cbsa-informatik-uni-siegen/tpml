package common.interpreters;

import common.ProofNode;

/**
 * Base interface for big and small step interpreter proof nodes,
 * used in the {@link common.interpreters.InterpreterProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.ProofNode
 * @see common.interpreters.InterpreterProofModel
 */
public interface InterpreterProofNode extends ProofNode {
  //
  // Accessors
  //
  
  /**
   * Returns the {@link Store} associated with this proof node,
   * and thereby used to prove the expression of this node.
   * 
   * Note that this is not the resulting store in case of the
   * big step interpreter, but the initial store.
   * 
   * This is only meaningful if the expression that is being
   * proved contains memory operations.
   * 
   * @return the store for this proof node.
   * 
   * @see Store
   * @see InterpreterProofModel#isMemoryEnabled()
   */
  public Store getStore();
}
