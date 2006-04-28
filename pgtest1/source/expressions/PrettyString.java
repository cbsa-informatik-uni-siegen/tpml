package expressions;

import java.util.Collection;

/**
 * Provides the functionality to pretty-print an expression
 * as string while extracting several informations about the
 * expressions contained within the pretty string.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see expressions.PrettyAnnotation
 * @see expressions.PrettyCharIterator
 */
public interface PrettyString {
  /**
   * Returns the list of annotations contained within the
   * pretty string.
   * 
   * @return the annotations contained within the pretty string.
   *
   * @see expressions.PrettyAnnotation         
   */
  public Collection<PrettyAnnotation> getAnnotations();
  
  /**
   * Looks up the <code>PrettyAnnotation</code> for the given
   * <code>expression</code> in this pretty string. If no
   * annotation is associated with <code>expression</code>
   * (that is, <code>expression</code> is not present in the
   * pretty string), <code>NoSuchExpressionException</code>
   * is thrown.
   * 
   * @param expression an <code>Expression</code> instance, which
   *        is valid for the pretty string. 
   *        
   * @return the annotation for <code>expression</code>. 
   * 
   * @throws NoSuchExpressionException if the <code>expression</code> is
   *                                   invalid for the pretty string.
   *
   * @see expressions.NoSuchExpressionException         
   * @see expressions.PrettyAnnotation         
   */
  public PrettyAnnotation getAnnotationForExpression(Expression expression) throws NoSuchExpressionException;

  /**
   * Returns an <code>PrettyCharIterator</code> which can be used to
   * iterate over the characters contained within the pretty string
   * and extract certain informations (like style) about the characters.
   * 
   * @return the character iterator for the pretty string.
   * 
   * @see expressions.PrettyCharIterator
   */
  public PrettyCharIterator toCharacterIterator();
  
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
