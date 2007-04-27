package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>tl</code> operator, which, when applied to a list, returns the
 * list without the first item, or when applied to an empty list, raises the
 * <code>empty_list</code> exception.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Hd
 * @see IsEmpty
 */
public final class Tl extends UnaryListOperator
{
  /**
   * Allocates a new <code>Tl</code> instance.
   */
  public Tl ( )
  {
    super ( "tl" ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>Tl</code> instance.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   */
  public Tl ( int pParserStartOffset , int pParserEndOffset )
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
  public Tl clone ( )
  {
    return new Tl ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Tail" ; //$NON-NLS-1$
  }
}
