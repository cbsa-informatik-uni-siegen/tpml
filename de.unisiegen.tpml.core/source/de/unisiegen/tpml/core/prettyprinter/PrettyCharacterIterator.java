package de.unisiegen.tpml.core.prettyprinter;

import java.text.CharacterIterator;

/**
 * Provides functionality to iterate over the characters contained
 * within an <code>PrettyString</code> and extract certain information
 * about the style and annotations for a given character.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public interface PrettyCharacterIterator extends CharacterIterator {
  /**
   * Returns the <code>PrettyStyle</code>, which should be used
   * for the current character, or <code>PrettyStyle.DEFAULT</code>
   * if no special highlighting should be applied to the current
   * character.
   * 
   * @return the <code>PrettyStyle</code> for the current character.
   * 
   * @see java.text.CharacterIterator#getIndex()
   */
  public PrettyStyle getStyle();
}
