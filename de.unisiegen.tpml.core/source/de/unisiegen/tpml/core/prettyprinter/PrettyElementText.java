package de.unisiegen.tpml.core.prettyprinter;

import org.w3c.dom.Node;

/**
 * Simple <code>PrettyElement</code>, which just appends a given text
 * using a given style (both determined from the rule specification)
 * to a <code>PrettyStringBuilder</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement
 */
final class PrettyElementText extends PrettyElement {
  /**
   * Allocates a new <code>PrettyElementText</code> instance based
   * on the XML <code>node</code> of the rule descriptor.
   * 
   * @param node the XML node used for the construction.
   */
  public PrettyElementText(Node node) {
    // lookup the style enum value for the style attribute value
    this.style = PrettyStyle.valueOf(node.getAttributes().getNamedItem("style").getTextContent().toUpperCase());

    // determine the text from the node content
    this.text = node.getTextContent();
  }
  
  /**
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement#appendObjectToBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder, java.lang.Object)
   */
  @Override
  void appendObjectToBuilder(PrettyStringBuilder builder, Object object) {
    builder.appendText(this.text, this.style);
  }

  private PrettyStyle style;
  private String text;
}
