package de.unisiegen.tpml.core.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Static class, which provides several static utility functions for
 * handling integers and related constructs.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see java.lang.Integer
 */
public final class IntegerUtilities {
  /**
   * Transforms a <code>Collection</code> of <code>Integer</code> instances
   * to a simple <code>int</code> array.
   * 
   * @param collection a <code>Collection</code> of <code>Integer</code>s.
   * 
   * @return an <code>int</code> array with the values from the specified
   *         <code>collection</code>.
   */
  public static int[] toArray(Collection<Integer> collection) {
    // check if we have an empty collection here
    if (collection.isEmpty())
      return EMPTY_ARRAY;
    
    // transform the collection into an array
    int[] array = new int[collection.size()];
    Iterator<Integer> iterator = collection.iterator();
    for (int n = 0; iterator.hasNext(); ++n)
      array[n] = iterator.next();
    return array;
  }
  
  /**
   * A shared, empty and immutable integer array, which can be used to
   * avoid allocating empty integer arrays whenever one needs to present
   * an empty integer array.
   */
  public static final int[] EMPTY_ARRAY = new int[0];
}
