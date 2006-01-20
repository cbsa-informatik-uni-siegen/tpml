package de.unisiegen.tpml.core.prettyprinter;

import org.w3c.dom.Node;

/**
 * Simple <code>PrettyElement</code> implementation, which does
 * nothing more than appending a break offset marker to a
 * <code>PrettyStringBuilder</code>, when processed as part of
 * a <code>PrettyRule</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement
 */
final class PrettyElementBreak extends PrettyElement {
  /**
   * Allocates a new <code>PrettyElementBreak</code> based on the
   * XML <code>node</code> of the rule descriptor.
   * 
   * @param node the XML node used for the construction.
   */
  public PrettyElementBreak(Node node) {
    // nothing to do for break elements
  }
  
  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement#appendObjectToBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder, java.lang.Object)
   */
  @Override
  void appendObjectToBuilder(PrettyStringBuilder builder, Object object) {
    builder.appendBreak();
  }
}
