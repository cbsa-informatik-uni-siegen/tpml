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
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Binary-Operator" ; //$NON-NLS-1$
  }


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
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public abstract BinaryOperator clone ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Constant#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_CONSTANT ) ;
    builder.addText ( "(" ) ; //$NON-NLS-1$
    builder.addConstant ( this.text ) ;
    builder.addText ( ")" ) ; //$NON-NLS-1$
    return builder ;
  }
}