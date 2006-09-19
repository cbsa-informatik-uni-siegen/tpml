package de.unisiegen.tpml.core.prettyprinter;

/**
 * Possible styles for items in a {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public enum PrettyStyle {
  /**
   * No special style.
   */
  NONE,
  
  /**
   * Style for constants.
   */
  CONSTANT,
  
  /**
   * Style for keywords.
   */
  KEYWORD,
  
  /**
   * Style for comments.
   */
  COMMENT,
}
