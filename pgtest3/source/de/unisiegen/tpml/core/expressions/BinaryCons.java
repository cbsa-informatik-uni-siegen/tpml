package de.unisiegen.tpml.core.expressions ;


/**
 * The binary version of the <code>cons</code> operator, written as
 * <code>::</code> (two colons), which is syntactic sugar for the unary
 * <code>cons</code> operator.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see BinaryOperator
 * @see UnaryCons
 */
public final class BinaryCons extends BinaryOperator
{
  /**
   * Allocates a new <code>BinaryCons</code> instances.
   */
  public BinaryCons ( )
  {
    super ( "::" , PrettyPrintPriorities.PRIO_BINARY_CONS ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @ Override
  public Expression applyTo ( final Expression pExpression1 ,
      final Expression pExpression2 ) throws BinaryOperatorException
  {
    try
    {
      // try to create a new list from e1 and e2
      return new List ( pExpression1 , pExpression2 ) ;
    }
    catch ( final ClassCastException e )
    {
      // we're stuck
      throw new BinaryOperatorException ( this , pExpression1 , pExpression2 ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public BinaryCons clone ( )
  {
    return new BinaryCons ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "BinaryCons" ; //$NON-NLS-1$
  }
}
