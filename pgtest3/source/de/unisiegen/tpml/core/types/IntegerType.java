package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>int</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see PrimitiveType
 */
public final class IntegerType extends PrimitiveType
{
  /**
   * The keyword <code>int</code>.
   */
  private static final String INT = "int" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( IntegerType.class ) ;


  /**
   * Allocates a new <code>IntegerType</code> instance.
   */
  public IntegerType ( )
  {
    super ( INT ) ;
  }


  /**
   * Allocates a new <code>IntegerType</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public IntegerType ( int pParserStartOffset , int pParserEndOffset )
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
  public IntegerType clone ( )
  {
    return new IntegerType ( ) ;
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
