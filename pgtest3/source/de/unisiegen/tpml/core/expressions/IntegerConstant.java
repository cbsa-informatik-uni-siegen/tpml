package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class are used to represent integer constants in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 * @see Value
 */
public final class IntegerConstant extends Constant
{
  /**
   * The numeric value of the integer constant.
   * 
   * @see #intValue()
   */
  private int intValue ;


  /**
   * Allocates a new <code>IntegerConstant</code> with the given
   * <code>intValue</code>.
   * 
   * @param pIntValue the numeric value for the integer constant.
   * @see #intValue()
   */
  public IntegerConstant ( final int pIntValue )
  {
    super ( String.valueOf ( pIntValue ) ) ;
    this.intValue = pIntValue ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public IntegerConstant clone ( )
  {
    return new IntegerConstant ( this.intValue ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Integer" ; //$NON-NLS-1$
  }


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
}
