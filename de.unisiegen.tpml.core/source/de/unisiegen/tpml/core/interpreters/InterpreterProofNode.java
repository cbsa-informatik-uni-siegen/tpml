package de.unisiegen.tpml.core.interpreters;

import de.unisiegen.tpml.core.ProofNode;

/**
 * Base interface for big and small step interpreter proof nodes, used in the
 * {@link de.unisiegen.tpml.core.interpreters.InterpreterProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofModel
 * @see de.unisiegen.tpml.core.ProofNode
 */
public interface InterpreterProofNode extends ProofNode {
  //
  // Accessors
  //
  
  /**
   * Returns the {@link Store} associated with this proof node, and thereby used to prove the
   * expression of this node.
   * 
   * Note that this is not the resulting store in case of the big step interpreter, but the
   * initial store.
   * 
   * This is only meaningful if the expression that is being proved contains memory operations.
   * 
   * @return the <code>Store</code> for this proof node.
   * 
   * @see Store
   * @see InterpreterProofModel#isMemoryEnabled()
   */
  public Store getStore();
}
