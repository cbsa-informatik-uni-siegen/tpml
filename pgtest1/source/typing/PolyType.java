package typing;

import java.util.Set;
import java.util.TreeSet;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class PolyType extends Type {
  /**
   * Allocates a new polymorphic type with the given
   * <code>quantifiedVariables</code> and the monomorphic
   * type <code>monoType</code>.
   * 
   * @param quantifiedVariables the set of quantified type variables
   *                            for <code>monoType</code>.
   * @param monoType the monomorphic type.                            
   */
  public PolyType(Set<String> quantifiedVariables, MonoType monoType) {
    this.quantifiedVariables = quantifiedVariables;
    this.monoType = monoType;
  }
  
  /**
   * {@inheritDoc}
   * 
   * For polymorphic types, this returns <code>true</code>
   * if the monomorphic type contains the <code>name</code>
   * and <code>name</code> is not bound by this polymorphic
   * type.
   * 
   * @see typing.Type#containsFreeTypeVariable(java.lang.String)
   */
  @Override
  public boolean containsFreeTypeVariable(String name) {
    // check if the monomorphic type contains a free
    // type variable of the given name
    if (this.monoType.containsFreeTypeVariable(name)) {
      // check if the type variable is not bound by
      // this polymorphic type
      return !this.quantifiedVariables.contains(name);
    }
    else {
      return false;
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * For polymorphic types, this is the set of
   * free type variables for the monomorphic
   * type, minus the set of quantified names. 
   * 
   * @see typing.Type#free()
   */
  @Override
  public Set<String> free() {
    // determine the set of free type variables
    // for the monomorphic type
    Set<String> freeMono = this.monoType.free();
    if (freeMono == EMPTY_SET)
      return EMPTY_SET;
    
    // generate the set of free names not bound
    // by the quantified names
    TreeSet<String> free = new TreeSet<String>();
    for (String name : freeMono)
      if (!this.quantifiedVariables.contains(name))
        free.add(name);
    return free;
  }

  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#substitute(typing.Substitution)
   */
  @Override
  PolyType substitute(Substitution s) {
    // we don't need to do anything if s is empty
    if (s == Substitution.EMPTY_SUBSTITUTION)
      return this;
    
    // remove the quantified type variables
    // and compress the substitution
    s = s.compress(this.quantifiedVariables);
    
    // apply the substitution to the
    // monomorphic type
    MonoType monoType = this.monoType.substitute(s);
    if (this.monoType == monoType)
      return this;
    
    // allocate a new polymorphic type
    return new PolyType(this.quantifiedVariables, monoType);
  }
  
  /**
   * Instantes this polymorphic type with new type variables allocated
   * from the specified <code>typeVariableAllocator</code>, and returns
   * the newly generated monomorphic type.
   * 
   * @param typeVariableAllocator the {@link TypeVariableAllocator} to use
   *                              to generate new type variables.
   *
   * @return the newly instantiated {@link MonoType}.                              
   */
  MonoType instantiate(TypeVariableAllocator typeVariableAllocator) {
    // generate a substitution, which replaces all bound type variables
    // with new type variables from the type variable allocator
    Substitution s = Substitution.EMPTY_SUBSTITUTION;
    for (String name : this.quantifiedVariables)
      s = new Substitution(name, typeVariableAllocator.allocateTypeVariable(), s);
    
    // instantiate the monomorphic type by applying the substitution
    return this.monoType.substitute(s);
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is a
   * polymorphic type (an instanceof {@link PolyType},
   * which is equal to this polymorphic type.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if equal.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PolyType) {
      PolyType polyType = (PolyType)obj;
      return (this.quantifiedVariables.equals(polyType.quantifiedVariables)
           && this.monoType.equals(polyType.monoType));
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns the string representation of the polymorphic
   * type.
   * 
   * @return the string representation of the polymorphic
   *         type.
   *         
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    if (!this.quantifiedVariables.isEmpty()) {
      builder.append('\u2200');
      for (String name : this.quantifiedVariables) {
        if (builder.length() > 1)
          builder.append(',');
        builder.append(name);
      }
      builder.append('.');
    }
    builder.append(this.monoType);
    return builder.toString();
  }
  
  private MonoType monoType;
  private Set<String> quantifiedVariables;
}
