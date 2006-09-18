package de.unisiegen.tpml.core.types;

import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Abstract base class for monomorphic types.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.types.Type
 */
public abstract class MonoType extends Type {
  //
  // Constructor (protected)
  //
  
  /**
   * Constructor for monomorphic types.
   */
  protected MonoType() {
    // nothing to do here...
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see types.Type#substitute(types.TypeSubstitution)
   */
  @Override
  public abstract MonoType substitute(TypeSubstitution substitution);
}
