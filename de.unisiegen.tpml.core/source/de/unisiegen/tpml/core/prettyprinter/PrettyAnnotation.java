package de.unisiegen.tpml.core.prettyprinter;

/**
 * An annotation for the pretty printer. Annotations are
 * used to mark regions within the text that are associated
 * with a specific object.
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
   * @param object the <code>Object</code> with which this annotation is associated.
   */
  PrettyAnnotation(int startOffset, int endOffset, int[] breakOffsets, Object object) {
    this.breakOffsets = breakOffsets;
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.object = object;
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
  
  /**
   * Returns the object with which this annotation is associated.
   * 
   * @return the object with which this annotation is associated.
   */
  Object getObject() {
    return this.object;
  }
  
  // member attributes
  private int[] breakOffsets;
  private int startOffset;
  private int endOffset;
  private Object object;
}
