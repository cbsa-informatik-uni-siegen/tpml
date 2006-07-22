package expressions;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Represents a memory location.
 *
 * @author Benedikt Meurer
 * @version $Id$
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
   * Allocates a new <code>Location</code> instance
   * with the specified <code>name</code>.
   * 
   * @param the name of the memory location.
   */
  public Location(String name) {
    this.name = name;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the name of the memory location.
   * 
   * @return the name of the memory location.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return true;
  }

  /**
   * Returns the pretty string builder for memory locations.
   * 
   * @return the pretty string builder for memory locations.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendText(this.name);
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns <code>true</code> if <code>obj</code> is an
   * <code>Location</code> with the same name as this
   * location.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> is
   *         the same location.
   *         
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // verify the obj
    if (obj == null || !(obj instanceof Location))
      return false;
    
    // check the location
    Location location = (Location)obj;
    if (location == this || this.name.equals(location.name)) {
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns the hash code for this location.
   * 
   * @return the hash code for this location.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}
