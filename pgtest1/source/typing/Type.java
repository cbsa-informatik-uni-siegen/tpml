package typing;

import java.util.Collections;
import java.util.Set;

/**
 * Basic class for all types in the type system.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class Type {
  /**
   * Returns <code>true</code> if the type contains a type
   * variable, whose name is <code>name</code>. Otherwise
   * <code>false</code> is returned.
   * 
   * This method is primarily used to implement the unification
   * algorithm.
   * 
   * @param name the name of the type variable to check for.
   * 
   * @return <code>true</code> if this type contains a type
   *         variable named <code>name</code>, else <code>false</code>.
   */
  public abstract boolean containsTypeVariable(String name);
  
  /**
   * Returns the set of free type variables within this type.
   * 
   * The default implementation for {@link Type} returns the
   * empty set, so you will need to override this method in
   * all types related to type variables.
   * 
   * @return the set of free type variables within this type.
   */
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  /**
   * Applies the {@link Substitution} <code>s</code> to the
   * type and returns the resulting type.
   * 
   * @param s the {@link Substitution} to apply to this type.
   * 
   * @return the resulting {@link Type}.
   */
  abstract Type substitute(Substitution s);
  
  /**
   * Shared empty set as an optimization for the {@link #free()}
   * method, so we don't need to allocate too many empty sets.
   */
  protected static final Set<String> EMPTY_SET = Collections.unmodifiableSet(Collections.<String>emptySet());
}
