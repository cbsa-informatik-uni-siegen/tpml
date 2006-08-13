package de.unisiegen.tpml.core.expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the <b>(ABSTR)</b> expression in the expression
 * hierarchy, which is used for lambda abstractions.
 * 
 * The string representation for lambda abstraction is:
 * <pre>lambda id.e1</pre>
 *
 * @author Benedikt Meurer
 * @version $Id:Abstraction.java 66 2006-01-19 17:07:56Z benny $
 */
public final class Abstraction extends Expression {
  //
  // Attributes
  //
  
  /**
   * The identifier of the abstraction parameter.
   */
  private String id;
  
  /**
   * The expression of the abstraction body.
   */
  private Expression e1;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new lambda abstraction with the specified
   * identifier <code>id</code> and the given sub expression
   * <code>e1</code>.
   * 
   * @param id the identifier of the lambda parameter.
   * @param e1 the sub expression.
   */
  public Abstraction(String id, Expression e1) {
    this.id = id;
    this.e1 = e1;
  }

  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifier of the parameter for the lambda expression.
   * @return the identifier of the parameter for the lambda expression.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Returns the subexpression of the lambda expression.
   * @return the subexpression of the lambda expression.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the free (unbound) identifiers of the lambda abstraction.
   * 
   * The free (unbound) identifiers of the lambda abstraction are
   * determined by querying the free identifiers of the <code>e1</code>
   * subexpression, and removing the identifier <code>id</code> from
   * the returned set.
   * 
   * @return the free identifiers for the lambda abstraction.
   * 
   * @see #getId()
   * @see #getE1()
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    // determine the free identifiers of e1, and
    // make sure it doesn't contain our id
    Set<String> freeE1 = this.e1.free();
    if (freeE1.contains(this.id)) {
      // allocate a new set without the identifier
      TreeSet<String> free = new TreeSet<String>(freeE1);
      free.remove(this.id);
      return free;
    }
    else {
      // we can just reuse the free set
      return freeE1;
    }
  }

  /**
   * Substitutes <code>e</code> for <code>id</code> within the
   * lambda expression, performing a bound rename if necessary
   * to avoid altering the binding of existing identifiers
   * within the sub expression.
   * 
   * @param id the identifier for which to substitute.
   * @param e the expression to substitute for <code>id</code>.
   * 
   * @return the resulting expression.
   * 
   * @see #getId()
   * @see #getE1()
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // check if the identifier is the same as our identifier,
    // in which case no substitution is performed below the
    // lambda expression
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> freeE = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (freeE.contains(newId))
        newId = newId + "'";

      // perform the bound renaming (if required)
      Expression newE1 = (this.id == newId) ? this.e1 : this.e1.substitute(this.id, new Identifier(newId));
      
      // perform the substitution for e1
      newE1 = newE1.substitute(id, e);
      
      // reuse this abstraction object if possible
      return (this.e1 == newE1) ? this : new Abstraction(newId, newE1);
    }
  }
}
