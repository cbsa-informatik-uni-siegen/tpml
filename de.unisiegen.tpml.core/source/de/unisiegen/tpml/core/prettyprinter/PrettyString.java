package de.unisiegen.tpml.core.prettyprinter;

import java.util.Collection;

/**
 * Provides the functionality to pretty-print an expression
 * as string while extracting several informations about the
 * expressions contained within the pretty string.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator
 */
public interface PrettyString extends CharSequence {
  /**
   * Returns the list of annotations contained within the
   * pretty string.
   * 
   * The returned <code>Collection</code> <b>MUST NOT</b> be modified
   * by the caller in any way.
   * 
   * @return the annotations contained within the pretty string.
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation         
   */
  public Collection<PrettyAnnotation> getAnnotations();
  
  /**
   * Looks up the <code>PrettyAnnotation</code> for the given
   * <code>object</code> in this pretty string. If no
   * annotation is associated with the <code>object</code>
   * (that is, the <code>object</code> is not present in the
   * pretty string), <code>IllegalArgumentException</code>
   * is thrown.
   * 
   * @param o an <code>Object</code>, which is valid for the pretty string. 
   *        
   * @return the annotation for <code>object</code> in this pretty string. 
   * 
   * @throws IllegalArgumentException if the <code>object</code> is
   *                                  invalid for the pretty string.
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation         
   */
  public PrettyAnnotation getAnnotationForObject(Object object) throws IllegalArgumentException;

  /**
   * Returns the <code>PrettyStyle</code> that should be used for the
   * character at the specified <code>index</code> within the pretty
   * string.
   * 
   * @param index the index of the character whose <code>PrettyStyle</code> should
   *              be returned.
   * 
   * @return the <code>PrettyStyle</code> that is associated with the character
   *         at the position <code>index</code> within this pretty string.
   * 
   * @throws IndexOutOfBoundsException if the <code>index</code> argument is negative
   *                                   or not less than <code>length()</code>.
   */
  public PrettyStyle styleAt(int index);
  
  /**
   * Returns an <code>PrettyCharacterIterator</code> which can be used to
   * iterate over the characters contained within the pretty string and
   * extract certain informations (like style) about the characters.
   * 
   * @return the character iterator for the pretty string.
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator
   */
  public PrettyCharacterIterator toCharacterIterator();
  
  /**
   * Returns the string representation of the pretty string
   * without any annotations or styles. This is mainly useful
   * for debugging purposes.
   * 
   * @return the string representation of the pretty string.
   * 
   * @see java.lang.Object#toString()
   */
  public String toString();
}
