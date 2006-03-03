package typing;

/**
 * A list of type equations.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see typing.Equation
 */
final class EquationList {
  /**
   * Allocates a new equation list, which basicly extends
   * <code>remaining</code> with a new {@link Equation}
   * <code>first</code>.
   * 
   * @param first the new {@link Equation}.
   * @param remaining an existing {@link EquationList}
   */
  EquationList(Equation first, EquationList remaining) {
    this.first = first;
    this.remaining = remaining;
  }
  
  /**
   * Applies the {@link Substitution} <code>s</code> to all
   * equations contained within this list.
   * 
   * @param s the {@link Substitution} to apply.
   * 
   * @return the resulting list of equations.
   * 
   * @see Equation#substitute(Substitution)
   */
  EquationList substitute(Substitution s) {
    // nothing to substitute on the empty list
    if (this == EMPTY_LIST)
      return this;
    
    // apply the substitution to the first equation
    Equation first = this.first.substitute(s);
    
    // apply the substitution to the remaining equations
    EquationList remaining = this.remaining.substitute(s);
    
    // check if anything changed, otherwise we can reuse
    // the existing equation list
    if (first != this.first || remaining != this.remaining)
      return new EquationList(first, remaining);
    else
      return this;
  }
  
  /**
   * This method implements the unification algorithm.
   * 
   * It returns the unificator for this type equation
   * list.
   * 
   * @return the unificator for this type equation. 
   * 
   * @throws UnificationException if one of the equations contained
   *                              within this list could not be unified.
   */
  Substitution unify() throws UnificationException {
    // an empty type equation list is easy to unify
    if (this == EMPTY_LIST)
      return Substitution.EMPTY_SUBSTITUTION;
    
    // otherwise, we examine the first equation in the list
    Type left = this.first.getLeft();
    Type right = this.first.getRight();
    
    // different actions, depending on the exact types
    if (left instanceof TypeVariable || right instanceof TypeVariable) {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = (TypeVariable)(left instanceof TypeVariable ? left : right);
      Type tau = (left instanceof TypeVariable ? right : left);
      
      // either tvar equals tau or tvar is not present in tau
      if (tvar.equals(tau) || !tau.containsTypeVariable(tvar.getName())) {
        Substitution s1 = new Substitution(tvar.getName(), tau);
        Substitution s2 = this.remaining.substitute(s1).unify();
        return s1.compose(s2);
      }
      
      // FALL-THROUGH: Otherwise it's a type error
    }
    else if (left instanceof ArrowType && right instanceof ArrowType) {
      // cast to ArrowType instances (tau and tau')
      ArrowType taul = (ArrowType)left;
      ArrowType taur = (ArrowType)right;
      
      // we need to check {tau1 = tau1', tau2 = tau2'} as well
      EquationList eqns = this.remaining;
      eqns = new EquationList(new Equation(taul.getT2(), taur.getT2()), eqns);
      eqns = new EquationList(new Equation(taul.getT1(), taur.getT1()), eqns);
      
      // try to unify the new list
      return eqns.unify();
    }
    else if (left.equals(right)) {
      // the types equal, just unify the remaining equations then
      return this.remaining.unify();
    }
  
    // (left = right) cannot be unified
    throw new UnificationException(this.first);
  }

  /**
   * Returns the string representation of the equations
   * contained in this list. This method is mainly useful
   * for debugging purposes.
   * 
   * @return the string representation.
   * 
   * @see Equation#toString()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    builder.append('{');
    for (EquationList list = this; list != EMPTY_LIST; list = list.remaining) {
      if (list != this)
        builder.append(", ");
      builder.append(list.first);
    }
    builder.append('}');
    return builder.toString();
  }
  
  /**
   * The empty equation list.
   */
  static final EquationList EMPTY_LIST = new EquationList();
  
  private EquationList() {
  }
  
  // member attributes
  private Equation first;
  private EquationList remaining;
}
