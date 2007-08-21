package de.unisiegen.tpml.core.latex ;


/**
 * This class represents breaks that can be inserted into the item list of a
 * pretty string builder using the
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBreak()}
 * method and indicate that the presenter may start a new line at this offset.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.latex.AbstractLatexItem
 * @see de.unisiegen.tpml.core.latex.LatexStringBuilder#addBreak()
 */
final class BreakLatexItem extends AbstractLatexItem
{
  //
  // Constants
  //
  /**
   * The single instance of the <code>BreakItem</code> class, which is used to
   * reduce the overhead of allocating several (empty) <code>BreakItem</code>
   * instances.
   */
  public static final BreakLatexItem BREAK_LATEX_ITEM = new BreakLatexItem ( ) ;


  //
  // Constructor (private)
  //
  /**
   * Allocates a new <code>BreakItem</code> instance.
   */
  private BreakLatexItem ( )
  {
    // nothing to do here...
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.AbstractLatexItem#determineString
   *      (StringBuilder)
   */
  @ Override
  void determineString ( @ SuppressWarnings ( "unused" )
  StringBuilder buffer )
  {
    // do nothing
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.AbstractLatexItem#determineStringLength()
   */
  @ Override
  int determineStringLength ( )
  {
    // breaks don't consume any additional space
    return 0 ;
  }
}
