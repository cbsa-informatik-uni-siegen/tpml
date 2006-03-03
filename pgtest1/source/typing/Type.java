package typing;

import java.util.Collections;
import java.util.Set;

import smallstep.ArithmeticOperator;
import smallstep.BooleanConstant;
import smallstep.Expression;
import smallstep.IntegerConstant;
import smallstep.RelationalOperator;
import smallstep.UnitConstant;

/**
 * Basic class for all types in the type system.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class Type {
  /**
   * Returns <code>true</code> if the type contains a type
   * variable, whose name is <code>name</code>. Otherwise
   * <code>false</code> is returned.
   * 
   * This method is primarily used to implement the unification
   * algorithm.
   * 
   * @param name the name of the type variable to check for.
   * 
   * @return <code>true</code> if this type contains a type
   *         variable named <code>name</code>, else <code>false</code>.
   */
  public abstract boolean containsTypeVariable(String name);
  
  /**
   * Returns the set of free type variables within this type.
   * 
   * The default implementation for {@link Type} returns the
   * empty set, so you will need to override this method in
   * all types related to type variables.
   * 
   * @return the set of free type variables within this type.
   */
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  /**
   * Applies the {@link Substitution} <code>s</code> to the
   * type and returns the resulting type.
   * 
   * @param s the {@link Substitution} to apply to this type.
   * 
   * @return the resulting {@link Type}.
   */
  abstract Type substitute(Substitution s);
  
  /**
   * Returns the {@link Type} for the given <code>expression</code>
   * if possible, i.e. <b>(BOOL)</b> if <code>expression</code> is 
   * an instance of {@link BooleanConstant}.
   * 
   * @param expression an expression.
   * 
   * @return the {@link Type} for the <code>expression</code>.
   * 
   * @throws IllegalArgumentException if <code>expression</code> is not a
   *                                  simple expression, like a constant,
   *                                  for which it is possible to determine
   *                                  a type out-of-the-box.
   */
  static Type getTypeForExpression(Expression expression) {
    if (expression instanceof BooleanConstant)
      return PrimitiveType.BOOL;
    else if (expression instanceof IntegerConstant)
      return PrimitiveType.INT;
    else if (expression instanceof UnitConstant)
      return PrimitiveType.UNIT;
    else if (expression instanceof ArithmeticOperator)
      return ArrowType.INT_INT_INT;
    else if (expression instanceof RelationalOperator)
      return ArrowType.INT_INT_BOOL;
    
    // no primitive type
    throw new IllegalArgumentException("Cannot determine the type for " + expression);
  }
  
  /**
   * Shared empty set as an optimization for the {@link #free()}
   * method, so we don't need to allocate too many empty sets.
   */
  protected static final Set<String> EMPTY_SET = Collections.unmodifiableSet(Collections.<String>emptySet());
}
