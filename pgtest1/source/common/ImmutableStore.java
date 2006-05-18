package common;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import expressions.Expression;
import expressions.Location;

/**
 * An immutable implementation of the {@link Store}, which
 * represents an empty store.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.Store
 * @see common.MutableStore
 */
final class ImmutableStore implements Store {
  //
  // Constants
  //
  
  /**
   * The single instance of the <code>ImmutableStore</code>
   * class which will be used for {@link ProofNode}s without
   * memory locations.
   */
  static final ImmutableStore EMPTY_STORE = new ImmutableStore();
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ImmutableStore</code>.
   */
  private ImmutableStore() {
    // nothing to do here...
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   * 
   * @see common.Store#containsLocation(expressions.Location)
   */
  public boolean containsLocation(Location location) {
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see common.Store#get(expressions.Location)
   */
  public Expression get(Location location) {
    throw new IllegalArgumentException("location is invalid"); 
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see common.Store#locations()
   */
  public Enumeration<Location> locations() {
    return new Enumeration<Location>() {
      public boolean hasMoreElements() { return false; }
      public Location nextElement() { throw new NoSuchElementException("no more elements"); }
    };
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns the string representation for the
   * immutable, empty store. Should be used for
   * debugging purpose only.
   * 
   * @return the string representation.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "[]";
  }
}
