package de.unisiegen.tpml.core.prettyprinter;


import java.text.CharacterIterator;


/**
 * Provides functionality to iterate over the characters contained within an
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyString} and extract certain
 * information about the style and annotations for a given character.
 * 
 * @author Benedikt Meurer
 * @version $Rev:277 $
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStyle
 */
public interface PrettyCharIterator extends CharacterIterator
{

  //
  // Primitives
  //
  /**
   * Returns the most significant {@link PrettyAnnotation} for the current
   * character.
   * 
   * @return the most significant annotation for the current character.
   * @throws java.lang.IllegalStateException if the current index is outside the
   *           string bounds. This holds if the character iterator points to the
   *           end index.
   * @see java.text.CharacterIterator#getIndex()
   */
  public PrettyAnnotation getAnnotation ();


  /**
   * Returns the <code>PrettyStyle</code>, which should be used for the
   * current character, or <code>PrettyStyle.NONE</code> if no special
   * highlighting should be applied to the current character.
   * 
   * @return the <code>PrettyStyle</code> for the current character.
   * @see java.text.CharacterIterator#getIndex()
   */
  public PrettyStyle getStyle ();
}
