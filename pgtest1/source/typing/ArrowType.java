package typing;

import java.util.Set;
import java.util.TreeSet;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ArrowType extends Type {
  /**
   * Allocates a new function type with <code>t1</code> as
   * argument type and <code>t2</code> as return type.
   * 
   * @param t1 the argument type.
   * @param t2 the return type.
   */
  public ArrowType(Type t1, Type t2) {
    this.t1 = t1;
    this.t2 = t2;
  }
  
  /**
   * {@inheritDoc}
   * 
   * For the {@link ArrowType} class, <code>true</code> will be
   * returned if either the operand or the return type contains
   * a type variable of the given <code>name</code>.
   * 
   * @see typing.Type#containsTypeVariable(java.lang.String)
   */
  @Override
  public final boolean containsTypeVariable(String name) {
    return (this.t1.containsTypeVariable(name) || this.t2.containsTypeVariable(name));
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#free()
   */
  @Override
  public final Set<String> free() {
    // determine the free type variables for
    // the type components of the arrow type
    Set<String> freeT1 = this.t1.free();
    Set<String> freeT2 = this.t2.free();
    
    // check if one of the sets is empty
    if (freeT1 == Type.EMPTY_SET)
      return freeT2;
    else if (freeT2 == Type.EMPTY_SET)
      return freeT1;
    
    // merge the sets into a new set
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(freeT1);
    free.addAll(freeT2);
    return free;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#substitute(typing.Substitution)
   */
  @Override
  Type substitute(Substitution s) {
    // apply the substitution to both types
    Type t1 = this.t1.substitute(s);
    Type t2 = this.t2.substitute(s);
    
    // check if anything changed, otherwise
    // we can reuse the existing object
    if (t1 != this.t1 || t2 != this.t2)
      return new ArrowType(t1, t2);
    else
      return this;
  }
  
  /**
   * @return Returns the t1.
   */
  public Type getT1() {
    return this.t1;
  }
  
  /**
   * @return Returns the t2.
   */
  public Type getT2() {
    return this.t2;
  }

  // member attributes
  private Type t1;
  private Type t2;
}
