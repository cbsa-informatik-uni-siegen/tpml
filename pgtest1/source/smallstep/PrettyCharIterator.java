package smallstep;

import java.text.CharacterIterator;

/**
 * Provides functionality to iterate over the characters contained
 * within an <code>PrettyString</code> and extract certain information
 * about the style and annotations for a given character.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public interface PrettyCharIterator extends CharacterIterator {
  /**
   * Returns the most significant <code>PrettyAnnotation</code> for
   * the current character.
   * 
   * @return the most significant annotation for the current
   *         character.
   *         
   * @throws java.lang.IllegalStateException if the current index is outside the string
   *                                         bounds. This holds if the character iterator
   *                                         points to the end index.
   *                                         
   * @see java.text.CharacterIterator#getIndex()                                         
   */
  public PrettyAnnotation getAnnotation();
  
  /**
   * Returns <code>true</code> if the current character is
   * part of an keyword and should be highlighted in the
   * presentation.
   * 
   * @return whether the current character belongs to a keyword.
   * 
   * @see java.text.CharacterIterator#getIndex()
   */
  public boolean isKeyword();
}
