package de.unisiegen.tpml.core.types;

import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * This class represents type variables in our type system.
 * Type variables can either be specified in the source code
 * by the user, in which case the <code>index</code> assigned
 * to this variable is <code>0</code>, or automatically generated
 * by the type inference algorithm, in which case the index will
 * be a positive integer.
 * 
 * The <code>offset</code> is the number of the variable at the
 * specified <code>index</code>. I.e. the first variable specified
 * by the user will have <code>0</code> for <code>index</code> and
 * <code>offset</code>, the second will have an <code>index</code>
 * of <code>0</code> and an <code>offset</code> of <code>1</code>
 * and so on.
 * 
 * The <code>offset</code> determines the greek letter assigned
 * to this variable, i.e. α for <code>0</code>, β for <code>1</code>
 * and so on. The <code>offset</code> is thereby limited to the
 * range <code>0..23</code> (the lower-case greek letters).
 * 
 * If the <code>index</code> is <code>0</code> only the greek letter
 * determined from the <code>offset</code> will be printed. Otherwise,
 * for positive indices, the greek letter plus the index will be
 * printed.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.types.Type
 */
public final class TypeVariable extends MonoType {
  //
  // Attributes
  //
  
  /**
   * The index of the variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number
   * for variables generated by the type inference algorithm. See the
   * description above for further details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int index;
  
  /**
   * The offset for the variable, which identifies the variable together
   * with the <code>index</code>. See the description above for further
   * details.
   * 
   * @see #getIndex()
   * @see #getOffset()
   */
  private int offset;

  
  
  //
  // Constructor
  //
  
  /**
   * @param index
   * @param offset
   * 
   * @throws IllegalArgumentException if <code>index</code> or <code>offset</code>
   *                                  is negative or <code>offset</code> is larger
   *                                  than <code>23</code>.
   *                                  
   * @see TypeVariable
   * @see #index
   * @see #offset
   */
  public TypeVariable(int index, int offset) {
    if (index < 0) {
      throw new IllegalArgumentException("index is negative");
    }
    if (offset < 0) {
      throw new IllegalArgumentException("offset is negative");
    }
    if (offset > 23) {
      throw new IllegalArgumentException("offset too large");
    }
    this.index = index;
    this.offset = offset;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the index of this variable, which is <code>0</code> for variables
   * specified by the user in the source code, and a positive number for 
   * variables generated by the type inference algorithm. See the description
   * of the {@link TypeVariable} class for details about the <code>index</code>
   * and the <code>offset</code>.
   * 
   * @return the index of this variable.
   * 
   * @see #index
   */
  public int getIndex() {
    return this.index;
  }
  
  /**
   * Returns the offset of this variable. See the description of the {@link TypeVariable}
   * class for details about the <code>index</code> and the <code>offset</code>.
   * 
   * @return the offset of this variable.
   * 
   * @see #offset
   */
  public int getOffset() {
    return this.offset;
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
  public Set<TypeVariable> free() {
    TreeSet<TypeVariable> free = new TreeSet<TypeVariable>();
    free.add(this);
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see types.Type#substitute(types.TypeSubstitution)
   */
  @Override
  public MonoType substitute(TypeSubstitution substitution) {
    if (substitution == null) {
      throw new NullPointerException("substitution is null");
    }
    // perform the substitution on this type variable
    MonoType tau = substitution.get(this);
    if (!tau.equals(this)) {
      // another type variable, substitute again
      tau = tau.substitute(substitution);
    }
    return tau;
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_TYPE_VARIABLE);
    builder.addText("" + offsetToGreekLetter(this.offset) + ((this.index > 0) ? this.index : ""));
    return builder;
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Compares this type variable to the <code>obj</code>.
   * Returns <code>true</code> if <code>obj</code> is a
   * type variable with the same index and offset as this
   * type variable. Otherwise <code>false</code> is returned.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> is a type
   *         variable with the same index and offset.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TypeVariable) {
      TypeVariable type = (TypeVariable)obj;
      return (this.index == type.index && this.offset == type.offset);
    }
    return false;
  }
  
  /**
   * Returns a hash value for this type variable, which is
   * based on the index and the offset of the type.
   * 
   * @return a hash value for this type variable.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return (this.index << 5) + this.offset;
  }
  
  
  
  //
  // Internals
  //
  
  /**
   * Returns the greek letter that is assigned to the specified
   * <code>offset</code>.
   * 
   * @param offset the offset to translate to a greek letter.
   * 
   * @return the greek letter that should be used to represent the
   *         specified <code>offset</code>.
   *
   * @throws IllegalArgumentException if <code>offset</code> is
   *                                  larger than <code>23</code>
   *                                  or negative.
   */
  private static char offsetToGreekLetter(int offset) {
    switch (offset) {
    case 0:     return 'α';
    case 1:     return 'β';
    case 2:     return 'γ';
    case 3:     return 'δ';
    case 4:     return 'ε';
    case 5:     return 'ζ';
    case 6:     return 'η';
    case 7:     return 'θ';
    case 8:     return 'ι';
    case 9:     return 'κ';
    case 10:    return 'λ';
    case 11:    return 'μ';
    case 12:    return 'ν';
    case 13:    return 'ξ';
    case 14:    return 'ο';
    case 15:    return 'π';
    case 16:    return 'ρ';
    case 17:    return 'σ';
    case 18:    return 'τ';
    case 19:    return 'υ';
    case 20:    return 'φ';
    case 21:    return 'χ';
    case 22:    return 'ψ';
    case 23:    return 'ω';
    default:    throw new IllegalArgumentException("offset is invalid");
    }
  }
}
