package common.prettyprinter;

import java.util.Collection;

/**
 * Provides the functionality to pretty-print a {@link common.prettyprinter.PrettyPrintable}
 * as string while extracting several informations about the printables contained within the
 * pretty string.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.prettyprinter.PrettyAnnotation
 * @see common.prettyprinter.PrettyCharIterator
 * @see common.prettyprinter.PrettyPrintable
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
   * @see common.prettyprinter.PrettyAnnotation         
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
   * @see common.prettyprinter.PrettyAnnotation         
   */
  public PrettyAnnotation getAnnotationForPrintable(PrettyPrintable printable) throws IllegalArgumentException;

  /**
   * Returns an <code>PrettyCharIterator</code> which can be used to
   * iterate over the characters contained within the pretty string
   * and extract certain informations (like style) about the characters.
   * 
   * @return the character iterator for the pretty string.
   * 
   * @see common.prettyprinter.PrettyCharIterator
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
