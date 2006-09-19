package de.unisiegen.tpml.core.expressions;

import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Instances of this class are used to represent recursive expressions in the expression hierarchy.
 * 
 * The string representation for recursive expressions is <code>rec id.e</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
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
   * The subexpression for the recursion.
   * 
   * @see #getE()
   */
  private Expression e;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Recursion</code> with the <code>id</code> and <code>e</code>.
   * 
   * @param id the identifier.
   * @param e the sub expression.
   * 
   * @throws NullPointerException if <code>id</code> or <code>e</code> is <code>null</code>.
   */
  public Recursion(String id, Expression e) {
    if (id == null) {
      throw new NullPointerException("id is null");
    }
    if (e == null) {
      throw new NullPointerException("e is null");
    }
    this.id = id;
    this.e = e;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifier for the recursion.
   * 
   * @return the identifier for the recursion.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Returns the recursion body.
   * 
   * @return the recursion body.
   */
  public Expression getE() {
    return this.e;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(this.e.free());
    free.remove(this.id);
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
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
      return new Recursion(newId, newE.substitute(id, e));
    }
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public @Override PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_REC);
    builder.addKeyword("rec");
    builder.addText(" " + this.id + ".");
    builder.addBuilder(this.e.toPrettyStringBuilder(factory), PRIO_REC_E);
    return builder;
  }

  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Recursion) {
      Recursion other = (Recursion)obj;
      return (this.id.equals(other.id) && this.e.equals(other.e));
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @Override
  public int hashCode() {
    return this.id.hashCode() + this.e.hashCode();
  }
}
