package de.unisiegen.tpml.core.types;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Abstract base class for primitive types, such as
 * <tt>bool</tt>, <tt>int</tt> and <tt>unit</tt>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.types.MonoType
 * @see de.unisiegen.tpml.core.types.Type
 */
public abstract class PrimitiveType extends MonoType {
  //
  // Attributes
  //
  
  /**
   * The name of this primitive type.
   */
  private String name;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>PrimitiveType</code> with
   * the specified <code>name</code>.
   * 
   * @param name the name of the primitive type, for
   *             example <tt>"bool"</tt> or <tt>"int"</tt>.
   */
  protected PrimitiveType(String name) {
    this.name = name.intern();
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see types.Type#substitute(types.TypeSubstitution)
   */
  @Override
  public MonoType substitute(TypeSubstitution substitution) {
    return this;
  }
  
  
  
  //
  // Pretty printing
  //

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.types.Type#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public @Override PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_PRIMITIVE);
    builder.addType(this.name);
    return builder;
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Compares this primitive type to the <code>obj</code>.
   * Returns <code>true</code> if the <code>obj</code> is
   * a <code>PrimitiveType</code> with the same name as
   * this instance.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if this instance is equal
   *         to the specified <code>obj</code>.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PrimitiveType) {
      PrimitiveType type = (PrimitiveType)obj;
      return (this.name == type.name);
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns a hash value for this primitive type,
   * which is based on the name of the type.
   * 
   * @return a hash value for this primitive type.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}
