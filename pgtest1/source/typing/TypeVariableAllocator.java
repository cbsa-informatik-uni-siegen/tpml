package typing;

/**
 * Type allocator interface, which is implemented by the
 * {@link typing.ProofTree} and used when instantiating
 * a polymorphic type.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
interface TypeVariableAllocator {
  /**
   * Allocates a new unique type variable.
   * 
   * @return a newly allocated type variable.
   */
  public TypeVariable allocateTypeVariable();
}
