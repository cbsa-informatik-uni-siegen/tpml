package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeVariable;

/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in OCaml.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.typechecker.TypeEquation
 */
final class TypeEquationList {
  //
  // Constants
  //
  
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationList()
   */
  static final TypeEquationList EMPTY_LIST = new TypeEquationList();

  
  
  //
  // Attributes
  //

  /**
   * The first equation in the list.
   */
  private TypeEquation first;
  
  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeEquationList remaining;
  
  
  
  //
  // Constructors (private)
  //
  
  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private TypeEquationList() {
    super();
  }
  
  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code> with a new {@link TypeEquation}
   * <code>first</code>.
   * 
   * @param first the new {@link TypeEquation}.
   * @param remaining an existing {@link TypeEquationList}
   * 
   * @throws NullPointerException if <code>first</code> or <code>remaining</code> is <code>null</code>.
   */
  private TypeEquationList(TypeEquation first, TypeEquationList remaining) {
    if (first == null) {
      throw new NullPointerException("first is null");
    }
    if (remaining == null) {
      throw new NullPointerException("remaining is null");
    }
    this.first = first;
    this.remaining = remaining;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * Allocates a new {@link TypeEquationList}, which extends this equation list with a new {@link TypeEquation}
   * for <code>left</code> and <code>right</code>.
   * 
   * @param left the left side of the new equation.
   * @param right the right side of the new equation.
   * 
   * @return the extended {@link TypeEquationList}.
   * 
   * @throws NullPointerException if <code>left</code> or <code>right</code> is <code>null</code>.
   */
  TypeEquationList extend(MonoType left, MonoType right) {
    return new TypeEquationList(new TypeEquation(left, right), this);
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
  TypeEquationList substitute(TypeSubstitution s) {
    // nothing to substitute on the empty list
    if (this == EMPTY_LIST) {
      return this;
    }
    
    // apply the substitution to the first and the remaining equations
    return new TypeEquationList(this.first.substitute(s), this.remaining.substitute(s));
  }
  
  
  
  //
  // Unification
  //
  
  /**
   * This method is the heart of the unification algorithm implementation. It returns the unificator for
   * this type equation list.
   * 
   * @return the unificator for this type equation. 
   * 
   * @throws UnificationException if one of the equations contained within this list could not be unified.
   */
  DefaultTypeSubstitution unify() throws UnificationException {
    // an empty type equation list is easy to unify
    if (this == EMPTY_LIST) {
      return DefaultTypeSubstitution.EMPTY_SUBSTITUTION;
    }
    
    // otherwise, we examine the first equation in the list
    MonoType left = this.first.getLeft();
    MonoType right = this.first.getRight();
    
    // different actions, depending on the exact types
    if (left instanceof TypeVariable || right instanceof TypeVariable) {
      // the left or right side of the equation is a type variable
      TypeVariable tvar = (TypeVariable)(left instanceof TypeVariable ? left : right);
      MonoType tau = (left instanceof TypeVariable ? right : left);
      
      // either tvar equals tau or tvar is not present in tau
      if (tvar.equals(tau) || !tau.free().contains(tvar)) {
        DefaultTypeSubstitution s1 = new DefaultTypeSubstitution(tvar, tau);
        DefaultTypeSubstitution s2 = this.remaining.substitute(s1).unify();
        return s1.compose(s2);
      }
      
      // FALL-THROUGH: Otherwise it's a type error
    }
    else if (left instanceof ArrowType && right instanceof ArrowType) {
      // cast to ArrowType instances (tau and tau')
      ArrowType taul = (ArrowType)left;
      ArrowType taur = (ArrowType)right;
      
      // we need to check {tau1 = tau1', tau2 = tau2'} as well
      TypeEquationList eqns = this.remaining;
      eqns = eqns.extend(taul.getTau2(), taur.getTau2());
      eqns = eqns.extend(taul.getTau1(), taur.getTau1());
      
      // try to unify the new list
      return eqns.unify();
    }
    // TODO: Tuple Types
    /*else if (left instanceof TupleType && right instanceof TupleType) {
      // cast to TupleType instances (tau and tau')
      TupleType taul = (TupleType)left;
      TupleType taur = (TupleType)right;
      
      // check if the arities match
      if (taul.arity() == taur.arity()) {
        // determine the sub types
        MonoType[] typesl = taul.getTypes();
        MonoType[] typesr = taur.getTypes();
        
        // check all sub types
        EquationList eqns = this.remaining;
        for (int n = 0; n < typesl.length; ++n)
          eqns = new EquationList(new Equation(typesl[n], typesr[n]), eqns);
        
        // try to unify the new list
        return eqns.unify();
      }
      
      // FALL-THROUGH: Otherwise it's a type error
    }*/
    else if (left.equals(right)) {
      // the types equal, just unify the remaining equations then
      return this.remaining.unify();
    }
  
    // (left = right) cannot be unified
    throw new UnificationException(this.first);
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Returns the string representation of the equations contained in this list. This method is mainly useful
   * for debugging purposes.
   * 
   * @return the string representation.
   * 
   * @see TypeEquation#toString()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    builder.append('{');
    for (TypeEquationList list = this; list != EMPTY_LIST; list = list.remaining) {
      if (list != this)
        builder.append(", ");
      builder.append(list.first);
    }
    builder.append('}');
    return builder.toString();
  }
}
