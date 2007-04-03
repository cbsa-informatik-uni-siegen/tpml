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
   * Allocates a new <code>IntegerType</code> instance.
   */
  public IntegerType ( )
  {
    super ( "int" ) ; //$NON-NLS-1$
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
    return "Integer-Type" ; //$NON-NLS-1$
  }
}
