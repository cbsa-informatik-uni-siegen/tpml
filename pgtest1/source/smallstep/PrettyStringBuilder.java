package smallstep;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides functionality to generate a <code>PrettyString</code>
 * in an incremental fashion.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see smallstep.PrettyString
 */
final class PrettyStringBuilder {
  /**
   * Allocates a new <code>PrettyStringBuilder</code>. The returned
   * builder will generate an annotation for <code>expression</code>
   * for the whole string represented by this builder.
   * 
   * @param expression
   * @param returnPriority
   */
  PrettyStringBuilder(Expression expression, int returnPriority) {
    this.expression = expression;
    this.returnPriority = returnPriority;
  }
  
  /**
   * Appends a break location to the string builder.
   * A break marks the location as possible newline
   * insertion position for the presenter. 
   */
  void appendBreak() {
    this.items.add(new BreakItem());
  }
  
  /**
   * Inserts the given <code>builder</code> at the specified
   * <code>argumentPriority</code> at the end of our builder.
   *   
   * @param builder the <code>PrettyStringBuilder</code> to insert.
   * @param argumentPriority the argument priority of the <code>builder</code>.
   */
  void appendBuilder(PrettyStringBuilder builder, int argumentPriority) {
    // check if we need to add parenthesis
    boolean parenthesis = (builder.getReturnPriority() < argumentPriority);
    
    // add the required items
    if (parenthesis)
      this.items.add(new TextItem("(", PrettyStyle.NONE));
    this.items.add(new BuilderItem(builder));
    if (parenthesis)
      this.items.add(new TextItem(")", PrettyStyle.NONE));
  }

  /**
   * Appends the given <code>constant</code> to the pretty string
   * builder. Constants will be highlighted when displayed to the
   * user.
   * 
   * @param constant the constant to append.
   * 
   * @see #appendKeyword(String)
   * @see #appendText(String)
   */
  void appendConstant(String constant) {
    this.items.add(new TextItem(constant, PrettyStyle.CONSTANT));
  }
  
  /**
   * Appends the given <code>keyword</code> to the pretty string
   * builder. Keywords will be highlighted when displayed to the
   * user.
   * 
   * @param keyword the keyword to append.
   *
   * @see #appendConstant(String)
   * @see #appendText(String)
   */
  void appendKeyword(String keyword) {
    this.items.add(new TextItem(keyword, PrettyStyle.KEYWORD));
  }
  
  /**
   * Appends the given <code>text</code> to the pretty string
   * builder. Don't use this method for keywords, but use the
   * <code>appendKeyword</code> method instead.
   * 
   * @param text the text to append.
   * 
   * @see #appendKeyword(String)
   */
  void appendText(String text) {
    this.items.add(new TextItem(text, PrettyStyle.NONE));
  }

  /**
   * Gives the <code>returnPriority</code> for the pretty printer
   * of this builder. The <code>returnPriority</code> is specified
   * when constructing the builder.
   * 
   * @return the returnPriority.
   */
  public int getReturnPriority() {
    return this.returnPriority;
  }
  
  /**
   * Converts the string builder content to a <code>PrettyString</code>.
   * The returned <code>PrettyString</code> will be read-only and not
   * updated when the state of the pretty string builder changes.
   * 
   * @return the pretty string for the current builder content.
   */
  PrettyString toPrettyString() {
    // determine the final string length for the builder contents
    int length = determineStringLength();
    
    // allocate a styles map
    PrettyStyle styles[] = new PrettyStyle[length];
    
    // allocate the string buffer
    StringBuilder buffer = new StringBuilder(length);
    
    // allocate an empty annotations map
    Map<Expression, PrettyAnnotation> annotations = new HashMap<Expression, PrettyAnnotation>();
    
    // determine the string representation and place it into the string buffer
    determineString(buffer, annotations, styles);
    
    return new DefaultPrettyString(buffer.toString(), annotations, styles);
  }

  /**
   * Returns the string representation of the current string
   * builder content. This method is used for debugging
   * purposes.
   * 
   * @return the string representation of the current content.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return toPrettyString().toString();
  }

  /**
   * Determines the required string size for the string representation
   * of the pretty string builder contents.
   * 
   * @return the required string size.
   * 
   * @see #determineString(StringBuilder, HashMap, boolean[])
   */
  private int determineStringLength() {
    int length = 0;
    for (ListIterator<Item> iterator = this.items.listIterator(); iterator.hasNext(); )
      length += iterator.next().determineStringLength();
    return length;
  }
  
  /**
   * Determines the string representation of the pretty string builder contents
   * and places the result into <code>buffer</code>.
   * 
   * The <code>styles</code> map must be large enough to contain a <code>PrettyStyle</code>
   * entry for each character in the target string,
   * 
   * @param buffer the target string buffer.
   * @param annotations the result annotations map.
   * @param styles the result <code>PrettyStyle</code> map.
   * 
   * @see #determineStringLength()
   */
  private void determineString(StringBuilder buffer, Map<Expression, PrettyAnnotation> annotations, PrettyStyle[] styles) {
    // remember the start offset for the annotation constructor
    int startOffset = buffer.length();
    
    // we allocate the break offset list only on-demand
    LinkedList<Integer> breakOffsetList = null;
    
    // process all items for this string builder
    for (ListIterator<Item> iterator = this.items.listIterator(); iterator.hasNext(); ) {
      Item item = iterator.next();
      
      if (item instanceof BreakItem) {
        if (breakOffsetList == null)
          breakOffsetList = new LinkedList<Integer>();
        breakOffsetList.add(buffer.length());
      }
      else if (item instanceof TextItem) {
        String text = ((TextItem)item).content;
        for (int i = 0; i < text.length(); ++i)
          styles[buffer.length() + i ] = ((TextItem)item).style;
        buffer.append(text);
      }
      else if (item instanceof BuilderItem) {
        ((BuilderItem)item).builder.determineString(buffer, annotations, styles);
      }
    }
    
    // add the annotation for the current builder
    if (breakOffsetList != null) {
      // transform the break offset list to an integer array
      int[] breakOffsets = new int[breakOffsetList.size()];
      for (int i = 0; i < breakOffsets.length; ++i)
        breakOffsets[i] = breakOffsetList.get(i);
      annotations.put(this.expression, new PrettyAnnotation(startOffset, buffer.length() - 1, breakOffsets));
    }
    else {
      // just use the empty array for the break offsets to be more efficient
      annotations.put(this.expression, new PrettyAnnotation(startOffset, buffer.length() - 1, EMPTY_ARRAY));
    }
  }
  
  private Expression expression;
  private int returnPriority;
  private LinkedList<Item> items = new LinkedList<Item>();
  private static int[] EMPTY_ARRAY = new int[0];
  

  // Pretty Printer Item classes
  private static abstract class Item {
    abstract int determineStringLength();
  }
  
  private static class BreakItem extends Item {
    BreakItem() {
    }
    
    int determineStringLength() {
      return 0;
    }
  }
  
  private static class TextItem extends Item {
    TextItem(String content, PrettyStyle style) {
      this.content = content;
      this.style = style;
    }
    
    int determineStringLength() {
      return this.content.length();
    }
    
    String content;
    PrettyStyle style;
  }
  
  private static class BuilderItem extends Item {
    BuilderItem(PrettyStringBuilder builder) {
      this.builder = builder;
    }
    
    int determineStringLength() {
      return this.builder.determineStringLength();
    }
    
    PrettyStringBuilder builder;
  }
}
