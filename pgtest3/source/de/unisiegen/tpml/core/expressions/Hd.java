package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>hd</code> operator, which, when applied to a list, returns the
 * first item of the list, or raises an <code>empty_list</code> exception if
 * the list is empty.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Tl
 * @see IsEmpty
 */
public final class Hd extends UnaryListOperator
{
  /**
   * The keyword <code>hd</code>.
   */
  private static final String HD = "hd" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = "Head" ; //$NON-NLS-1$


  /**
   * Allocates a new <code>Hd</code> instance.
   */
  public Hd ( )
  {
    super ( HD ) ;
  }


  /**
   * Allocates a new <code>Hd</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Hd ( int pParserStartOffset , int pParserEndOffset )
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
  public Hd clone ( )
  {
    return new Hd ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }
}
