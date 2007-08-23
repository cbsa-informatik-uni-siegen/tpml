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
  protected abstract void determineString ( StringBuilder pBuffer , int pIndent ) ;
}
