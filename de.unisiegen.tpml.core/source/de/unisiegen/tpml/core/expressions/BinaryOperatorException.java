package de.unisiegen.tpml.core.expressions ;


/**
 * This exception is thrown whenever a {@link expressions.BinaryOperator} is
 * being applied to invalid operands during the evaluation. For example if the
 * arithmetic plus operator is applied to <code>true</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 */
public final class BinaryOperatorException extends Exception
{
  //
  // Constants
  //
  /**
   * The unique serial version id.
   */
  private static final long serialVersionUID = 3206810160508438333L ;


  //
  // Attributes
  //
  /**
   * The binary operator that failed to apply.
   * 
   * @see #getOperator()
   */
  private BinaryOperator operator ;


  /**
   * The first operand.
   * 
   * @see #getE1()
   */
  private Expression e1 ;


  /**
   * The second operand.
   * 
   * @see #getE2()
   */
  private Expression e2 ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>BinaryOperatorException</code>, which signals that
   * the specified binary <code>operator</code> could not be applied to the
   * operands <code>e1</code> and <code>e2</code>.
   * 
   * @param operator the {@link BinaryOperator} that failed to apply.
   * @param e1 the first operand.
   * @param e2 the second operand.
   * @throws NullPointerException if <code>operator</code>, <code>e1</code>
   *           or <code>e2</code> is <code>null</code>.
   */
  public BinaryOperatorException ( BinaryOperator operator , Expression e1 ,
      Expression e2 )
  {
    super ( "Cannot apply " + operator + " to " + e1 + " and " + e2 ) ;
    if ( operator == null )
    {
      throw new NullPointerException ( "operator is null" ) ;
    }
    if ( e1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ;
    }
    if ( e2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ;
    }
    this.operator = operator ;
    this.e1 = e1 ;
    this.e2 = e2 ;
  }


  /**
   * Returns the caption of this {@link Exception}.
   * 
   * @return The caption of this {@link Exception}.
   */
  public String getCaption ( )
  {
    return "Binary-Operator-Exception" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * Returns the {@link BinaryOperator} that failed to apply.
   * 
   * @return the binary operator that failed to apply.
   */
  public BinaryOperator getOperator ( )
  {
    return this.operator ;
  }


  /**
   * Returns the first operand of the operator application that failed.
   * 
   * @return the first operand.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second operand of the operator application that failed.
   * 
   * @return the second operand.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }
}
