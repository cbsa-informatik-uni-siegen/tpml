package common.interpreters;

import java.util.Enumeration;
import java.util.LinkedList;

import common.AbstractEnvironment;
import expressions.Expression;
import expressions.Location;

/**
 * Mutable implementation of the <code>Store</code> interface, used
 * for the big and small step interpreters.
 *
 * @author Benedikt Meurer
 * @version $Id: MutableStore.java 168 2006-06-15 13:41:38Z benny $
 */
public final class MutableStore extends AbstractEnvironment<Location, Expression> implements Store {
  //
  // Constructor 
  //
  
  /**
   * Allocates a new store, based on the
   * items from the specified <code>store</code>.
   * 
   * @param store the {@link MutableStore}, whose
   *              mappings should be copied.
   */
  public MutableStore(MutableStore store) {
    super(store);
  }
  
  
  
  //
  // Constructor (packaged)
  //
  
  /**
   * Allocates a new empty default store.
   */
  MutableStore() {
    super();
  }
  
  
  
  //
  // Store queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.interpreters.Store#containsLocation(expressions.Location)
   */
  public boolean containsLocation(Location location) {
    return containsSymbol(location);
  }

  /**
   * {@inheritDoc}
   *
   * @see common.interpreters.Store#locations()
   */
  public Enumeration<Location> locations() {
    return symbols();
  }

  

  //
  // Store modification
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
        if (!containsLocation(location)) {
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
  
    // generate the new list with the new mapping
    LinkedList<Mapping<Location, Expression>> newMappings = new LinkedList<Mapping<Location, Expression>>();
    newMappings.add(new Mapping<Location, Expression>(location, expression));
    
    // add remaining entries
    for (Mapping<Location, Expression> mapping : this.mappings) {
      if (!location.equals(mapping.getSymbol())) {
        newMappings.add(mapping);
      }
    }
    
    // use the new mappings
    this.mappings = newMappings;
  }
}
