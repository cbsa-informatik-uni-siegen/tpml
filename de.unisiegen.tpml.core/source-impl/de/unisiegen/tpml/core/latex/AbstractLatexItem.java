package de.unisiegen.tpml.core.latex ;


/**
 * Abstract base class for items in the {@link DefaultLatexStringBuilder},
 * which are used to collect the information while generating the latex string
 * content.
 * 
 * @author Christian Fehler
 * @see DefaultLatexStringBuilder
 */
abstract class AbstractLatexItem
{
  /**
   * @param pBuffer The string of the latex string.
   */
  abstract void determineString ( StringBuilder pBuffer ) ;


  /**
   * Returns the length of the string in characters required to serialize this
   * item to a latex string.
   * 
   * @return The required string length.
   * @see #determineString(StringBuilder)
   */
  abstract int determineStringLength ( ) ;
}
