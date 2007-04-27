package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>is_empty</code> operator, which, when applied to a list, returns
 * <code>true</code> if the list is empty, or <code>false</code> if the list
 * contains atleast one item.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Hd
 * @see Tl
 */
public final class IsEmpty extends UnaryListOperator
{
  /**
   * Allocates a new <code>IsEmpty</code> instance.
   */
  public IsEmpty ( )
  {
    super ( "is_empty" ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>IsEmpty</code> instance.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   */
  public IsEmpty ( int pParserStartOffset , int pParserEndOffset )
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
  public IsEmpty clone ( )
  {
    return new IsEmpty ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Is-Empty" ; //$NON-NLS-1$
  }
}
