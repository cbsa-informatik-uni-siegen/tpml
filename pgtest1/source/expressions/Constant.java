package expressions;

import common.prettyprinter.PrettyStringBuilder;

/**
 * Abstract class to represent a constant expression
 * (only values can be constants).
 *
 * @author bmeurer
 * @version $Id:Constant.java 121 2006-04-28 16:45:27Z benny $
 */
public abstract class Constant extends Value {
  //
  // Attributes
  //
  
  /**
   * The string representation of the constant.
   * 
   * @see #toString()
   */
  protected String text;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Constructs a new <code>Constant</code> with the
   * string representation given in <code>text</code>.
   * 
   * @param text the string representation of the
   *             constant.
   */
  protected Constant(String text) {
    this.text = text;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the pretty string builder for constants.
   * 
   * @return the pretty string builder for constants.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendConstant(this.text);
    return builder;
  }
  
  
  
  //
  // Overwritten methods
  //
  
  /**
   * Returns the string representation of this constant.
   * 
   * @return the string representation of this constant.
   *
   * @see expressions.Expression#toString()
   */
  @Override
  public final String toString() {
    return this.text;
  }
}
