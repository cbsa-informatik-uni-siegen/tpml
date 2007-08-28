package de.unisiegen.tpml.core.latex ;


/**
 * This class represents a possible break in the latex export.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see DefaultLatexStringBuilder
 */
final class BreakLatexItem extends AbstractLatexItem
{
  /**
   * Allocates a new <code>BreakLatexItem</code>.
   */
  protected BreakLatexItem ( )
  {
    // nothing to do here
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineString(StringBuilder,int)
   */
  @ Override
  protected void determineString ( @ SuppressWarnings ( "unused" )
  StringBuilder pBuffer , @ SuppressWarnings ( "unused" )
  int pIndent )
  {
    // nothing to do here
  }
}
