package de.unisiegen.tpml.expressions;

import java.util.Set;

/**
 * Represents the <b>(APP)</b> expression in the expression
 * hierarchy.
 * 
 * The string representation for applications is:
 * <pre>e1 e2</pre>
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Application extends Expression {
  /**
   * Allocates a new application of <code>e1</code> to <code>e2</code>.
   * 
   * @param e1 the first expression (the operation).
   * @param e2 the second expression (the operand).
   */
  public Application(Expression e1, Expression e2) {
    this.e1 = e1;
    this.e2 = e2;
  }
  
  /**
   * Returns the free (unbound) identifiers of the application.
   * 
   * The free (unbound) identifiers of the application are simply
   * determined by querying the free identifiers of the two sub
   * expressions.
   * 
   * @return the free identifiers for the application.
   * 
   * @see #getE1()
   * @see #getE2()
   * @see de.unisiegen.tpml.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    // determine the free identifiers for the sub expressions
    Set<String> freeE1 = this.e1.free();
    Set<String> freeE2 = this.e2.free();
    
    // check if any of the sub expressions contains no free identifiers
    if (freeE1 == Expression.EMPTY_SET)
      return freeE2;
    else if (freeE2 == Expression.EMPTY_SET)
      return freeE1;
    
    // add all free identifiers from e2 to the set returned for e1
    freeE1.addAll(freeE2);
    return freeE1;
  }

  /**
   * Substitutes <code>e</code> for <code>id</code> in the two
   * sub expressions of the application.
   * 
   * @param id the identifier for which to substitute.
   * @param e the expression to substitute for <code>id</code>.
   * 
   * @return the resulting expression.
   * 
   * @see #getE1()
   * @see #getE2()
   * @see de.unisiegen.tpml.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // substitute for the two sub expression
    Expression e1 = this.e1.substitute(id, e);
    Expression e2 = this.e2.substitute(id, e);
    
    // check if we can reuse this application object
    if (e1 == this.e1 && e2 == this.e2)
      return this;
    
    // generate a new application
    return new Application(e1, e2);
  }
  
  /**
   * Returns the first expression of the application.
   * @return the first expression of the application.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second expression of the application.
   * @return the second expression of the application.
   */
  public Expression getE2() {
    return this.e2;
  }

  private Expression e1;
  private Expression e2;
}
