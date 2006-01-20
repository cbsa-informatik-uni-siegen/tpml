package de.unisiegen.tpml.core.prettyprinter;

/**
 * Pretty rules are constructed of so called elements, which
 * provide the specific functionality used for constructing
 * a pretty string for an object handled to a rule. 
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyRule
 */
abstract class PrettyElement {
  /**
   * Formats <code>object</code> according to the specification of this
   * rule element and appends it to the given <code>builder</code>.
   * 
   * @param builder the pretty string builder used for constructing the pretty string.
   * @param object the <code>Object</code> to append to <code>builder</code>.
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyRule#appendObjectToBuilder(PrettyStringBuilder, Object)
   */
  abstract void appendObjectToBuilder(PrettyStringBuilder builder, Object object);
}
