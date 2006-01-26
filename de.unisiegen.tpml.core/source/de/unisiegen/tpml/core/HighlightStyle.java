package de.unisiegen.tpml.core;

import de.unisiegen.tpml.core.prettyprinter.PrettyString;

/**
 * Possible styles for items in a {@link PrettyString}. Note that
 * these are just suggestions provided by the {@link PrettyString},
 * it's up to the output controlling instances to decide whether and
 * what to highlight according to the styles provided.
 * 
 * The highlight style will also be used for the syntax highlighting
 * in the editor panes.
 *
 * @author Benedikt Meurer
 * @version $Id: HighlightStyle.java 77 2006-01-22 19:53:04Z benny $
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator#getStyle()
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#styleAt(int)
 */
public enum HighlightStyle {
  DEFAULT,
  ERROR,
  CONSTANT,
  KEYWORD,
  TYPE,
}
