package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class are used to represent infix operations, which act as
 * syntactic sugar for applications. The string representation of an infix
 * operation is <code>e1 op e2</code> where <code>op</code> is a binary
 * operator.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Application
 * @see BinaryOperator
 * @see Expression
 */
public final class InfixOperation extends Expression
{
  /**
   * The operator of the infix operation.
   * 
   * @see #getOp()
   */
  private BinaryOperator op ;


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


  /**
   * Allocates a new <code>InfixOperation</code> with the specified
   * parameters.
   * 
   * @param pBinaryOperator the binary operator.
   * @param pExpression1 the first operand.
   * @param pExpression2 the second operand.
   * @throws NullPointerException if <code>op</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public InfixOperation ( BinaryOperator pBinaryOperator ,
      Expression pExpression1 , Expression pExpression2 )
  {
    if ( pBinaryOperator == null )
    {
      throw new NullPointerException ( "op is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.op = pBinaryOperator ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Infix-Operation" ; //$NON-NLS-1$
  }


  /**
   * Returns the binary operator that is applied to <code>e1</code> and
   * <code>e2</code>.
   * 
   * @return the binary operator.
   * @see #getE1()
   * @see #getE2()
   */
  public BinaryOperator getOp ( )
  {
    return this.op ;
  }


  /**
   * Returns the first operand.
   * 
   * @return the first operand.
   * @see #getOp()
   * @see #getE2()
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second operand.
   * 
   * @return the second operand.
   * @see #getOp()
   * @see #getE1()
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public InfixOperation clone ( )
  {
    return new InfixOperation ( this.op.clone ( ) , this.e1.clone ( ) , this.e2
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public InfixOperation substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this
        : new InfixOperation ( this.op , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public InfixOperation substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.e2.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return ( this.e1 == newE1 && this.e2 == newE2 ) ? this
        : new InfixOperation ( this.op , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , this.op.getPrettyPriority ( ) ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , this.op
        .getPrettyPriority ( ) ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addConstant ( this.op.toString ( ) ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , this.op
        .getPrettyPriority ( ) + 1 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof InfixOperation )
    {
      InfixOperation other = ( InfixOperation ) pObject ;
      return ( this.op.equals ( other.op ) && this.e1.equals ( other.e1 ) && this.e2
          .equals ( other.e2 ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.op.hashCode ( ) + this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
  }
}
