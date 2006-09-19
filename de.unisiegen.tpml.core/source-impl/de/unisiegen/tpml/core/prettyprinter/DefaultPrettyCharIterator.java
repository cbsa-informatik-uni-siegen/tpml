package de.unisiegen.tpml.core.prettyprinter;

import java.text.CharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of the <code>PrettyCharIterator</code> interface,
 * which operates on data provided by the <code>PrettyString</code> class.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharIterator
 */
final class DefaultPrettyCharIterator implements PrettyCharIterator {
  //
  // Attributes
  //
  
  /**
   * The current character index of the iterator.
   * 
   * @see #getIndex()
   */
  private int index;
  
  /**
   * The raw character content of the {@link PrettyString} we're iterating.
   */
  private String content;
  
  /**
   * The mapping of printables to annotations.
   * 
   * @see #getAnnotation()
   */
  private Map<PrettyPrintable, PrettyAnnotation> annotations;
  
  /**
   * The mapping of character indices to {@link common.prettyprinter.PrettyStyle}s.
   * 
   * @see #getStyle()
   */
  private PrettyStyle[] styles;
  
  
  
  //
  // Constructors (package)
  //
  
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters and an <code>index</code> of 0.
   * 
   * @param content the string content.
   * @param annotations the annotations for <code>content</code>.
   * @param styles the <code>PrettyStyle</code> mapping in <code>content</code>.
   * 
   * @throws NullPointerException if either <code>content</code>, <code>annotations</code>
   *                              or <code>styles</code> is <code>null</code>.
   */
  DefaultPrettyCharIterator(String content, Map<PrettyPrintable, PrettyAnnotation> annotations, PrettyStyle[] styles) {
    this(content, annotations, styles, 0);
  }
  
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters.
   * 
   * @param content the string content.
   * @param annotations the annotations for <code>content</code>.
   * @param styles the <code>PrettyStyle</code> mapping in <code>content</code>.
   * @param index the start index.
   * 
   * @throws IllegalArgumentException if <code>index</code> is negative.
   * @throws NullPointerException if either <code>content</code>, <code>annotations</code>
   *                              or <code>styles</code> is <code>null</code>.
   */
  DefaultPrettyCharIterator(String content, Map<PrettyPrintable, PrettyAnnotation> annotations, PrettyStyle[] styles, int index) {
    if (index < 0) {
      throw new IllegalArgumentException("index is negative");
    }
    if (content == null) {
      throw new NullPointerException("content is null");
    }
    if (annotations == null) {
      throw new NullPointerException("annotations is null");
    }
    if (styles == null) {
      throw new NullPointerException("styles is null");
    }
    this.index = index;
    this.content = content;
    this.annotations = annotations;
    this.styles = styles;
  }
  
  
  
  //
  // PrettyCharIterator methods
  //

  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyCharIterator#getAnnotation()
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
   * {@inheritDoc}
   *         
   * @see common.prettyprinter.PrettyCharIterator#getStyle()
   */
  public PrettyStyle getStyle() {
    if (this.index < getEndIndex()) {
      return this.styles[this.index];
    }
    else {
      return PrettyStyle.NONE;
    }
  }

  
  
  //
  // CharacterIterator methods
  //
  
  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#first()
   */
  public char first() {
    return setIndex(getBeginIndex());
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#last()
   */
  public char last() {
    return setIndex(getEndIndex());
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#current()
   */
  public char current() {
    if (this.index == getEndIndex()) {
      return CharacterIterator.DONE;
    }
    else {
      return this.content.charAt(this.index);
    }
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#next()
   */
  public char next() {
    if (this.index == getEndIndex()) {
      return CharacterIterator.DONE;
    }
    return setIndex(this.index + 1);
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#previous()
   */
  public char previous() {
    if (this.index == getBeginIndex()) {
      return CharacterIterator.DONE;
    }
    return setIndex(this.index - 1);
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#setIndex(int)
   */
  public char setIndex(int position) {
    if (position < getBeginIndex() || position > getEndIndex()) {
      throw new IllegalArgumentException("Invalid character iterator position " + position);
    }
    this.index = position;
    return current();
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#getBeginIndex()
   */
  public int getBeginIndex() {
    return 0;
  }

  /**
   * {@inheritDoc}
   *         
   * @see java.text.CharacterIterator#getEndIndex()
   */
  public int getEndIndex() {
    return this.content.length();
  }

  /**
   * {@inheritDoc}
   *         
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
  @Override
  public Object clone() {
    return new DefaultPrettyCharIterator(this.content, this.annotations, this.styles, this.index);
  }
}
