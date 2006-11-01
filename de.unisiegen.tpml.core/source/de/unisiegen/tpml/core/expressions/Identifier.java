package de.unisiegen.tpml.core.expressions;

import java.util.Collections;
import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Represents an identifier in the expression hierarchy. Identifiers are values wrt the semantics
 * of the various languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class Identifier extends Value {
  //
  // Attributes
  //

  /**
   * The name of the identifier.
   * 
   * @see #getName()
   */
  private String name;

  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new identifier with the given <code>name</code>.
   * 
   * @param name the name of the identifier.
   */
  public Identifier(String name) {
    this.name = name;
  }

  
  
  //
  // Accessors
  //
  
  /**
   * Returns the name of the identifier.
   * @return the name of the identifier.
   */
  public String getName() {
    return this.name;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns a set that contains exacty one element, which is
   * the name of the identifier.
   * 
   * @return a set which contains the name of the identifier.
   * 
   * @see #getName()
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    return Collections.singleton(this.name);
  }

  /**
   * Returns <code>e</code> if <code>id</code> equals the
   * name of the identifier. Else the identifier itself
   * is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the
   *         name of the identifier, else the identifier
   *         itself.
   *
   * @see #getName()
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (id.equals(getName())) {
      return e;
    }
    else {
      return this;
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
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_IDENTIFIER);
    builder.addText(this.name);
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
    if (obj instanceof Identifier) {
      Identifier other = (Identifier)obj;
      return this.name.equals(other.name);
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
    return this.name.hashCode();
  }
}
