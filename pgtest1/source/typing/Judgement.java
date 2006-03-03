package typing;

import smallstep.Expression;

/**
 * A type judgement is the atomic item of the typing proof, it
 * consists of the expression, for which the type should be
 * determined, the type environment within which the type
 * of the expression should be determined, and the type of
 * the expression (may be a type variable of no concrete
 * type is known). 
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Judgement {
  /**
   * Allocates a new type {@link Judgement} with the given
   * <code>environment</code>, <code>expression</code> and
   * <code>type</code>.
   * 
   * @param environment the type {@link Environment} in which to
   *                    determine the type of expression.
   * @param expression the {@link Expression} for which a type
   *                   should be determined.
   * @param type the {@link Type} for this judgement, which may be
   *             a {@link TypeVariable} if no concrete type is known
   *             yet.
   */
  Judgement(Environment environment, Expression expression, Type type) {
    this.environment = environment;
    this.expression = expression;
    this.type = type;
  }
  
  /**
   * Returns the type environment for this type judgement,
   * that is, the environment in which the type of the
   * expression was determined.
   * 
   * @return the type environment for this type judgement.
   */
  public Environment getEnvironment() {
    return this.environment;
  }
  
  /**
   * Returns the expression for this type judgement.
   * 
   * @return the expression for this type judgement.
   */
  public Expression getExpression() {
    return this.expression;
  }
  
  /**
   * Return the type for this type judgement, which is
   * either a type variable or a concrete type.
   * 
   * @return the type for this judgement.
   */
  public Type getType() {
    return this.type;
  }
  
  /**
   * Applies the <code>substitution</code> to the environment
   * and the type of this judgement.
   * 
   * @param substitution the {@link Substitution} to apply.
   * 
   * @return the new judgement.
   */
  Judgement substitute(Substitution substitution) {
    // apply the substitution to the environment
    Environment environment = this.environment.substitute(substitution);
    
    // apply the substitution to the type
    Type type = this.type.substitute(substitution);
    
    // check if anything changed
    if (environment != this.environment || type != this.type)
      return new Judgement(environment, this.expression, type);
    else
      return this;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is a
   * {@link Judgement} and is equal to this type judgement.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if the <code>obj</code> is
   *         equal to this judgement.
   *         
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    else if (obj instanceof Judgement) {
      Judgement judgement = (Judgement)obj;
      return (this.environment.equals(judgement.environment)
           && this.expression.equals(judgement.expression)
           && this.type.equals(judgement.type));
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns a string representation of the type judgement,
   * which is primarly useful for debugging purposes.
   * 
   * @return the string represention of the type judgement.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.environment + " > " + this.expression + " :: " + this.type; 
  }
  
  // member attributes
  private Environment environment;
  private Expression expression;
  private Type type;
}
