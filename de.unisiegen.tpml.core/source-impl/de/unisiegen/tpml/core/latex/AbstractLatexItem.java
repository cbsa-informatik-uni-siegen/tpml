package de.unisiegen.tpml.core.latex ;


/**
 * Abstract base class for items in the
 * {@link de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder}, which are
 * used to collect the information while generating the pretty string content.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder
 */
abstract class AbstractLatexItem
{
  /**
   * @param buffer
   */
  abstract void determineString ( StringBuilder buffer ) ;


  /**
   * Returns the length of the string in characters required to serialize this
   * item to a pretty string.
   * 
   * @return the required string length.
   * @see #determineString(StringBuilder)
   */
  abstract int determineStringLength ( ) ;
}
