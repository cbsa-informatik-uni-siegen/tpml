package de.unisiegen.tpml.core.types;

import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Instances of this class represent list types in our type system. List types are types for expressions of
 * type {@link de.unisiegen.tpml.core.expressions.List} and {@link de.unisiegen.tpml.core.expressions.EmptyList}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.List
 * @see de.unisiegen.tpml.core.expressions.EmptyList
 * @see de.unisiegen.tpml.core.types.MonoType
 */
public final class ListType extends MonoType {
  //
  // Attributes
  //
  
  /**
   * The base type of the list elements.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ListType</code> for the monomorphic type <code>tau</code>, which represents
   * the base type of the elements in the list. I.e. if <code>tau</code> is <code>int</code>, the list
   * type is <code>int list</code>.
   * 
   * @param tau the type for the list elements.
   * 
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   */
  public ListType(MonoType tau) {
    if (tau == null) {
      throw new NullPointerException("tau is null");
    }
    this.tau = tau;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the base element type.
   * 
   * @return the base element type
   */
  public MonoType getTau() {
    return this.tau;
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
  public Set<TypeVariable> free() {
    return this.tau.free();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.types.MonoType#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public MonoType substitute(TypeSubstitution substitution) {
    return new ListType(this.tau.substitute(substitution));
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.types.Type#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LIST);
    builder.addBuilder(this.tau.toPrettyStringBuilder(factory), PRIO_LIST_TAU);
    builder.addText(" ");
    builder.addType("list");
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
    if (obj instanceof ListType) {
      ListType other = (ListType)obj;
      return (this.tau.equals(other.tau));
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
    return ((this.tau.hashCode() + 17) * 13) / 5;
  }
}
