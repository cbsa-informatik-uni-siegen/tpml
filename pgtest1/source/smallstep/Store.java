package smallstep;

import java.util.Enumeration;

import common.Environment;

import expressions.Expression;
import expressions.Location;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface Store extends Environment<Location, Expression>{
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
