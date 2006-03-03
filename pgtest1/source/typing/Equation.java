package typing;

/**
 * Represents a type equation. Used primarily for the
 * unification algorithm.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 */
final class Equation {
  /**
   * Allocates a new type equation with the given
   * <code>left</code> and <code>right</code> side
   * types.
   * 
   * @param left the type on the left side.
   * @param right the type on the right side.
   */
  Equation(Type left, Type right) {
    this.left = left;
    this.right = right;
  }
  
  /**
   * Returns the type on the left side of the equation.
   * 
   * @return the left side type.
   */
  Type getLeft() {
    return this.left;
  }
  
  /**
   * Returns the type on the right side of the equation.
   * 
   * @return the right side type.
   */
  Type getRight() {
    return this.right;
  }
  
  /**
   * Applies the {@link Substitution} <code>s</code> to the
   * types on both sides of the equation and returns the
   * resulting equation.
   * 
   * @param s the {@link Substitution} to apply.
   * 
   * @return the resulting {@link Equation}.
   * 
   * @see Type#substitute(Substitution)
   */
  Equation substitute(Substitution s) {
    // apply the substitution to both types
    Type left = this.left.substitute(s);
    Type right = this.right.substitute(s);
    
    // check if anything changed, otherwise we
    // can reuse the existing object
    if (left != this.left || right != this.right)
      return new Equation(left, right);
    else
      return this;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is
   * an {@link Equation}, whose operands are equal to
   * this equation.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if object equals this.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Equation) {
      Equation eqn = (Equation)obj;
      return (this.left.equals(eqn.left)
           && this.right.equals(eqn.right));
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns the string representation of the equation.
   * 
   * @return the string representaiton of the equation.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return (this.left + " = " + this.right);
  }
  
  // member attributes
  private Type left;
  private Type right;
}
