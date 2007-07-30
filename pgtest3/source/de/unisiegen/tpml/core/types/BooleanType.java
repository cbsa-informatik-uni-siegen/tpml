package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>bool</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see PrimitiveType
 */
public final class BooleanType extends PrimitiveType
{
  /**
   * The keyword <code>bool</code>.
   */
  private static final String BOOL = "bool" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = "Boolean-Type" ; //$NON-NLS-1$


  /**
   * Allocates a new <code>BooleanType</code> instance.
   */
  public BooleanType ( )
  {
    super ( BOOL ) ;
  }


  /**
   * Allocates a new <code>BooleanType</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public BooleanType ( int pParserStartOffset , int pParserEndOffset )
  {
    this ( ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public BooleanType clone ( )
  {
    return new BooleanType ( ) ;
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
