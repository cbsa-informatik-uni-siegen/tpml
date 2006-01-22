package de.unisiegen.tpml.core.prettyprinter;

/**
 * Possible styles for items in a {@link PrettyString}. Note that
 * these are just suggestions provided by the {@link PrettyString},
 * it's up to the output controlling instances to decide whether and
 * what to highlight according to the styles provided.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyCharacterIterator#getStyle()
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString#styleAt(int)
 */
public enum PrettyStyle {
  DEFAULT,
  CONSTANT,
  KEYWORD,
}
