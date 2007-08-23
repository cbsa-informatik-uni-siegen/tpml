package de.unisiegen.tpml.core.latex ;


/**
 * This class represents breaks that must be inserted into the item list of a
 * latex string builder using the {@link LatexStringBuilder#addMustBreakHere()}
 * method and indicate that the presenter muyt start a new line at this offset.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see LatexStringBuilder#addMustBreakHere()
 */
final class MustBreakHereLatexItem extends AbstractLatexItem
{
  /**
   * The single instance of the <code>MustBreakHereLatexItem</code> class,
   * which is used to reduce the overhead of allocating several (empty)
   * <code>MustBreakHereLatexItem</code> instances.
   */
  public static final MustBreakHereLatexItem ITEM = new MustBreakHereLatexItem ( ) ;


  /**
   * Allocates a new <code>BreakLatexItem</code> instance.
   */
  private MustBreakHereLatexItem ( )
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
