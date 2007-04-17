package de.unisiegen.tpml.core.expressions ;


/**
 * Abstract base class for unary operators in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 */
public abstract class UnaryOperator extends Constant
{
  /**
   * Allocates a new <code>UnaryOperator</code> with the specified string
   * representation <code>text</code>.
   * 
   * @param pText the string representation of the operator.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   * @see Constant#Constant(String)
   */
  protected UnaryOperator ( String pText )
  {
    super ( pText ) ;
  }


  /**
   * Applies this unary operator to the specified expression <code>e</code>.
   * If the operator cannot be applied to the expression <code>e</code>, and
   * {@link UnaryOperatorException} is thrown.
   * 
   * @param pExpression the operand expression.
   * @return the resulting expression.
   * @throws NullPointerException if <code>e</code> is <code>null</code>.
   * @throws UnaryOperatorException if the unary operator cannot be applied to
   *           the expression <code>e</code>.
   */
  public abstract Expression applyTo ( Expression pExpression )
      throws UnaryOperatorException ;


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract UnaryOperator clone ( ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;
}
