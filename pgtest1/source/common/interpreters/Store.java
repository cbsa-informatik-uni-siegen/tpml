package common.interpreters;

import java.util.Enumeration;

import common.Environment;

import expressions.Expression;
import expressions.Location;

/**
 * Base interface for stores used in the big and small step
 * interpreters to implement memory operations.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.interpreters.MutableStore
 */
public interface Store extends Environment<Location, Expression>{
  //
  // Constants
  //
  
  /**
   * Default empty store instance.
   */
  public static final MutableStore EMPTY_STORE = new MutableStore();
  
  

  //
  // Store queries
  //
  
  /**
   * Returns <code>true</code> if this store contains the
   * specified <code>location</code>.
   * 
   * @param location the {@link Location} to look for.
   * 
   * @return <code>true</code> if <code>location</code> is
   *         valid within this store.
   *         
   * @throws NullPointerException if the given <code>location</code>
   *                              is <code>null</code>.
   *                              
   * @see #get(Location)                                       
   */
  public boolean containsLocation(Location location);
  
  /**
   * Returns an enumeration of the {@link Location}s within this
   * store, in the reverse order of the allocation.
   * 
   * @return an enumeration of the {@link Locations}s.
   * 
   * @see #get(Location)
   */
  public Enumeration<Location> locations();
}
