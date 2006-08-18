package de.unisiegen.tpml.core.prettyprinter;

import java.util.Collection;
import java.util.Map;

/**
 * Default implementation of the {@link de.unisiegen.tpml.core.prettyprinter.PrettyString} interface.
 * Generated by the {@link de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder}s.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder
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
   * @see PrettyAnnotation
   */
  private Map<PrettyPrintable, PrettyAnnotation> annotations;
  
  /**
   * The mapping of character indices to {@link PrettyStyle}s.
   * 
   * @see #toCharacterIterator()
   * @see PrettyStyle
   */
  private PrettyStyle[] styles;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultPrettyString</code> for the specified <code>content</code>.
   * The <code>annotations</code> and <code>styles</code> give additional information about
   * the data contained in of <code>content</code>.
   * 
   * @param content the string representation of an expression.
   * @param annotations the annotations within <code>content</code>.
   * @param styles a map, which specifies for each character in <code>content</code>
   *               the <code>PrettyStyle</code> to use.
   * 
   * @see DefaultPrettyStringBuilder#toPrettyString()
   */
  DefaultPrettyString(String content, Map<PrettyPrintable, PrettyAnnotation> annotations, PrettyStyle[] styles) {
    this.content = content;
    this.annotations = annotations;
    this.styles = styles;
  }

  /**
   * {@inheritDoc}
   *
   * @see PrettyString#getAnnotations()
   */
  public Collection<PrettyAnnotation> getAnnotations() {
    return this.annotations.values();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see PrettyString#getAnnotationForPrintable(PrettyPrintable)
   */
  public PrettyAnnotation getAnnotationForPrintable(PrettyPrintable printable) throws IllegalArgumentException {
    PrettyAnnotation annotation = this.annotations.get(printable);
    if (annotation == null) {
      throw new IllegalArgumentException("printable is invalid");
    }
    return annotation;
  }

  /**
   * {@inheritDoc}
   *
   * @see PrettyString#toCharacterIterator()
   */
  public PrettyCharIterator toCharacterIterator() {
    return new DefaultPrettyCharIterator(this.content, this.annotations, this.styles);
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Returns <code>true</code> if the <code>obj</code> is a <code>DefaultPrettyString</code>
   * with the same attribute values as this pretty string instance.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> is equal to this pretty string.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DefaultPrettyString) {
      DefaultPrettyString other = (DefaultPrettyString)obj;
      return (this.annotations.equals(other.annotations)
           && this.content.equals(other.content)
           && this.styles.equals(other.styles));
    }
    return false;
  }
  
  /**
   * Returns a hash value for this pretty string.
   * 
   * @return a hash value for this pretty string.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.annotations.hashCode()
         + this.content.hashCode()
         + this.styles.hashCode();
  }
  
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
