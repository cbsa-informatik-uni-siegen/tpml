package de.unisiegen.tpml.core.interpreters;

import de.unisiegen.tpml.core.ProofModel;

/**
 * Base interface for the big and small step interpreter proof models. It extends the
 * {@link de.unisiegen.tpml.core.ProofModel} interface with functionality to query
 * whether memory operations are enabled during the evaluation of the expression.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 * @see de.unisiegen.tpml.core.ProofModel
 */
public interface InterpreterProofModel extends ProofModel {
  //
  // Accessors
  //
  
  /**
   * Returns <code>true</code> if memory operations, as part of the imperative concepts,
   * are used while proving properties of a program.
   * 
   * If memory is enabled, the {@link InterpreterProofNode#getStore()} method returns
   * the {@link Store} at the given node. Otherwise an invalid - usually empty - store
   * will be returned.
   * 
   * @return <code>true</code> if memory operations are used while evaluating the expression.
   * 
   * @see Store
   * @see InterpreterProofNode#getStore()
   */
  public boolean isMemoryEnabled();
}
