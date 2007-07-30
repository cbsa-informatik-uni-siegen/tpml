package de.unisiegen.tpml.core.prettyprinter ;


import java.util.List ;
import java.util.Map ;


/**
 * This class represents breaks that can be inserted into the item list of a
 * pretty string builder using the
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBreak()}
 * method and indicate that the presenter may start a new line at this offset.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBreak()
 */
final class BreakItem extends AbstractItem
{
  //
  // Constants
  //
  /**
   * The single instance of the <code>BreakItem</code> class, which is used to
   * reduce the overhead of allocating several (empty) <code>BreakItem</code>
   * instances.
   */
  public static final BreakItem BREAK_ITEM = new BreakItem ( ) ;


  //
  // Constructor (private)
  //
  /**
   * Allocates a new <code>BreakItem</code> instance.
   */
  private BreakItem ( )
  {
    // nothing to do here...
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineString(StringBuilder,
   *      Map, List, PrettyStyle[])
   */
  @ Override
  void determineString ( StringBuilder buffer , @ SuppressWarnings ( "unused" )
  Map < PrettyPrintable , PrettyAnnotation > annotations ,
      List < Integer > breakOffsets , @ SuppressWarnings ( "unused" )
      PrettyStyle [ ] styles )
  {
    // just add a break offset at the current buffer position
    breakOffsets.add ( new Integer ( buffer.length ( ) ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineStringLength()
   */
  @ Override
  int determineStringLength ( )
  {
    // breaks don't consume any additional space
    return 0 ;
  }
}
