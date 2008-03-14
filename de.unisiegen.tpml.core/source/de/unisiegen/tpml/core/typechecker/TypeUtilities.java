package de.unisiegen.tpml.core.typechecker;


import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;


/**
 * This class contains only static methods that serve as utility functions for
 * implementing various parts of the type checker and the type system in
 * general.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.types.Type
 */
public final class TypeUtilities
{

  //
  // Utilities
  //
  /**
   * Allocates a new <code>TypeSubstitution</code> that substitutes
   * <code>tau</code> for <code>tvar</code>.
   * 
   * @param tvar the type variable.
   * @param tau the (concrete) monomorphic type to substitute for
   *          <code>tvar</code>.
   * @return the {@link TypeSubstitution} that substitutes <code>tau</code>
   *         for <code>tvar</code>.
   * @throws NullPointerException if <code>tvar</code> or <code>tau</code>
   *           is <code>null</code>.
   * @see DefaultTypeSubstitution#DefaultTypeSubstitution(TypeVariable,
   *      MonoType)
   */
  public static TypeSubstitution newSubstitution ( TypeVariable tvar,
      MonoType tau )
  {
    return new DefaultTypeSubstitution ( tvar, tau );
  }
}
