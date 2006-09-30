package de.unisiegen.tpml.core.types;

import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Instances of this class represent reference types in our type system. Reference types are types 
 * for expressions of type {@link de.unisiegen.tpml.core.expressions.Location} as returned by the
 * {@link de.unisiegen.tpml.core.expressions.Ref} operator.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Location
 * @see de.unisiegen.tpml.core.expressions.Ref
 * @see de.unisiegen.tpml.core.types.MonoType
 */
public final class RefType extends MonoType {
  //
  // Attributes
  //
  
  /**
   * The type of the reference, i.e. <code>int</code> in case of an <code>int ref</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>RefType</code> for the monomorphic type <code>tau</code>. I.e. if
   * <code>tau</code> is <code>bool</code>, the reference type will be <code>bool ref</code>.
   * 
   * @param tau the monomorphic base type.
   * 
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   */
  public RefType(MonoType tau) {
    if (tau == null) {
      throw new NullPointerException("tau is null");
    }
    this.tau = tau;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the base type of the reference type.
   * 
   * @return the base type.
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
    return new RefType(this.tau.substitute(substitution));
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_REF);
    builder.addBuilder(this.tau.toPrettyStringBuilder(factory), PRIO_REF_TAU);
    builder.addText(" ");
    builder.addType("ref");
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
    if (obj instanceof RefType) {
      RefType other = (RefType)obj;
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
    return ((this.tau.hashCode() + 13) * 17) / 7;
  }
}
