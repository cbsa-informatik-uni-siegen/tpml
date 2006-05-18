package common;

import java.util.Enumeration;

import expressions.Expression;
import expressions.Location;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface Store {
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
   * Returns the value stored at the specified <code>location</code>.
   * 
   * @param location a location in this store.
   * 
   * @return the expression stored at the specified <code>location</code>.
   * 
   * @throws IllegalArgumentException if the <code>location</code> is
   *                                  not valid for the store.
   * @throws NullPointerException if the given <code>location</code>
   *                              is <code>null</code>.
   *                              
   * @see #containsLocation(Location)                              
   */
  public Expression get(Location location);
  
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
