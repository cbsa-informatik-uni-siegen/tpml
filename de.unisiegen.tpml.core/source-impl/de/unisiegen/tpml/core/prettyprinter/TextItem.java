package de.unisiegen.tpml.core.prettyprinter;

import java.util.List;
import java.util.Map;

/**
 * This class represents a chunk of text in {@link de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder}s
 * item list, which is associated with a given {@link de.unisiegen.tpml.core.prettyprinter.PrettyStyle}. For example
 * this is used to implement various methods of the pretty string builder, like <code>addConstant()</code>,
 * <code>addKeyword()</code> and <code>addText()</code>.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem
 * @see de.unisiegen.tpml.core.prettyprinter.DefaultPrettyStringBuilder
 */
final class TextItem extends AbstractItem {
  //
  // Attributes
  //
  
  /**
   * The text content.
   */
  private String content;
  
  /**
   * The style for all characters in {@link #content}.
   */
  private PrettyStyle style;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>TextItem</code>, which represents a portion of text, as given by the
   * <code>content</code>, associated with the specified <code>style</code>.
   * 
   * @param content the text content.
   * @param style the style for all characters in the <code>content</code>.
   * 
   * @throws NullPointerException if either <code>content</code> or <code>style</code> is <code>null</code>.
   */
  TextItem(String content, PrettyStyle style) {
    if (content == null) {
      throw new NullPointerException("content is null");
    }
    if (style == null) {
      throw new NullPointerException("style is null");
    }
    this.content = content;
    this.style = style;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineString(StringBuilder, Map, List, PrettyStyle[])
   */
  @Override
  void determineString(StringBuilder buffer, Map<PrettyPrintable, PrettyAnnotation> annotations, List<Integer> breakOffsets, PrettyStyle[] styles) {
    // set the style for all characters
    for (int n = 0; n < this.content.length(); ++n) {
      styles[buffer.length() + n] = this.style;
    }
    
    // append the text content
    buffer.append(this.content);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.AbstractItem#determineStringLength()
   */
  @Override
  int determineStringLength() {
    return this.content.length();
  }
}
