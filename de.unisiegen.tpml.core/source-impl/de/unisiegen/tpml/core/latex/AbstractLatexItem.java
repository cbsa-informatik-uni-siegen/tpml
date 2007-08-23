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
   * @param pIndent The indent of this object.
   */
  abstract void determineString ( StringBuilder pBuffer , int pIndent ) ;


  /**
   * Returns the length of the string in characters required to serialize this
   * item to a latex string.
   * 
   * @return The required string length.
   * @see #determineString(StringBuilder,int)
   */
  abstract int determineStringLength ( ) ;
}
