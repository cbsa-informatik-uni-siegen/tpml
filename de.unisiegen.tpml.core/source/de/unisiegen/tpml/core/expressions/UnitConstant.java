package de.unisiegen.tpml.core.expressions;


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
   * The keyword <code>()</code>.
   */
  private static final String UNIT = "()"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( UnitConstant.class );


  /**
   * Allocates a new <code>UnitConstant</code>.
   */
  public UnitConstant ()
  {
    super ( UNIT );
  }


  /**
   * Allocates a new <code>UnitConstant</code>.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public UnitConstant ( int pParserStartOffset, int pParserEndOffset )
  {
    this ();
    this.parserStartOffset = pParserStartOffset;
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public UnitConstant clone ()
  {
    return new UnitConstant ();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }
}
