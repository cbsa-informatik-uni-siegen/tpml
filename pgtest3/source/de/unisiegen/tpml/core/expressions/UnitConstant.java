package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the constant unit value in the expression
 * hierarchy. The string representation is <tt>()</tt>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 * @see Value
 */
public final class UnitConstant extends Constant
{
  /**
   * Allocates a new <code>UnitConstant</code>.
   */
  public UnitConstant ( )
  {
    super ( "()" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unit" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public UnitConstant clone ( )
  {
    return new UnitConstant ( ) ;
  }
}
