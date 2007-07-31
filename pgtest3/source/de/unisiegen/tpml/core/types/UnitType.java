package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>unit</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 */
public final class UnitType extends PrimitiveType
{
  /**
   * The keyword <code>unit</code>.
   */
  private static final String UNIT = "unit" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( UnitType.class ) ;


  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnitType ( )
  {
    super ( UNIT ) ;
  }


  /**
   * Allocates a new <code>UnitType</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public UnitType ( int pParserStartOffset , int pParserEndOffset )
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
  public UnitType clone ( )
  {
    return new UnitType ( ) ;
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
