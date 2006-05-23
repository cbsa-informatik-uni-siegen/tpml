package util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Various static string utility functions.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class StringUtilities {
  //
  // Constructor (private)
  //
  
  /**
   * Cannot create instances of this class.
   */
  private StringUtilities() {
    // nothing to do
  }
  
  
  
  //
  // Collection methods
  //
  
  /**
   * Joins a number of <code>strings</code> together to form one long string,
   * with the optional <code>separator</code> inserted between each of them.
   * 
   * This method is implemented as convenience wrapper for the 
   * {@link #join(String, Collection)} method.
   * 
   * @param separator a string to insert between each of the <code>strings</code>
   *                  or <code>null</code>.
   * @param strings a variable list (an array) of strings.
   * 
   * @return a string containing all of the <code>strings</code> joined together
   *         with <code>separator</code> between them.
   *         
   * @see #join(String, Collection)         
   */
  public static String join(String separator, String... strings) {
    return join(separator, Arrays.asList(strings));
  }
  
  /**
   * Joins a number of <code>strings</code> together to form one long string,
   * with the optional <code>separator</code> inserted between each of them.
   * 
   * @param separator a string to insert between each of the <code>strings</code>
   *                  or <code>null</code>.
   * @param strings a {@link Collection} of strings.
   * 
   * @return a string containing all of the <code>strings</code> joined together
   *         with <code>separator</code> between them.
   *         
   * @throws NullPointerException if <code>strings</code> is <code>null</code>.         
   */
  public static String join(String separator, Collection<String> strings) {
    StringBuilder builder = new StringBuilder();
    for (String string : strings) {
      if (builder.length() > 0 && separator != null)
        builder.append(separator);
      builder.append(string);
    }
    return builder.toString();
  }
}
