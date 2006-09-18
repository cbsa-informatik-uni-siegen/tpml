package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.types.MonoType;

/**
 * Represents a type equation. Used primarily for the unification algorithm.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeEquationList
 */
final class TypeEquation {
  //
  // Attributes
  //
  
  /**
   * The monomorphic type on the left side.
   * 
   * @see #getLeft()
   */
  private MonoType left;
  
  /**
   * The monomorphic type on the right side.
   * 
   * @see #getRight()
   */
  private MonoType right;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>TypeEquation</code> with the given <code>left</code> and <code>right</code>
   * side types.
   * 
   * @param left the monomorphic type on the left side.
   * @param right the monomorphic type on the right side.
   * 
   * @throws NullPointerException if <code>left</code> or <code>right</code> is <code>null</code>.
   */
  TypeEquation(MonoType left, MonoType right) {
    if (left == null) {
      throw new NullPointerException("left is null");
    }
    if (right == null) {
      throw new NullPointerException("right is null");
    }
    this.left = left;
    this.right = right;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the monomorphic type on the left side.
   * 
   * @return the left side type.
   */
  public MonoType getLeft() {
    return this.left;
  }
  
  /**
   * Returns the monomorphic type on the right side.
   * 
   * @return the right side type.
   */
  public MonoType getRight() {
    return this.right;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Applies the {@link TypeSubstitution} <code>s</code> to the types on both sides of the equation and
   * returns the resulting equation.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * 
   * @return the resulting {@link TypeEquation}.
   * 
   * @see de.unisiegen.tpml.core.types.Type#substitute(TypeSubstitution)
   */
  TypeEquation substitute(TypeSubstitution s) {
    // apply the substitution to the left and the right side
    return new TypeEquation(this.left.substitute(s), this.right.substitute(s));
  }
  

  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   * 
   * Returns the string representation for the type equation, which is primarily useful for debugging.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return (this.left + " = " + this.right);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TypeEquation) {
      TypeEquation other = (TypeEquation)obj;
      return (this.left.equals(other.left) && this.right.equals(other.right));
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
    return this.left.hashCode() + this.right.hashCode();
  }
}

