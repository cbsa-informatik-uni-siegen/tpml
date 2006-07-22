package common.prettyprinter;

import java.util.Collection;
import java.util.Map;

/**
 * Default implementation of the {@link common.prettyprinter.PrettyString} interface.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.prettyprinter.PrettyAnnotation
 * @see common.prettyprinter.PrettyCharIterator
 * @see common.prettyprinter.PrettyString
 * @see common.prettyprinter.PrettyStringBuilder
 */
final class DefaultPrettyString implements PrettyString {
  //
  // Attributes
  //
  
  /**
   * The raw character content of the pretty string.
   * 
   * @see #toString()
   */
  private String content;
  
  /**
   * The {@link PrettyAnnotation}s within this pretty string.
   * 
   * @see #getAnnotations()
   */
  private Map<PrettyPrintable, PrettyAnnotation> annotations;
  
  /**
   * The mapping of character indices to {@link common.prettyprinter.PrettyStyle}s.
   * 
   * @see #toCharacterIterator()
   */
  private PrettyStyle[] styles;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultPrettyString</code> for the specified <code>content</code>.
   * The <code>annotations</code> and <code>keywordsMapping</code> give additional information
   * about the data contained in of <code>content</code>.
   * 
   * @param content the string representation of an expression.
   * @param annotations the annotations within <code>content</code>.
   * @param styles a map, which specifies for each character in <code>content</code>
   *               the <code>PrettyStyle</code> to use.
   */
  DefaultPrettyString(String content, Map<PrettyPrintable, PrettyAnnotation> annotations, PrettyStyle[] styles) {
    this.content = content;
    this.annotations = annotations;
    this.styles = styles;
  }

  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyString#getAnnotations()
   */
  public Collection<PrettyAnnotation> getAnnotations() {
    return this.annotations.values();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyString#getAnnotationForPrintable(common.prettyprinter.PrettyPrintable)
   */
  public PrettyAnnotation getAnnotationForPrintable(PrettyPrintable printable) throws IllegalArgumentException {
    PrettyAnnotation annotation = this.annotations.get(printable);
    if (annotation != null)
      return annotation;
    throw new IllegalArgumentException("printable is invalid");
  }

  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyString#toCharacterIterator()
   */
  public PrettyCharIterator toCharacterIterator() {
    return new DefaultPrettyCharIterator(this.content, this.annotations, this.styles);
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Returns the string representation of the pretty string
   * without any annotations. This is mainly useful for
   * debugging purposes.
   * 
   * @return the string representation of the pretty string.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.content;
  }
}
