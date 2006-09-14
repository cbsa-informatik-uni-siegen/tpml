package de.unisiegen.tpml.core.expressions;

/**
 * The single {@link UnitConstant#UNIT} instance of this class represents the constant unit
 * value in the expression hierarchy. The string representation is <tt>()</tt>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class UnitConstant extends Constant {
  //
  // Constants
  //
  
  /**
   * The single <code>UnitConstant</code> instance, which represents the unit value, written as <tt>()</tt>.
   */
  public static final UnitConstant UNIT = new UnitConstant();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>UnitConstant</code>.
   * 
   * @see #UNIT
   */
  private UnitConstant() {
    super("()");
  }
}
