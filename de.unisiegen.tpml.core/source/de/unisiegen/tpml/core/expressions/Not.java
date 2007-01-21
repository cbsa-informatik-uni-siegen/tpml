package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>not</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Not extends UnaryOperator
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>Not</code> operator.
   */
  public Not ( )
  {
    super ( "not" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Not" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression applyTo ( Expression e ) throws UnaryOperatorException
  {
    try
    {
      return new BooleanConstant ( ! ( ( BooleanConstant ) e ).booleanValue ( ) ) ;
    }
    catch ( ClassCastException cause )
    {
      throw new UnaryOperatorException ( this , e , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Expression clone ( )
  {
    return new Not ( ) ;
  }
}
