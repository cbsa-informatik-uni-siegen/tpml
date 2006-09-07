package de.unisiegen.tpml.core.expressions;

import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Instances of this class are used to represent memory locations as returned by the <code>ref</code>
 * operator and used by the <code>:=</code> (Assign) and <code>!</code> (Deref) operators.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class Location extends Value {
  //
  // Attributes
  //
  
  /**
   * The name of the location (uses uppercase letters).
   * 
   * @see #getName()
   */
  private String name;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Location</code> instance with the specified <code>name</code>.
   * 
   * @param the name of the memory location.
   */
  public Location(String name) {
    this.name = name;
  }
  
  

  //
  // Accessors
  //
  
  /**
   * Returns the name of the memory location.
   * 
   * @return the name of the memory location.
   */
  public String getName() {
    return this.name;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * For <code>Location</code>s, this method always returns the {@link Expression#EMPTY_SET} because
   * locations do not include any free identifiers.
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  /**
   * {@inheritDoc}
   *
   * For <code>Location</code>s, this method always returns the location itself, because substituting
   * below a location is not possible.
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    return this;
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
  protected PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LOCATION);
    builder.addText(this.name);
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Location) {
      Location other = (Location)obj;
      return (this.name.equals(other.name));
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
