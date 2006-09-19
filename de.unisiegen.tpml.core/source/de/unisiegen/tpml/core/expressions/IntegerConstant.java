package de.unisiegen.tpml.core.expressions;

/**
 * Instances of this class are used to represent integer constants in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class IntegerConstant extends Constant {
  //
  // Attributes
  //
  
  /**
   * The numeric value of the integer constant.
   * 
   * @see #getNumber()
   */
  private int number;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>IntegerConstant</code> with the given <code>number</code>.
   * 
   * @param number the numeric value for the integer constant.
   * 
   * @see #getNumber()
   */
  public IntegerConstant(int number) {
    super(String.valueOf(number));
    this.number = number;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the numeric value of this integer constant.
   * 
   * @return the numeric value.
   */
  public int getNumber() {
    return this.number;
  }
}
