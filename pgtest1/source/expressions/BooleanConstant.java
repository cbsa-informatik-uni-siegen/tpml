package expressions;

/**
 * Represents boolean constants.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BooleanConstant extends Constant {
  //
  // Constants
  //
  
  /**
   * The <code>true</code> expression.
   */
  public static final BooleanConstant TRUE = new BooleanConstant("true");
  
  /**
   * The <code>false</code> expression.
   */
  public static final BooleanConstant FALSE = new BooleanConstant("false");

  
  
  //
  // Constructor (private)
  //

  /**
   * Allocates a new <code>BooleanConstant</code> with the
   * string representation given in <code>text</code>.
   * 
   * @param text the string representation of the constant.
   */
  private BooleanConstant(String text) {
    super(text);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if the primitive value of
   * this boolean constant is <code>true</code>.
   * 
   * @return the primitive value of the boolean constant.
   */
  public final boolean isTrue() {
    return (this == TRUE);
  }
}
