package typing;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a type for tuples.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TupleType extends MonoType {
  /**
   * Allocates a new <code>TupleType</code> with
   * the given monomorphic <code>types</code>.
   * 
   * @param types a non-empty array of monomorphic
   *              types for the items of a tuple.
   */
  public TupleType(MonoType[] types) {
    // validate the types
    assert (types.length > 0);
    
    // apply the types
    this.types = types;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.MonoType#containsFreeTypeVariable(java.lang.String)
   */
  @Override
  public boolean containsFreeTypeVariable(String name) {
    for (MonoType type : this.types)
      if (type.containsFreeTypeVariable(name))
        return true;
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see typing.MonoType#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    for (MonoType type : this.types)
      free.addAll(type.free());
    return free;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.MonoType#substitute(typing.Substitution)
   */
  @Override
  MonoType substitute(Substitution s) {
    MonoType[] types = new MonoType[this.types.length];
    for (int n = 0; n < types.length; ++n)
      types[n] = this.types[n].substitute(s);
    return new TupleType(types);
  }
  
  /**
   * Checks if the <code>obj</code> is a {@link TupleType}
   * which is equal to this tuple type.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if equal.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TupleType) {
      TupleType tt = (TupleType)obj;
      if (tt.types.length == this.types.length) {
        for (int i = 0; i < tt.types.length; ++i)
          if (!tt.types[i].equals(this.types[i]))
            return false;
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Returns the string representation of the
   * tuple type.
   * 
   * @return the string representation.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    for (MonoType type : this.types) {
      if (builder.length() > 0)
        builder.append(" * ");
      if (type.getClass().equals(TypeVariable.class) || type.getClass().equals(PrimitiveType.class)) {
        builder.append(type);
      }
      else {
        builder.append("(");
        builder.append(type);
        builder.append(")");
      }
    }
    return builder.toString();
  }
  
  /**
   * Returns the arity of the tuple type.
   * 
   * @return the arity of the tuple type.
   */
  public int arity() {
    return this.types.length;
  }
  
  /**
   * Returns the types for the items of this tuple type.
   * 
   * @return the types for the items of this tuple type.
   */
  public MonoType[] getTypes() {
    return this.types;
  }

  // member attributes
  private MonoType[] types;
}
