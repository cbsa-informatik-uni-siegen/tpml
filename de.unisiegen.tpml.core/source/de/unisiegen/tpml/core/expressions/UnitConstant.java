package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the constant unit value in the expression
 * hierarchy. The string representation is <tt>()</tt>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class UnitConstant extends Constant
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>UnitConstant</code>.
   */
  public UnitConstant ( )
  {
    super ( "()" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unit" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public UnitConstant clone ( )
  {
    return new UnitConstant ( ) ;
  }
}
