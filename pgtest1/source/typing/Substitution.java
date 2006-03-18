package typing;

/**
 * This class represents a substitution, as returned by the unification
 * algorithm.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class Substitution {
  /**
   * Allocates a new {@link Substitution} whose parent is the
   * {@link #EMPTY_SUBSTITUTION}.
   * 
   * @param name the name of the type variable.
   * @param type the monomorphic type to substitute for <code>name</code>.
   */
  Substitution(String name, MonoType type) {
    this(name, type, EMPTY_SUBSTITUTION);
  }
  
  /**
   * Allocates a new {@link Substitution} with the given parameters.
   * 
   * @param name the name of the type variable.
   * @param type the monomorphic type to substitute for <code>name</code>.
   * @param parent the parent substitution, which will be applied
   *               after the (<code>name</code>,<code>type</code>)
   *               pair has been applied. 
   */
  Substitution(String name, MonoType type, Substitution parent) {
    this.name = name;
    this.type = type;
    this.parent = parent;
  }
  
  /**
   * Applies the substitution to the given {@link TypeVariable}
   * <code>tvar</code> and returns the matching {@link Type} for
   * <code>tvar</code>s name. If no such type exists in this
   * substitution, <code>tvar</code> is returned.
   * 
   * @param tvar the {@link TypeVariable} to which this substitution
   *             should be applied.
   * 
   * @return the resulting {@link Type}.
   */
  MonoType apply(TypeVariable tvar) {
    // once we've reached the empty substitution,
    // we know that there's no type for the name
    if (this == EMPTY_SUBSTITUTION)
      return tvar;
    
    // check if this substitution matches, otherwise
    // continue with the parent and so on...
    MonoType tau = (this.name.equals(tvar.getName())) ? this.type : tvar;
    
    // continue with the parent substitution
    return tau.substitute(this.parent);
  }
  
  /**
   * Returns the composition of this {@link Substitution} and <code>s</code>.
   * The composition will apply pairs from this substitution first and
   * afterwards the ones from <code>s</code>.
   * 
   * @param s another {@link Substitution}.
   * 
   * @return the composition of this and <code>s</code>.
   */
  Substitution compose(Substitution s) {
    // if this is the empty substitution, the
    // result of the composition is simply s
    if (this == EMPTY_SUBSTITUTION)
      return s;

    // compose(parent, s)
    Substitution parent = this.parent.compose(s);
    
    // and prepend (name,type) pair
    return new Substitution(this.name, this.type, parent);
  }

  /**
   * Returns <code>true</code> if the substitution contains
   * a type, which in turn contains a free type variable of
   * the given <code>name</code>.
   * 
   * @param name the name of the type variable to test.
   * 
   * @return <code>true</code> if <code>name</code> is present
   *         as free type variable in the substitution.
   */
  boolean containsFreeTypeVariable(String name) {
    // if this is the empty substitution, then name is not present
    if (this == EMPTY_SUBSTITUTION)
      return false;
    
    // check if name is equal or this type contains name
    if (this.name.equals(name) || this.type.containsFreeTypeVariable(name))
      return true;
    
    // check the parent
    return this.parent.containsFreeTypeVariable(name);
  }
  
  /**
   * Returns the string representation of the substitution.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation of the substitution.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    builder.append('[');
    for (Substitution subst = this; subst != EMPTY_SUBSTITUTION; subst = subst.parent) {
      if (subst != this)
        builder.insert(1, ", ");
      builder.insert(1, subst.type + "/" + subst.name);
    }
    builder.append(']');
    return builder.toString();
  }
  
  /**
   * The empty substitution.
   */
  static final Substitution EMPTY_SUBSTITUTION = new Substitution();
  
  private Substitution() {
  }
  
  // member attributes
  private String name;
  private MonoType type;
  private Substitution parent;
}
