package de.unisiegen.tpml.core.prettyprinter;

import java.text.CharacterIterator;

/**
 * Provides functionality to iterate over the characters contained
 * within an {@link PrettyString} and extract certain information
 * about the style and annotations for a given character.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public interface PrettyCharacterIterator extends CharacterIterator {
  /**
   * Returns the {@link PrettyStyle}, which should be used
   * for the current character, or {@link PrettyStyle#DEFAULT}
   * if no special highlighting should be applied to the current
   * character.
   * 
   * @return the {@link PrettyStyle} for the current character.
   * 
   * @see java.text.CharacterIterator#getIndex()
   */
  public PrettyStyle getStyle();
}
