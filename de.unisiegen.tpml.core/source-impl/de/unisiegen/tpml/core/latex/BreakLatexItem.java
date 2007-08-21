package de.unisiegen.tpml.core.latex ;


/**
 * This class represents breaks that can be inserted into the item list of a
 * latex string builder using the {@link LatexStringBuilder#addBreak()} method
 * and indicate that the presenter may start a new line at this offset.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see LatexStringBuilder#addBreak()
 */
final class BreakLatexItem extends AbstractLatexItem
{
  /**
   * The single instance of the <code>BreakLatexItem</code> class, which is
   * used to reduce the overhead of allocating several (empty)
   * <code>BreakLatexItem</code> instances.
   */
  public static final BreakLatexItem BREAK_LATEX_ITEM = new BreakLatexItem ( ) ;


  /**
   * Allocates a new <code>BreakLatexItem</code> instance.
   */
  private BreakLatexItem ( )
  {
    // nothing to do here
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineString (StringBuilder)
   */
  @ Override
  void determineString ( @ SuppressWarnings ( "unused" )
  StringBuilder pBuffer )
  {
    // do nothing
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLatexItem#determineStringLength()
   */
  @ Override
  int determineStringLength ( )
  {
    // breaks don't consume any additional space
    return 0 ;
  }
}
