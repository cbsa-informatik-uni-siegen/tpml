package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>!</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Assign
 * @see Ref
 * @see UnaryOperator
 */
public final class Deref extends UnaryOperator
{
  /**
   * Allocates a new <code>Deref</code> instance.
   */
  public Deref ( )
  {
    super ( "!" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc} This method always throws
   * {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>!</code> operator requires special handling in
   * the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the
   *           <code>!</code> operator must be handled by the interpreter.
   * @see UnaryOperator#applyTo(Expression)
   */
  @ SuppressWarnings ( "unused" )
  @ Override
  public Expression applyTo ( @ SuppressWarnings ( "unused" )
  Expression pExpression ) throws UnaryOperatorException
  {
    throw new UnsupportedOperationException (
        "deref operator must be handled by the interpreter" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Deref clone ( )
  {
    return new Deref ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Deref" ; //$NON-NLS-1$
  }
}
