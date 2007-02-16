package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>Assign</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Deref
 * @see Ref
 * @see BinaryOperator
 */
public final class Assign extends BinaryOperator
{
  /**
   * Allocates a new <code>Assign</code> operator.
   */
  public Assign ( )
  {
    super ( ":=" , PRIO_ASSIGN ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Assign" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc} The assign operator does not a machine equivalent like the
   * arithmetic or relational operators, but instead operates on a store. So
   * applying an assign operator to its operands must be implemented in the
   * interpreter. This method simply throws
   * {@link UnsupportedOperationException} on every invokation.
   * 
   * @throws UnsupportedOperationException on every invokation.
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @ SuppressWarnings ( "unused" )
  @ Override
  public Expression applyTo ( @ SuppressWarnings ( "unused" )
  Expression pExpression1 , @ SuppressWarnings ( "unused" )
  Expression pExpression2 ) throws BinaryOperatorException
  {
    throw new UnsupportedOperationException (
        "assign operator must be handled by the interpreter" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Assign clone ( )
  {
    return new Assign ( ) ;
  }
}
