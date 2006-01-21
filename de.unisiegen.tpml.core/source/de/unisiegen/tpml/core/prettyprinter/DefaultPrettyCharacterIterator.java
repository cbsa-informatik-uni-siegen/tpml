package de.unisiegen.tpml.core.prettyprinter;

import java.text.CharacterIterator;

/**
 * Default implementation of the <code>PrettyCharIterator</code> interface,
 * which operates on data provided by the <code>PrettyString</code> class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
final class DefaultPrettyCharacterIterator implements PrettyCharacterIterator {
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters and an <code>index</code> of 0.
   * 
   * @param string the <code>DefaultPrettyString</code>, which content is to
   *               be iterated over by this character iterator.
   */
  DefaultPrettyCharacterIterator(DefaultPrettyString string) {
    this(string, 0);
  }
  
  /**
   * Allocates a new <code>DefaultPrettyCharIterator</code> with the
   * specified parameters.
   * 
   * @param string the <code>DefaultPrettyString</code>, which content is to
   *               be iterated over by this character iterator.
   * @param index the start index.
   */
  DefaultPrettyCharacterIterator(DefaultPrettyString string, int index) {
    this.index = index;
    this.string = string;
  }
  
  /**
   * Returns the <code>PrettyStyle</code> for the current
   * character iterator position.
   * 
   * @return the <code>PrettyStyle</code> for the current character iterator position.
   *         
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator#getStyle()
   */
  public PrettyStyle getStyle() {
    if (this.index < getEndIndex())
      return this.string.styleAt(this.index);
    else
      return PrettyStyle.DEFAULT;
  }

  /**
   * Sets the index to the first character and returns the first
   * character.
   * 
   * @return the first character.
   * 
   * @see java.text.CharacterIterator#first()
   */
  public char first() {
    return setIndex(getBeginIndex());
  }

  /**
   * Sets the index to the last character and returns the last
   * character.
   * 
   * @return the last character.
   * 
   * @see java.text.CharacterIterator#last()
   */
  public char last() {
    return setIndex(getEndIndex());
  }

  /**
   * Returns the character at the current index.
   * 
   * @return the character at the current index.
   * 
   * @see java.text.CharacterIterator#current()
   */
  public char current() {
    if (this.index == getEndIndex())
      return CharacterIterator.DONE;
    else
      return this.string.charAt(this.index);
  }

  /**
   * Increments the index and returns the next
   * character.
   * 
   * @return the next character.
   * 
   * @see java.text.CharacterIterator#next()
   */
  public char next() {
    if (this.index == getEndIndex())
      return CharacterIterator.DONE;
    return setIndex(this.index + 1);
  }

  /**
   * Decrements the index and returns the previous
   * character.
   * 
   * @return the previous character.
   * 
   * @see java.text.CharacterIterator#previous()
   */
  public char previous() {
    if (this.index == getBeginIndex())
      return CharacterIterator.DONE;
    return setIndex(this.index - 1);
  }

  /**
   * Sets the current index to <code>position</code> returns the
   * character at <code>position</code>.
   * 
   * @param position the new index.
   * 
   * @return the character at <code>position</code>.
   * 
   * @see java.text.CharacterIterator#setIndex(int)
   */
  public char setIndex(int position) {
    if (position < getBeginIndex() || position > getEndIndex())
      throw new IllegalArgumentException("Invalid character iterator position " + position);
    this.index = position;
    return current();
  }

  /**
   * Returns the start index.
   * 
   * @return the start index.
   * 
   * @see java.text.CharacterIterator#getBeginIndex()
   */
  public int getBeginIndex() {
    return 0;
  }

  /**
   * Returns the end index.
   * 
   * @return the end index.
   * 
   * @see java.text.CharacterIterator#getEndIndex()
   */
  public int getEndIndex() {
    return this.string.length();
  }

  /**
   * Returns the current index.
   * 
   * @return the current index.
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
    return new DefaultPrettyCharacterIterator(this.string, this.index);
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is an instance of
   * <code>DefaultPrettyCharacterIterator</code> whose index and string
   * attributes are equal to this iterator.
   * 
   * @param obj another object.
   * 
   * @return <code>true</code> if <code>obj</code> equals this iterator.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof DefaultPrettyCharacterIterator) {
      DefaultPrettyCharacterIterator iterator = (DefaultPrettyCharacterIterator)obj;
      return (this.index == iterator.index && this.string.equals(iterator.string));
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns a hash code value for this iterator.
   * 
   * @return a hash code value for this iterator.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return (this.index + this.string.hashCode());
  }
  
  // member variables
  private int index;
  private DefaultPrettyString string;
}
