package de.unisiegen.tpml.core.types;

/**
 * This class represents a substitution that can be performed on types. For example the unification algorithm
 * returns a substitution that will be used to automatically infer types for expressions.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.types.TypeVariable
 */
public interface TypeSubstitution {
  /**
   * Returns the monomorphic type that is specified for the given <code>tvar</code> in this substitution or the
   * <code>tvar</code> itself if no other type is specified for the <code>tvar</code>.
   * 
   * @param tvar the type variable for which to lookup the type to substitute.
   * 
   * @return the substituted type for the <code>tvar</code> or the <code>tvar</code> itself if no type is
   *         registered for the type variable.
   * 
   * @throws NullPointerException if <code>tvar</code> is <code>null</code>.
   */
  public MonoType get(TypeVariable tvar);
}
