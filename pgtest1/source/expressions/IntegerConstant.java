package expressions;

/**
 * Represents integer constants.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class IntegerConstant extends Constant {
  //
  // Attributes
  //
  
  /**
   * The numeric value of this <code>IntegerConstant</code>.
   * 
   * @see #getNumber()
   */
  private int number;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new integer constant with the given
   * <code>number</code>.
   * 
   * @param number the integer number.
   */
  public IntegerConstant(int number) {
    super(String.valueOf(number));
    this.number = number;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * Returns the number represented by this integer constant.
   * 
   * @return the number represented by this integer constant.
   */
  public int getNumber() {
    return this.number;
  }
}
