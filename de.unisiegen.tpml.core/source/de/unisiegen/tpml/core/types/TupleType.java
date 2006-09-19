package de.unisiegen.tpml.core.types;

import java.util.Arrays;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * This class represents tuple types in our type system. Tuple types are composed of two or more types, all
 * of which are monomorphic types. 
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.types.MonoType
 */
public final class TupleType extends MonoType {
  //
  // Attributes
  //
  
  /**
   * The types for the tuple elements.
   */
  private MonoType[] types;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>TupleType</code> with the specified <code>types</code>.
   * 
   * @param types the monomorphic types for the tuple elements.
   * 
   * @throws IllegalArgumentException if <code>types</code> contains less than two elements.
   * @throws NullPointerException if <code>types</code> is <code>null</code>.
   */
  public TupleType(MonoType[] types) {
    if (types == null) {
      throw new NullPointerException("types is null");
    }
    if (types.length < 2) {
      throw new IllegalArgumentException("types must contain atleast two elements");
    }
    this.types = types;
  }
  
  

  //
  // Accessors
  //
  
  /**
   * Returns the element types of the tuple.
   * 
   * @return the element types.
   * 
   * @see #getTypes(int)
   */
  public MonoType[] getTypes() {
    return this.types;
  }
  
  /**
   * Returns the <code>n</code>th type in the tuple type.
   * 
   * @param n the index of the type.
   * 
   * @return the <code>n</code>th type.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of bounds.
   * 
   * @see #getTypes()
   */
  public MonoType getTypes(int n) {
    return this.types[n];
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.types.Type#free()
   */
  @Override
  public TreeSet<TypeVariable> free() {
    TreeSet<TypeVariable> free = new TreeSet<TypeVariable>();
    for (MonoType type : this.types) {
      free.addAll(type.free());
    }
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.types.MonoType#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public TupleType substitute(TypeSubstitution substitution) {
    if (substitution == null) {
      throw new NullPointerException("substitution is null");
    }
    MonoType[] types = new MonoType[this.types.length];
    for (int n = 0; n < types.length; ++n) {
      types[n] = this.types[n].substitute(substitution);
    }
    return new TupleType(types);
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_TUPLE);
    for (int n = 0; n < this.types.length; ++n) {
      if (n > 0) {
        builder.addText(" * ");
      }
      builder.addBuilder(this.types[n].toPrettyStringBuilder(factory), PRIO_TUPLE_TAU);
    }
    return builder;
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TupleType) {
      TupleType other = (TupleType)obj;
      return Arrays.equals(this.types, other.types);
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(this.types);
  }
}
