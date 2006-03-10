package typing;

import java.util.Collections;
import java.util.Set;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TypeVariable extends MonoType {
  /**
   * Allocates a new type variable of the given <code>name</code>.
   * 
   * @param name the name of the new type variable.
   */
  public TypeVariable(String name) {
    this.name = name;
  }
  
  /**
   * {@inheritDoc}
   * 
   * For the {@link TypeVariable} class, <code>true</code> is
   * returned if the name of the type variable is equal to
   * <code>name</code>.
   * 
   * @see typing.Type#containsFreeTypeVariable(java.lang.String)
   */
  @Override
  public final boolean containsFreeTypeVariable(String name) {
    return this.name.equals(name);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#free()
   */
  @Override
  public final Set<String> free() {
    return Collections.singleton(this.name);
  }

  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#substitute(typing.Substitution)
   */
  @Override
  MonoType substitute(Substitution s) {
    return s.apply(this);
  }
  
  /**
   * Returns the name of the type variable.
   * 
   * @return Returns the name.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is a
   * <code>TypeVariable</code>, whose name is equal to
   * this type variable.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if same as <code>obj</code>.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TypeVariable) {
      TypeVariable tvar = (TypeVariable)obj;
      return this.name.equals(tvar.name);
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns the string representation of the type variable.
   * 
   * @return the string representation of the type variable.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }
  
  private String name;
}
