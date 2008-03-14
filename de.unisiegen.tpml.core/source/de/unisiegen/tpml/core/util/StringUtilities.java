package de.unisiegen.tpml.core.util;


import java.util.Arrays;
import java.util.Collection;


/**
 * Static class, which provides several static utility functions for handling
 * strings.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see java.lang.String
 */
public final class StringUtilities
{

  //
  // Constructor (private)
  //
  /**
   * No instances of this class can be created.
   */
  private StringUtilities ()
  {
    // nothing to do here...
  }


  //
  // Case transformations
  //
  /**
   * Simple wrapper to <code>toCamelCase</code> which sets
   * <code>lowerCase</code> to <code>false</code>.
   * 
   * @param string the string to transform.
   * @return a version of <code>string</code>, whose first character is
   *         upper-cased.
   * @see #toCamelCase(String, boolean)
   */
  public static String toCamelCase ( String string )
  {
    return toCamelCase ( string, false );
  }


  /**
   * Transform the specified <code>string</code> to its camel-case
   * representation. That says, the first character of <code>string</code>
   * will be changed to its upper-case representation. If <code>lowerCase</code>
   * is <code>true</code> then the remaining characters will be changed to
   * their lower-case representations, else the case will be kept as is. For
   * example, say <code>string</code> is <tt>"text"</tt>, then this
   * function will return <tt>"Text"</tt>. Else, if for example,
   * <code>string</code> is <tt>"teXT"</tt> and <code>lowerCase</code> is
   * <code>false</code> then <tt>"TeXT"</tt> will be returned (if
   * <code>lowerCase</code> is <code>true</code> then it would be
   * <tt>"Text"</tt>). If <code>string</code> is the empty string (<tt>""</tt>),
   * this function will just return <code>string</code>.
   * 
   * @param string a string to transform.
   * @param lowerCase <code>true</code> to lower-case the characters in the
   *          range <code>[1..string.length()]</code>.
   * @return the camel case version of <code>string</code>.
   * @see #toCamelCase(String)
   */
  public static String toCamelCase ( String string, boolean lowerCase )
  {
    // verify that string contains atleast one character
    if ( string.length () == 0 )
      return string;
    // allocate a string builder to generate the camel-case version
    StringBuilder builder = new StringBuilder ( string.length () );
    // add the first character in upper-case
    builder.append ( Character.toUpperCase ( string.charAt ( 0 ) ) );
    // append the remaining string (if not empty)
    if ( string.length () > 1 )
    {
      // determine the remaining string
      String remaining = string.substring ( 1 );
      // append remaining string (lower-cased if requested)
      builder.append ( lowerCase ? remaining.toLowerCase () : remaining );
    }
    return builder.toString ();
  }


  //
  // Collection methods
  //
  /**
   * Joins a number of <code>strings</code> together to form one long string,
   * with the optional <code>separator</code> inserted between each of them.
   * This method is implemented as convenience wrapper for the
   * {@link #join(String, Collection)} method.
   * 
   * @param separator a string to insert between each of the
   *          <code>strings</code> or <code>null</code>.
   * @param strings a variable list (an array) of strings.
   * @return a string containing all of the <code>strings</code> joined
   *         together with <code>separator</code> between them.
   * @see #join(String, Collection)
   */
  public static String join ( String separator, String ... strings )
  {
    return join ( separator, Arrays.asList ( strings ) );
  }


  /**
   * Joins a number of <code>strings</code> together to form one long string,
   * with the optional <code>separator</code> inserted between each of them.
   * 
   * @param separator a string to insert between each of the
   *          <code>strings</code> or <code>null</code>.
   * @param strings a {@link Collection} of strings.
   * @return a string containing all of the <code>strings</code> joined
   *         together with <code>separator</code> between them.
   * @throws NullPointerException if <code>strings</code> is <code>null</code>.
   */
  public static String join ( String separator, Collection < String > strings )
  {
    StringBuilder builder = new StringBuilder ();
    for ( String string : strings )
    {
      if ( builder.length () > 0 && separator != null )
        builder.append ( separator );
      builder.append ( string );
    }
    return builder.toString ();
  }
}
