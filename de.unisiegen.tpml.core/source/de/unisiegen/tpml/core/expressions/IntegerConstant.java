package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class are used to represent integer constants in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Constant
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class IntegerConstant extends Constant
{
  //
  // Attributes
  //
  /**
   * The numeric value of the integer constant.
   * 
   * @see #intValue()
   */
  private int intValue ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>IntegerConstant</code> with the given
   * <code>intValue</code>.
   * 
   * @param intValue the numeric value for the integer constant.
   * @see #intValue()
   */
  public IntegerConstant ( int intValue )
  {
    super ( String.valueOf ( intValue ) ) ;
    this.intValue = intValue ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Integer" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * Returns the value of this <code>IntegerConstant</code> as an
   * <code>int</code>.
   * 
   * @return the numeric value represented by this object after conversion to
   *         type <code>int</code>.
   */
  public int intValue ( )
  {
    return this.intValue ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public IntegerConstant clone ( )
  {
    return new IntegerConstant ( this.intValue ) ;
  }
}
