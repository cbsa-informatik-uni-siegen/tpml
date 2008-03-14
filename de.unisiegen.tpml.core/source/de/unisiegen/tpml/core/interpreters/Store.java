package de.unisiegen.tpml.core.interpreters;


import java.util.Enumeration;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Location;
import de.unisiegen.tpml.core.util.Environment;


/**
 * Base interface to stores used in the big and small step interpreters to
 * implement memory operations. A store basicly maps
 * {@link de.unisiegen.tpml.core.expressions.Location}s to
 * {@link de.unisiegen.tpml.core.expressions.Expression}s, i.e. for every
 * location present within a store, the expression at the specified location can
 * be determined.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.expressions.Location
 */
public interface Store extends Environment < Location, Expression >
{

  //
  // Store queries
  //
  /**
   * Returns <code>true</code> if this store contains the specified
   * <code>location</code>.
   * 
   * @param location the {@link Location} whose existance within this store to
   *          test.
   * @return <code>true</code> if the <code>location</code> is present
   *         within this store.
   * @throws NullPointerException if the given <code>location</code> is
   *           <code>null</code>.
   * @see Environment#containsSymbol(Object)
   * @see Environment#get(Object)
   */
  public boolean containsLocation ( Location location );


  /**
   * Returns an enumeration of the {@link Location}s within this store, in the
   * reverse order of their allocation. That is, the last allocated
   * <code>Location</code> is returned first by the enumeration.
   * 
   * @return an enumeration of the {@link Location}s within this store.
   * @see Environment#symbols()
   */
  public Enumeration < Location > locations ();


  //
  // Store modifications
  //
  /**
   * Allocates a new {@link Location} within this store. This method is used to
   * implement the <code>ref</code> operator in the big and small step
   * interpreters, which requires a new location that has not been allocated
   * before.
   * 
   * @return a new {@link Location} within this store.
   * @see #containsLocation(Location)
   */
  public Location alloc ();


  /**
   * Adds a new mapping for the <code>expression</code> to the
   * <code>location</code>. If there is already a mapping for the
   * <code>location</code> the previous mapping is deleted.
   * 
   * @param location the {@link Location} for the <code>expression</code>
   * @param expression the {@link Expression}.
   * @throws IllegalArgumentException if <code>expression</code> is not a
   *           value (see the description for the {@link Expression#isValue()}
   *           method).
   * @throws NullPointerException if either <code>location</code> or
   *           <code>expression</code> is <code>null</code>.
   * @see #alloc()
   * @see #containsLocation(Location)
   * @see Environment#get(Object)
   * @see Expression#isValue()
   */
  public void put ( Location location, Expression expression );
}
