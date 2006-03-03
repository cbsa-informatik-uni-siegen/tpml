package typing;

/**
 * Exception to indicate that an error occurred during the
 * unification algorithm, i.e. that a type equation could
 * not be unified.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnificationException extends Exception {
  /**
   * Allocates a new {@link UnificationException} object to
   * indicate that the unification of the type {@link Equation}
   * <code>equationn</code> failed.
   * 
   * @param equation the {@link Equation} that could not be unified.
   */
  UnificationException(Equation equation) {
    super("Cannot unify " + equation);
    this.equation = equation;
  }
  
  /**
   * Returns the type on the left side of the type equation that
   * could not be unified.
   * 
   * @return the type on the left side of the type equation.
   */
  public Type getTau1() {
    return this.equation.getLeft();
  }
  
  /**
   * Returns the type on the right side of the type equation that
   * could not be unified.
   *  
   * @return the type on the rightside of the type equation.
   */
  public Type getTau2() {
    return this.equation.getRight();
  }
  
  // member attributes
  private Equation equation;
  
  // unique serialization id
  private static final long serialVersionUID = -8919516367132400570L;
}
