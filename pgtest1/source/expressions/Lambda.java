package expressions;

import java.util.Set;
import java.util.TreeSet;

import types.MonoType;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Represents a lambda abstraction.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Lambda extends Value {
  //
  // Attributes
  //

  /**
   * The identifier for the parameter.
   * 
   * @see #getId()
   */
  private String id;
  
  /**
   * The type of the parameter or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
  /**
   * The expression in the body.
   * 
   * @see #getE()
   */
  private Expression e;
  
  
  
  //
  // Constructors
  //
  
  /**
   * Convenience wrapper for {@link #Lambda(String, MonoType, Expression)}
   * passing <code>null</code> for <code>tau</code>.
   * 
   * @param id the name of the parameter.
   * @param e the expression.
   * 
   * @throws NullPointerException if <code>id</code> or <code>e</code>
   *                              is <code>null</code>.
   */
  public Lambda(String id, Expression e) {
    this(id, null, e);
  }
  
  /**
   * Generates a new abstraction.
   * 
   * @param id the name of the parameter.
   * @param tau the {@link types.MonoType} for the <code>id</code>,
   *            or <code>null</code> if no type is known.
   * @param e the expression.
   * 
   * @throws NullPointerException if <code>id</code> or <code>e</code>
   *                              is <code>null</code>.
   */
  public Lambda(String id, MonoType tau, Expression e) {
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
   * Performs the substitution for <b>(LAMBDA)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
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
      return new Lambda(newId, this.tau, newE.substitute(id, e));
    }
  }

  /**
   * Returns the free identifiers minus the bound identifier.
   * @return the free identifiers minus the bound identifier.
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e.free());
    set.remove(this.id);
    return set;
  }
  
  /**
   * Returns the pretty string builder for abstractions.
   * @return the pretty string builder for abstractions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("\u03bb");
    builder.appendText(this.id);
    if (this.tau != null) {
      builder.appendText(":");
      builder.appendText(this.tau.toString());
    }
    builder.appendText(".");
    builder.appendBuilder(this.e.toPrettyStringBuilder(), 0);
    return builder;
  }
}
