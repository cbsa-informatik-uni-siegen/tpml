package de.unisiegen.tpml.core.expressions;

/**
 * The {@link #FALSE} and {@link #TRUE} instances of the <code>BooleanConstant</code> class
 * are used to represent the logic <code>false</code> and <code>true</code> values in the
 * expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class BooleanConstant extends Constant {
  //
  // Constants
  //
  
  /**
   * The <code>false</code> expression.
   * 
   * @see #TRUE
   */
  public static final BooleanConstant FALSE = new BooleanConstant("false");

  /**
   * The <code>true</code> expression.
   * 
   * @see #FALSE
   */
  public static final BooleanConstant TRUE = new BooleanConstant("true");
  
  
  
  //
  // Constructor (private)
  //

  /**
   * Allocates a new <code>BooleanConstant</code> with the
   * string representation given in <code>text</code>.
   * 
   * @param text the string representation of the constant.
   * 
   * @see #FALSE
   * @see #TRUE
   */
  private BooleanConstant(String text) {
    super(text);
  }
}
