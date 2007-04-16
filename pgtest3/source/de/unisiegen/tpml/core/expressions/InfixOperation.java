package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
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
public final class InfixOperation extends Expression implements
    ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , - 1 , 2 } ;


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


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
    this.expressions = new Expression [ 3 ] ;
    this.expressions [ 0 ] = pExpression1 ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ] = pBinaryOperator ;
    if ( this.expressions [ 1 ].getParent ( ) != null )
    {
      this.expressions [ 1 ] = this.expressions [ 1 ].clone ( ) ;
    }
    this.expressions [ 1 ].setParent ( this ) ;
    this.expressions [ 2 ] = pExpression2 ;
    if ( this.expressions [ 2 ].getParent ( ) != null )
    {
      this.expressions [ 2 ] = this.expressions [ 2 ].clone ( ) ;
    }
    this.expressions [ 2 ].setParent ( this ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public InfixOperation clone ( )
  {
    return new InfixOperation ( ( BinaryOperator ) this.expressions [ 1 ]
        .clone ( ) , this.expressions [ 0 ].clone ( ) , this.expressions [ 2 ]
        .clone ( ) ) ;
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
      return ( ( this.expressions [ 1 ].equals ( other.expressions [ 1 ] ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 2 ]
          .equals ( other.expressions [ 2 ] ) ) ) ;
    }
    return false ;
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
   * Returns the first operand.
   * 
   * @return the first operand.
   * @see #getOp()
   * @see #getE2()
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 0 ] ;
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
    return this.expressions [ 2 ] ;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the <code>n</code>th sub expression.
   * 
   * @param pIndex the index of the expression to return.
   * @return the <code>n</code>th sub expression.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
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
    return ( BinaryOperator ) this.expressions [ 1 ] ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 1 ].hashCode ( )
        + this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 2 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public InfixOperation substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public InfixOperation substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE1 = this.expressions [ 0 ].substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.expressions [ 2 ].substitute ( pId , pExpression ,
        pAttributeRename ) ;
    return new InfixOperation ( ( BinaryOperator ) this.expressions [ 1 ]
        .clone ( ) , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public InfixOperation substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 2 ].substitute ( pTypeSubstitution ) ;
    return new InfixOperation ( ( BinaryOperator ) this.expressions [ 1 ]
        .clone ( ) , newE1 , newE2 ) ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          ( ( BinaryOperator ) this.expressions [ 1 ] ).getPrettyPriority ( ) ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          ( ( BinaryOperator ) this.expressions [ 1 ] ).getPrettyPriority ( ) ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          ( ( BinaryOperator ) this.expressions [ 1 ] ).getPrettyPriority ( ) ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder
          .addBuilder ( this.expressions [ 2 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              ( ( BinaryOperator ) this.expressions [ 1 ] )
                  .getPrettyPriority ( ) + 1 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
