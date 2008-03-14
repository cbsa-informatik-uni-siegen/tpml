package de.unisiegen.tpml.core.expressions;


/**
 * Instances of this class are used to represent integer constants in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see Constant
 * @see Value
 */
public final class IntegerConstant extends Constant
{

  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( IntegerConstant.class );


  /**
   * The numeric value of the integer constant.
   * 
   * @see #intValue()
   */
  private int intValue;


  /**
   * Allocates a new <code>IntegerConstant</code> with the given
   * <code>intValue</code>.
   * 
   * @param pIntValue the numeric value for the integer constant.
   * @see #intValue()
   */
  public IntegerConstant ( int pIntValue )
  {
    super ( String.valueOf ( pIntValue ) );
    this.intValue = pIntValue;
  }


  /**
   * Allocates a new <code>IntegerConstant</code> with the given
   * <code>intValue</code>.
   * 
   * @param pIntValue the numeric value for the integer constant.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @see #intValue()
   */
  public IntegerConstant ( int pIntValue, int pParserStartOffset,
      int pParserEndOffset )
  {
    this ( pIntValue );
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public IntegerConstant clone ()
  {
    return new IntegerConstant ( this.intValue );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns the value of this <code>IntegerConstant</code> as an
   * <code>int</code>.
   * 
   * @return the numeric value represented by this object after conversion to
   *         type <code>int</code>.
   */
  public int intValue ()
  {
    return this.intValue;
  }
}
