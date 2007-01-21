package de.unisiegen.tpml.core.expressions ;


/**
 * The binary version of the <code>cons</code> operator, written as
 * <code>::</code> (two colons), which is syntactic sugar for the unary
 * <code>cons</code> operator.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 * @see de.unisiegen.tpml.core.expressions.UnaryCons
 */
public final class BinaryCons extends BinaryOperator
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>BinaryCons</code> instances.
   */
  public BinaryCons ( )
  {
    super ( "::" , PRIO_BINARY_CONS ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "BinaryCons" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.BinaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression applyTo ( Expression e1 , Expression e2 )
      throws BinaryOperatorException
  {
    try
    {
      // try to create a new list from e1 and e2
      return new List ( e1 , e2 ) ;
    }
    catch ( ClassCastException e )
    {
      // we're stuck
      throw new BinaryOperatorException ( this , e1 , e2 ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public BinaryCons clone ( )
  {
    return new BinaryCons ( ) ;
  }
}
