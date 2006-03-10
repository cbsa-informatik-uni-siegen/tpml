package typing;

/**
 * Represents primitive types such as bool, int and unit.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class PrimitiveType extends MonoType {
  /**
   * {@inheritDoc}
   * 
   * Always returns <code>false</code> as primitive types
   * don't contain type variables.
   * 
   * @see typing.Type#containsFreeTypeVariable(java.lang.String)
   */
  @Override
  public boolean containsFreeTypeVariable(String name) {
    return false;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see typing.Type#substitute(typing.Substitution)
   */
  @Override
  MonoType substitute(Substitution s) {
    return this;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is a primitive
   * type and <code>obj</code> refers to the same primitive type
   * as the object on which <code>equals</code> was invoked.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> is the same
   *         primitive type as this object.
   *         
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // we can use a simple reference comparison, since
    // we don't provide a public constructor
    return (obj instanceof PrimitiveType && obj == this);
  }
  
  /**
   * Returns the name of this primitive type, i.e.
   * <code>"bool"</code> or <code>"int"</code>.
   * 
   * @return the name of this primitive type.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Returns the name of the primitive type, i.e. <code>"bool"</code>,
   * <code>"int"</code> and <code>"unit"</code>.
   * 
   * @return Returns the name of the primitive type.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * The primitive type for boolean expressions.
   */
  public static final PrimitiveType BOOL = new PrimitiveType("bool");
  
  /**
   * The primitive type for integer expression.
   */
  public static final PrimitiveType INT = new PrimitiveType("int");
  
  /**
   * The primitive type for unit expressions.
   */
  public static final PrimitiveType UNIT = new PrimitiveType("unit");
  
  private PrimitiveType(String name) {
    this.name = name;
  }
  
  // member attributes
  private String name;
}
