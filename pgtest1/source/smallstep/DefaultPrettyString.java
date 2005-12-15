package smallstep;

import java.text.CharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of the <code>PrettyString</code> interface.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see smallstep.PrettyAnnotation
 * @see smallstep.PrettyCharIterator
 * @see smallstep.PrettyString
 */
final class DefaultPrettyString implements PrettyString {
  /**
   * Allocates a new <code>DefaultPrettyString</code> for the specified <code>content</code>.
   * The <code>annotations</code> and <code>keywordsMapping</code> give additional information
   * about the data contained in of <code>content</code>.
   * 
   * @param content the string representation of an expression.
   * @param annotations the annotations within <code>content</code>.
   * @param keywordsMapping a map, which tells for each character in <code>content</code>
   *                        whether it belongs to a keyword or not.
   */
  DefaultPrettyString(String content, Map<Expression, PrettyAnnotation> annotations, boolean[] keywordsMapping) {
    this.content = content;
    this.annotations = annotations;
    this.keywordsMapping = keywordsMapping;
  }
  
  /**
   * Returns the list of annotations contained within the
   * pretty string.
   * 
   * @return the annotations contained within the pretty string.
   *
   * @see smallstep.PrettyAnnotation         
   */
  public Collection<PrettyAnnotation> getAnnotations() {
    return this.annotations.values();
  }
  
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
   * @see smallstep.NoSuchExpressionException         
   * @see smallstep.PrettyAnnotation         
   */
  public PrettyAnnotation getAnnotationForExpression(Expression expression) throws NoSuchExpressionException {
    PrettyAnnotation annotation = this.annotations.get(expression);
    if (annotation != null)
      return annotation;
    throw new NoSuchExpressionException("No annotation found for expression " + expression);
  }

  /**
   * Returns an <code>PrettyCharIterator</code> which can be used to
   * iterate over the characters contained within the pretty string
   * and extract certain informations (like style) about the characters.
   * 
   * @return the character iterator for the pretty string.
   * 
   * @see smallstep.PrettyCharIterator
   */
  public PrettyCharIterator toCharacterIterator() {
    return new CharIterator();
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

  // member variables
  private String content;
  private Map<Expression, PrettyAnnotation> annotations;
  private boolean[] keywordsMapping;
  
  /**
   * Implementation of the <code>PrettyCharIterator</code> interface
   * for <code>DefaultPrettyString</code>s. We use an internal class here
   * to gain direct access to the <code>DefaultPrettyString</code> member
   * variables which are required to operate properly.
   */
  private final class CharIterator implements PrettyCharIterator {
    public PrettyAnnotation getAnnotation() {
      Collection<PrettyAnnotation> annotations = getAnnotations();
      for (Iterator<PrettyAnnotation> iterator = annotations.iterator(); iterator.hasNext(); ) {
        PrettyAnnotation annotation = iterator.next();
        if (annotation.getStartOffset() <= this.index && this.index <= annotation.getEndOffset())
          return annotation;
      }
      
      // should never be reached
      throw new IllegalStateException("Character iterator index out of bounds");
    }

    public boolean isKeyword() {
      return keywordsMapping[getIndex()];
    }

    public char first() {
      return setIndex(getBeginIndex());
    }

    public char last() {
      return setIndex(getEndIndex());
    }

    public char current() {
      return content.charAt(getIndex());
    }

    public char next() {
      if (this.index == getEndIndex())
        return CharacterIterator.DONE;
      this.index += 1;
      return current();
    }

    public char previous() {
      if (this.index == 0)
        return CharacterIterator.DONE;
      this.index -= 1;
      return current();
    }

    public char setIndex(int position) {
      if (position < getBeginIndex() || position > getEndIndex())
        throw new IllegalArgumentException("Invalid character iterator position " + position);
      this.index = position;
      return current();
    }

    public int getBeginIndex() {
      return 0;
    }

    public int getEndIndex() {
      return content.length();
    }

    public int getIndex() {
      return this.index;
    }

    public Object clone() {
      return new CharIterator();
    }
    
    private int index = 0;
  }
}
