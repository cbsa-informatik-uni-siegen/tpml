/**
 * 
 */
package expressions;

import java.util.Set;
import java.util.TreeSet;

import types.MonoType;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Implementation of the <b>(REC)</b> rule.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class Recursion extends Expression {
  //
  // Attributes
  //
  
  /**
   * The identifier for the recursion.
   * 
   * @see #getId()
   */
  private String id;
  
  /**
   * The type for the <code>id</code> or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
  /**
   * The body of the recursion.
   * 
   * @see #getE()g
   */
  private Expression e;
  
  
  
  //
  // Constructors
  //
  
  /**
   * Convenience wrapper for {@link #Recursion(String, MonoType, Expression)}
   * passing <code>null</code> for <code>tau</code>.
   * 
   * @param id the identifier of the recursive expression.
   * @param e the expression.
   * 
   * @throws NullPointerException if either <code>id</code> or
   *                              <code>e</code> is <code>null</code>.
   */
  public Recursion(String id, Expression e) {
    this(id, null, e);
  }
  
  /**
   * Allocates a new <b>(REC)</b> expression.
   * 
   * @param id the identifier of the recursive expression.
   * @param e the expression.
   * 
   * @throws NullPointerException if either <code>id</code> or
   *                              <code>e</code> is <code>null</code>.
   */
  public Recursion(String id, MonoType tau, Expression e) {
    if (id == null) {
      throw new NullPointerException("id is null");
    }
    if (e == null) {
      throw new NullPointerException("e is null");
    }
    this.id = id;
    this.tau = tau;
    this.e = e;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * @return Returns the tau.
   */
  public MonoType getTau() {
    return this.tau;
  }
  
  /**
   * @return Returns the e.
   */
  public Expression getE() {
    return this.e;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * Performs the substitution for recursive expressions.
   * 
   * @param id the identifier.
   * @param e the expression to substitute.
   * @return the resulting expression.
   * 
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> free = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId))
        newId = newId + "'";

      // perform the bound renaming
      Expression newE = this.e.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new Recursion(newId, this.tau, newE.substitute(id, e));
    }
  }

  /**
   * Returns the free identifiers of the
   * subexpression minus the identifier of
   * the <b>(REC)</b> expression.
   *
   * @return the free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e.free());
    set.remove(this.id);
    return set;
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * Returns the pretty string builder for rec expressions.
   * @return the pretty string builder for rec expressions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("rec");
    builder.appendText(" " + this.id);
    if (this.tau != null) {
      builder.appendText(":");
      builder.appendText(this.tau.toString());
    }
    builder.appendText(".");
    builder.appendBuilder(this.e.toPrettyStringBuilder(), 0);
    return builder;
  }
}
