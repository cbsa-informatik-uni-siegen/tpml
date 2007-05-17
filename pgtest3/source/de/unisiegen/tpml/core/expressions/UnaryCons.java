package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>cons</code> operator, which takes a pair, with an element and a
 * list, and constructs a new list.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 */
public final class UnaryCons extends Constant
{
  /**
   * Constructs a new <code>UnaryCons</code> instance.
   */
  public UnaryCons ( )
  {
    super ( "cons" ) ; //$NON-NLS-1$
  }


  /**
   * Constructs a new <code>UnaryCons</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public UnaryCons ( int pParserStartOffset , int pParserEndOffset )
  {
    this ( ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public UnaryCons clone ( )
  {
    return new UnaryCons ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unary-Cons" ; //$NON-NLS-1$
  }
}
