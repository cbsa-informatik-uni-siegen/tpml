package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of the <code>BooleanConstant</code> class are used to represent
 * the logic <code>false</code> and <code>true</code> values in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 * @see Value
 */
public final class BooleanConstant extends Constant
{
  /**
   * The boolean value of the constant.
   * 
   * @see #booleanValue()
   */
  private boolean booleanValue ;


  /**
   * Allocates a new <code>BooleanConstant</code> with the value given in
   * <code>booleanValue</code>.
   * 
   * @param pBooleanValue the boolean value.
   * @see #booleanValue()
   */
  public BooleanConstant ( boolean pBooleanValue )
  {
    super ( pBooleanValue ? "true" : "false" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    this.booleanValue = pBooleanValue ;
  }


  /**
   * Allocates a new <code>BooleanConstant</code> with the value given in
   * <code>booleanValue</code>.
   * 
   * @param pBooleanValue the boolean value.
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @see #booleanValue()
   */
  public BooleanConstant ( boolean pBooleanValue , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pBooleanValue ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Returns the value of this <code>BooleanConstant</code> object as a
   * boolean primitive.
   * 
   * @return the primitive <code>boolean</code> value of this object.
   */
  public boolean booleanValue ( )
  {
    return this.booleanValue ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public BooleanConstant clone ( )
  {
    return new BooleanConstant ( this.booleanValue ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Boolean" ; //$NON-NLS-1$
  }
}
