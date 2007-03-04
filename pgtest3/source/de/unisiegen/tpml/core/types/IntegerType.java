package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>int</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see #INT
 * @see PrimitiveType
 */
public final class IntegerType extends PrimitiveType
{
  /**
   * The single instance of the <code>IntegerType</code> class, which
   * represents the <tt>int</tt> type in our type system.
   */
  public static final IntegerType INT = new IntegerType ( ) ;


  /**
   * Allocates a new <code>IntegerType</code> instance.
   * 
   * @see #INT
   */
  private IntegerType ( )
  {
    super ( "int" ) ; //$NON-NLS-1$
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
