package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * The proof context for the type checker. The proof context acts as an interface to the proof model
 * for the type rules.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel
 */
public interface TypeCheckerProofContext {
  //
  // Primitives
  //
  
  /**
   * Allocates a new <code>TypeVariable</code> that is not already used in the type checker proof
   * model associated with this type checker proof context.
   * 
   * @return a new, unique <code>TypeVariable</code>.
   */
  public TypeVariable newTypeVariable();
}
