package de.unisiegen.tpml.core.typechecker;

import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * Default implementation of the <code>TypeSubstitution</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution
 */
final class DefaultTypeSubstitution implements TypeSubstitution {
  //
  // Constants
  //
  
  /**
   * The empty type substitution, which does not contain any mappings.
   * 
   * @see #DefaultTypeSubstitution()
   */
  public static final DefaultTypeSubstitution EMPTY_SUBSTITUTION = new DefaultTypeSubstitution();
  
  
  
  //
  // Attributes
  //

  /**
   * The type variable at this level of the substitution.
   */
  private TypeVariable tvar;
  
  /**
   * The type to substitute for the <code>tvar</code>.
   */
  private MonoType type;
  
  /**
   * The remaining <code>(tvar,type)</code> pairs in the substitution.
   */
  private DefaultTypeSubstitution parent;

  
  
  //
  // Constructors (package)
  //
  
  /**
   * Allocates a new empty <code>DefaultTypeSubstitution</code>.
   * 
   * @see TypeSubstitution#EMPTY_SUBSTITUTION
   */
  DefaultTypeSubstitution() {
    super();
  }
  
  /**
   * Convenience wrapper for the {@link #DefaultTypeSubstitution(TypeVariable, MonoType, DefaultTypeSubstitution)}
   * constructor, which passes {@link #EMPTY_SUBSTITUTION} for the <code>parent</code> parameter.
   * 
   * @param tvar the type variable.
   * @param type the (concrete) monomorphic type to substitute for <code>tvar</code>.
   * 
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  DefaultTypeSubstitution(TypeVariable tvar, MonoType type) {
    this(tvar, type, EMPTY_SUBSTITUTION);
  }
  
  /**
   * Allocates a new <code>DefaultTypeSubstitution</code> which represents a pair <code>(tvar,type)</code>
   * and chains up to the specified <code>parent</code>.
   * 
   * @param tvar the type variable.
   * @param type the (concrete) monomorphic type to substitute for <code>tvar</code>.
   * @param parent the parent substitution to chain up to.
   * 
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  DefaultTypeSubstitution(TypeVariable tvar, MonoType type, DefaultTypeSubstitution parent) {
    if (tvar == null) {
      throw new NullPointerException("tvar is null");
    }
    if (type == null) {
      throw new NullPointerException("type is null");
    }
    if (parent == null) {
      throw new NullPointerException("parent is null");
    }
    this.tvar = tvar;
    this.type = type;
    this.parent = parent;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#compose(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  public DefaultTypeSubstitution compose(TypeSubstitution s) {
    // if this is the empty substitution, the
    // result of the composition is simply s
    if (this == EMPTY_SUBSTITUTION) {
      return (DefaultTypeSubstitution)s;
    }

    // compose(parent, s)
    DefaultTypeSubstitution parent = this.parent.compose(s);
    
    // and prepend (name,type) pair
    return new DefaultTypeSubstitution(this.tvar, this.type, parent);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#free()
   */
  public Set<TypeVariable> free() {
    if (this == EMPTY_SUBSTITUTION) {
      return new TreeSet<TypeVariable>();
    }
    TreeSet<TypeVariable> free = new TreeSet<TypeVariable>();
    free.addAll(this.type.free());
    free.remove(this.tvar);
    free.addAll(this.parent.free());
    return free;
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeSubstitution#get(de.unisiegen.tpml.core.types.TypeVariable)
   */
  public MonoType get(TypeVariable tvar) {
    if (this == EMPTY_SUBSTITUTION) {
      // reached the end of the substitution chain
      return tvar;
    }
    else if (this.tvar.equals(tvar)) {
      // we have a match here
      return this.type;
    }
    else {
      // check the parent substitution
      return this.parent.get(tvar);
    }
  }
}
