package de.unisiegen.tpml.core.interpreters;

import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.util.AbstractEnvironment;

/**
 * Default implementation of the <code>Store</code> interface, which is based on the abstract
 * {@link de.unisiegen.tpml.core.util.AbstractEnvironment} class.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.interpreters.Store
 * @see de.unisiegen.tpml.core.util.AbstractEnvironment
 */
public final class DefaultStore extends AbstractEnvironment<Location, Expression> implements Store {
  //
  // Constructors
  //
  
  /**
   * Default constructor, creates a new store with no mappings.
   */
  public DefaultStore() {
    super();
  }
  
  /**
   * Allocates a new <code>DefaultStore</code>, based on the mappings from the <code>store</code>.
   * 
   * @param store another <code>DefaultStore</code> whose mappings to inherit.
   * 
   * @throws NullPointerException if <code>store</code> is <code>null</code>.
   */
  public DefaultStore(DefaultStore store) {
    super(store);
  }
  
  
  //
  // Store queries
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.interpreters.Store#containsLocation(de.unisiegen.tpml.core.expressions.Location)
   */
  public boolean containsLocation(Location location) {
    return containsSymbol(location);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.interpreters.Store#locations()
   */
  public Enumeration<Location> locations() {
    return symbols();
  }

  
  
  //
  // Store modifications
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.interpreters.Store#alloc()
   */
  public Location alloc() {
    // try to find a new unique location
    for (String suffix = "";; suffix += "'") {
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
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.interpreters.Store#put(de.unisiegen.tpml.core.expressions.Location, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public void put(Location location, Expression expression) {
    if (!expression.isValue()) {
      throw new IllegalArgumentException("expression must be a value");
    }
    super.put(location, expression);
  }
}
