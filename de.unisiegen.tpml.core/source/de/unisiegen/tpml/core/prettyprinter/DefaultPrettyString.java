package de.unisiegen.tpml.core.prettyprinter;

import java.util.Collection;
import java.util.Collections;

/**
 * Default implementation of the {@link PrettyString} interface,
 * created by the {@link PrettyStringBuilder} class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class DefaultPrettyString implements PrettyString {
  /**
   * Allocates a new {@link DefaultPrettyString} with the specified
   * parameters.
   * 
   * @param annotations the list of <code>PrettyAnnotation</code>s within the string.
   * @param content the actual string content.
   * @param styles the <code>PrettyStyle</code> for every character in <code>content</code>.
   * 
   * @throws IllegalArgumentException if the number of <code>PrettyStyle</code>s in
   *                                  <code>styles</code> doesn't match the number of
   *                                  characters in <code>content</code>.
   */
  DefaultPrettyString(Collection<PrettyAnnotation> annotations, CharSequence content, PrettyStyle[] styles) throws IllegalArgumentException {
    // verify that we have a style for every character
    if (content.length() != styles.length)
      throw new IllegalArgumentException("The number of styles doesn't match the number of characters");

    // initialize member attributes
    this.annotations = Collections.unmodifiableCollection(annotations);
    this.content = content;
    this.styles = styles;
  }

  /**
   * @see java.lang.CharSequence#charAt(int)
   */
  public char charAt(int index) {
    // String.charAt() will take care to validate the index
    return this.content.charAt(index);
  }
  
  /**
   * @see java.lang.CharSequence#length()
   */
  public int length() {
    return this.content.length();
  }
  
  /**
   * This method is not implemented for <code>PrettyString</code>s right now
   * and thereby just throws an <code>UnsupportedOperationException</code> if
   * invoked.
   * 
   * @param start the start index (inclusive).
   * @param end the end index (exclusive).
   * 
   * @return does not return normally.
   * 
   * @see java.lang.CharSequence#subSequence(int, int)
   * 
   * @throws UnsupportedOperationException since this method is not supported.
   */
  public CharSequence subSequence(int start, int end) {
    throw new UnsupportedOperationException("subSequence() is not implemented for DefaultPrettyString");
  }
  
  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#getAnnotations()
   */
  public Collection<PrettyAnnotation> getAnnotations() {
    return this.annotations;
  }

  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#getAnnotationForObject(java.lang.Object)
   */
  public PrettyAnnotation getAnnotationForObject(Object object) throws IllegalArgumentException {
    // look for a matching annotation in our list
    for (PrettyAnnotation annotation : this.annotations)
      if (annotation.getObject() == object)
        return annotation;
    
    // nothing, so throw an exception
    throw new IllegalArgumentException("No annotation available for " + object);
  }

  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#styleAt(int)
   */
  public PrettyStyle styleAt(int index) {
    // verify that index is valid, and return the style
    if (index < 0 || index >= length())
      throw new StringIndexOutOfBoundsException(index);
    else
      return this.styles[index];
  }
  
  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#toCharacterIterator()
   */
  public PrettyCharacterIterator toCharacterIterator() {
    return new DefaultPrettyCharacterIterator(this);
  }
  
  /**
   * Compares this pretty string implementation to <code>obj</code>. If
   * <code>obj</code> is a <code>DefaultPrettyString</code> instance, and
   * it's field values are equal to the field values of this pretty string.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> is equal to this
   *         <code>DefaultPrettyString</code>.
   *         
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof DefaultPrettyString) {
      DefaultPrettyString string = (DefaultPrettyString)obj;
      return (this.annotations.equals(string.annotations)
           && this.content.equals(string.content)
           && this.styles.equals(string.styles));
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns a hash code value for this pretty string.
   * 
   * @return a hash code value for this pretty string.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.annotations.hashCode() + this.content.hashCode() + this.styles.hashCode();
  }
  
  /**
   * Returns the raw string representation of this <code>PrettyString</code>
   * without any style information or annotations.
   * 
   * @return the raw string representation.
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#toString()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.content.toString();
  }

  // member attributes
  private Collection<PrettyAnnotation> annotations;
  private CharSequence content;
  private PrettyStyle[] styles;
}
