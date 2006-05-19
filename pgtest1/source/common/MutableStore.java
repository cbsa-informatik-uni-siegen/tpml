package common;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import expressions.Expression;
import expressions.Location;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class MutableStore implements Store {
  //
  // Inner classes
  //
  
  /**
   * The store entries, mapping locations to values.
   */
  private static class Entry {
    private Location location;
    private Expression expression;
    
    /**
     * Allocates a new mapping for <code>location</code>
     * to the <code>expression</code>.
     * 
     * @param location the {@link Location}
     * @param expression the {@link Expression}
     */
    Entry(Location location, Expression expression) {
      this.location = location;
      this.expression = expression;
    }
    
    /**
     * Returns the {@link Location}.
     * 
     * @return the location.
     */
    public Location getLocation() {
      return this.location;
    }
    
    /**
     * Returns the {@link Expression}.
     * 
     * @return the expression.
     */
    public Expression getExpression() {
      return this.expression;
    }
  }
  
  
  
  //
  // Attributes
  //

  /**
   * The list of entries in the store.
   */
  private LinkedList<Entry> entries;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new empty <code>MutableStore</code>.
   */
  public MutableStore() {
    this.entries = new LinkedList<Entry>();
  }
  
  /**
   * Allocates a new <code>MutableStore</code> that includes
   * all memory locations from the specified <code>store</code>.
   * 
   * @param store another {@link Store}.
   */
  public MutableStore(Store store) {
    if (store instanceof MutableStore) {
      // for mutable stores, we can just COW
      this.entries = ((MutableStore)store).entries;
    }
    else {
      // copy the items from the other store
      this.entries = new LinkedList<Entry>();
      for (Enumeration<Location> enumeration = store.locations(); enumeration.hasMoreElements(); ) {
        Location location = enumeration.nextElement();
        this.entries.add(new Entry(location, store.get(location)));
      }
    }
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
    if (location == null) {
      throw new IllegalArgumentException("location is null");
    }
    for (Entry entry : this.entries) {
      if (location.equals(entry.getLocation())) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see common.Store#get(expressions.Location)
   */
  public Expression get(Location location) {
    if (location == null) {
      throw new IllegalArgumentException("location is null");
    }
    for (Entry entry : this.entries) {
      if (location.equals(entry.getLocation())) {
        return entry.getExpression();
      }
    }
    throw new IllegalArgumentException("location is invalid");
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see common.Store#locations()
   */
  public Enumeration<Location> locations() {
    return new Enumeration<Location>() {
      private Iterator<Entry> iterator = MutableStore.this.entries.iterator();
      public boolean hasMoreElements() { return this.iterator.hasNext(); }
      public Location nextElement() { return this.iterator.next().getLocation(); } 
    };
  }

  
  
  //
  // Allocation/Insertion
  //
  
  /**
   * Allocates a new {@link Location} within this store. This method is
   * used to implement the <code>ref</code> operator, which requires a
   * new location that has not been allocated before.
   * 
   * @return a new {@link Location} within this store.
   * 
   * @see #put(Location, Expression)
   */
  public Location alloc() {
    // try to find a new unique location
    for (String suffix = "";; suffix = suffix + "'") {
      for (int n = 0; n < 26; ++n) {
        // generate the location base character
        char c = (char)('A' + ((('X' - 'A') + n) % 26));
        
        // try the location with this name
        Location location = new Location(c + suffix);
        for (Entry entry : this.entries) {
          if (location.equals(entry.getLocation())) {
            location = null;
            break;
          }
        }
        
        // if we get here and location is not null, it is unique
        if (location != null) {
          return location;
        }
      }
    }
  }
  
  /**
   * Adds a new mapping for the <code>expression</code> to the <code>location</code>.
   * If there's already a mapping for the <code>location</code> the previous
   * mapping is deleted.
   * 
   * @param location the {@link Location} for the <code>value</code>.
   * @param expression the new {@link Expression}.
   * 
   * @throws IllegalArgumentException if either <code>location</code> or
   *                                  <code>expression</code> is <code>null</code>.
   *
   * @see #alloc()
   * @see #get(Location)
   */
  public void put(Location location, Expression expression) {
    // verify the location and expression
    if (location == null) {
      throw new IllegalArgumentException("location is null");
    }
    if (expression == null) {
      throw new IllegalArgumentException("expression is null");
    }
    
    // generate the new list with the new entry
    LinkedList<Entry> newEntries = new LinkedList<Entry>();
    newEntries.add(new Entry(location, expression));
    
    // add remaining entries
    for (Entry entry : this.entries) {
      if (!location.equals(entry.getLocation())) {
        newEntries.add(entry);
      }
    }
    
    // use the new entries
    this.entries = newEntries;
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns the string representation for the mutable
   * store. Should be used for debugging purpose only.
   * 
   * @return the string representation.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (Entry entry : this.entries) {
      if (builder.length() > 1)
        builder.append(", ");
      builder.append(entry.getLocation());
      builder.append(": ");
      builder.append(entry.getExpression());
    }
    builder.append(']');
    return builder.toString();
  }
}
