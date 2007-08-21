package de.unisiegen.tpml.core.latex ;


/**
 * Provides the functionality to latex print a {@link LatexPrintable}..
 * 
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see LatexPrintable
 */
public interface LatexString
{
  /**
   * Returns the string representation of the latex string without any
   * annotations.
   * 
   * @return The string representation of the pretty string.
   * @see Object#toString()
   */
  public String toString ( ) ;
}
