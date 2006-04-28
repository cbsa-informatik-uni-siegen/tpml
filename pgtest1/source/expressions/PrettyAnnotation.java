package expressions;

/**
 * An annotation for the pretty printer. Annotations are
 * used to mark regions within the text that are associated
 * with a specific expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class PrettyAnnotation {
  /**
   * Allocates a new annotation for the text within
   * <code>startIndex</code> and <code>endIndex</code>. The
   * <code>breakOffsets</code> specify the positions where
   * breaks may be inserted for this annotation.
   *  
   * @param startOffset the starting offset of the annotation in the text.
   * @param endOffset the end offset of the annotation in the text (the last character of
   *                  the annotation).
   * @param breakOffsets the list of possible break offsets.
   */
  PrettyAnnotation(int startOffset, int endOffset, int[] breakOffsets) {
    this.breakOffsets = breakOffsets;
    this.startOffset = startOffset;
    this.endOffset = endOffset;
  }
  
  /**
   * Returns the break offsets for this annotation. The
   * list of break offsets may be empty if no breaks are
   * allowed within this annotation.
   * 
   * Breaks should be applied all or nothing. That said,
   * if you decide to break text for an annotation, you
   * should break at all specified offsets.
   * 
   * @return the break offsets for this annotation.
   */
  public int[] getBreakOffsets() {
    return this.breakOffsets;
  }
  
  /**
   * Returns the start offset of the annotation in
   * the text. The start offset points to the first
   * character of the annotation in the text.
   * 
   * @return the start offset of the annotation.
   */
  public int getStartOffset() {
    return this.startOffset;
  }
  
  /**
   * Returns the end offset of the annotation in the
   * text. The end offset points to the last character
   * of the annotation in the text.
   * 
   * @return the end offset of the annotation.
   */
  public int getEndOffset() {
    return this.endOffset;
  }
  
  private int[] breakOffsets;
  private int startOffset;
  private int endOffset;
}
