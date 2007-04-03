package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Abstract base class for binary operators. Binary operators, like unary
 * operators, are always constants, and as such, are always values.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Constant
 */
public abstract class BinaryOperator extends Constant
{
  /**
   * The base pretty print priority for this binary operator, when used within
   * an infix operation.
   * 
   * @see #getPrettyPriority()
   * @see InfixOperation
   */
  private int prettyPriority ;


  /**
   * TODO
   */
  private boolean infixOperator ;


  /**
   * Allocates a new <code>BinaryOperator</code> with the specified
   * <code>prettyPriority</code> used for pretty printing of
   * {@link InfixOperation}s.
   * 
   * @param pText the string representation for this binary operator.
   * @param pPrettyPriority the pretty print priority for infix operations.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  protected BinaryOperator ( String pText , int pPrettyPriority )
  {
    super ( pText ) ;
    this.prettyPriority = pPrettyPriority ;
    this.infixOperator = false ;
  }


  /**
   * Applies the binary operator to the operands <code>e1</code> and
   * <code>e2</code> and returns the resulting expression. If the operator
   * cannot be applied to <code>e1</code> and <code>e2</code> because of a
   * runtime type exception, a {@link BinaryOperatorException} is thrown.
   * 
   * @param pExpression1 the first operand.
   * @param pExpression2 the second operand.
   * @return the resulting expression.
   * @throws BinaryOperatorException if the operator cannot be applied to
   *           <code>e1</code> and <code>e2</code>.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public abstract Expression applyTo ( Expression pExpression1 ,
      Expression pExpression2 ) throws BinaryOperatorException ;


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract BinaryOperator clone ( ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


  /**
   * Returns the base pretty print priority for this binary operator, when used
   * within an {@link de.unisiegen.tpml.core.expressions.InfixOperation}.
   * 
   * @return the base pretty print priority for this binary operator.
   */
  public int getPrettyPriority ( )
  {
    return this.prettyPriority ;
  }


  /**
   * TODO
   * 
   * @param pInfixOperator The infixOperator to set.
   */
  public void useInfixOperator ( boolean pInfixOperator )
  {
    this.infixOperator = pInfixOperator ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Constant#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CONSTANT ) ;
      if ( ! this.infixOperator )
      {
        this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
      }
      this.prettyStringBuilder.addConstant ( this.text ) ;
      if ( ! this.infixOperator )
      {
        this.prettyStringBuilder.addText ( ")" ) ; //$NON-NLS-1$
      }
    }
    return this.prettyStringBuilder ;
  }
}
