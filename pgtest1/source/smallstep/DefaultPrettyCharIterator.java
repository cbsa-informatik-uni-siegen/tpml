package smallstep;

import java.text.CharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of the <code>PrettyCharIterator</code> interface,
 * which operates on data provided by the <code>PrettyString</code> class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see DefaultPrettyString
 */
final class DefaultPrettyCharIterator implements PrettyCharIterator {
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters and an <code>index</code> of 0.
   * 
   * @param content the string content.
   * @param annotations the annotations for <code>content</code>.
   * @param keywordsMapping the keywords mapping in <code>content</code>.
   */
  DefaultPrettyCharIterator(String content, Map<Expression, PrettyAnnotation> annotations, boolean[] keywordsMapping) {
    this(content, annotations, keywordsMapping, 0);
  }
  
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters.
   * 
   * @param content the string content.
   * @param annotations the annotations for <code>content</code>.
   * @param keywordsMapping the keywords mapping in <code>content</code>.
   * @param index the start index.
   */
  DefaultPrettyCharIterator(String content, Map<Expression, PrettyAnnotation> annotations, boolean[] keywordsMapping, int index) {
    this.index = index;
    this.content = content;
    this.annotations = annotations;
    this.keywordsMapping = keywordsMapping;
  }
  
  /**
   * Returns the most significant <code>PrettyAnnotation</code> for
   * the current character iterator position.
   * 
   * @return the most significat <code>PrettyAnnotation</code>.
   *
   * @throws java.lang.IllegalStateException if the current index is outside the string
   *                                         bounds. This holds if the character iterator
   *                                         points to the end index.
   *                                         
   * @see #getIndex()
   * @see smallstep.PrettyCharIterator#getAnnotation()
   */
  public PrettyAnnotation getAnnotation() {
    Collection<PrettyAnnotation> annotations = this.annotations.values();
    for (Iterator<PrettyAnnotation> iterator = annotations.iterator(); iterator.hasNext(); ) {
      PrettyAnnotation annotation = iterator.next();
      if (annotation.getStartOffset() <= this.index && this.index <= annotation.getEndOffset())
        return annotation;
    }
    
    // should never be reached
    throw new IllegalStateException("Character iterator index out of bounds");
  }

  /**
   * Returns <code>true</code> if the character at the current
   * character iterator position belongs to a keyword and should
   * be highlighted appropriately, else <code>false</code>.
   * 
   * @return whether the current character belongs to a keyword.
   * 
   * @see #getIndex()
   * @see smallstep.PrettyCharIterator#isKeyword()
   */
  public boolean isKeyword() {
    if (this.index < getEndIndex())
      return this.keywordsMapping[this.index];
    else
      return false;
  }

  /**
   * @see java.text.CharacterIterator#first()
   */
  public char first() {
    return setIndex(getBeginIndex());
  }

  /**
   * @see java.text.CharacterIterator#last()
   */
  public char last() {
    return setIndex(getEndIndex());
  }

  /**
   * @see java.text.CharacterIterator#current()
   */
  public char current() {
    if (this.index == getEndIndex())
      return CharacterIterator.DONE;
    else
      return this.content.charAt(this.index);
  }

  /**
   * @see java.text.CharacterIterator#next()
   */
  public char next() {
    if (this.index == getEndIndex())
      return CharacterIterator.DONE;
    return setIndex(this.index + 1);
  }

  /**
   * @see java.text.CharacterIterator#previous()
   */
  public char previous() {
    if (this.index == getBeginIndex())
      return CharacterIterator.DONE;
    return setIndex(this.index - 1);
  }

  /**
   * @see java.text.CharacterIterator#setIndex(int)
   */
  public char setIndex(int position) {
    if (position < getBeginIndex() || position > getEndIndex())
      throw new IllegalArgumentException("Invalid character iterator position " + position);
    this.index = position;
    return current();
  }

  /**
   * @see java.text.CharacterIterator#getBeginIndex()
   */
  public int getBeginIndex() {
    return 0;
  }

  /**
   * @see java.text.CharacterIterator#getEndIndex()
   */
  public int getEndIndex() {
    return this.content.length();
  }

  /**
   * @see java.text.CharacterIterator#getIndex()
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * Clones the character iterator with its current state.
   * 
   * @return the cloned character iterator.
   * 
   * @see java.lang.Object#clone()
   */
  public Object clone() {
    return new DefaultPrettyCharIterator(this.content, this.annotations, this.keywordsMapping, this.index);
  }
  
  // member variables
  private int index;
  private String content;
  private Map<Expression, PrettyAnnotation> annotations;
  private boolean[] keywordsMapping;
}
