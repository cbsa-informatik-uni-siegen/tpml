package de.unisiegen.tpml.core.expressions ;


/**
 * The constant for the empty list, represented as <code>[]</code>, which
 * must be handled differently from the <code>List</code> expressions, which
 * are syntactic sugar.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 */
public final class EmptyList extends Constant
{
  /**
   * Allocates a new <code>EmptyList</code> instance.
   */
  public EmptyList ( )
  {
    super ( "[]" ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>EmptyList</code> instance.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   */
  public EmptyList ( int pParserStartOffset , int pParserEndOffset )
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
  public EmptyList clone ( )
  {
    return new EmptyList ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Empty-List" ; //$NON-NLS-1$
  }
}
