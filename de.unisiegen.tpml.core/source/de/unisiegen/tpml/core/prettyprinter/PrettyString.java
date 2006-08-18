package de.unisiegen.tpml.core.prettyprinter;

import java.util.Collection;

/**
 * Provides the functionality to pretty-print a {@link de.unisiegen.tpml.core.prettyprinter.PrettyPrintable}
 * as string while extracting several informations about the printables contained within the pretty string.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable
 */
public interface PrettyString {
  //
  // Primitives
  //

  /**
   * Returns the list of annotations contained within the
   * pretty string.
   * 
   * @return the annotations contained within the pretty string.
   *
   * @see PrettyAnnotation
   */
  public Collection<PrettyAnnotation> getAnnotations();
  
  /**
   * Looks up the <code>PrettyAnnotation</code> for the given
   * <code>printable</code> in this pretty string. If no
   * annotation is associated with <code>printable</code>
   * (that is, <code>printable</code> is not present in the
   * pretty string), an <code>IllegalArgumentException</code>
   * is thrown.
   * 
   * @param printable a <code>PrettyPrintable</code> instance, which
   *                  is valid for the pretty string. 
   *        
   * @return the annotation for <code>printable</code>. 
   * 
   * @throws IllegalArgumentException if the <code>printable</code> is
   *                                  invalid for the pretty string.
   *
   * @see PrettyAnnotation         
   */
  public PrettyAnnotation getAnnotationForPrintable(PrettyPrintable printable) throws IllegalArgumentException;

  /**
   * Returns an {@link PrettyCharIterator} which can be used to iterate
   * over the characters contained within the pretty string and extract
   * certain informations (like style) about the characters.
   * 
   * @return the character iterator for the pretty string.
   * 
   * @see PrettyCharIterator
   */
  public PrettyCharIterator toCharacterIterator();
  
  
  
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
  public String toString();
}
