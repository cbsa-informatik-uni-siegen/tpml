package de.unisiegen.tpml.core.prettyprinter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.unisiegen.tpml.core.util.IntegerUtilities;

/**
 * Default implementation of the <code>PrettyStringBuilder</code> interface, which is the
 * heart of the pretty printer.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory
 */
final class DefaultPrettyStringBuilder implements PrettyStringBuilder {
  //
  // Attributes
  //
  
  /**
   * The items already added to this pretty string builder.
   * 
   * @see AbstractItem
   */
  private LinkedList<AbstractItem> items = new LinkedList<AbstractItem>();
  
  /**
   * The pretty printable for which this builder is used to generate a pretty string.
   * 
   * @see PrettyPrintable
   * @see PrettyString
   */
  private PrettyPrintable printable;
  
  /**
   * The return priority of the printable according to the priority grammar.
   */
  private int returnPriority;
  
  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultPrettyStringBuilder</code> for the <code>printable</code>, where
   * the priority used for the <code>printable</code> is <code>returnPriority</code>.
   * 
   * @param printable the printable for which to generate a pretty string.
   * @param returnPriority the priority for the <code>printable</code>, which determines where and
   *                       when to add parenthesis around {@link PrettyPrintable}s added to this
   *                       builder.
   * 
   * @throws NullPointerException if <code>printable</code> is <code>null</code>.
   */
  DefaultPrettyStringBuilder(PrettyPrintable printable, int returnPriority) {
    if (printable == null) {
      throw new NullPointerException("printable is null");
    }
    this.printable = printable;
    this.returnPriority = returnPriority;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBreak()
   */
  public void addBreak() {
    this.items.add(BreakItem.BREAK_ITEM);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder, int)
   */
  public void addBuilder(PrettyStringBuilder builder, int argumentPriority) {
    if (builder == null) {
      throw new NullPointerException("builder is null");
    }
    
    // cast to a default pretty string builder
    DefaultPrettyStringBuilder defaultBuilder = (DefaultPrettyStringBuilder)builder;
    
    // check if we need parenthesis
    boolean parenthesis = (defaultBuilder.returnPriority < argumentPriority);
    
    // add the required items
    if (parenthesis)
      this.items.add(new TextItem("(", PrettyStyle.NONE));
    this.items.add(new BuilderItem(defaultBuilder));
    if (parenthesis)
      this.items.add(new TextItem(")", PrettyStyle.NONE));
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addConstant(java.lang.String)
   */
  public void addConstant(String constant) {
    this.items.add(new TextItem(constant, PrettyStyle.CONSTANT));
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addKeyword(java.lang.String)
   */
  public void addKeyword(String keyword) {
    this.items.add(new TextItem(keyword, PrettyStyle.KEYWORD));
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#addText(java.lang.String)
   */
  public void addText(String text) {
    this.items.add(new TextItem(text, PrettyStyle.NONE));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder#toPrettyString()
   */
  public PrettyString toPrettyString() {
    // determine the final string length for the builder contents
    int length = determineStringLength();
    
    // allocate a styles map
    PrettyStyle styles[] = new PrettyStyle[length];
    
    // allocate the string buffer
    StringBuilder buffer = new StringBuilder(length);
    
    // allocate an empty annotations map
    Map<PrettyPrintable, PrettyAnnotation> annotations = new HashMap<PrettyPrintable, PrettyAnnotation>();
    
    // determine the string representation and place it into the string buffer
    determineString(buffer, annotations, styles);
    
    return new DefaultPrettyString(buffer.toString(), annotations, styles);
  }
  
  
  
  //
  // Pretty string construction
  //

  /**
   * Determines the string representation of this builders contents and adds it to the <code>buffer</code>.
   * The {@link PrettyAnnotation}s present within this builder will be added to the map of <code>annotations</code>,
   * while the {@link PrettyStyle}s for the text chunks within this builder will be added to the <code>styles</code>
   * array.
   * 
   * @param buffer string buffer to store the characters to.
   * @param annotation map to store the annotations to.
   * @param styles array to store the highlighting styles to (per character).
   */
  void determineString(StringBuilder buffer, Map<PrettyPrintable, PrettyAnnotation> annotations, PrettyStyle[] styles) {
    // remember the start offset for the annotation constructor
    int startOffset = buffer.length();
    
    // allocate the list for the break offsets
    LinkedList<Integer> breakOffsets = new LinkedList<Integer>();
    
    // process all items for this string builder
    for (AbstractItem item : this.items) {
      // determine the string representation for the item
      item.determineString(buffer, annotations, breakOffsets, styles);
    }
    
    // add the annotation for the current builder
    annotations.put(this.printable, new PrettyAnnotation(startOffset, buffer.length() - 1, IntegerUtilities.toArray(breakOffsets)));
  }
  
  /**
   * Determines the number of characters required to serialize the pretty string builder
   * content to a pretty string.
   * 
   * @return the number of characters in the generated pretty string.
   */
  int determineStringLength() {
    int length = 0;
    for (AbstractItem item : this.items) {
      length += item.determineStringLength();
    }
    return length;
  }
}
