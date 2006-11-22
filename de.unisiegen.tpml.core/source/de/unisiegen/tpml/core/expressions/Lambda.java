package de.unisiegen.tpml.core.expressions;

import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Represents the <b>(ABSTR)</b> expression in the expression hierarchy, which is used for lambda abstractions.
 * 
 * The string representation for lambda abstraction is <pre>lambda id.e</pre>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Application
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class Lambda extends Value {
  //
  // Attributes
  //
  
  /**
   * The identifier of the abstraction parameter.
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
   * The expression of the abstraction body.
   * 
   * @see #getE()
   */
  private Expression e;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new lambda abstraction with the specified
   * identifier <code>id</code> and the given body <code>e</code>.
   * 
   * @param id the identifier of the lambda parameter.
   * @param tau the type for the parameter or <code>null</code>.
   * @param e the body.
   * 
   * @throws NullPointerException if either <code>id</code> or <code>e</code> is <code>null</code>.
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
   * Returns the identifier of the parameter for the lambda expression.
   * 
   * @return the identifier of the parameter for the lambda expression.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Returns the type for the parameter or <code>null</code>.
   * 
   * @return the type for the parameter or <code>null</code>.
   */
  public MonoType getTau() {
    return this.tau;
  }
  
  /**
   * Returns the body of the lambda expression.
   * 
   * @return the bodyof the lambda expression.
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
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public Expression clone() {
    return new Lambda(this.id, this.tau, this.e.clone());
  }
  
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
   * @see #getE()
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    // determine the free identifiers of e1, and
    // make sure it doesn't contain our id
    Set<String> freeE = this.e.free();
    if (freeE.contains(this.id)) {
      // allocate a new set without the identifier
      TreeSet<String> free = new TreeSet<String>(freeE);
      free.remove(this.id);
      return free;
    }
    else {
      // we can just reuse the free set
      return freeE;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Expression substitute(TypeSubstitution substitution) {
    MonoType tau = (this.tau != null) ? this.tau.substitute(substitution) : null;
    return new Lambda(this.id, tau, this.e.substitute(substitution));
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
   * @see #getE()
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Lambda substitute(String id, Expression e) {
    // check if the identifier is the same as our identifier,
    // in which case no substitution is performed below the
    // lambda expression
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers of this lambda
      Set<String> free = free();
      
      // determine the free identifiers for e
      Set<String> freeE = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId) || freeE.contains(newId) || newId.equals(id)) {
        newId = newId + "'";
      }

      // perform the bound renaming (if required)
      Expression newE = (this.id == newId) ? this.e : this.e.substitute(this.id, new Identifier(newId));
      
      // perform the substitution for e1
      newE = newE.substitute(id, e);
      
      // reuse this abstraction object if possible
      return (this.e == newE) ? this : new Lambda(newId, this.tau, newE);
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LAMBDA);
    builder.addKeyword("\u03bb");
    builder.addText(this.id);
    if (this.tau != null) {
      builder.addText(":");
      builder.addBuilder(this.tau.toPrettyStringBuilder(factory), PRIO_LAMBDA_TAU);
    }
    builder.addText(".");
    builder.addBuilder(this.e.toPrettyStringBuilder(factory), PRIO_LAMBDA_E);
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
    if (obj instanceof Lambda) {
      Lambda other = (Lambda)obj;
      return (this.id.equals(other.id) && this.e.equals(other.e)
          && ((this.tau == null) ? (other.tau == null) : (this.tau.equals(other.tau))));
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
    return this.id.hashCode() + ((this.tau != null) ? this.tau.hashCode() : 0) + this.e.hashCode();
  }
}
