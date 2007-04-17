package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>not</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Not extends UnaryOperator
{
  /**
   * Allocates a new <code>Not</code> operator.
   */
  public Not ( )
  {
    super ( "not" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see UnaryOperator#applyTo(Expression)
   */
  @ Override
  public Expression applyTo ( final Expression pExpression )
      throws UnaryOperatorException
  {
    try
    {
      return new BooleanConstant ( ! ( ( BooleanConstant ) pExpression )
          .booleanValue ( ) ) ;
    }
    catch ( final ClassCastException cause )
    {
      throw new UnaryOperatorException ( this , pExpression , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Not clone ( )
  {
    return new Not ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Not" ; //$NON-NLS-1$
  }
}
