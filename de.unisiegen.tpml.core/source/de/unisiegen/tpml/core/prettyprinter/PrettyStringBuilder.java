package de.unisiegen.tpml.core.prettyprinter;

import java.util.LinkedList;
import java.util.Vector;

import de.unisiegen.tpml.core.util.IntegerUtilities;

/**
 * Helper class used in the construction of pretty strings.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
final class PrettyStringBuilder {
  /**
   * Allocates a new <code>PrettyStringBuilder</code>, which uses the
   * <code>PrettyRule</code>s provided by the <code>ruleSet</code>.
   * 
   * @param ruleSet the set of <code>PrettyRule</code>s.
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyRuleSet
   */
  PrettyStringBuilder(PrettyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
  }
  
  /**
   * Appends the current text offset as possible linebreak
   * to the list of break offsets for the current object
   * (and therefore as break offset for the current
   * annotation).
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation
   * @see #appendObject(Object)
   * @see #appendText(String, PrettyStyle)
   */
  void appendBreak() {
    // add the current buffer length as possible break offset
    this.breakOffsets.add(this.buffer.length());
  }
  
  /**
   * Appends the specified <code>object</code> at the given <code>priority</code>
   * to the pretty string builder. If a <code>PrettyRule</code> is present that
   * specifies how to handle the <code>object</code> this rule is used to append
   * the <code>object</code>, else the string representation of <code>object</code>
   * is appended without any special processing.
   * 
   * This method also takes care of adding brackets around <code>object</code> if
   * the <code>priority</code> is greater than the priority of the matching
   * <code>PrettyRule</code>.
   * 
   * @param object the <code>Object</code> to append to the pretty string builder.
   * @param priority the priority at which <code>object</code> is to be inserted.
   *
   * @see #appendBreak()
   * @see #appendText(String, PrettyStyle)
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyElement#appendObjectToBuilder(PrettyStringBuilder, Object)
   */
  void appendObject(Object object, int priority) {
    // check if we have a rule for this kind of object
    PrettyRule rule = this.ruleSet.lookupRuleForObject(object);
    if (rule != null) {
      // check if we need to add brackets
      boolean requiresBrackets = (priority > rule.getPriority());
      
      // remember the previous break offsets
      Vector<Integer> previousBreakOffsets = this.breakOffsets;
      try {
        // add opening (left) bracket
        if (requiresBrackets)
          this.buffer.append('(');
        
        // setup an empty break offsets list, to be filled while processing the object
        this.breakOffsets = new Vector<Integer>();
        
        // remember the current offset
        int startOffset = this.buffer.length();
        
        // let the rule do it's job
        rule.appendObjectToBuilder(this, object);
        
        // generate an annotation for the object if we have atleast one char
        int endOffset = this.buffer.length() - 1;
        if (endOffset >= startOffset) {
          // generate a break offset table from the collection
          int[] breakOffsets = IntegerUtilities.toArray(this.breakOffsets);
          
          // allocate an annotation and add it to our list
          this.annotations.add(new PrettyAnnotation(startOffset, endOffset, breakOffsets, object));
        }
      }
      finally {
        // reset the previously remembered break offsets
        this.breakOffsets = previousBreakOffsets;
        
        // add the closing (right) bracket
        if (requiresBrackets)
          this.buffer.append(')');
      }
    }
    else {
      // just append the string representation of the object
      appendText(object.toString(), PrettyStyle.DEFAULT);
    }
  }
  
  /**
   * Appends the given <code>text</code> chunk to the pretty string,
   * using the specified <code>style</code>. 
   * 
   * @param text the text to append.
   * @param style the <code>PrettyStyle</code> to apply to <code>text</code>.
   * 
   * @see #appendBreak()
   * @see #appendObject(Object)
   */
  void appendText(String text, PrettyStyle style) {
    // remember the current length as start offset
    int startOffset = this.buffer.length();
    
    // append the text to the buffer
    this.buffer.append(text);

    // no need to set a marker for DEFAULT styles
    if (style != PrettyStyle.DEFAULT) {
      // determine the new length (as end offset)
      int endOffset = this.buffer.length();
    
      // allocate a marker for the text
      this.markers.add(new Marker(startOffset, endOffset - startOffset, style));
    }
  }
  
  /**
   * Allocates a new <code>PrettyString</code>, which represents
   * the current contents of this <code>PrettyStringBuilder</code>
   * in an immutable way.
   * 
   * The <code>PrettyStringBuilder</code> itself may not be used
   * again afterwards, as certain internal structures aren't
   * clone while constructing the pretty string (for efficiency
   * reasons).
   * 
   * @return the <code>PrettyString</code> for the current builder contents.
   */
  PrettyString toPrettyString() {
    // determine the pretty string styles
    PrettyStyle[] styles = new PrettyStyle[this.buffer.length()];
    for (int i = 0; i < styles.length; ++i)
      styles[i] = PrettyStyle.DEFAULT;
    for (Marker marker : this.markers)
      for (int i = 0; i < marker.getLength(); ++i)
        styles[marker.getOffset() + i] = marker.getStyle();
    
    // and put it all together
    return new DefaultPrettyString(this.annotations, this.buffer, styles);
  }
  
  /**
   * Simply throws an exception as comparing string builders
   * don't make sense and should never be performed.
   * 
   * @param obj another object.
   * 
   * @return never returns.
   * 
   * @throws UnsupportedOperationException always, since one should not
   *                                       try to compare pretty string
   *                                       builders.
   *                                       
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    throw new UnsupportedOperationException();
  }
  
  // memeber attributes
  private LinkedList<PrettyAnnotation> annotations = new LinkedList<PrettyAnnotation>(); 
  private Vector<Integer> breakOffsets;
  private StringBuilder buffer = new StringBuilder(256);
  private LinkedList<Marker> markers = new LinkedList<Marker>();
  private PrettyRuleSet ruleSet;
  
  // helper class to associate styles with text
  // chunks during pretty string construction
  private static class Marker {
    Marker(int offset, int length, PrettyStyle style) {
      this.offset = offset;
      this.length = length;
      this.style = style;
    }
    
    int getOffset() {
      return this.offset;
    }
    
    int getLength() {
      return this.length;
    }
    
    PrettyStyle getStyle() {
      return this.style;
    }
    
    private int offset;
    private int length;
    private PrettyStyle style;
  }
}
