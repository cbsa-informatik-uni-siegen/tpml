package typing;

import java.util.Set;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class MonoType extends Type {
  /**
   * @see typing.Type#containsFreeTypeVariable(java.lang.String)
   */
  @Override
  public abstract boolean containsFreeTypeVariable(String name);

  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#free()
   */
  @Override
  public Set<String> free() {
    return Type.EMPTY_SET;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#substitute(typing.Substitution)
   */
  @Override
  abstract MonoType substitute(Substitution s);
}
