package de.unisiegen.tpml.core.prettyprinter ;


/**
 * An annotation for the pretty printer. Annotations are used to mark regions
 * within the text that are associated with a specific
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyPrintable}.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public final class PrettyAnnotation
{
  //
  // Attributes
  //
  /**
   * Possible break offsets for this annotation.
   * 
   * @see #getBreakOffsets()
   */
  private int [ ] breakOffsets ;


  /**
   * The starting offset of this annotation in the {@link PrettyString}.
   * 
   * @see #getStartOffset()
   */
  private int startOffset ;


  /**
   * The end offset of this annotation in the {@link PrettyString}.
   * 
   * @see #getEndOffset()
   */
  private int endOffset ;


  //
  // Constructor (package)
  //
  /**
   * Allocates a new annotation for the text within <code>startIndex</code>
   * and <code>endIndex</code>. The <code>breakOffsets</code> specify the
   * positions where breaks may be inserted for this annotation.
   * 
   * @param pStartOffset the starting offset of the annotation in the text.
   * @param pEndOffset the end offset of the annotation in the text (the last
   *          character of the annotation).
   * @param pBreakOffsets the list of possible break offsets.
   */
  PrettyAnnotation ( int pStartOffset , int pEndOffset , int [ ] pBreakOffsets )
  {
    this.breakOffsets = pBreakOffsets ;
    this.startOffset = pStartOffset ;
    this.endOffset = pEndOffset ;
  }


  //
  // Accessors
  //
  /**
   * Returns the break offsets for this annotation. The list of break offsets
   * may be empty if no breaks are allowed within this annotation. Breaks should
   * be applied all or nothing. That said, if you decide to break text for an
   * annotation, you should break at all specified offsets.
   * 
   * @return the break offsets for this annotation.
   */
  public int [ ] getBreakOffsets ( )
  {
    return this.breakOffsets ;
  }


  /**
   * Returns the start offset of the annotation in the text. The start offset
   * points to the first character of the annotation in the text.
   * 
   * @return the start offset of the annotation.
   */
  public int getStartOffset ( )
  {
    return this.startOffset ;
  }


  /**
   * Returns the end offset of the annotation in the text. The end offset points
   * to the last character of the annotation in the text.
   * 
   * @return the end offset of the annotation.
   */
  public int getEndOffset ( )
  {
    return this.endOffset ;
  }
}
