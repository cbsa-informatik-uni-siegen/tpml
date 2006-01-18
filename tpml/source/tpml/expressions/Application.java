package tpml.expressions;

import java.util.Set;
import java.util.TreeSet;

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
   * @see tpml.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    // determine the free identifiers for the sub expressions
    Set<String> freeE1 = this.e1.free();
    Set<String> freeE2 = this.e2.free();
    
    // check if both sub expression contain no free identifiers
    if (freeE1 == Expression.EMPTY_SET && freeE2 == Expression.EMPTY_SET)
      return Expression.EMPTY_SET;
    
    // allocate a new set
    Set<String> free = new TreeSet<String>();
    free.addAll(this.e1.free());
    free.addAll(this.e2.free());
    
    return free;
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
   * @see tpml.expressions.Expression#substitute(java.lang.String, tpml.expressions.Expression)
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
