package de.unisiegen.tpml.core.prettyprinter;

/**
 * Possible styles for items in a <code>PrettyString</code>. Note that
 * these are just suggestions provided by the <code>PrettyString</code>,
 * it's up to the output controlling instances to decide whether and
 * what to highlight according to the styles provided.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public enum PrettyStyle {
  DEFAULT,
  CONSTANT,
  KEYWORD,
}
