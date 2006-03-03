package typing;

import java.util.Collections;
import java.util.Set;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TypeVariable extends Type {
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
   * @see typing.Type#containsTypeVariable(java.lang.String)
   */
  @Override
  public final boolean containsTypeVariable(String name) {
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
  Type substitute(Substitution s) {
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
  
  private String name;
}
