package de.unisiegen.tpml.core.types;

import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * This class represents function types (so called <i>arrow types</i>) in our type systems. Arrow types are composed
 * of two types, the parameter type <code>tau1</code> and the result type <code>tau2</code>, both monomorphic types.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.types.BooleanType
 * @see de.unisiegen.tpml.core.types.IntegerType
 * @see de.unisiegen.tpml.core.types.MonoType
 */
public final class ArrowType extends MonoType {
  //
  // Constants
  //
  
  /**
   * The type for <code>int -&gt; int -&gt; bool</code>.
   */
  public static final ArrowType INT_INT_BOOL = new ArrowType(IntegerType.INT, new ArrowType(IntegerType.INT, BooleanType.BOOL));
  
  /**
   * The type for <code>int -&gt; int -&gt; int</code>.
   */
  public static final ArrowType INT_INT_INT = new ArrowType(IntegerType.INT, new ArrowType(IntegerType.INT, IntegerType.INT));
  
  
  
  //
  // Attributes
  //
  
  /**
   * The parameter type <code>tau1</code>.
   * 
   * @see #getTau1()
   */
  private MonoType tau1;
  
  /**
   * The result type <code>tau2</code>.
   * 
   * @see #getTau2()
   */
  private MonoType tau2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ArrowType</code> with the types <code>tau1</code>
   * and <code>tau2</code>, which represents the type <code>tau1 -&gt; tau2</code>.
   * 
   * @param tau1 the parameter type.
   * @param tau2 the result type.
   * 
   * @throws NullPointerException if either <code>tau1</code> or
   *                              <code>tau2</code> are <code>null</code>.
   */
  public ArrowType(MonoType tau1, MonoType tau2) {
    if (tau1 == null) {
      throw new NullPointerException("tau1 is null");
    }
    if (tau2 == null) {
      throw new NullPointerException("tau2 is null");
    }
    this.tau1 = tau1;
    this.tau2 = tau2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the parameter type <code>tau1</code> for this arrow type.
   * 
   * @return the parameter type <code>tau1</code>.
   * 
   * @see #getTau2()
   */
  public MonoType getTau1() {
    return this.tau1;
  }
  
  /**
   * Returns the result type <code>tau2</code> for this arrow type.
   * 
   * @return the result type <code>tau2</code>.
   * 
   * @see #getTau1()
   */
  public MonoType getTau2() {
    return this.tau2;
  }
  
  

  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see types.Type#free()
   */
  @Override
  public TreeSet<TypeVariable> free() {
    TreeSet<TypeVariable> free = new TreeSet<TypeVariable>();
    free.addAll(this.tau1.free());
    free.addAll(this.tau2.free());
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see types.Type#substitute(types.TypeSubstitution)
   */
  @Override
  public ArrowType substitute(TypeSubstitution substitution) {
    if (substitution == null) {
      throw new NullPointerException("substitution is null");
    }
    return new ArrowType(this.tau1.substitute(substitution), this.tau2.substitute(substitution));
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_ARROW);
    builder.addBuilder(this.tau1.toPrettyStringBuilder(factory), PRIO_ARROW_TAU1);
    builder.addText(" \u2192 ");
    builder.addBuilder(this.tau2.toPrettyStringBuilder(factory), PRIO_ARROW_TAU2);
    return builder;
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Compares this arrow type to the <code>obj</code>.
   * Returns <code>true</code> if the <code>obj</code>
   * is an <code>ArrowType</code> with the same parameter
   * and result types as this instance.
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
    if (obj instanceof ArrowType) {
      ArrowType type = (ArrowType)obj;
      return (this.tau1.equals(type.tau1) && this.tau2.equals(type.tau2));
    }
    return false;
  }
  
  /**
   * Returns a hash value for this arrow type,
   * which is based on the hash values for the
   * parameter and result types.
   * 
   * @return a hash value for this arrow type.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return (this.tau1.hashCode() + this.tau2.hashCode());
  }
}
