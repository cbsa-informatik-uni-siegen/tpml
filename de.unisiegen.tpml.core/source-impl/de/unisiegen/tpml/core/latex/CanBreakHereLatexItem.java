package de.unisiegen.tpml.core.latex ;


/**
 * This class represents breaks that can be inserted into the item list of a
 * latex string builder using the {@link LatexStringBuilder#addCanBreakHere()}
 * method and indicate that the presenter may start a new line at this offset.
 * 
 * @author Christian Fehler
 * @see AbstractLatexItem
 * @see LatexStringBuilder#addCanBreakHere()
 */
final class CanBreakHereLatexItem extends AbstractLatexItem
{
  /**
   * The single instance of the <code>CanBreakHereLatexItem</code> class,
   * which is used to reduce the overhead of allocating several (empty)
   * <code>CanBreakHereLatexItem</code> instances.
   */
  public static final CanBreakHereLatexItem ITEM = new CanBreakHereLatexItem ( ) ;


  /**
   * Allocates a new <code>CanBreakHereLatexItem</code> instance.
   */
  private CanBreakHereLatexItem ( )
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
