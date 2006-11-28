package de.unisiegen.tpml.core.expressions;

/**
 * Instances of the <code>BooleanConstant</code> class are used to represent the logic 
 * <code>false</code> and <code>true</code> values in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class BooleanConstant extends Constant {
  //
  // Attributes
  //
  
  /**
   * The boolean value of the constant.
   * 
   * @see #booleanValue()
   */
  private boolean booleanValue;
  
  
  
  //
  // Constructor
  //

  /**
   * Allocates a new <code>BooleanConstant</code> with the
   * value given in <code>booleanValue</code>.
   * 
   * @param booleanValue the boolean value.
   * 
   * @see #booleanValue()
   */
  public BooleanConstant(boolean booleanValue) {
    super(booleanValue ? "true" : "false");
    this.booleanValue = booleanValue;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the value of this <code>BooleanConstant</code> object as a boolean primitive.
   * 
   * @return the primitive <code>boolean</code> value of this object.
   */
  public boolean booleanValue() {
    return this.booleanValue;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @Override
  public BooleanConstant clone() {
    return new BooleanConstant(this.booleanValue);
  }
}
