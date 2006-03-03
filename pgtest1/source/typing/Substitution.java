package typing;

/**
 * TODO Add documentation here.
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
   * @param type the type to substitute for <code>name</code>.
   */
  Substitution(String name, Type type) {
    this(name, type, EMPTY_SUBSTITUTION);
  }
  
  /**
   * Allocates a new {@link Substitution} with the given parameters.
   * 
   * @param name the name of the type variable.
   * @param type the type to substitute for <code>name</code>.
   * @param parent the parent substitution, which will be applied
   *               after the (<code>name</code>,<code>type</code>)
   *               pair has been applied. 
   */
  Substitution(String name, Type type, Substitution parent) {
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
  Type apply(TypeVariable tvar) {
    // once we've reached the empty substitution,
    // we know that there's no type for the name
    if (this == EMPTY_SUBSTITUTION)
      return tvar;
    
    // check if this substitution matches, otherwise
    // continue with the parent and so on...
    if (this.name.equals(tvar.getName()))
      return this.type;
    else
      return this.parent.apply(tvar);
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
  private Type type;
  private Substitution parent;
}
